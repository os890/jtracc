/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/
package org.apache.myfaces.blank;

import at.irian.i18n.jtracc.persistence.util.PersistableMessagesBeanFactory;
import at.irian.i18n.jtracc.persistence.PersistenceDelegationEntryBean;
import at.irian.i18n.jtracc.persistence.PersistableMessagesBean;
import at.irian.i18n.jtracc.util.TranslationUtils;
import at.irian.i18n.jtracc.util.LocaleUtils;
import at.irian.i18n.jtracc.renderkit.translation.model.TranslationSelectItem;

import java.util.*;

/**
 * A typical simple backing bean, that is backed to <code>helloworld.jsp</code>
 *
 * @author Gerhard Petracek
 */
public class HelloWorldBacking
{
    //properties
    private String name = "Gerhard Petracek";

    /**
     * default empty constructor
     */
    public HelloWorldBacking()
    {
        try
        {
            initManyOptions();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public List getOptions()
    {
        List options = new ArrayList();

        for (int i = 2; i <= 4; i++)
        {
            TranslationSelectItem item = new TranslationSelectItem( "" + i,
                                                                    "#{" + TranslationUtils.getMessageResolverBeanName()
                                                                    + ".option_" + i + "}" );

            options.add( item );
        }

        return options;
    }

    public List getMultiSelected()
    {
        List selected = new ArrayList();
        selected.add( "1");
        selected.add( "2");
        return selected;
    }

    public void setMultiSelected(List list)
    {
    }

    //-------------------getter & setter
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getStaticContent()
    {
        return "static el content";
    }

    public List getTableContent()
    {
        return tableContent;
    }

    public void setTableContent(List tableContent)
    {
    }

    List tableContent = new ArrayList();

    private void initManyOptions() throws ClassNotFoundException
    {
        PersistableMessagesBean pmb = PersistableMessagesBeanFactory.lookup( TranslationUtils.getMessageResolverBeanName() );

        String value;

        List loadableLocales = new ArrayList();
        Map addedLocals = new HashMap();
        // try to load first content
        // success -> locale is loadable -> add it to list
        Iterator li = pmb.getSupportedLocales().iterator();
        Locale currentLocale;
        while (li.hasNext())
        {
            currentLocale = (Locale) li.next();
            if (pmb.getMessage( "content_1", currentLocale ) != null
                && !addedLocals.containsKey( LocaleUtils.getLocaleString( currentLocale ) ))
            {

                addedLocals.put( LocaleUtils.getLocaleString( currentLocale ), "" );
                loadableLocales.add( currentLocale );
            }
        }

        int j = 1;
        Locale tmp;

        Iterator current = loadableLocales.iterator();
        PersistenceDelegationEntryBean entry;
        boolean added = false;

        while (current.hasNext())
        {
            tmp = (Locale) current.next();
            for (; ;)
            {
                if (tableContent.size() > j - 1)
                {
                    entry = (PersistenceDelegationEntryBean) tableContent.get( j - 1 );
                }
                else
                {
                    entry = PersistableMessagesBeanFactory.getPersistenceEntryBean();
                }
                value = pmb.getMessage( "content_" + j, tmp );
                if (value == null || value.equals( TranslationUtils.getDefaultString() ))
                {
                    break;
                }
                entry.setPersistableMessagesBean( pmb );
                entry.setIndex( j );
                if (value.contains( "_" ) && value.startsWith( LocaleUtils.getLocaleString( tmp ) ))
                {
                    entry.setMessage( "content", TranslationUtils.TRANSLATION_MARKER_TRANSLATED + value, tmp );
                }
                else
                {
                    int offset = 0;
                    if (value.contains( "_" ))
                    {
                        offset = 3;
                    }
                    String newValue = value.substring( offset, value.length() );
                    entry.setMessage( "content", TranslationUtils.TRANSLATION_MARKER_TRANSLATED + newValue, tmp );
                }

                if(tableContent.size() > j -1)
                {
                    tableContent.set( j-1, entry );
                }
                else
                {
                    tableContent.add(entry);
                }

                added = true;
                j++;
            }
            j = 1;
        }
        if (!added)
        {
            entry = PersistableMessagesBeanFactory.getPersistenceEntryBean();
            entry.setPersistableMessagesBean( pmb );
            entry.setIndex( 1 );
            entry.setMessage( "content", TranslationUtils.TRANSLATION_MARKER_NOT_TRANSLATED
                                         + TranslationUtils.getDefaultString(), LocaleUtils.getDefaultLocale() );
            tableContent.add( entry );
        }
    }

}
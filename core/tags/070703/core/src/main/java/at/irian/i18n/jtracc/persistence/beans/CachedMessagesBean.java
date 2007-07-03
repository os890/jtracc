/*
 * jtracc - i18n JSF component library
 * Copyright 2007, IRIAN Solutions GmbH Vienna, Austria
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package at.irian.i18n.jtracc.persistence.beans;

import java.util.Map;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.io.IOException;

import at.irian.i18n.jtracc.util.LocaleUtils;
import at.irian.i18n.jtracc.util.TranslationUtils;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

/**
 * Bean with caching functionality
 * <p/>
 * !!!NEVER use/call specific JSF functionality (such as FacesContext) in the constructor of this class or of any subclass!!!
 */
public abstract class CachedMessagesBean extends AbstractPersistableMessagesBean
{
    private static Map properties = new Hashtable();

    /**
     * @see at.irian.i18n.jtracc.persistence.beans.AbstractPersistableMessagesBean
     */
    protected void saveMessage(String key, String value, Locale locale)
    {
        synchronized (this)
        {
            String path = (String) localesUrlMapping.get( LocaleUtils.getLocaleString( locale ) );

            try
            {
                if (value.startsWith( TranslationUtils.TRANSLATION_MARKER_TRANSLATED ) == false)
                {
                    return;
                }

                value = value.replace( TranslationUtils.TRANSLATION_MARKER_TRANSLATED, "" );

                // cache changed properties
                Properties p = loadMessages( path, locale, true );
                p.put( key, value );
                properties.put( LocaleUtils.getLocaleString( locale ), p );

                writeMessage( localesUrlMapping.get( LocaleUtils.getLocaleString( locale ) ).toString(), key, value );
            }
            catch (IOException e)
            {
                String msg = "can't save key: " + key + " value: " + value;
                FacesContext.getCurrentInstance().addMessage( null, new FacesMessage( FacesMessage.SEVERITY_ERROR, msg, null ) );
                logger.error( msg, e );
            }
        }
    }

    protected abstract void writeMessage(String url, String key, String value) throws IOException;

    /**
     * Method with caching functionality
     *
     * @see at.irian.i18n.jtracc.persistence.beans.AbstractPersistableMessagesBean
     */
    protected Properties loadMessages(String url, Locale locale, boolean reload)
    {
        if (properties.containsKey( LocaleUtils.getLocaleString( locale ) ) && !reload)
        {
            return (Properties) properties.get( LocaleUtils.getLocaleString( locale ) );
        }

        Properties p;

        try
        {
            p = readMessages( url );

            properties.put( LocaleUtils.getLocaleString( locale ), p );

            return p;
        }
        catch (IOException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    protected abstract Properties readMessages(String url) throws IOException;
}

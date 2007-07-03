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
package at.irian.i18n.jtracc.beans;

import at.irian.i18n.jtracc.util.LocaleUtils;
import at.irian.i18n.jtracc.util.SettingsUtils;
import at.irian.i18n.jtracc.util.TranslationUtils;

import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Iterator;

/**
 * Base class for in-memory usages
 * <p/>
 * !!!NEVER use/call specific JSF functionality (such as FacesContext) in the constructor of this class or of any subclass!!!
 */
public class InMemoryMessagesBean extends AbstractMessagesBean
{
    protected Map messagesMap = new Hashtable();

    /**
     * @see at.irian.i18n.jtracc.MessageResolver
     */
    //TODO: test
    public String getMessage(String key, Locale locale)
    {
        // try 1) try to load message - context: current locale (language + country)
        Map localeMap = (Map) this.messagesMap.get( LocaleUtils.getLocaleString( locale ) );

        // try 2) try to load message - context: current locale (language)
        if (localeMap == null || localeMap.containsKey( key ) == false)
        {
            localeMap = (Map) this.messagesMap.get( locale.getLanguage() );
            locale = LocaleUtils.parseLocale( locale.getLanguage() );
        }

        // try 3) try to load message - context: accept-language locales
        if (localeMap == null || localeMap.containsKey( key ) == false)
        {
            Iterator rli = LocaleUtils.getRequestedLocalesAsDefault().iterator();
            String currentLocale;
            while (rli.hasNext())
            {
                currentLocale = (String) rli.next();

                if (this.messagesMap.containsKey( currentLocale ))
                {
                    localeMap = (Map) this.messagesMap.get( currentLocale );
                    locale = LocaleUtils.parseLocale( currentLocale );
                    break;
                }
            }
        }

        // try 4) no message found - try with default locale
        if (localeMap == null || localeMap.containsKey( key ) == false)
        {
            localeMap = (Map) this.messagesMap.get( LocaleUtils.getLocaleString( getDefaultLocale() ) );
            locale = getDefaultLocale();
        }

        if (localeMap != null && localeMap.containsKey( key ))
        {
            createKeyLocaleMapping( key, LocaleUtils.getLocaleString( locale ) );
            return (String) localeMap.get( key );
        }

        // don't return null - null is not visible for inline translation
        return key.startsWith( SettingsUtils.getComponentProperty( "image_prefix" ) ) ? "" : TranslationUtils.getDefaultString();
    }

    /**
     * @see at.irian.i18n.jtracc.MessageResolver
     */
    public void setMessage(String key, String value, Locale locale)
    {
        if (key == null || value == null)
        {
            return;
        }

        // in-memory beans don't need the translation marker
        if (value.startsWith( TranslationUtils.TRANSLATION_MARKER ))
        {
            value = value.substring( value.indexOf( TranslationUtils.TRANSLATION_MARKER_END ) + TranslationUtils.TRANSLATION_MARKER_END.length(), value.length() );
        }

        Map localeMap = (Map) this.messagesMap.get( LocaleUtils.getLocaleString( locale ) );

        if (localeMap == null)
        {
            messagesMap.put( LocaleUtils.getLocaleString( locale ), new Hashtable() );
            localeMap = (Map) this.messagesMap.get( LocaleUtils.getLocaleString( locale ) );
        }

        localeMap.put( key, value );
    }
}

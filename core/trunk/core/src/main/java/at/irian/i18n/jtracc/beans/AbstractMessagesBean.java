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

import at.irian.i18n.jtracc.MessageResolver;
import at.irian.i18n.jtracc.util.LocaleUtils;
import at.irian.i18n.jtracc.util.TranslationUtils;
import at.irian.i18n.jtracc.util.SettingsUtils;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Messages bean with basic functionality
 * <p/>
 * !!!NEVER use/call specific JSF functionality (such as FacesContext) in the constructor of any subclass!!!
 */
public abstract class AbstractMessagesBean implements MessageResolver
{
    public void changeMessage(ValueChangeEvent event)
    {
        TranslationUtils.changeMessage( event );
    }

    public List getSupportedLocales()
    {
        return LocaleUtils.getSupportedLocales();
    }

    public Locale getDefaultLocale()
    {
        return LocaleUtils.getDefaultLocale();
    }

    /**
     * creates mapping for locale-marker (only in translation mode available)
     *
     * @param key    message key
     * @param locale found locale - a message is available with this locale for the given key
     */
    protected void createKeyLocaleMapping(String key, String locale)
    {
        if (TranslationUtils.isTranslationMode())
        {
            // has to be a session map - to continue translation mode
            Map sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            String keyLocaleMappingKey = SettingsUtils.getComponentProperty( "key_locale_mapping_key" );
            Map keyLocaleMapping;

            if (sessionMap.containsKey( keyLocaleMappingKey ))
            {
                keyLocaleMapping = (Map) sessionMap.get( keyLocaleMappingKey );
            }
            else
            {
                keyLocaleMapping = new HashMap();
            }

            keyLocaleMapping.put( key, locale );
            sessionMap.put( keyLocaleMappingKey, keyLocaleMapping );
        }
    }

}

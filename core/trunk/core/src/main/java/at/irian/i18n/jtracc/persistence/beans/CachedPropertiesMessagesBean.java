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

import at.irian.i18n.jtracc.persistence.util.PropertyUtils;
import at.irian.i18n.jtracc.util.LocaleUtils;
import at.irian.i18n.jtracc.util.TranslationUtils;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

/**
 * Bean with caching functionality
 * <p/>
 * !!!NEVER use/call specific JSF functionality (such as FacesContext) in the constructor of this class or of any subclass!!!
 */
public class CachedPropertiesMessagesBean extends AbstractCachedMessagesBean
{
    protected void writeMessage(String url, String key, String value) throws IOException
    {
        PropertyUtils.setProperty( url, key, value );
    }

    /**
     * @return filesystem path to property files
     *
     * @see AbstractPersistableMessagesBean
     */
    protected String getUrlToLocale(Locale locale)
    {
        return TranslationUtils.getMessagesLocation() + "/" + LocaleUtils.getLocaleString( locale ) + ".properties";
    }

    protected Properties readMessages(String url) throws IOException
    {
        return PropertyUtils.getProperties( url );
    }
}

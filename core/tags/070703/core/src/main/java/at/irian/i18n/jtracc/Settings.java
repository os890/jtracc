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
package at.irian.i18n.jtracc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import at.irian.i18n.jtracc.util.TranslationUtils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Settings
{
    protected final Log logger = LogFactory.getLog( getClass() );

    Map settings = new HashMap();

    private static final String PACKAGE_NAME = "at.irian.i18n";

    // keep it hardcoded!!!
    public static final String COMPONENT_NAME = "jtracc";
    public static final String WEB_XML_CONTEXT_PARAMETER_PARAM_NAME_EXT_CONFIG = PACKAGE_NAME + "." + COMPONENT_NAME + "Config";
    public static final String INTERNAL_RESOURCE_PREFIX = "jar:";

    // keep hardcoded for better performance
    public static final String BEAN_NAME = PACKAGE_NAME + "." + COMPONENT_NAME + ".Settings";

    public Settings(String componentConfigResouceBundleName)
    {
        // load out-of-the-box settings
        loadConfig( PACKAGE_NAME + "." + COMPONENT_NAME + ".componentConfig" );

        // load custom settings
        if (!TranslationUtils.isEmtyString( componentConfigResouceBundleName ))
        {
            loadConfig( componentConfigResouceBundleName );
        }
    }

    private void loadConfig(String appConfigResouceBundle)
    {
        ResourceBundle appConfig;

        try
        {
            appConfig = ResourceBundle.getBundle( appConfigResouceBundle );
        }
        catch (RuntimeException re)
        {
            logger.info( "!!! there is no custom config: " + appConfigResouceBundle );
            return;
        }

        Enumeration content = appConfig.getKeys();

        while (content.hasMoreElements())
        {
            String key = (String) content.nextElement();
            String value = appConfig.getString( key );

            if (value.startsWith( ";" ))
            {
                value = getValue( key ) + value;
            }

            setValue( key, value );
        }
    }

    public String getValue(String property)
    {
        if (settings.containsKey( property.toUpperCase() ))
        {
            String value = (String) settings.get( property.toUpperCase() );
            return ( value.startsWith( "&" ) ) ? getValue( value.substring( 1, value.length() ) ) : value;
        }
        return null;
    }

    public void setValue(String property, String value)
    {
        settings.put( property.toUpperCase(), value );
    }
}

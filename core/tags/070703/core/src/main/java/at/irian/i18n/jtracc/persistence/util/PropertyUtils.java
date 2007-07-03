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
package at.irian.i18n.jtracc.persistence.util;

import at.irian.i18n.jtracc.util.SettingsUtils;
import at.irian.i18n.jtracc.util.FileUtils;
import org.springframework.jtracc.util.DefaultPropertiesPersister;
import org.springframework.jtracc.util.PropertiesPersister;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.*;
import java.util.Properties;

/**
 * Class for handling I/O with property files
 */
public class PropertyUtils
{
    /**
     * @param propertyFilePath path to property file
     * @param key              key of the message
     * @param value            value of the message
     *
     * @throws IOException is thrown if an exception occurs when writing a property.
     *                     Should not occur if method isWriteableProperty is used before.
     */
    public static synchronized void setProperty(String propertyFilePath, String key, String value) throws IOException
    {
        FileUtils.initFile( propertyFilePath );

        PropertiesPersister pp = new DefaultPropertiesPersister();
        Properties p = getProperties( propertyFilePath );

        if (value == null)
        {
            p.remove( key );
        }
        else
        {
            // change message
            p.setProperty( key, value );
        }

        // persist properties
        OutputStream os = new FileOutputStream( propertyFilePath );
        pp.store( p, os, "www.irian.at - translated with jtracc" );
        os.close();
    }

    /**
     * @param propertyFilePath path to property file
     *
     * @return all properties of the given property file
     */
    public static Properties getProperties(String propertyFilePath)
    {
        PropertiesPersister pp = new DefaultPropertiesPersister();
        Properties p = new Properties();

        try
        {
            InputStream is = new FileInputStream( propertyFilePath );
            pp.load( p, is );
            is.close();
        }
        catch (Exception ex)
        {
            return new Properties();
        }

        return p;
    }
}

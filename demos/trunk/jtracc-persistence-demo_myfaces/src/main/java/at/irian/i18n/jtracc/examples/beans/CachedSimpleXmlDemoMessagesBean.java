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
package at.irian.i18n.jtracc.examples.beans;

import at.irian.i18n.jtracc.persistence.beans.CachedMessagesBean;
import at.irian.i18n.jtracc.util.LocaleUtils;
import at.irian.i18n.jtracc.util.TranslationUtils;
import at.irian.i18n.jtracc.util.FileUtils;

import java.io.*;
import java.util.Properties;
import java.util.Locale;
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;

/**
 * class for demonstrating jtracc persistence extension concept
 * <p/>
 * !!!NEVER use/call specific JSF functionality (such as FacesContext) in the constructor of this class or of any subclass!!!
 */
public class CachedSimpleXmlDemoMessagesBean extends CachedMessagesBean
{
    protected synchronized void writeMessage(String url, String key, String value) throws IOException
    {
        FileUtils.initFile( url );

        Properties p = readMessages( url );

        p.put( key, value );

        try
        {
            // Serialize object into XML
            XMLEncoder encoder = new XMLEncoder( new BufferedOutputStream(
                    new FileOutputStream( url ) ) );
            encoder.writeObject( p );
            encoder.close();
        }
        catch (FileNotFoundException e)
        {
            logger.error( e );
        }
    }

    protected Properties readMessages(String url) throws IOException
    {
        Properties p;

        try
        {
            if(FileUtils.isEmtyFile( url ))
            {
                throw new Exception( "creating new language file");
            }
            XMLDecoder decoder = new XMLDecoder( new BufferedInputStream(
                    new FileInputStream( url ) ) );

            p = (Properties) decoder.readObject();
            decoder.close();
        }
        catch (Exception e)
        {
            return new Properties();
        }

        return p;
    }

    protected String getUrlToLocale(Locale locale)
    {
        return TranslationUtils.getMessagesLocation() + "/" + LocaleUtils.getLocaleString( locale ) + ".xml";
    }
}

package at.irian.i18n.jtracc.examples.beans;

import at.irian.i18n.jtracc.persistence.beans.CachedMessagesBean;
import at.irian.i18n.jtracc.persistence.util.PropertyUtils;
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
 *
 * @author Gerhard Petracek
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

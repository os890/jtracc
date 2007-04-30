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
 *
 * @author Gerhard Petracek
 */
public class CachedPropertiesMessagesBean extends CachedMessagesBean
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

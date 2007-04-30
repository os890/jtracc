package at.irian.i18n.jtracc.persistence.beans;

import at.irian.i18n.jtracc.beans.AbstractMessagesBean;
import at.irian.i18n.jtracc.persistence.PersistableMessagesBean;
import at.irian.i18n.jtracc.util.LocaleUtils;
import at.irian.i18n.jtracc.util.SettingsUtils;
import at.irian.i18n.jtracc.util.TranslationUtils;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Messages bean with, which forces subclasses to implement special methods for persistence concerns
 * <p/>
 * !!!NEVER use/call specific JSF functionality (such as FacesContext) in the constructor of this class or of any subclass!!!
 *
 * @author Gerhard Petracek
 */
public abstract class AbstractPersistableMessagesBean extends AbstractMessagesBean implements PersistableMessagesBean
{
    protected final Log logger = LogFactory.getLog( getClass() );
    protected Map localesUrlMapping = null;

    public AbstractPersistableMessagesBean()
    {
        //don't use initLocales() here
    }

    /**
     * Default implementation to get a message
     *
     * @return message for the given key and locale
     */
    public String getMessage(String key, Locale locale)
    {
        if (TranslationUtils.isEmtyString( key ))
        {
            return null;
        }

        if (localesUrlMapping == null)
        {
            initLocales();
        }

        String urlToLoad;

        if (!localesUrlMapping.containsKey( LocaleUtils.getLocaleString( locale ) ))
        {
            initLocaleStorage( locale );
        }

        String message = null;
        // try 1) try to load message - context: current locale (language + country)
        if (localesUrlMapping.containsKey( LocaleUtils.getLocaleString( locale ) ))
        {
            urlToLoad = localesUrlMapping.get( LocaleUtils.getLocaleString( locale ) ).toString();
            message = loadMessages( urlToLoad, locale, false ).getProperty( key );
        }

        if (message != null)
        {
            createKeyLocaleMapping( key, LocaleUtils.getLocaleString( locale ) );
            return message;
        }

        // try 2) try to load message - context: current locale (language)
        if (localesUrlMapping.containsKey( locale.getLanguage() ))
        {
            urlToLoad = localesUrlMapping.get( locale.getLanguage() ).toString();
            message = loadMessages( urlToLoad, locale, false ).getProperty( key );
        }

        if (message != null)
        {
            createKeyLocaleMapping( key, locale.getLanguage() );
            return message;
        }

        // try 3) try to load message - context: accept-language locales
        Iterator rli = LocaleUtils.getRequestedLocalesAsDefault().iterator();
        String currentLocale;
        while (rli.hasNext())
        {
            currentLocale = (String) rli.next();

            if (!localesUrlMapping.containsKey( currentLocale ))
            {
                continue;
            }

            urlToLoad = localesUrlMapping.get( currentLocale ).toString();
            message = loadMessages( urlToLoad, LocaleUtils.parseLocale( currentLocale ), false ).getProperty( key );

            if (message != null)
            {
                createKeyLocaleMapping( key, currentLocale );
                return message;
            }
        }

        // try 4) no message found - try with default locale
        if (localesUrlMapping.containsKey( getDefaultLocale().toString() ))
        {
            urlToLoad = localesUrlMapping.get( LocaleUtils.getLocaleString( getDefaultLocale() ) ).toString();
            message = loadMessages( urlToLoad, getDefaultLocale(), false ).getProperty( key );
        }
        if (message != null)
        {
            createKeyLocaleMapping( key, LocaleUtils.getLocaleString( getDefaultLocale() ) );
            return message;
        }

        return key.startsWith( SettingsUtils.getComponentProperty( "image_prefix" ) ) ? "" : TranslationUtils.getDefaultString();
    }

    /**
     * Default implementation to set a message
     */
    public void setMessage(String key, String value, Locale locale)
    {
        if (key != null && value != null && locale != null)
        {
            if (localesUrlMapping == null)
            {
                initLocales();
            }

            saveMessage( key, value, locale );
        }
    }

    /**
     * Set default values for locales
     */
    protected void initLocales()
    {
        localesUrlMapping = new HashMap();
        String key;
        Locale locale;
        Iterator sli = getSupportedLocales().iterator();

        while (sli.hasNext())
        {
            locale = (Locale) sli.next();

            key = locale.toString();
            if (!TranslationUtils.isEmtyString( key ))
            {
                initLocaleStorage( locale );
            }
        }

        initLocaleStorage( getDefaultLocale() );
    }

    /**
     * Load all stored messages for the given path and locale.
     *
     * @param url    support for manual pathes - or use method getUrlToLocale.
     * @param locale defines a specific locale to load messages connected with this locale.
     * @param reload if caching is supported it is possible to avoid or force reloading.
     *
     * @return stored messages as Properties object
     */
    protected abstract Properties loadMessages(String url, Locale locale, boolean reload);

    protected abstract void saveMessage(String key, String value, Locale locale);

    /**
     * @return URL to a storage for messages
     */
    protected abstract String getUrlToLocale(Locale locale);

    /**
     * override it (if necessary) with special persistence code to create the new locale
     *
     * @param locale new locale
     */
    protected void createLocaleStorage(Locale locale)
    {
        // override (if necessary) it with special persistence code to create the new locale
    }

    // don't change or merge - to avoid that this logic is overridden
    private void initLocaleStorage(Locale locale)
    {
        localesUrlMapping.put( LocaleUtils.getLocaleString( locale ), getUrlToLocale( locale ) );
        createLocaleStorage( locale );
    }
}

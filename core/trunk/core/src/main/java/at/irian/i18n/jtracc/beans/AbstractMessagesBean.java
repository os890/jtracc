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
 *
 * @author Gerhard Petracek
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

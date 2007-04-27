package at.irian.i18n.jtracc.beans;

import at.irian.i18n.jtracc.persistence.beans.CachedPropertiesMessagesBean;
import at.irian.i18n.jtracc.persistence.util.PropertyUtils;
import at.irian.i18n.jtracc.util.BeanUtils;
import at.irian.i18n.jtracc.util.LocaleUtils;
import at.irian.i18n.jtracc.util.TranslationUtils;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import java.util.Locale;
import java.util.Properties;
import java.util.Map;
import java.util.Hashtable;

/**
 * class for in-session usages - to simulate translation - ONLY for online live demo - a user doesn't translate reale page
 * <p/>
 * !!!NEVER use/call specific JSF functionality (such as FacesContext) in the constructor of this class or of any subclass!!!
 *
 * @author Gerhard Petracek
 */
public class LiveDemoBean extends CachedPropertiesMessagesBean
{
    private final static String DEMO_BEAN_NEME = "custom_demo_messages_";

    protected Properties loadMessages(String url, Locale locale, boolean reload)
    {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        String localeKey = LocaleUtils.getLocaleString( locale );
        String sessionBeanName = DEMO_BEAN_NEME + localeKey;

        Map properties = (Hashtable) BeanUtils.getSessionBean( context, sessionBeanName );

        if(properties == null)
        {
            properties = new Hashtable();
            BeanUtils.setSessionBean( context, sessionBeanName, properties);
            properties = (Hashtable) BeanUtils.getSessionBean( context, sessionBeanName );
        }


        if (properties.containsKey( localeKey ))
            return (Properties) properties.get( localeKey );

        Properties p = PropertyUtils.getProperties( url );
        properties.put( localeKey, p );

        return p;
    }

    protected void saveMessage(String key, String value, Locale locale)
    {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        String localeKey = LocaleUtils.getLocaleString( locale );
        String sessionBeanName = DEMO_BEAN_NEME + localeKey;

        Map properties = (Hashtable) BeanUtils.getSessionBean( context, sessionBeanName );

        if(properties == null)
        {
            properties = new Hashtable();
            BeanUtils.setSessionBean( context, sessionBeanName, properties);
            properties = (Hashtable) BeanUtils.getSessionBean( context, sessionBeanName );
        }

        String path = (String) localesUrlMapping.get( localeKey );

        if (value.startsWith( TranslationUtils.TRANSLATION_MARKER_TRANSLATED ) == false)
            return;

        value = value.replace( TranslationUtils.TRANSLATION_MARKER_TRANSLATED, "" );

        Properties p = loadMessages( path, locale, true );
        p.put( key, value );
        properties.put( localeKey, p );

        BeanUtils.setSessionBean( context, sessionBeanName, properties);
    }
}

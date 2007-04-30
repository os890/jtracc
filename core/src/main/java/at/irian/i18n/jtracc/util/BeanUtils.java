package at.irian.i18n.jtracc.util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * @author Gerhard Petracek
 */

public class BeanUtils
{
    /*
     * setter
     */
    public static Object setApplicationBean(ExternalContext externalContext, String beanName, Object bean)
    {
        if (!( TranslationUtils.isEmtyString( beanName ) || bean == null ))
        {
            externalContext.getApplicationMap().put( beanName, bean );
        }

        return TranslationUtils.isEmtyString( beanName ) ? null : bean;
    }

    public static Object setApplicationBean(FacesContext context, String beanName, Object bean)
    {
        return setApplicationBean( context.getExternalContext(), beanName, bean );
    }

    public static Object setRequestBean(ExternalContext externalContext, String beanName, Object bean)
    {
        if (!( TranslationUtils.isEmtyString( beanName ) || bean == null ))
        {
            externalContext.getRequestMap().put( beanName, bean );
        }

        return TranslationUtils.isEmtyString( beanName ) ? null : bean;
    }

    public static Object setRequestBean(FacesContext context, String beanName, Object bean)
    {
        return setRequestBean( context.getExternalContext(), beanName, bean );
    }

    public static Object setSessionBean(ExternalContext externalContext, String beanName, Object bean)
    {
        if (!( TranslationUtils.isEmtyString( beanName ) || bean == null ))
        {
            externalContext.getSessionMap().put( beanName, bean );
        }

        return TranslationUtils.isEmtyString( beanName ) ? null : bean;
    }

    public static Object setSessionBean(FacesContext context, String beanName, Object bean)
    {
        return setSessionBean( context.getExternalContext(), beanName, bean );
    }

    public static Object getApplicationBean(ExternalContext externalContext, String beanName)
    {
        if (!TranslationUtils.isEmtyString( beanName ))
        {
            Map applicationMap = externalContext.getApplicationMap();

            if (applicationMap.containsKey( beanName ))
            {
                return applicationMap.get( beanName );
            }
        }

        return null;
    }

    /*
     * getter
     */

    public static Object getApplicationBean(FacesContext context, String beanName)
    {
        return getApplicationBean( context.getExternalContext(), beanName );
    }

    public static Object getRequestBean(ExternalContext externalContext, String beanName)
    {
        if (!TranslationUtils.isEmtyString( beanName ))
        {
            Map requestMap = externalContext.getRequestMap();

            if (requestMap.containsKey( beanName ))
            {
                return requestMap.get( beanName );
            }
        }

        return null;
    }

    public static Object getRequestBean(FacesContext context, String beanName)
    {
        return getRequestBean( context.getExternalContext(), beanName );
    }

    public static Object getSessionBean(ExternalContext externalContext, String beanName)
    {
        if (!TranslationUtils.isEmtyString( beanName ))
        {
            Map sessionMap = externalContext.getSessionMap();

            if (sessionMap.containsKey( beanName ))
            {
                return sessionMap.get( beanName );
            }
        }

        return null;
    }

    public static Object getSessionBean(FacesContext context, String beanName)
    {
        return getSessionBean( context.getExternalContext(), beanName );
    }

    /*
     * remove
     */
    public static void removeApplicationBean(ExternalContext externalContext, String beanName)
    {
        if (!TranslationUtils.isEmtyString( beanName ))
        {
            Map applicationMap = externalContext.getApplicationMap();

            if (applicationMap.containsKey( beanName ))
            {
                applicationMap.remove( beanName );
            }
        }
    }

    public static void removeApplicationBean(FacesContext context, String beanName)
    {
        removeApplicationBean( context.getExternalContext(), beanName );
    }

    public static void removeRequestBean(ExternalContext externalContext, String beanName)
    {
        if (!TranslationUtils.isEmtyString( beanName ))
        {
            Map requestMap = externalContext.getRequestMap();

            if (requestMap.containsKey( beanName ))
            {
                requestMap.remove( beanName );
            }
        }
    }

    public static void removeRequestBean(FacesContext context, String beanName)
    {
        removeRequestBean( context.getExternalContext(), beanName );
    }

    public static void removeSessionBean(ExternalContext externalContext, String beanName)
    {
        if (!TranslationUtils.isEmtyString( beanName ))
        {
            Map sessionMap = externalContext.getSessionMap();

            if (sessionMap.containsKey( beanName ))
            {
                sessionMap.remove( beanName );
            }
        }
    }

    public static void removeSessionBean(FacesContext context, String beanName)
    {
        removeSessionBean( context.getExternalContext(), beanName );
    }
}

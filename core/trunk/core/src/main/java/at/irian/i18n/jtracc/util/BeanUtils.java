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
package at.irian.i18n.jtracc.util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Map;

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

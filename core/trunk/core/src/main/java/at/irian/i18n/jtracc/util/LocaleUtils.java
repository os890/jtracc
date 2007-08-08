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

import javax.faces.context.FacesContext;
import java.util.*;

public class LocaleUtils
{
    //get selected value of <t2:targetLocale/>
    public static String getLocale(String id)
    {
        FacesContext context = FacesContext.getCurrentInstance();

        String cachedKey = SettingsUtils.getComponentProperty( "jtracc_prefix" ) + id;

        String value = (String)BeanUtils.getRequestBean( context, cachedKey );

        if(value != null)
        {
            return value;
        }

        // find submitted value
        Iterator ki = context.getExternalContext().getRequestParameterMap().keySet().iterator();

        // get locale value
        while (ki.hasNext())
        {
            Object o = ki.next();

            if (o != null && o.toString().endsWith( id ))
            {
                value = (String) context.getExternalContext().getRequestParameterMap().get( o.toString() );
                // cache result in request scope
                BeanUtils.setRequestBean( context, cachedKey, value );
                return value;
            }
        }
        return null;
    }

    public static Locale getDefaultLocale()
    {
        FacesContext context = FacesContext.getCurrentInstance();

        Locale defaultLocale = context.getApplication().getDefaultLocale();

        if (defaultLocale == null)
        {
            // keep it hardcoded!!!
            // set custom default locale in faces-config.xml
            return Locale.ENGLISH;
        }

        return defaultLocale;
    }

    //TODO: improve
    public static List getRequestedLocalesAsDefault()
    {
        List requestedLocales = new ArrayList();
        String acceptedLanguages = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get( "accept-language" );

        if (acceptedLanguages != null && acceptedLanguages.length() > 0)
        {
            if (acceptedLanguages.contains( "," ))
            {
                StringTokenizer st = new StringTokenizer( acceptedLanguages, "," );
                String part;

                while (st.hasMoreTokens())
                {
                    part = getLocaleLanguage( st.nextToken() );

                    // create locale & call getLocaleString: useless in this context
                    // don't change - future changes will need this functionality
                    requestedLocales.add( getLocaleString( org.apache.myfaces.jtracc.util.LocaleUtils.toLocale( part ) ) );
                }
            }
            else
            {
                requestedLocales.add( getLocaleString( new Locale( getLocaleLanguage( acceptedLanguages ) ) ) );
            }
        }

        return requestedLocales;
    }

    private static String getLocaleLanguage(String localeString)
    {
        if (localeString.length() > 2)
        {
            localeString = localeString.substring( 0, 2 );
        }
        return localeString;
    }

    public static List getSupportedLocales()
    {
        List supportedLocales = new ArrayList();
        Iterator sli = FacesContext.getCurrentInstance().getApplication().getSupportedLocales();

        Map addedValues = new HashMap();

        if (sli == null)
        {
            supportedLocales.add( getDefaultLocale() );
        }
        else
        {
            Locale current;
            while (sli.hasNext())
            {
                current = (Locale) sli.next();

                if (!addedValues.containsKey( getLocaleString( current ) ))
                {
                    supportedLocales.add( current );
                    addedValues.put( getLocaleString( current ), null );
                }
            }
        }

        Collections.sort( supportedLocales, new LocaleComparator() );
        return supportedLocales;
    }

    public static Locale getCurrentLocale()
    {
        return FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }

    public static String getLocaleString(Locale locale)
    {
        if (locale == null)
        {
            return null;
        }

        return TranslationUtils.isEmtyString( locale.getCountry() ) ? locale.getLanguage() :
               locale.getLanguage() + "_" + locale.getCountry();
    }

    public static Locale parseLocale(String locale)
    {
        if (locale.contains( "_" ))
        {
            String[] parts = locale.split( "_" );
            return new Locale( parts[0], parts[1] );
        }

        return new Locale( locale );
    }
}

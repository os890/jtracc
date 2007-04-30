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

import at.irian.i18n.jtracc.MessageResolver;
import at.irian.i18n.jtracc.Settings;
import at.irian.i18n.jtracc.components.filter.ComponentClassFilter;
import at.irian.i18n.jtracc.components.filter.ComponentClassFilterImpl;
import at.irian.i18n.jtracc.custom.translationpane.HtmlTranslationPaneRenderer;
import at.irian.i18n.jtracc.renderkit.translation.taglib.HtmlTargetLocaleSelectOneMenuTag;
import org.apache.myfaces.jtracc.renderkit.html.util.AddResource;
import org.apache.myfaces.jtracc.renderkit.html.util.AddResourceFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletContext;
import java.util.*;

/**
 * Helper methods and routines for the translation functionality.
 */
public class TranslationUtils
{
    public static final String TRANSLATION_MARKER = "<jtracc translated=";
    public static final String TRANSLATION_MARKER_END = "/>";
    public static final String TRANSLATION_MARKER_TRANSLATED = "<jtracc translated=\"true\"" + TRANSLATION_MARKER_END;
    public static final String TRANSLATION_MARKER_NOT_TRANSLATED = "<jtracc translated=\"false\"" + TRANSLATION_MARKER_END;

    public static String getDefaultString()
    {
        return SettingsUtils.getComponentProperty( "default_string" );
    }

    public static List getTranslatableAttributeNames()
    {
        List valueBindingAttributeNames = new ArrayList();

        StringTokenizer st = new StringTokenizer( SettingsUtils.getComponentProperty( "value_binding_attribute_names" ), ";" );

        while (st.hasMoreTokens())
        {
            valueBindingAttributeNames.add( st.nextToken() );
        }

        return valueBindingAttributeNames;
    }

    public static List getVarAttributeNames()
    {
        List attributes = new ArrayList();

        StringTokenizer st = new StringTokenizer( SettingsUtils.getComponentProperty( "var_attribute_names" ), ";" );

        while (st.hasMoreTokens())
        {
            attributes.add( st.nextToken() );
        }

        return attributes;
    }

    public static boolean isExcludedObject(Object o)
    {
        String filterClass = SettingsUtils.getComponentProperty( "component_class_filter" );
        // keep hardcoded
        String filterInterface = "at.irian.i18n.jtracc.components.filter.ComponentClassFilter";
        boolean implementedFilterInterface = false;

        try
        {
            Class filter = TranslationUtils.loadClassForName( filterClass );
            Class[] interfaces = filter.getInterfaces();

            // prove configured filter class
            for (int i = 0; i < interfaces.length; i++)
            {
                if (interfaces[i].getName().equals( filterInterface ))
                {
                    implementedFilterInterface = true;
                }
            }

            if (!implementedFilterInterface)
            {
                throw new ClassNotFoundException( filterInterface );
            }

            // delegate to filter implementation
            ComponentClassFilter componentFilter = (ComponentClassFilter) filter.newInstance();
            return componentFilter.isExcludedObject( o );
        }
        catch (ClassNotFoundException e)
        {
            // delegate to default filter
            return new ComponentClassFilterImpl().isExcludedObject( o );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public static String getMessageResolverBeanName()
    {
        return SettingsUtils.getComponentProperty( "messages_bean_name" );
    }

    /**
     * Utility method to create a UICommand component that is a link with some
     * default values.
     */
    public static UIOutput createTranslationLink(FacesContext facesContext)
    {
        //Additionl layout test
        HtmlDiv div = new HtmlDiv( "translationLink", "display:none;" );

        HtmlGraphicImage image = (HtmlGraphicImage) facesContext.getApplication()
                .createComponent( HtmlGraphicImage.COMPONENT_TYPE );

        String translateIcon = SettingsUtils.getComponentProperty( "translation_image_translate" );

        if (translateIcon.startsWith( Settings.INTERNAL_RESOURCE_PREFIX ))
        {
            translateIcon = translateIcon.substring( Settings.INTERNAL_RESOURCE_PREFIX.length(), translateIcon.length() );

            AddResource addresource = AddResourceFactory.getInstance( facesContext );
            image.setValue( addresource.getResourceUri( facesContext, HtmlTranslationPaneRenderer.class, translateIcon ) );
        }
        else
        {
            image.setValue( translateIcon );
        }

        image.getAttributes().put( "width", SettingsUtils.getComponentProperty( "translation_image_width" ) );
        image.getAttributes().put( "height", SettingsUtils.getComponentProperty( "translation_image_height" ) );
        image.getAttributes().put( "border", SettingsUtils.getComponentProperty( "translation_image_border" ) );
        div.getChildren().add( image );

        return div;
    }

    public static UIOutput createLocaleMarkerIcon(FacesContext facesContext, String locale)
    {
        HtmlDiv div = new HtmlDiv();

        HtmlGraphicImage image = (HtmlGraphicImage) facesContext.getApplication()
                .createComponent( HtmlGraphicImage.COMPONENT_TYPE );

        if (locale == null)
        {
            locale = "new";
        }

        String path = SettingsUtils.getComponentProperty( locale + "_locale_image_location" );

        if (path == null)
        {
            path = SettingsUtils.getComponentProperty( "unknown_locale_image_location" );
        }

        if (path.startsWith( Settings.INTERNAL_RESOURCE_PREFIX ))
        {
            path = path.substring( Settings.INTERNAL_RESOURCE_PREFIX.length(), path.length() );

            AddResource addresource = AddResourceFactory.getInstance( facesContext );
            image.setValue( addresource.getResourceUri( facesContext, HtmlTranslationPaneRenderer.class, path ) );
        }
        else
        {
            image.setValue( path );
        }

        image.getAttributes().put( "width", SettingsUtils.getComponentProperty( "locale_image_width" ) );
        image.getAttributes().put( "height", SettingsUtils.getComponentProperty( "locale_image_height" ) );
        image.getAttributes().put( "border", SettingsUtils.getComponentProperty( "locale_image_border" ) );

        div.getChildren().add( image );

        return div;
    }

    /**
     * Method for ValueChangeListener
     * Write translation back into managed beans
     */
    public static void changeMessage(ValueChangeEvent event)
    {
        String newValue = TranslationUtils.TRANSLATION_MARKER_TRANSLATED + event.getNewValue();

        FacesContext context = FacesContext.getCurrentInstance();

        boolean found = false;
        UIInput foundELInfoComponent = null;

        /*
         * search for hidden field, which contains the el-term as value
         */
        Iterator ci = event.getComponent().getParent().getChildren().iterator();

        while (ci.hasNext())
        {
            Object current = ci.next();

            UIComponent c = (UIComponent) current;
            if (found)
            {
                // hidden field
                foundELInfoComponent = (UIInput) c;
                break;
            }
            if (event.getComponent().getClientId( context ).equals( c.getClientId( context ) ))
            {
                found = true;
            }
        }

        try
        {
            if (found)
            {
                /*
                 * tetrieve and check found el-term
                 */
                String elTerm = foundELInfoComponent.getValue().toString();
                if (!TranslationELUtils.isResolvableELTerm( elTerm ))
                {
                    elTerm = TranslationELUtils.createCustomizedELTerm( elTerm );
                }

                if (elTerm == null)
                {
                    throw new NullPointerException();
                }

                /*
                 * process complex el-terms
                 */
                // if elTerm = #{[parent_value_binding]}:[var_name] -> separate content
                if (TranslationELUtils.isCustomizedEL( elTerm ))
                {
                    String rewrittenELTerm = TranslationELUtils.separateELTermOfCustomizedEL( elTerm );
                    String varName = TranslationELUtils.separateVarCustomizedEL( elTerm );

                    Object tmp = context.getApplication().createValueBinding( rewrittenELTerm ).getValue( context );
                    if (tmp instanceof List)
                    {
                        List content = (List) tmp;

                        // change list content
                        // overwrite equal entries with the same translation
                        Iterator current = content.iterator();
                        while (current.hasNext())
                        {
                            Object o = current.next();

                            if (o instanceof MessageResolver)
                            {
                                MessageResolver msgResolver = (MessageResolver) o;
                                String oldMsg = msgResolver.getMessage( varName, LocaleUtils.parseLocale( LocaleUtils.getLocale( HtmlTargetLocaleSelectOneMenuTag.ID ) ) );
                                if (oldMsg.equals( event.getOldValue() ))
                                {
                                    msgResolver.setMessage( varName, newValue, LocaleUtils.parseLocale( LocaleUtils.getLocale( HtmlTargetLocaleSelectOneMenuTag.ID ) ) );
                                }
                            }
                        }
                    }
                    else
                    {
                        throw new NullPointerException();
                    }
                }
                /*
                 * process simple el-terms
                 */
                // simple components - reset value binding content
                else
                {
                    Locale currentLocale = (Locale) context.getViewRoot().getLocale().clone();

                    // reset locale to target language
                    context.getViewRoot().setLocale( LocaleUtils.parseLocale( LocaleUtils.getLocale( HtmlTargetLocaleSelectOneMenuTag.ID ) ) );
                    // set value binding in target language
                    context.getApplication().createValueBinding( foundELInfoComponent.getValue().toString() ).setValue( context, newValue );
                    // restore working locale
                    context.getViewRoot().setLocale( currentLocale );
                }

            }
        }
        catch (Exception e)
        {
            String msg = "translation not possible";
            context.addMessage( null, new FacesMessage( FacesMessage.SEVERITY_ERROR, msg, msg ) );
        }

        context.renderResponse();
    }

    public static boolean isEmtyString(String string)
    {
        return ( string == null || string.length() == 0 );
    }

    public static void switchTranslationMode(boolean switchOn)
    {
        // has to be a session map - to continue translation mode
        String key = SettingsUtils.getComponentProperty( "translation_mode_switch_name" );

        if (switchOn)
        {
            BeanUtils.setSessionBean( FacesContext.getCurrentInstance(), key, Boolean.TRUE );
        }
        else
        {
            // delete jtracc session infos
            BeanUtils.removeSessionBean( FacesContext.getCurrentInstance(), key );
            BeanUtils.removeSessionBean( FacesContext.getCurrentInstance(), "key_locale_mapping_key" );
            BeanUtils.removeSessionBean( FacesContext.getCurrentInstance(), "content_mapping_key" );
        }
    }

    // needed for: locale-marker functionality - only create locale-marker in translation mode
    public static boolean isTranslationMode()
    {
        // has to be a session map - to continue translation mode
        Map sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        String key = SettingsUtils.getComponentProperty( "translation_mode_switch_name" );

        if (!sessionMap.containsKey( key ) || sessionMap.get( key ).equals( Boolean.FALSE ))
        {
            return false;
        }
        return true;
    }

    public static Class loadClassForName(String name) throws ClassNotFoundException
    {
        try
        {
            // Try WebApp ClassLoader first
            return Class.forName( name,
                                  false, // do not initialize for faster startup
                                  Thread.currentThread().getContextClassLoader() );
        }
        catch (ClassNotFoundException ignore)
        {
            // fallback: Try ClassLoader for ClassUtils (i.e. the myfaces.jar lib)
            return Class.forName( name,
                                  false, // do not initialize for faster startup
                                  TranslationUtils.class.getClassLoader() );
        }
    }

    /**
     * @return root directory of the property files
     */
    public static String getMessagesLocation()
    {
        String messagesLocation = SettingsUtils.getComponentProperty( "messages_location" );
        return ( (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext() ).getRealPath( messagesLocation );
    }
}

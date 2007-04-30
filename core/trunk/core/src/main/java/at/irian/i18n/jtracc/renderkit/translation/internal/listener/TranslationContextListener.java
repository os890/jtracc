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
package at.irian.i18n.jtracc.renderkit.translation.internal.listener;

import org.apache.myfaces.context.servlet.ServletExternalContextImpl;
import at.irian.i18n.jtracc.custom.translationpane.HtmlTranslationPaneRenderer;
import at.irian.i18n.jtracc.Settings;
import at.irian.i18n.jtracc.renderkit.translation.HtmlTranslationCommandLink;
import at.irian.i18n.jtracc.renderkit.translation.model.HtmlTargetLocaleSelectOneMenu;
import at.irian.i18n.jtracc.renderkit.translation.internal.TranslationRenderKit;
import at.irian.i18n.jtracc.util.BeanUtils;
import at.irian.i18n.jtracc.util.TranslationUtils;
import at.irian.i18n.jtracc.util.SettingsUtils;
import at.irian.i18n.jtracc.util.LocaleUtils;
import org.apache.myfaces.jtracc.custom.dojolayouts.FloatingPaneBaseRenderer;
import at.irian.i18n.jtracc.renderkit.translation.internal.TranslationPropertyResolver;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.ExternalContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Locale;

public class TranslationContextListener implements ServletContextListener
{

    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        ApplicationFactory factory = (ApplicationFactory) FactoryFinder.getFactory( FactoryFinder.APPLICATION_FACTORY );
        Application appliation = factory.getApplication();
        appliation.setPropertyResolver( new TranslationPropertyResolver( appliation.getPropertyResolver() ) );
        LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory( FactoryFinder.LIFECYCLE_FACTORY );
        Lifecycle lifecycle = lifecycleFactory.getLifecycle( LifecycleFactory.DEFAULT_LIFECYCLE );
        lifecycle.addPhaseListener( new TranslationRenderKitListener() );
        RenderKitFactory renderFactory = (RenderKitFactory) FactoryFinder.getFactory( FactoryFinder.RENDER_KIT_FACTORY );

        appliation.addComponent( HtmlTranslationCommandLink.COMPONENT_TYPE, HtmlTranslationCommandLink.class.getName() );
        appliation.addComponent( HtmlTargetLocaleSelectOneMenu.COMPONENT_TYPE, HtmlTargetLocaleSelectOneMenu.class.getName() );

        appliation.addComponent( "org.apache.myfaces.FloatingPaneBase", "org.apache.myfaces.jtracc.custom.dojolayouts.FloatingPaneBase" );
        appliation.addComponent( "org.apache.myfaces.HtmlTranslationPane", "at.irian.i18n.jtracc.custom.translationpane.HtmlTranslationPane" );

        ExternalContext externalContext = new ServletExternalContextImpl( servletContextEvent.getServletContext(), null, null );

        // create Settings-Bean for component settings
        String configFile = externalContext.getInitParameter( Settings.WEB_XML_CONTEXT_PARAMETER_PARAM_NAME_EXT_CONFIG );

        Settings settings = new Settings( configFile );
        BeanUtils.setApplicationBean( externalContext, Settings.BEAN_NAME, settings );

        setupMessagesBean( externalContext, settings );

        TranslationRenderKit trk = new TranslationRenderKit( settings.getValue( "render_kit" ) );
        trk.addRenderer( "javax.faces.Output", "org.apache.myfaces.FloatingPaneBaseRenderer", new FloatingPaneBaseRenderer() );
        trk.addRenderer( "javax.faces.Output", "org.apache.myfaces.HtmlTranslationPaneRenderer", new HtmlTranslationPaneRenderer() );

        renderFactory.addRenderKit( TranslationRenderKit.TRANSLATION_RENDERKIT_ID, trk );
    }

    private void setupMessagesBean(ExternalContext externalContext, Settings settings)
    {
        String messagesBeanName = settings.getValue( "messages_bean_name" );

        Class mbcl;
        Object messagesBean = null;

        try
        {
            mbcl = TranslationUtils.loadClassForName( settings.getValue( messagesBeanName ) );
            messagesBean = mbcl.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        BeanUtils.setApplicationBean( externalContext, messagesBeanName, messagesBean );
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent)
    {
        //do nothing
    }
}
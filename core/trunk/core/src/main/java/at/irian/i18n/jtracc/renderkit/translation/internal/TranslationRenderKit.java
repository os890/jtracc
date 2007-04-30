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
package at.irian.i18n.jtracc.renderkit.translation.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import at.irian.i18n.jtracc.util.SettingsUtils;
import at.irian.i18n.jtracc.util.TranslationUtils;
import org.springframework.jtracc.util.ReflectionUtils;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The TranslationRenderKit implements javax.faces.render.RenderKit. Basically
 * it is a RenderKit wrapper and accepts the original RenderKit as constructor
 * argument. It delegates all methods to the parent RenderKit, except
 * getRender(family, rendererType). This method returns a
 * TranslationRendererWrapper if the Renderer shall be enhanced by translation
 * functionality.
 */
public class TranslationRenderKit extends RenderKit
{
    public static final String TRANSLATION_RENDERKIT_ID = "HTML_TOMAHAWK_TRANSLATION";

    private static Log log = LogFactory.getLog( TranslationRenderKit.class );

    // Name of the config parameter in web.xml that holds the concrete
    // rendererWrapper class to use.
    private final String RENDERER_WRAPPER_CONFIG_PARAM = "org.apache.myfaces.RENDERER_WRAPPER";

    // Wrapped RenderKit.
    private String wrappedRenderKit;

    public TranslationRenderKit(String renderKit)
    {
        this.wrappedRenderKit = renderKit;
    }

    public void beforeRenderResponse()
    {
        if (this.wrappedRenderKit.equals( "org.apache.myfaces.trinidad.core" ))
        {
            try
            {
                Class trinidadSupport = TranslationUtils.loadClassForName( "at.irian.i18n.jtracc.renderkit.translation.internal.util.TrinidadSupport" );
                Method initRenderingContext = trinidadSupport.getMethod( "initRenderingContext", null );
                ReflectionUtils.invokeMethod( initRenderingContext, trinidadSupport.newInstance() );
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            catch (InstantiationException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            catch (NoSuchMethodException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    // Do delegation to the wrapped RenderKit.
    public void addRenderer(String family, String rendererType,
                            Renderer renderer)
    {
        this.getWrappedRenderKit().addRenderer( family, rendererType, renderer );
    }

    /**
     * Determine whether the given Renderer should be affected by translation
     * functionality.
     *
     * @param componentFamily Renderer family
     * @param rendererType    Renderer type
     *
     * @return Original renderer or TranslationRendererWrapper when translation
     *         needs are detected
     */
    public Renderer getRenderer(String componentFamily, String rendererType)
    {
        if (componentFamily == null)
        {
            throw new NullPointerException( "component family must not be null." );
        }
        if (rendererType == null)
        {
            throw new NullPointerException( "renderer type must not be null." );
        }

        Renderer _renderer = this.getWrappedRenderKit().getRenderer(
                componentFamily, rendererType );

        if (_renderer == null)
        {
            throw new NullPointerException( "renderer must not be null." );
        }

        return this.calculateRendererWrapper( _renderer );
    }

    // Do delegation to the wrapped RenderKit.
    public ResponseStateManager getResponseStateManager()
    {
        return this.getWrappedRenderKit().getResponseStateManager();
    }

    // Do delegation to the wrapped RenderKit.
    public ResponseWriter createResponseWriter(Writer writer,
                                               String contentTypeList, String characterEncoding)
    {
        return this.getWrappedRenderKit().createResponseWriter( writer,
                                                                contentTypeList, characterEncoding );
    }

    // Do delegation to the wrapped RenderKit.
    public ResponseStream createResponseStream(OutputStream out)
    {
        return this.getWrappedRenderKit().createResponseStream( out );
    }

    private RenderKit getWrappedRenderKit()
    {
        RenderKitFactory renderKitFactory = (RenderKitFactory) FactoryFinder
                .getFactory( FactoryFinder.RENDER_KIT_FACTORY );

        return renderKitFactory.getRenderKit( FacesContext.getCurrentInstance(),
                                              this.wrappedRenderKit );
    }

    private String rendererWrapper;

    private TranslationRendererWrapper calculateRendererWrapper(
            Renderer renderer)
    {
        if (rendererWrapper == null)
        {
            rendererWrapper = FacesContext.getCurrentInstance()
                    .getExternalContext().getInitParameter(
                    RENDERER_WRAPPER_CONFIG_PARAM );

            if (TranslationUtils.isEmtyString( rendererWrapper ))
            {
                rendererWrapper = SettingsUtils.getComponentProperty( "renderer_wrapper_config_param" );
                log
                        .info(
                                "RendererWrapper specified in web.xml in parameter <"
                                + RENDERER_WRAPPER_CONFIG_PARAM
                                + "> cannot be instantiated!!! --> injecting default RendererWrapper " );
            }
        }
        try
        {
            Constructor _wrappedConstructor = TranslationUtils.loadClassForName( rendererWrapper )
                    .getDeclaredConstructor( new Class[]{Renderer.class} );

            return (TranslationRendererWrapper) _wrappedConstructor
                    .newInstance( new Object[]{renderer} );
        }
        catch (InstantiationException e)
        {
            log
                    .error(
                            "RendererWrapper specified in web.xml in parameter <"
                            + RENDERER_WRAPPER_CONFIG_PARAM
                            + "> cannot be instantiated!!! "
                            + "Please check that "
                            + rendererWrapper
                            + " extends TranslationRendererWrapper and "
                            + "provides only a public constructor with a single parameter (the original renderer to be wrapped)",
                            e );
        }
        catch (IllegalAccessException e)
        {
            log.error(
                    "RendererWrapper specified in web.xml in parameter <"
                    + RENDERER_WRAPPER_CONFIG_PARAM
                    + "> cannot be accessed!!! "
                    + "Please check accessibility for class "
                    + rendererWrapper, e );
        }
        catch (ClassNotFoundException e)
        {
            log.error( "RendererWrapper specified in web.xml in parameter <"
                       + RENDERER_WRAPPER_CONFIG_PARAM
                       + "> cannot be found!!! Please check existence of class "
                       + rendererWrapper, e );
        }
        catch (NoSuchMethodException e)
        {
            log
                    .error(
                            "RendererWrapper specified in web.xml in parameter <"
                            + RENDERER_WRAPPER_CONFIG_PARAM
                            + "> cannot be instantiated!!! "
                            + "Please check that "
                            + rendererWrapper
                            + " extends TranslationRendererWrapper and "
                            + "provides only a public constructor with a single parameter (the original renderer to be wrapped)",
                            e );
        }
        catch (InvocationTargetException e)
        {
            log
                    .error(
                            "RendererWrapper specified in web.xml in parameter <"
                            + RENDERER_WRAPPER_CONFIG_PARAM
                            + "> cannot be instantiated!!! "
                            + "Please check that "
                            + rendererWrapper
                            + " extends TranslationRendererWrapper and "
                            + "provides only a public constructor with a single parameter (the original renderer to be wrapped)",
                            e );
        }

        return new TranslationRendererWrapper( renderer );
    }

}

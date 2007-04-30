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
package at.irian.i18n.jtracc.custom.translationpane;

import at.irian.i18n.jtracc.custom.dojo.DojoUtils;
import at.irian.i18n.jtracc.Settings;
import at.irian.i18n.jtracc.util.SettingsUtils;
import org.apache.myfaces.jtracc.custom.dojolayouts.FloatingPaneBaseRenderer;
import org.apache.myfaces.jtracc.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.jtracc.renderkit.html.util.AddResource;
import org.apache.myfaces.jtracc.renderkit.html.HTML;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class HtmlTranslationPaneRenderer extends FloatingPaneBaseRenderer
{
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException
    {
        HtmlTranslationPane pane = (HtmlTranslationPane) component;
        if (pane.getIconSrc() == null)
        {

            String logoIconPath = SettingsUtils.getComponentProperty( "translation_pane_logo" );

            // config starts with jar: -> load resource from component
            if (logoIconPath.startsWith( Settings.INTERNAL_RESOURCE_PREFIX ))
            {
                logoIconPath = logoIconPath.substring( Settings.INTERNAL_RESOURCE_PREFIX.length(), logoIconPath.length() );

                AddResource addResource = AddResourceFactory.getInstance( context );
                pane.setIconSrc( addResource.getResourceUri( context, HtmlTranslationPaneRenderer.class, logoIconPath ) );
            }
            else
            {
                pane.setIconSrc( logoIconPath );
            }
        }
        super.encodeBegin( context, component );
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        //Regular floating pane
        super.encodeEnd( context, component );

        //Specialized addon
        HtmlTranslationPane pane = (HtmlTranslationPane) component;
        AddResource addResource = AddResourceFactory.getInstance( context );
        //String theme = ( pane.getTheme() != null ) ? pane.getTheme() : TranslationUtils.getComponentProperty( "theme" );

        //Add the dojo requires
        DojoUtils.addRequire( context, component, "dojo.html.layout" );
        DojoUtils.addRequire( context, component, "dojo.html.style" );
        DojoUtils.addRequire( context, component, "dojo.event.browser" );
        DojoUtils.addRequire( context, component, "dojo.string.extras" );

        //Write resource JS variables
        ResponseWriter out = context.getResponseWriter();
        out.startElement( HTML.SCRIPT_ELEM, component );
        out.writeAttribute( HTML.TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null );

        StringBuffer buffer = new StringBuffer();

        //Create the var for the floatingpane
        String floatingPaneVar = DojoUtils.calculateWidgetVarName( pane.getClientId( context ) );
        buffer.append( "var transPane = dojo.widget.manager.getWidgetById('" ).append( floatingPaneVar ).append( "');" );

        String translateIcon = SettingsUtils.getComponentProperty( "translation_image_translate" );
        String changedIcon = SettingsUtils.getComponentProperty( "translation_image_changed" );

        // config starts with jar: -> load resource from component
        if (translateIcon.startsWith( Settings.INTERNAL_RESOURCE_PREFIX ))
        {
            translateIcon = translateIcon.substring( Settings.INTERNAL_RESOURCE_PREFIX.length(), translateIcon.length() );
            buffer.append( "var transImage = '" ).append( addResource.getResourceUri( context, HtmlTranslationPaneRenderer.class, translateIcon ) ).append( "';" );
        }
        else
        {
            buffer.append( "var transImage = '" ).append( translateIcon ).append( "';" );
        }

        // config starts with jar: -> load resource from component
        if (changedIcon.startsWith( Settings.INTERNAL_RESOURCE_PREFIX ))
        {
            changedIcon = changedIcon.substring( Settings.INTERNAL_RESOURCE_PREFIX.length(), changedIcon.length() );
            buffer.append( "var transAltImage = '" ).append( addResource.getResourceUri( context, HtmlTranslationPaneRenderer.class, changedIcon ) ).append( "';" );
        }
        else
        {
            buffer.append( "var transAltImage = '" ).append( changedIcon ).append( "';" );
        }

        out.write( buffer.toString() );
        out.endElement( HTML.SCRIPT_ELEM );

        //Include resources
        String theme = SettingsUtils.getComponentProperty( "theme" );
        // config starts with jar: -> load resource from component
        if (theme.startsWith( Settings.INTERNAL_RESOURCE_PREFIX ))
        {
            theme = theme.substring( Settings.INTERNAL_RESOURCE_PREFIX.length(), theme.length() );
            addResource.addStyleSheet( context, AddResource.HEADER_BEGIN, HtmlTranslationPaneRenderer.class, SettingsUtils.getComponentProperty( "styles_location" ) + "/jtraccTheme_" + theme + ".css" );
        }
        else
        {
            out.startElement( HTML.SCRIPT_ELEM, component );
            out.writeAttribute( HTML.TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null );
            buffer = new StringBuffer().append( "dojo.html.insertCssFile(\"" + SettingsUtils.getComponentProperty( "styles_location" ) + "/jtraccTheme_" ).append( theme ).append( ".css\");" );
            out.write( buffer.toString() );
            out.endElement( HTML.SCRIPT_ELEM );
        }
        addResource.addJavaScriptHere( context, HtmlTranslationPaneRenderer.class, "javascript/translation.js" );
    }

}

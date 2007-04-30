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
package at.irian.i18n.jtracc.renderkit.translation.model;

import at.irian.i18n.jtracc.util.SettingsUtils;
import org.apache.myfaces.jtracc.renderkit.RendererUtils;
import org.apache.myfaces.jtracc.renderkit.html.HtmlRendererUtils;

import javax.faces.context.FacesContext;
import javax.faces.component.html.HtmlSelectOneMenu;
import java.io.IOException;

/**
 * Component to specify the target lacale for translation
 */
public class HtmlTargetLocaleSelectOneMenu extends HtmlSelectOneMenu
{

    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlTargetLocaleSelectOneMenu";

    public HtmlTargetLocaleSelectOneMenu()
    {
    }

    public void encodeEnd(FacesContext facesContext) throws IOException
    {
        RendererUtils.checkParamValidity( facesContext, this, null );

        facesContext.getResponseWriter().write( SettingsUtils.getComponentProperty( "html_target_locale_select_one_menu_label" ) );
        HtmlRendererUtils.renderMenu( facesContext, this, false );
    }

}

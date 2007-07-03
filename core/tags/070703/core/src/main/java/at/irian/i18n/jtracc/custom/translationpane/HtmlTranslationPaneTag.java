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

import org.apache.myfaces.jtracc.custom.dojolayouts.FloatingPaneTag;

import javax.faces.component.UIComponent;

public class HtmlTranslationPaneTag extends FloatingPaneTag
{
    public static final String ID = "_translationPaneId";

    public HtmlTranslationPaneTag()
    {
        super.setHasShadow( "true" );
        super.setDisplayCloseAction( "true" );
        super.setResizable( "false" );
        super.setStyle( "display: none; width: 400px; height: auto;" );
    }

    public String getId()
    {
        return ID;
    }

    public String getComponentType()
    {
        return HtmlTranslationPane.DEFAULT_COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return HtmlTranslationPane.DEFAULT_RENDERER_TYPE;
    }

    public void release()
    {
        super.release();
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties( component );
    }
}

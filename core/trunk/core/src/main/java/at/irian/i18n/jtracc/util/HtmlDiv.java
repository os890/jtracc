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

import org.apache.myfaces.jtracc.renderkit.RendererUtils;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class HtmlDiv extends UIOutput
{
    private String styleClass;
    private String style;

    public HtmlDiv()
    {
    }

    public HtmlDiv(String styleClass, String style)
    {
        this.styleClass = styleClass;
        this.style = style;
    }

    public boolean getRendersChildren()
    {
        return true;
    }

    public void encodeBegin(FacesContext context) throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement( "div", this );
        if (styleClass != null)
        {
            writer.writeAttribute( "class", styleClass, null );
        }
        if (style != null)
        {
            writer.writeAttribute( "style", style, null );
        }
    }

    public void encodeChildren(FacesContext context) throws IOException
    {
        RendererUtils.renderChildren(context, this);
    }

    public void encodeEnd(FacesContext context) throws IOException
    {
        context.getResponseWriter().endElement( "div" );
    }
}

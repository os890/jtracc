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
package at.irian.i18n.jtracc.components.filter;

import org.apache.myfaces.jtracc.custom.dojolayouts.FloatingPaneBase;

import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlPanelGroup;

public class ComponentClassFilterImpl implements ComponentClassFilter
{
    public boolean isExcludedObject(Object o)
    {
        return o == null ||
               o instanceof javax.faces.el.MethodBinding ||
               o instanceof HtmlPanelGrid ||
               o instanceof HtmlPanelGroup ||
               o instanceof javax.faces.component.html.HtmlForm ||
               o instanceof javax.faces.component.UIViewRoot ||
               o instanceof Number ||
               //o instanceof org.apache.myfaces.application.ApplicationImpl ||
               //o instanceof org.apache.myfaces.custom.div.Div ||
               //o instanceof org.apache.myfaces.custom.navmenu.htmlnavmenu.HtmlCommandNavigationItem ||
               o instanceof FloatingPaneBase;

    }
}

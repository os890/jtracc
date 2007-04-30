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

import org.apache.myfaces.jtracc.custom.dojolayouts.FloatingPaneBase;

import javax.faces.context.FacesContext;

public class HtmlTranslationPane extends FloatingPaneBase
{

    public static final String DEFAULT_COMPONENT_FAMILY = "javax.faces.Output";
    public static final String DEFAULT_COMPONENT_TYPE = "org.apache.myfaces.HtmlTranslationPane";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.HtmlTranslationPaneRenderer";

    public HtmlTranslationPane()
    {
        super();
        setRendererType( HtmlTranslationPane.DEFAULT_RENDERER_TYPE );
    }

    public String getFamily()
    {
        return HtmlTranslationPane.DEFAULT_COMPONENT_FAMILY;
    }

    public String getComponentType()
    {
        return HtmlTranslationPane.DEFAULT_COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return HtmlTranslationPane.DEFAULT_RENDERER_TYPE;
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[]) state;
        super.restoreState( context, values[0] );
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[2];
        values[0] = super.saveState( context );

        return values;
    }

    //Following getters won't be neccessary with patch TOMAHAWK-928
    public Boolean getConstrainToContainer()
    {
        return super.getConstrainToContainer();
    }

    public Boolean getDisplayCloseAction()
    {
        return super.getDisplayCloseAction();
    }

    public Boolean getDisplayMinimizeAction()
    {
        return super.getDisplayMinimizeAction();
    }

    public Boolean getHasShadow()
    {
        return super.getHasShadow();
    }

    public String getIconSrc()
    {
        return super.getIconSrc();
    }

    public Boolean getModal()
    {
        return super.getModal();
    }

    public Boolean getResizable()
    {
        return super.getResizable();
    }

    public String getTaskBarId()
    {
        return super.getTaskBarId();
    }

    public String getTitle()
    {
        return super.getTitle();
    }

    public Boolean getTitleBarDisplay()
    {
        return super.getTitleBarDisplay();
    }

    public String getWidgetId()
    {
        return super.getWidgetId();
    }

    public String getWidgetVar()
    {
        return super.getWidgetVar();
    }

    public String getWindowState()
    {
        return super.getWindowState();
    }
}

package at.irian.i18n.jtracc.components.filter;

import org.apache.myfaces.jtracc.custom.dojolayouts.FloatingPaneBase;

import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlPanelGroup;

/**
 * @author Gerhard Petracek
 */
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

package at.irian.i18n.jtracc.examples.filter;

import at.irian.i18n.jtracc.components.filter.*;

/**
 * @author Gerhard Petracek
 */
public class ComponentFilterExtensionImpl extends ComponentClassFilterImpl implements ComponentClassFilter
{
    public boolean isExcludedObject(Object o)
    {
        // call super.isExcludedObject( o ) to extend the standard filter
        return super.isExcludedObject( o ) ||
               // sample extension
               o instanceof javax.faces.component.html.HtmlCommandButton;
    }
}

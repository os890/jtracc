package at.irian.i18n.jtracc.examples.filter;

import at.irian.i18n.jtracc.components.filter.ComponentClassFilter;
import at.irian.i18n.jtracc.components.filter.ComponentClassFilterImpl;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlOutputLink;

/**
 * @author Gerhard Petracek
 */
public class DemoComponentFilterExtensionImpl extends ComponentClassFilterImpl implements ComponentClassFilter
{
    public boolean isExcludedObject(Object o)
    {
        // call super.isExcludedObject( o ) to extend the standard filter
        return super.isExcludedObject( o ) ||
               // sample extension
               o instanceof HtmlOutputText ||
               o instanceof HtmlOutputLink;
    }
}
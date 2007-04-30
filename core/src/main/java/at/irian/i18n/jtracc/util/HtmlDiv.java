package at.irian.i18n.jtracc.util;

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


    public void encodeEnd(FacesContext context) throws IOException
    {
        context.getResponseWriter().endElement( "div" );
    }
}

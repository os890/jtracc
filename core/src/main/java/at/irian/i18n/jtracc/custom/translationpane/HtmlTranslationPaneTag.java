package at.irian.i18n.jtracc.custom.translationpane;

import org.apache.myfaces.jtracc.custom.dojolayouts.FloatingPaneTag;

import javax.faces.component.UIComponent;

/**
 * @author Gerhard Petracek
 * @author Stefan Schuster
 */
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

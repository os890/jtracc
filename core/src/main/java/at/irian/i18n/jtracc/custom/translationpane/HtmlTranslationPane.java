package at.irian.i18n.jtracc.custom.translationpane;

import org.apache.myfaces.jtracc.custom.dojolayouts.FloatingPaneBase;

import javax.faces.context.FacesContext;

/**
 * @author Stefan Schuster
 */
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

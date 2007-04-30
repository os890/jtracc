package at.irian.i18n.jtracc.renderkit.translation.model;

import at.irian.i18n.jtracc.util.SettingsUtils;
import org.apache.myfaces.jtracc.renderkit.RendererUtils;
import org.apache.myfaces.jtracc.renderkit.html.HtmlRendererUtils;

import javax.faces.context.FacesContext;
import javax.faces.component.html.HtmlSelectOneMenu;
import java.io.IOException;

/**
 * Component to specify the target lacale for translation
 */
public class HtmlTargetLocaleSelectOneMenu extends HtmlSelectOneMenu
{

    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlTargetLocaleSelectOneMenu";

    public HtmlTargetLocaleSelectOneMenu()
    {
    }

    public void encodeEnd(FacesContext facesContext) throws IOException
    {
        RendererUtils.checkParamValidity( facesContext, this, null );

        facesContext.getResponseWriter().write( SettingsUtils.getComponentProperty( "html_target_locale_select_one_menu_label" ) );
        HtmlRendererUtils.renderMenu( facesContext, this, false );
    }

}

package at.irian.i18n.jtracc.renderkit.translation.taglib;

import at.irian.i18n.jtracc.util.SettingsUtils;
import at.irian.i18n.jtracc.util.TranslationUtils;
import at.irian.i18n.jtracc.util.FileUtils;
import at.irian.i18n.jtracc.renderkit.translation.HtmlTranslationCommandLink;
import org.apache.myfaces.jtracc.taglib.html.HtmlCommandLinkTag;

import javax.faces.component.UIComponent;
import java.io.IOException;

public class HtmlTranslationCommandLinkTag extends HtmlCommandLinkTag
{
    private String _immediate;
    private static boolean INIT_FINISHED = false;

    public HtmlTranslationCommandLinkTag()
    {
        super.setValue( SettingsUtils.getComponentProperty( "html_translation_command_link_tag_label" ) );

        if (!INIT_FINISHED)
        {
            String path = TranslationUtils.getMessagesLocation();

            try
            {
                FileUtils.initFile( path + "//www.irian.at" );
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            INIT_FINISHED = true;
        }
    }

    protected void setProperties(UIComponent uiComponent)
    {
        if (_immediate == null)
        {
            super.setImmediate( "true" ); // default
        }
        super.setProperties( uiComponent );
    }

    public String getComponentType()
    {
        return HtmlTranslationCommandLink.COMPONENT_TYPE;
    }

    public void setImmediate(String string)
    {
        _immediate = string;
    }
}

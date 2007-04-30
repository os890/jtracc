package at.irian.i18n.jtracc.renderkit.translation;

import javax.faces.event.ActionListener;
import javax.faces.component.html.HtmlCommandLink;

public class HtmlTranslationCommandLink extends HtmlCommandLink
{
    public static final String COMPONENT_TYPE = "at.irian.i18n.jtracc.HtmlTranslationCommandLink";

    private static final ActionListener _translationActionListener = new TranslationController.TranslationModeActionListener();

    public HtmlTranslationCommandLink()
    {
        super.addActionListener( _translationActionListener );
    }

}

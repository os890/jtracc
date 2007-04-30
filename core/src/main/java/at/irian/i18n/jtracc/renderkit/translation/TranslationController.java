package at.irian.i18n.jtracc.renderkit.translation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import at.irian.i18n.jtracc.util.TranslationUtils;
import at.irian.i18n.jtracc.util.LocaleUtils;
import at.irian.i18n.jtracc.renderkit.translation.internal.TranslationRenderKit;
import at.irian.i18n.jtracc.renderkit.translation.internal.listener.TranslationRenderKitListener;

import javax.faces.context.FacesContext;
import javax.faces.event.*;
import java.util.Locale;

public class TranslationController
{
    public static String MANAGED_BEAN_NAME = "myfaces_translationController";

    /**
     * Logger available to subclasses
     */
    protected final Log logger = LogFactory.getLog( getClass() );

    public static class TranslationModeActionListener implements ActionListener
    {
        public void processAction(ActionEvent actionEvent) throws AbortProcessingException
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();

            // ...see whether it must be activated or deactivated.
            String previousKey = (String) facesContext
                    .getExternalContext()
                    .getSessionMap()
                    .get(
                            TranslationRenderKitListener.PREVIOUS_RENDERKIT_VARIABLE_KEY );

            if (previousKey == null)
            {
                facesContext
                        .getExternalContext()
                        .getSessionMap()
                        .put(
                                TranslationRenderKitListener.PREVIOUS_RENDERKIT_VARIABLE_KEY,
                                facesContext.getViewRoot().getRenderKitId() );

                facesContext.getExternalContext().getSessionMap().put(
                        TranslationRenderKitListener.RENDERKIT_VARIABLE_KEY,
                        TranslationRenderKit.TRANSLATION_RENDERKIT_ID );

                // switch on
                TranslationUtils.switchTranslationMode( true );
            }
            else
            {
                facesContext
                        .getExternalContext()
                        .getSessionMap()
                        .put(
                                TranslationRenderKitListener.PREVIOUS_RENDERKIT_VARIABLE_KEY,
                                null );

                facesContext.getExternalContext().getSessionMap().put(
                        TranslationRenderKitListener.RENDERKIT_VARIABLE_KEY,
                        previousKey );

                // switch off
                TranslationUtils.switchTranslationMode( false );
            }
        }
    }

    public static class TranslationValueChangeListener implements
                                                       ValueChangeListener
    {
        public void processValueChange(ValueChangeEvent event)
                throws AbortProcessingException
        {
            FacesContext.getCurrentInstance().getViewRoot().setLocale(
                    LocaleUtils.parseLocale( (String) event.getNewValue() ) );

            FacesContext.getCurrentInstance().renderResponse();
        }

    }
}
package at.irian.i18n.jtracc.renderkit.translation.internal.listener;

import at.irian.i18n.jtracc.renderkit.translation.internal.TranslationRenderKit;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * On a navigation from page to page, the renderKitId is lost and the default
 * HTML_BASIC renderKitId is used. This listener allows to save the current
 * renderKitId (when different from the default one), so it can be restored
 * later from the new page.
 *
 * @author Enrique Medina
 */
public class TranslationRenderKitListener implements PhaseListener
{

    // Key for the session map entry that will hold the previous value of the
    // renderKitId.
    public static final String PREVIOUS_RENDERKIT_VARIABLE_KEY = "org.apache.myfaces.translation.previousRenderKit";
    // Key for the session map entry that will hold the value of the current
    // renderKitId.
    public static final String RENDERKIT_VARIABLE_KEY = "org.apache.myfaces.translation.actualRenderKit";

    /**
     * Once the view is rendered, save the current RenderKitId.
     */
    public void afterPhase(PhaseEvent event)
    {
        FacesContext facesContext = event.getFacesContext();

        if (facesContext.getViewRoot().getRenderKitId() != null)
        {
            facesContext.getExternalContext().getSessionMap().put(
                    TranslationRenderKitListener.RENDERKIT_VARIABLE_KEY,
                    facesContext.getViewRoot().getRenderKitId() );
        }
    }

    /**
     * Before the view is going to be rendered, inject the previous RenderKitId
     * in the current viewRoot.
     */
    public void beforePhase(PhaseEvent event)
    {
        FacesContext facesContext = event.getFacesContext();
        String currentRenderKitId = (String) facesContext.getExternalContext()
                .getSessionMap().get( RENDERKIT_VARIABLE_KEY );

        if (currentRenderKitId != null)
        {
            facesContext.getViewRoot().setRenderKitId( currentRenderKitId );
            if (currentRenderKitId.equals( TranslationRenderKit.TRANSLATION_RENDERKIT_ID ))
            {
                ( (TranslationRenderKit) event.getFacesContext().getRenderKit() ).beforeRenderResponse();
            }
        }
    }

    /**
     * Only listen to the "render response" JSF phase.
     */
    public PhaseId getPhaseId()
    {
        return PhaseId.RENDER_RESPONSE;
    }

}

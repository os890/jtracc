/*
 * jtracc - i18n JSF component library
 * Copyright 2007, IRIAN Solutions GmbH Vienna, Austria
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package at.irian.i18n.jtracc.renderkit.translation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import at.irian.i18n.jtracc.util.TranslationUtils;
import at.irian.i18n.jtracc.util.LocaleUtils;
import at.irian.i18n.jtracc.util.BeanUtils;
import at.irian.i18n.jtracc.util.SettingsUtils;
import at.irian.i18n.jtracc.renderkit.translation.internal.TranslationRenderKit;
import at.irian.i18n.jtracc.renderkit.translation.internal.listener.TranslationRenderKitListener;

import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.faces.component.UICommand;

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

            UICommand translationLink = (UICommand) BeanUtils.getRequestBean( FacesContext.getCurrentInstance(), HtmlTranslationCommandLink.class.getName());

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

                // workaround for myfaces 1.1.4
                if(translationLink != null)
                {
                    translationLink.setValue( SettingsUtils.getComponentProperty( "html_translation_command_link_tag_label_on" ) );
                }
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

                // workaround for myfaces 1.1.4
                if(translationLink != null)
                {
                    translationLink.setValue( SettingsUtils.getComponentProperty( "html_translation_command_link_tag_label_off" ) );
                }
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
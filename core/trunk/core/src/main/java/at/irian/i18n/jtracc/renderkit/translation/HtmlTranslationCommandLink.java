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

import at.irian.i18n.jtracc.util.SettingsUtils;
import at.irian.i18n.jtracc.util.TranslationUtils;
import at.irian.i18n.jtracc.util.BeanUtils;

import javax.faces.event.ActionListener;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class HtmlTranslationCommandLink extends HtmlCommandLink
{
    public static final String COMPONENT_TYPE = "at.irian.i18n.jtracc.HtmlTranslationCommandLink";

    private static final ActionListener _translationActionListener = new TranslationController.TranslationModeActionListener();

    public HtmlTranslationCommandLink()
    {
        super.addActionListener( _translationActionListener );

        // workaround for myfaces 1.1.4
        // (to check translation mode and choose the right label)
        // further usage - see TranslationController
        BeanUtils.setRequestBean( FacesContext.getCurrentInstance(), HtmlTranslationCommandLink.class.getName(), this);
    }


    public void encodeBegin(FacesContext context) throws IOException
    {
        // for myfaces 1.1.5
        if(TranslationUtils.isTranslationMode())
        {
            setValue( SettingsUtils.getComponentProperty( "html_translation_command_link_tag_label_on" ) );
        }
        else
        {
            setValue( SettingsUtils.getComponentProperty( "html_translation_command_link_tag_label_off" ) );
        }
        
        super.encodeBegin( context );    //To change body of overridden methods use File | Settings | File Templates.
    }
}

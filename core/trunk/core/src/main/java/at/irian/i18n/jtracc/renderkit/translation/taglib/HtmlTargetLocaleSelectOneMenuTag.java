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
package at.irian.i18n.jtracc.renderkit.translation.taglib;

import at.irian.i18n.jtracc.util.LocaleUtils;
import at.irian.i18n.jtracc.util.TranslationUtils;
import at.irian.i18n.jtracc.renderkit.translation.TranslationController;
import at.irian.i18n.jtracc.renderkit.translation.model.HtmlTargetLocaleSelectOneMenu;
import org.apache.myfaces.jtracc.taglib.html.HtmlSelectOneMenuTag;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Tag for the target locale menu
 *
 * @see HtmlTargetLocaleSelectOneMenu
 */
public class HtmlTargetLocaleSelectOneMenuTag extends HtmlSelectOneMenuTag
{
    public static final String ID = "_targetLocaleSelectOneMenuId";

    public HtmlTargetLocaleSelectOneMenuTag()
    {
    }

    public String getRendererType()
    {
        return null;
    }

    public String getComponentType()
    {
        return HtmlTargetLocaleSelectOneMenu.COMPONENT_TYPE;
    }

    public String getId()
    {
        return ID;
    }

    public String getImmediate()
    {
        return Boolean.TRUE.toString();
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties( component );

        ( (UIInput) component ).addValueChangeListener( new TranslationController.TranslationValueChangeListener() );

        // Create as many items as available locales.
        List _listItems = new ArrayList();
        for (Iterator iter = LocaleUtils.getSupportedLocales().iterator(); iter.hasNext();)
        {
            Locale supportedLocale = (Locale) iter.next();

            if (TranslationUtils.isEmtyString( supportedLocale.getCountry() ))
            {
                _listItems.add( new SelectItem( LocaleUtils.getLocaleString( supportedLocale ),
                                                supportedLocale.getDisplayLanguage() ) );
            }
            else
            {
                _listItems.add( new SelectItem( LocaleUtils.getLocaleString( supportedLocale ),
                                                supportedLocale.getDisplayLanguage() + " (" + supportedLocale.getCountry() + ")" ) );
            }
        }

        UISelectItems selItems = new UISelectItems();
        selItems.setValue( _listItems );
        component.getChildren().add( selItems );

        component.getAttributes().put( "onchange", "submit()" );
        component.getAttributes().put( "immediate", Boolean.TRUE );

        component.getAttributes().put(
                "value",
                FacesContext.getCurrentInstance().getViewRoot().getLocale()
                        .getLanguage() );
    }
}

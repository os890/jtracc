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

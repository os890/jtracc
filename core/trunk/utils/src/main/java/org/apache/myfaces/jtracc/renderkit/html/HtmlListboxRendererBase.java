/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.jtracc.renderkit.html;

import org.apache.myfaces.jtracc.renderkit.JSFAttr;
import org.apache.myfaces.jtracc.renderkit.RendererUtils;
import org.apache.myfaces.jtracc.config.MyfacesConfig;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.component.html.HtmlSelectManyListbox;
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import java.io.IOException;


/**
 * @author Thomas Spiegl (latest modification by $Author: grantsmith $)
 * @author Anton Koinov
 * @version $Revision: 472618 $ $Date: 2006-11-08 20:06:54 +0000 (Wed, 08 Nov 2006) $
 */
public class HtmlListboxRendererBase
        extends HtmlRenderer
{
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        org.apache.myfaces.jtracc.renderkit.RendererUtils.checkParamValidity(facesContext, uiComponent, null);

        if (uiComponent instanceof UISelectMany)
        {
            int size;
            if (uiComponent instanceof HtmlSelectManyListbox)
            {
                size = ((HtmlSelectManyListbox)uiComponent).getSize();
            }
            else
            {
                Integer i = (Integer)uiComponent.getAttributes().get(JSFAttr.SIZE_ATTR);
                size = i != null ? i.intValue() : 0;
            }
            HtmlRendererUtils.renderListbox(facesContext,
                                            (UISelectMany)uiComponent,
                                            isDisabled(facesContext, uiComponent),
                                            size);
        }
        else if (uiComponent instanceof HtmlSelectOneListbox)
        {
            int size;
            if (uiComponent instanceof HtmlSelectOneListbox)
            {
                size = ((HtmlSelectOneListbox)uiComponent).getSize();
            }
            else
            {
                Integer i = (Integer)uiComponent.getAttributes().get(JSFAttr.SIZE_ATTR);
                size = i != null ? i.intValue() : 0;
            }
            HtmlRendererUtils.renderListbox(facesContext,
                                            (UISelectOne)uiComponent,
                                            isDisabled(facesContext, uiComponent),
                                            size);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + uiComponent.getClass().getName());
        }
    }


    protected boolean isDisabled(FacesContext facesContext, UIComponent uiComponent)
    {
        //TODO: overwrite in extended HtmlListboxRenderer and check for enabledOnUserRole
        boolean disabled;
        boolean readonly;
        if (uiComponent instanceof HtmlSelectManyListbox)
        {
            disabled = ((HtmlSelectManyListbox)uiComponent).isDisabled();
            readonly = ((HtmlSelectManyListbox)uiComponent).isReadonly();
        }
        else if (uiComponent instanceof HtmlSelectOneListbox)
        {
            disabled = ((HtmlSelectOneListbox)uiComponent).isDisabled();
            readonly = ((HtmlSelectOneListbox)uiComponent).isReadonly();
        }
        else
        {
            disabled = RendererUtils.getBooleanAttribute(uiComponent, HTML.DISABLED_ATTR, false);
            readonly = RendererUtils.getBooleanAttribute(uiComponent, HTML.READONLY_ATTR, false);
        }
        if (!disabled && readonly) {
			disabled = MyfacesConfig.getCurrentInstance(facesContext
							.getExternalContext()).isReadonlyAsDisabledForSelect();
		}
        return disabled;
    }


    public void decode(FacesContext facesContext, UIComponent uiComponent)
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, null);

        if (uiComponent instanceof UISelectMany)
        {
            HtmlRendererUtils.decodeUISelectMany(facesContext, uiComponent);
        }
        else if (uiComponent instanceof UISelectOne)
        {
            HtmlRendererUtils.decodeUISelectOne(facesContext, uiComponent);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + uiComponent.getClass().getName());
        }
    }

    public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue) throws ConverterException
    {
        org.apache.myfaces.jtracc.renderkit.RendererUtils.checkParamValidity(facesContext, uiComponent, null);

        if (uiComponent instanceof UISelectMany)
        {
            return org.apache.myfaces.jtracc.renderkit.RendererUtils.getConvertedUISelectManyValue(facesContext,
                                                               (UISelectMany)uiComponent,
                                                               submittedValue);
        }
        else if (uiComponent instanceof UISelectOne)
        {
            return RendererUtils.getConvertedUIOutputValue(facesContext,
                                                           (UISelectOne)uiComponent,
                                                           submittedValue);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + uiComponent.getClass().getName());
        }
    }

}

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.jtracc.renderkit.JSFAttr;
import org.apache.myfaces.jtracc.renderkit.RendererUtils;
import org.apache.myfaces.jtracc.config.MyfacesConfig;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UISelectOne;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author Manfred Geiler (latest modification by $Author: grantsmith $)
 * @author Thomas Spiegl
 * @version $Revision: 472618 $ $Date: 2006-11-08 20:06:54 +0000 (Wed, 08 Nov 2006) $
 */
public class HtmlRadioRendererBase
        extends HtmlRenderer
{
    private static final Log log = LogFactory.getLog(HtmlRadioRendererBase.class);

    private static final String PAGE_DIRECTION = "pageDirection";
    private static final String LINE_DIRECTION = "lineDirection";

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        org.apache.myfaces.jtracc.renderkit.RendererUtils.checkParamValidity(facesContext, uiComponent, UISelectOne.class);

        UISelectOne selectOne = (UISelectOne)uiComponent;

        String layout = getLayout(selectOne);

        boolean pageDirectionLayout = false; // Defaults to LINE_DIRECTION
        if (layout != null)
        {
            if (layout.equals(PAGE_DIRECTION))
            {
                pageDirectionLayout = true;
            }
            else if (layout.equals(LINE_DIRECTION))
            {
                pageDirectionLayout = false;
            }
            else
            {
                log.error("Wrong layout attribute for component " + selectOne.getClientId(facesContext) + ": " + layout);
            }
        }

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.TABLE_ELEM, selectOne);
        HtmlRendererUtils.renderHTMLAttributes(writer, selectOne,
                                               HTML.SELECT_TABLE_PASSTHROUGH_ATTRIBUTES);
        HtmlRendererUtils.writeIdIfNecessary(writer, uiComponent, facesContext);

        if (!pageDirectionLayout) writer.startElement(HTML.TR_ELEM, selectOne);

        Converter converter;
        List selectItemList = org.apache.myfaces.jtracc.renderkit.RendererUtils.getSelectItemList(selectOne);
        converter = HtmlRendererUtils.findUIOutputConverterFailSafe(facesContext, selectOne);

        Object currentValue = org.apache.myfaces.jtracc.renderkit.RendererUtils.getObjectValue(selectOne);

        currentValue = org.apache.myfaces.jtracc.renderkit.RendererUtils.getConvertedStringValue(facesContext, selectOne, converter, currentValue);

        for (Iterator it = selectItemList.iterator(); it.hasNext(); )
        {
            SelectItem selectItem = (SelectItem)it.next();

            renderGroupOrItemRadio(facesContext, selectOne,
                                   selectItem, currentValue,
                                   converter, pageDirectionLayout);
        }

        if (!pageDirectionLayout) writer.endElement(HTML.TR_ELEM);
        writer.endElement(HTML.TABLE_ELEM);
    }


    protected String getLayout(UIComponent selectOne)
    {
        if (selectOne instanceof HtmlSelectOneRadio)
        {
            return ((HtmlSelectOneRadio)selectOne).getLayout();
        }

        return (String)selectOne.getAttributes().get(JSFAttr.LAYOUT_ATTR);
    }


    protected String getStyleClass(UISelectOne selectOne)
     {
         if (selectOne instanceof HtmlSelectOneRadio)
         {
             return ((HtmlSelectOneRadio)selectOne).getStyleClass();
         }

         return (String)selectOne.getAttributes().get(JSFAttr.STYLE_CLASS_ATTR);
     }


    protected void renderGroupOrItemRadio(FacesContext facesContext,
                                          UIComponent uiComponent, SelectItem selectItem,
                                          Object currentValue,
                                          Converter converter, boolean pageDirectionLayout) throws IOException{

        ResponseWriter writer = facesContext.getResponseWriter();

        boolean isSelectItemGroup = (selectItem instanceof SelectItemGroup);

        // TODO : Check here for getSubmittedValue. Look at RendererUtils.getValue
        // this is useless object creation
//    	Object itemValue = selectItem.getValue();

        UISelectOne selectOne = (UISelectOne)uiComponent;

        if (isSelectItemGroup) {
            if (pageDirectionLayout)
                writer.startElement(HTML.TR_ELEM, selectOne);

            writer.startElement(HTML.TD_ELEM, selectOne);
            writer.write(selectItem.getLabel());
            writer.endElement(HTML.TD_ELEM);

            if (pageDirectionLayout) {
                writer.endElement(HTML.TR_ELEM);
                writer.startElement(HTML.TR_ELEM, selectOne);
            }
            writer.startElement(HTML.TD_ELEM, selectOne);

            writer.startElement(HTML.TABLE_ELEM, selectOne);
            writer.writeAttribute(HTML.BORDER_ATTR, "0", null);

            SelectItemGroup group = (SelectItemGroup) selectItem;
            SelectItem[] selectItems = group.getSelectItems();

            for (int i=0; i<selectItems.length; i++) {
                renderGroupOrItemRadio(facesContext, selectOne, selectItems[i], currentValue, converter, pageDirectionLayout);
            }

            writer.endElement(HTML.TD_ELEM);
            writer.endElement(HTML.TR_ELEM);
            writer.endElement(HTML.TABLE_ELEM);
            writer.endElement(HTML.TD_ELEM);

            if (pageDirectionLayout)
                writer.endElement(HTML.TR_ELEM);

        } else {

        writer.write("\t\t");
        if (pageDirectionLayout)
            writer.startElement(HTML.TR_ELEM, selectOne);
        writer.startElement(HTML.TD_ELEM, selectOne);
        writer.startElement(HTML.LABEL_ELEM, selectOne);

        String itemStrValue = org.apache.myfaces.jtracc.renderkit.RendererUtils.getConvertedStringValue(facesContext, selectOne, converter, selectItem.getValue());
        boolean itemDisabled = selectItem.isDisabled();

        boolean itemChecked = itemStrValue.equals(currentValue);

        String labelClass;
        boolean componentDisabled = isDisabled(facesContext, selectOne);

        if (componentDisabled || itemDisabled) {
            labelClass = (String) selectOne.getAttributes().get(JSFAttr.DISABLED_CLASS_ATTR);
        } else {
            labelClass = (String) selectOne.getAttributes().get(JSFAttr.ENABLED_CLASS_ATTR);
        }
        if (labelClass != null) {
            writer.writeAttribute("class", labelClass, "labelClass");
        }

        renderRadio(facesContext, selectOne, itemStrValue, selectItem
                .getLabel(), itemDisabled, itemChecked, false);
        writer.endElement(HTML.LABEL_ELEM);
        writer.endElement(HTML.TD_ELEM);
        if (pageDirectionLayout)
            writer.endElement(HTML.TR_ELEM);
        }
    }

    protected void renderRadio(FacesContext facesContext,
                               UIComponent uiComponent,
                               String value,
                               String label,
                               boolean disabled,
                               boolean checked, boolean renderId)
            throws IOException
    {
        String clientId = uiComponent.getClientId(facesContext);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_RADIO, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);

        if (renderId) {
            writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        }

        if (checked)
        {
            writer.writeAttribute(HTML.CHECKED_ATTR, HTML.CHECKED_ATTR, null);
        }

        if ((value != null) && (value.length() > 0))
        {
            writer.writeAttribute(HTML.VALUE_ATTR, value, null);
        }

        HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
        if (disabled || isDisabled(facesContext, uiComponent))
        {
            writer.writeAttribute(HTML.DISABLED_ATTR, HTML.DISABLED_ATTR, null);
        }

        writer.endElement(HTML.INPUT_ELEM);

        if ((label != null) && (label.length() > 0))
        {
            writer.write(HTML.NBSP_ENTITY);
            writer.writeText(label, null);
        }
    }


    protected boolean isDisabled(FacesContext facesContext, UIComponent uiComponent)
    {
        //TODO: overwrite in extended HtmlRadioRenderer and check for enabledOnUserRole
        boolean disabled;
        boolean readonly;
        if (uiComponent instanceof HtmlSelectOneRadio)
        {
            disabled = ((HtmlSelectOneRadio) uiComponent).isDisabled();
            readonly = ((HtmlSelectOneRadio) uiComponent).isReadonly();
        }
        else
        {
            disabled = RendererUtils.getBooleanAttribute(uiComponent,
                    HTML.DISABLED_ATTR, false);
            readonly = RendererUtils.getBooleanAttribute(uiComponent,
                    HTML.READONLY_ATTR, false);
        }
 
        if (!disabled && readonly)
        {
			disabled = MyfacesConfig.getCurrentInstance(facesContext
							.getExternalContext()).isReadonlyAsDisabledForSelect();
        }
        return disabled;
    }


    public void decode(FacesContext facesContext, UIComponent uiComponent)
    {
        org.apache.myfaces.jtracc.renderkit.RendererUtils.checkParamValidity(facesContext, uiComponent, null);
        if (uiComponent instanceof EditableValueHolder)
        {
            HtmlRendererUtils.decodeUISelectOne(facesContext, uiComponent);
        }
    }


    public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue) throws ConverterException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UIOutput.class);
        return org.apache.myfaces.jtracc.renderkit.RendererUtils.getConvertedUIOutputValue(facesContext,
                                                                                           (UIOutput)uiComponent,
                                                                                           submittedValue);
    }

}

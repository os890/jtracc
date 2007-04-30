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
import org.apache.myfaces.jtracc.config.MyfacesConfig;
import org.apache.myfaces.jtracc.renderkit.JSFAttr;
import org.apache.myfaces.jtracc.renderkit.RendererUtils;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectMany;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectManyCheckbox;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Thomas Spiegl (latest modification by $Author: grantsmith $)
 * @author Anton Koinov
 * @version $Revision: 472618 $ $Date: 2006-11-08 20:06:54 +0000 (Wed, 08 Nov 2006) $
 */
public class HtmlCheckboxRendererBase extends HtmlRenderer {
    private static final Log log = LogFactory
            .getLog(HtmlCheckboxRendererBase.class);

    private static final String PAGE_DIRECTION = "pageDirection";

    private static final String LINE_DIRECTION = "lineDirection";

    private static final String EXTERNAL_TRUE_VALUE = "true";

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        org.apache.myfaces.jtracc.renderkit.RendererUtils.checkParamValidity(facesContext, uiComponent, null);
        if (uiComponent instanceof UISelectBoolean) {
            Boolean value = org.apache.myfaces.jtracc.renderkit.RendererUtils.getBooleanValue( uiComponent );
            boolean isChecked = value != null ? value.booleanValue() : false;
            renderCheckbox(facesContext, uiComponent, EXTERNAL_TRUE_VALUE,
                    null, false,isChecked, true);
        } else if (uiComponent instanceof UISelectMany) {
            renderCheckboxList(facesContext, (UISelectMany) uiComponent);
        } else {
            throw new IllegalArgumentException("Unsupported component class "
                    + uiComponent.getClass().getName());
        }
    }

    public void renderCheckboxList(FacesContext facesContext,
            UISelectMany selectMany) throws IOException {

        String layout = getLayout(selectMany);
        boolean pageDirectionLayout = false; //Default to lineDirection
        if (layout != null) {
            if (layout.equals(PAGE_DIRECTION)) {
                pageDirectionLayout = true;
            } else if (layout.equals(LINE_DIRECTION)) {
                pageDirectionLayout = false;
            } else {
                log.error("Wrong layout attribute for component "
                        + selectMany.getClientId(facesContext) + ": " + layout);
            }
        }

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.TABLE_ELEM, selectMany);
        HtmlRendererUtils.renderHTMLAttributes(writer, selectMany,
                HTML.SELECT_TABLE_PASSTHROUGH_ATTRIBUTES);
        HtmlRendererUtils.writeIdIfNecessary(writer, selectMany, facesContext);

        if (!pageDirectionLayout)
            writer.startElement(HTML.TR_ELEM, selectMany);
        
        Converter converter;
        try {
            converter = org.apache.myfaces.jtracc.renderkit.RendererUtils.findUISelectManyConverter(facesContext,
                    selectMany);
        } catch (FacesException e) {
            log.error("Error finding Converter for component with id "
                    + selectMany.getClientId(facesContext));
            converter = null;
        }

        Set lookupSet = org.apache.myfaces.jtracc.renderkit.RendererUtils.getSubmittedValuesAsSet(facesContext, selectMany, converter, selectMany);
        boolean useSubmittedValues = lookupSet != null;

        if (!useSubmittedValues) {
            lookupSet = org.apache.myfaces.jtracc.renderkit.RendererUtils.getSelectedValuesAsSet(facesContext, selectMany, converter, selectMany);
        }

        for (Iterator it = org.apache.myfaces.jtracc.renderkit.RendererUtils.getSelectItemList(selectMany)
                .iterator(); it.hasNext();) {
            SelectItem selectItem = (SelectItem) it.next();
            
            renderGroupOrItemCheckbox(facesContext, selectMany, 
            		selectItem, useSubmittedValues, lookupSet, 
            		converter, pageDirectionLayout);
        }

        if (!pageDirectionLayout)
            writer.endElement(HTML.TR_ELEM);
        writer.endElement(HTML.TABLE_ELEM);
    }

    protected String getLayout(UISelectMany selectMany) {
        if (selectMany instanceof HtmlSelectManyCheckbox) {
            return ((HtmlSelectManyCheckbox) selectMany).getLayout();
        } else {
            return (String) selectMany.getAttributes().get(JSFAttr.LAYOUT_ATTR);
        }
    }
    
    protected void renderGroupOrItemCheckbox(FacesContext facesContext,
    		UIComponent uiComponent, SelectItem selectItem,
    		boolean useSubmittedValues, Set lookupSet,
    		Converter converter, boolean pageDirectionLayout) throws IOException{
    	
    	ResponseWriter writer = facesContext.getResponseWriter();
    	
    	boolean isSelectItemGroup = (selectItem instanceof SelectItemGroup);
    	
    	Object itemValue = selectItem.getValue(); // TODO : Check here for getSubmittedValue. Look at RendererUtils.getValue

    	UISelectMany selectMany = (UISelectMany)uiComponent;
    	
        String itemStrValue;
        if (converter == null) {
            itemStrValue = itemValue.toString();
        } else {
            itemStrValue = converter.getAsString(facesContext, selectMany,
                    itemValue);
        }

        if (isSelectItemGroup) {
        	if (pageDirectionLayout)
                writer.startElement(HTML.TR_ELEM, selectMany);
        	
        	writer.startElement(HTML.TD_ELEM, selectMany);
        	writer.write(selectItem.getLabel());
        	writer.endElement(HTML.TD_ELEM);
        	
        	if (pageDirectionLayout) {
	        	writer.endElement(HTML.TR_ELEM);
	        	writer.startElement(HTML.TR_ELEM, selectMany);
        	}
        	writer.startElement(HTML.TD_ELEM, selectMany);
        	
        	writer.startElement(HTML.TABLE_ELEM, selectMany);
        	writer.writeAttribute(HTML.BORDER_ATTR, "0", null);

        	SelectItemGroup group = (SelectItemGroup) selectItem;
        	SelectItem[] selectItems = group.getSelectItems();
        	
        	for (int i=0; i<selectItems.length; i++) {
        		renderGroupOrItemCheckbox(facesContext, selectMany, selectItems[i], useSubmittedValues, lookupSet, converter, pageDirectionLayout);
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
            writer.startElement(HTML.TR_ELEM, selectMany);
        writer.startElement(HTML.TD_ELEM, selectMany);
        writer.startElement(HTML.LABEL_ELEM, selectMany);

        boolean checked = lookupSet.contains(itemStrValue); 

        boolean disabled = selectItem.isDisabled();
        

        String labelClass = null;
        boolean componentDisabled = isDisabled(facesContext, selectMany);

        if (componentDisabled || disabled) {
            labelClass = (String) selectMany.getAttributes().get(JSFAttr.DISABLED_CLASS_ATTR);
        } else {
            labelClass = (String) selectMany.getAttributes().get(org.apache.myfaces.jtracc.renderkit.JSFAttr.ENABLED_CLASS_ATTR);
        }
        if (labelClass != null) {
            writer.writeAttribute("class", labelClass, "labelClass");
        }

            renderCheckbox(facesContext, selectMany, itemStrValue, selectItem
                .getLabel(), disabled, checked, false);

        writer.endElement(HTML.LABEL_ELEM);
        writer.endElement(HTML.TD_ELEM);
        if (pageDirectionLayout)
            writer.endElement(HTML.TR_ELEM);
        }
    }

    protected void renderCheckbox(FacesContext facesContext,
            UIComponent uiComponent, String value, String label,
            boolean disabled, boolean checked, boolean renderId) throws IOException {
        String clientId = uiComponent.getClientId(facesContext);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_CHECKBOX, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        
        if (renderId) {
            HtmlRendererUtils.writeIdIfNecessary(writer, uiComponent, facesContext);
        }

        if (checked) {
            writer.writeAttribute(HTML.CHECKED_ATTR, org.apache.myfaces.jtracc.renderkit.html.HTML.CHECKED_ATTR, null);
        }
        
        if ((value != null) && (value.length() > 0)) {
            writer.writeAttribute(HTML.VALUE_ATTR, value, null);
        }

        HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent,
                HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
        if (disabled || isDisabled(facesContext, uiComponent)) {
        	writer.writeAttribute(HTML.DISABLED_ATTR, HTML.DISABLED_ATTR, null);
        }
        
        writer.endElement(HTML.INPUT_ELEM);
        
        if ((label != null) && (label.length() > 0)) {
            writer.write(HTML.NBSP_ENTITY);
            writer.writeText(label, null);
        }
    }

    protected boolean isDisabled(FacesContext facesContext,
            UIComponent component) {
        //TODO: overwrite in extended HtmlCheckboxRenderer and check for
        // enabledOnUserRole
        boolean disabled;
        boolean readonly;
        if (component instanceof HtmlSelectBooleanCheckbox) {
            disabled = ((HtmlSelectBooleanCheckbox) component).isDisabled();
            readonly = ((HtmlSelectBooleanCheckbox) component).isReadonly();
        } else if (component instanceof HtmlSelectManyCheckbox) {
            disabled = ((HtmlSelectManyCheckbox) component).isDisabled();
            readonly = ((HtmlSelectManyCheckbox) component).isReadonly();
        } else {
            disabled = RendererUtils.getBooleanAttribute(component,
                    HTML.DISABLED_ATTR, false);
            readonly = RendererUtils.getBooleanAttribute(component,
                    HTML.READONLY_ATTR, false);
        }
        if (!disabled && readonly) {
			disabled = MyfacesConfig.getCurrentInstance(facesContext
							.getExternalContext()).isReadonlyAsDisabledForSelect();
        }
        return disabled;
    }

    public void decode(FacesContext facesContext, UIComponent component) {
        org.apache.myfaces.jtracc.renderkit.RendererUtils.checkParamValidity(facesContext, component, null);
        if (component instanceof UISelectBoolean) {
            HtmlRendererUtils.decodeUISelectBoolean(facesContext, component);
        } else if (component instanceof UISelectMany) {
            HtmlRendererUtils.decodeUISelectMany(facesContext, component);
        } else {
            throw new IllegalArgumentException("Unsupported component class "
                    + component.getClass().getName());
        }
    }

    public Object getConvertedValue(FacesContext facesContext,
            UIComponent component, Object submittedValue)
            throws ConverterException {
        org.apache.myfaces.jtracc.renderkit.RendererUtils.checkParamValidity(facesContext, component, null);
        if (component instanceof UISelectBoolean) {
            return submittedValue;
        } else if (component instanceof UISelectMany) {
            return org.apache.myfaces.jtracc.renderkit.RendererUtils.getConvertedUISelectManyValue(facesContext,
                    (UISelectMany) component, submittedValue);
        } else {
            throw new IllegalArgumentException("Unsupported component class "
                    + component.getClass().getName());
        }
    }
}

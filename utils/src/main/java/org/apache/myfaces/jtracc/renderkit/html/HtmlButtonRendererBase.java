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

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import org.apache.myfaces.jtracc.config.MyfacesConfig;
import org.apache.myfaces.jtracc.renderkit.JSFAttr;
import org.apache.myfaces.jtracc.renderkit.html.util.FormInfo;
import org.apache.myfaces.jtracc.renderkit.html.util.JavascriptUtils;
import org.apache.myfaces.jtracc.renderkit.RendererUtils;


/**
 * @author Manfred Geiler (latest modification by $Author: mmarinschek $)
 * @author Thomas Spiegl
 * @author Anton Koinov
 * @version $Revision: 513793 $ $Date: 2007-03-02 14:59:27 +0000 (Fri, 02 Mar 2007) $
 */
public class HtmlButtonRendererBase
    extends HtmlRenderer {
    private static final String IMAGE_BUTTON_SUFFIX_X = ".x";
    private static final String IMAGE_BUTTON_SUFFIX_Y = ".y";

    public static final String ACTION_FOR_LIST = "org.apache.myfaces.ActionForList";

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        org.apache.myfaces.jtracc.renderkit.RendererUtils.checkParamValidity(facesContext, uiComponent, UICommand.class);

        //super.decode must not be called, because value is handled here
        if (!isReset(uiComponent) && isSubmitted(facesContext, uiComponent)) {
            uiComponent.queueEvent(new ActionEvent(uiComponent));

            org.apache.myfaces.jtracc.renderkit.RendererUtils.initPartialValidationAndModelUpdate(uiComponent, facesContext);
        }
    }

    private static boolean isReset(UIComponent uiComponent) {
        return "reset".equalsIgnoreCase((String) uiComponent.getAttributes().get(HTML.TYPE_ATTR));
    }

    private static boolean isSubmitted(FacesContext facesContext, UIComponent uiComponent) {
        String clientId = uiComponent.getClientId(facesContext);
        Map paramMap = facesContext.getExternalContext().getRequestParameterMap();
        return paramMap.containsKey(clientId) || paramMap.containsKey(clientId + IMAGE_BUTTON_SUFFIX_X) || paramMap.containsKey(clientId + IMAGE_BUTTON_SUFFIX_Y);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException {
        org.apache.myfaces.jtracc.renderkit.RendererUtils.checkParamValidity(facesContext, uiComponent, UICommand.class);

        String clientId = uiComponent.getClientId(facesContext);

        ResponseWriter writer = facesContext.getResponseWriter();

        HtmlRendererUtils.renderFormSubmitScript(facesContext);

        writer.startElement(HTML.INPUT_ELEM, uiComponent);

        writer.writeAttribute(HTML.ID_ATTR, clientId, org.apache.myfaces.jtracc.renderkit.JSFAttr.ID_ATTR);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, JSFAttr.ID_ATTR);

        String image = getImage(uiComponent);

        ExternalContext externalContext = facesContext.getExternalContext();

        if (image != null) {
            writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_IMAGE, org.apache.myfaces.jtracc.renderkit.JSFAttr.TYPE_ATTR);
            String src = facesContext.getApplication().getViewHandler().getResourceURL(
                facesContext, image);
            writer.writeURIAttribute(HTML.SRC_ATTR, externalContext.encodeResourceURL(src),
                                     org.apache.myfaces.jtracc.renderkit.JSFAttr.IMAGE_ATTR);
        }
        else {
            String type = getType(uiComponent);

            if (type == null) {
                type = HTML.INPUT_TYPE_SUBMIT;
            }
            writer.writeAttribute(HTML.TYPE_ATTR, type, org.apache.myfaces.jtracc.renderkit.JSFAttr.TYPE_ATTR);
            Object value = getValue(uiComponent);
            if (value != null) {
                writer.writeAttribute(org.apache.myfaces.jtracc.renderkit.html.HTML.VALUE_ATTR, value, org.apache.myfaces.jtracc.renderkit.JSFAttr.VALUE_ATTR);
            }
        }
        if (JavascriptUtils.isJavascriptAllowed(externalContext)) {
            StringBuffer onClick = buildOnClick(uiComponent, facesContext, writer);
            writer.writeAttribute(HTML.ONCLICK_ATTR, onClick.toString(), null);
            HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent,
                                                   HTML.BUTTON_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED_AND_ONCLICK);
        }
        else {
            HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent,
                                                   HTML.BUTTON_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
        }

        if (isDisabled(facesContext, uiComponent)) {
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, org.apache.myfaces.jtracc.renderkit.JSFAttr.DISABLED_ATTR);
        }

        writer.endElement(HTML.INPUT_ELEM);

        HtmlFormRendererBase.renderScrollHiddenInputIfNecessary(
            findNestingForm(uiComponent, facesContext).getForm(), facesContext, writer);
    }


    protected StringBuffer buildOnClick(UIComponent uiComponent, FacesContext facesContext, ResponseWriter writer)
        throws IOException {
        FormInfo formInfo = findNestingForm(uiComponent, facesContext);
        if (formInfo == null) {
            throw new IllegalArgumentException("Component " + uiComponent.getClientId(facesContext) + " must be embedded in an form");
        }
        String formName = formInfo.getFormName();
        UIComponent nestingForm = formInfo.getForm();

        StringBuffer onClick = new StringBuffer();
        String commandOnClick = (String) uiComponent.getAttributes().get(HTML.ONCLICK_ATTR);

        if (commandOnClick != null) {
            onClick.append(commandOnClick);
            onClick.append(';');
        }

        //call the script to clear the form (clearFormHiddenParams_<formName>) method
        HtmlRendererUtils.appendClearHiddenCommandFormParamsFunctionCall(onClick, formName);

        if (MyfacesConfig.getCurrentInstance(facesContext.getExternalContext()).isAutoScroll()) {
            HtmlRendererUtils.appendAutoScrollAssignment(onClick, formName);
        }

        //add hidden field for the case there is no commandLink in the form
        String hiddenFieldName = HtmlRendererUtils.getHiddenCommandLinkFieldName(formInfo);
        addHiddenCommandParameter(facesContext, nestingForm, hiddenFieldName);

        //add hidden field for the case there is no commandLink in the form
        String hiddenFieldNameMyFacesOld = HtmlRendererUtils.getHiddenCommandLinkFieldNameMyfacesOld(formInfo);
        addHiddenCommandParameter(facesContext, nestingForm, hiddenFieldNameMyFacesOld);

        return onClick;
    }

    protected void addHiddenCommandParameter(FacesContext facesContext, UIComponent nestingForm, String hiddenFieldName) {
        if (nestingForm != null) {
            HtmlFormRendererBase.addHiddenCommandParameter(facesContext, nestingForm, hiddenFieldName);
        }
    }

    /**
     * find nesting form<br />
     * need to be overrideable to deal with dummyForm stuff in tomahawk.
     */
    protected FormInfo findNestingForm(UIComponent uiComponent, FacesContext facesContext) {
        return RendererUtils.findNestingForm(uiComponent, facesContext);
    }

    protected boolean isDisabled(FacesContext facesContext, UIComponent uiComponent) {
        //TODO: overwrite in extended HtmlButtonRenderer and check for enabledOnUserRole
        if (uiComponent instanceof HtmlCommandButton) {
            return ((HtmlCommandButton) uiComponent).isDisabled();
        }
        else {
            return org.apache.myfaces.jtracc.renderkit.RendererUtils.getBooleanAttribute(uiComponent, HTML.DISABLED_ATTR, false);
        }
    }


    private String getImage(UIComponent uiComponent) {
        if (uiComponent instanceof HtmlCommandButton) {
            return ((HtmlCommandButton) uiComponent).getImage();
        }
        return (String) uiComponent.getAttributes().get(JSFAttr.IMAGE_ATTR);
    }

    private String getType(UIComponent uiComponent) {
        if (uiComponent instanceof HtmlCommandButton) {
            return ((HtmlCommandButton) uiComponent).getType();
        }
        return (String) uiComponent.getAttributes().get(org.apache.myfaces.jtracc.renderkit.JSFAttr.TYPE_ATTR);
    }

    private Object getValue(UIComponent uiComponent) {
        if (uiComponent instanceof ValueHolder) {
            return ((ValueHolder) uiComponent).getValue();
        }
        return uiComponent.getAttributes().get(JSFAttr.VALUE_ATTR);
    }
}

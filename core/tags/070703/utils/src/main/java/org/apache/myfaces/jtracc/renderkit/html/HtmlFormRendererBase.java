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

import org.apache.myfaces.jtracc.config.MyfacesConfig;
import org.apache.myfaces.jtracc.renderkit.RendererUtils;
import org.apache.myfaces.jtracc.renderkit.JSFAttr;
import org.apache.myfaces.jtracc.renderkit.html.util.FormInfo;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Manfred Geiler (latest modification by $Author: grantsmith $)
 * @author Thomas Spiegl
 * @author Anton Koinov
 * @version $Revision: 486711 $ $Date: 2006-12-13 16:25:37 +0000 (Wed, 13 Dec 2006) $
 */
public class HtmlFormRendererBase
    extends HtmlRenderer {
    //private static final Log log = LogFactory.getLog(HtmlFormRenderer.class);

    private static final String HIDDEN_SUBMIT_INPUT_SUFFIX = "_SUBMIT";
    private static final String HIDDEN_SUBMIT_INPUT_VALUE = "1";

    private static final String HIDDEN_COMMAND_INPUTS_SET_ATTR
        = UIForm.class.getName() + ".org.apache.myfaces.HIDDEN_COMMAND_INPUTS_SET";

    private static final String VALIDATE_CONTEXT_PARAM = "org.apache.myfaces.VALIDATE";
    private static final String SCROLL_HIDDEN_INPUT = "org.apache.myfaces.SCROLL_HIDDEN_INPUT";

    /**
     * Okay, we're going far for compatibility here, but well, we ought to.
     * this is the parameter that is set with the form id in the RI
     * if you don't set this parameter, buttons in the RI 1.1. won't work with your form
     * failing with a javascript error.
     * <p/>
     * nice, eh?
     * <p/>
     * fixed in the RI in 1.2
     * <p/>
     * if you want to know more, search for this comment in the 1.1. RI sources:
     * <p/>
     * // look up the clientId of the form in request scope to arrive the name of
     * // the javascript function to invoke from the onclick event handler.
     * // PENDING (visvan) we need to fix this dependency between the renderers.
     * // This solution is only temporary.
     */
    private static final String FORM_CLIENT_ID_ATTR = "com.sun.faces.FORM_CLIENT_ID_ATTR";


    public void encodeBegin(FacesContext facesContext, UIComponent component)
        throws IOException {
        org.apache.myfaces.jtracc.renderkit.RendererUtils.checkParamValidity(facesContext, component, UIForm.class);

        // see comment for FORM_CLIENT_ID_ATTR above - this is entirely for compatibility with RI in version 1.1
        //todo: move out for 1.2!
        facesContext.getExternalContext().getRequestMap().put(FORM_CLIENT_ID_ATTR,
                                                              component.getClientId(facesContext));

        if ("true".equals(facesContext.getExternalContext().getInitParameter(VALIDATE_CONTEXT_PARAM))) {
            FormInfo info = RendererUtils.findNestingForm(component, facesContext);

            if (info != null && info.getForm() != null) {
                throw new FacesException("You should never nest HTML-forms. " +
                    "This leads to unpredictable behaviour in all major browsers. " +
                    "You can use multiple forms on a page, but they may not be nested!" +
                    "If you need to disable this check (on your own risk!) set the param " +
                    "org.apache.myfaces.VALIDATE in your web.xml context parameters to false");
            }
        }

        UIForm htmlForm = (UIForm) component;

        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = htmlForm.getClientId(facesContext);
        String actionURL = getActionUrl(facesContext, htmlForm);
        String method = getMethod(facesContext, htmlForm);
        String acceptCharset = getAcceptCharset(facesContext, htmlForm);

        writer.startElement(HTML.FORM_ELEM, htmlForm);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        writer.writeAttribute(HTML.METHOD_ATTR, method, null);
        if((acceptCharset != null) && (acceptCharset.compareTo("")!=0)) writer.writeAttribute(HTML.ACCEPT_CHARSET_ATTR, acceptCharset, null);
        writer.writeURIAttribute(HTML.ACTION_ATTR,
                                 facesContext.getExternalContext().encodeActionURL(actionURL),
                                 null);

        HtmlRendererUtils.renderHTMLAttributes(writer, htmlForm, HTML.FORM_PASSTHROUGH_ATTRIBUTES);

        writer.write(""); // force start element tag to be closed

        // not needed in this version as nothing is written to the form tag, but
        // included for backward compatibility to the 1.1.1 patch (JIRA MYFACES-1276)
        // However, might be needed in the future
        beforeFormElementsStart(facesContext, component);
        afterFormElementsStart(facesContext, component);
    }

    protected String getActionUrl(FacesContext facesContext, UIForm form) {
        return getActionUrl(facesContext);
    }

    protected String getMethod(FacesContext facesContext, UIForm form) {
        return "post";
    }

    protected String getAcceptCharset(FacesContext facesContext, UIForm form ) {
    	return (String)form.getAttributes().get( JSFAttr.ACCEPTCHARSET_ATTR );
    }

    public void encodeEnd(FacesContext facesContext, UIComponent component)
        throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        beforeFormElementsEnd(facesContext, component);

        //write hidden input to determine "submitted" value on decode
        writer.startElement(HTML.INPUT_ELEM, component);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
        writer.writeAttribute(HTML.NAME_ATTR, component.getClientId(facesContext) +
            HIDDEN_SUBMIT_INPUT_SUFFIX, null);
        writer.writeAttribute(HTML.VALUE_ATTR, HIDDEN_SUBMIT_INPUT_VALUE, null);
        writer.endElement(HTML.INPUT_ELEM);

        renderScrollHiddenInputIfNecessary(component, facesContext, writer);

        /*
        see JspViewHandlerImpl.writeState(...)
        if(!facesContext.getApplication().getStateManager().isSavingStateInClient(facesContext))
        {
            writer.startElement(HTML.INPUT_ELEM, component);
            writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
            writer.writeAttribute(HTML.NAME_ATTR, org.apache.myfaces.jtracc.renderkit.RendererUtils.SEQUENCE_PARAM, null);
            writer.writeAttribute(org.apache.myfaces.jtracc.renderkit.html.HTML.VALUE_ATTR, org.apache.myfaces.jtracc.renderkit.RendererUtils.getViewSequence(facesContext), null);
            writer.endElement(HTML.INPUT_ELEM);
        }
        */

        //render hidden command inputs
        Set set = (Set) facesContext.getExternalContext().getRequestMap().get(
            getHiddenCommandInputsSetName(facesContext, component));
        if (set != null && !set.isEmpty()) {
            HtmlRendererUtils.renderHiddenCommandFormParams(writer, set);

            String target;
            if (component instanceof HtmlForm) {
                target = ((HtmlForm) component).getTarget();
            }
            else {
                target = (String) component.getAttributes().get(HTML.TARGET_ATTR);
            }
            HtmlRendererUtils.renderClearHiddenCommandFormParamsFunction(writer,
                                                                         component.getClientId(facesContext),
                                                                         set,
                                                                         target);
        }

        //write state marker at the end of the form
        //Todo: this breaks client-side enabled AJAX components again which are searching for the state
        //we'll need to fix this
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        viewHandler.writeState(facesContext);

        afterFormElementsEnd(facesContext, component);
        writer.endElement(HTML.FORM_ELEM);
    }

    private static String getHiddenCommandInputsSetName(FacesContext facesContext, UIComponent form) {
        StringBuffer buf = new StringBuffer();
        buf.append(HIDDEN_COMMAND_INPUTS_SET_ATTR);
        buf.append("_");
        buf.append(form.getClientId(facesContext));
        return buf.toString();
    }

    private static String getScrollHiddenInputName(FacesContext facesContext, UIComponent form) {
        StringBuffer buf = new StringBuffer();
        buf.append(SCROLL_HIDDEN_INPUT);
        buf.append("_");
        buf.append(form.getClientId(facesContext));
        return buf.toString();
    }


    public void decode(FacesContext facesContext, UIComponent component) {
        org.apache.myfaces.jtracc.renderkit.RendererUtils.checkParamValidity(facesContext, component, UIForm.class);

        /*
        if (HTMLUtil.isDisabled(component))
        {
            return;
        }
        */

        UIForm htmlForm = (UIForm) component;

        Map paramMap = facesContext.getExternalContext().getRequestParameterMap();
        String submittedValue = (String) paramMap.get(component.getClientId(facesContext) +
            HIDDEN_SUBMIT_INPUT_SUFFIX);
        if (submittedValue != null && submittedValue.equals(HIDDEN_SUBMIT_INPUT_VALUE)) {
            htmlForm.setSubmitted(true);
        }
        else {
            htmlForm.setSubmitted(false);
        }
    }


    public static void addHiddenCommandParameter(FacesContext facesContext, UIComponent form, String paramName) {
        Set set = (Set) facesContext.getExternalContext().getRequestMap().get(getHiddenCommandInputsSetName(facesContext, form));
        if (set == null) {
            set = new HashSet();
            facesContext.getExternalContext().getRequestMap().put(getHiddenCommandInputsSetName(facesContext, form), set);
        }
        set.add(paramName);
    }

    public static void renderScrollHiddenInputIfNecessary(UIComponent form, FacesContext facesContext, ResponseWriter writer)
        throws IOException {
        if (form == null) {
            return;
        }

        if (facesContext.getExternalContext().getRequestMap().get(getScrollHiddenInputName(facesContext, form)) == null)
        {
            if (MyfacesConfig.getCurrentInstance(facesContext.getExternalContext()).isAutoScroll()) {
                HtmlRendererUtils.renderAutoScrollHiddenInput(facesContext, writer);
            }
            facesContext.getExternalContext().getRequestMap().put(getScrollHiddenInputName(facesContext, form), Boolean.TRUE);
        }
    }

    /**
     * Called before the state and any elements are added to the form tag in the
     * encodeBegin method
     */
    protected void beforeFormElementsStart(FacesContext facesContext, UIComponent component)
        throws IOException {
    }

    /**
     * Called after the state and any elements are added to the form tag in the
     * encodeBegin method
     */
    protected void afterFormElementsStart(FacesContext facesContext, UIComponent component)
        throws IOException {
    }

    /**
     * Called before the state and any elements are added to the form tag in the
     * encodeEnd method
     */
    protected void beforeFormElementsEnd(FacesContext facesContext, UIComponent component)
        throws IOException {
    }

    /**
     * Called after the state and any elements are added to the form tag in the
     * encodeEnd method
     */
    protected void afterFormElementsEnd(FacesContext facesContext, UIComponent component)
        throws IOException {
    }
}

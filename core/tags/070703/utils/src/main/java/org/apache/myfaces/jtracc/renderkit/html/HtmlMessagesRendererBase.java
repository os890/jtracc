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
import org.apache.myfaces.jtracc.renderkit.html.HTML;
import org.apache.myfaces.jtracc.renderkit.html.HtmlMessageRendererBase;
import org.apache.myfaces.jtracc.util.NullIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;
import javax.faces.component.html.HtmlMessages;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Manfred Geiler (latest modification by $Author: grantsmith $)
 * @version $Revision: 472618 $ $Date: 2006-11-08 20:06:54 +0000 (Wed, 08 Nov 2006) $
 */
public abstract class HtmlMessagesRendererBase
        extends HtmlMessageRendererBase
{
    private static final Log log = LogFactory.getLog(HtmlMessagesRendererBase.class);

    protected static final String LAYOUT_LIST  = "list";
    protected static final String LAYOUT_TABLE = "table";


    protected void renderMessages(FacesContext facesContext,
                                  UIComponent messages)
            throws IOException
    {
        MessagesIterator messagesIterator = new MessagesIterator(facesContext,
                isGlobalOnly(messages));

        if (messagesIterator.hasNext())
        {
            String layout = getLayout(messages);
            if (layout == null)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("No messages layout given, using default layout 'list'.");
                }
                renderList(facesContext, messages, messagesIterator);
            }
            else if (layout.equalsIgnoreCase(LAYOUT_TABLE))
            {
                renderTable(facesContext, messages, messagesIterator);
            }
            else
            {
                if (log.isWarnEnabled() && !layout.equalsIgnoreCase(LAYOUT_LIST))
                {
                    log.warn("Unsupported messages layout '" + layout + "' - using default layout 'list'.");
                }
                renderList(facesContext, messages, messagesIterator);
            }
        }
    }


    protected void renderList(FacesContext facesContext,
                            UIComponent messages,
                            MessagesIterator messagesIterator)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.UL_ELEM, messages);
        HtmlRendererUtils.writeIdIfNecessary(writer, messages, facesContext);

        while(messagesIterator.hasNext())
        {
            writer.startElement(org.apache.myfaces.jtracc.renderkit.html.HTML.LI_ELEM, messages);
            renderSingleFacesMessage(facesContext,
                    messages,
                    (FacesMessage)messagesIterator.next(),
                    messagesIterator.getClientId());
            writer.endElement(HTML.LI_ELEM);
        }

        writer.endElement(HTML.UL_ELEM);
    }


    protected void renderTable(FacesContext facesContext,
                             UIComponent messages,
                             MessagesIterator messagesIterator)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.TABLE_ELEM, messages);
        HtmlRendererUtils.writeIdIfNecessary(writer, messages, facesContext);

        while(messagesIterator.hasNext())
        {
            writer.startElement(HTML.TR_ELEM, messages);
            writer.startElement(HTML.TD_ELEM, messages);
            renderSingleFacesMessage(facesContext,
                    messages,
                    (FacesMessage)messagesIterator.next(),
                    messagesIterator.getClientId());

            writer.endElement(HTML.TD_ELEM);
            writer.endElement(HTML.TR_ELEM);
        }

        writer.endElement(HTML.TABLE_ELEM);
    }


    public static String[] getStyleAndStyleClass(UIComponent messages,
                                             FacesMessage.Severity severity)
    {
        String style = null;
        String styleClass = null;
        if (messages instanceof HtmlMessages)
        {
            if (severity == FacesMessage.SEVERITY_INFO)
            {
                style = ((HtmlMessages)messages).getInfoStyle();
                styleClass = ((HtmlMessages)messages).getInfoClass();
            }
            else if (severity == FacesMessage.SEVERITY_WARN)
            {
                style = ((HtmlMessages)messages).getWarnStyle();
                styleClass = ((HtmlMessages)messages).getWarnClass();
            }
            else if (severity == FacesMessage.SEVERITY_ERROR)
            {
                style = ((HtmlMessages)messages).getErrorStyle();
                styleClass = ((HtmlMessages)messages).getErrorClass();
            }
            else if (severity == FacesMessage.SEVERITY_FATAL)
            {
                style = ((HtmlMessages)messages).getFatalStyle();
                styleClass = ((HtmlMessages)messages).getFatalClass();
            }

            if (style == null)
            {
                style = ((HtmlMessages)messages).getStyle();
            }

            if (styleClass == null)
            {
                styleClass = ((HtmlMessages)messages).getStyleClass();
            }
        }
        else
        {
            Map attr = messages.getAttributes();
            if (severity == FacesMessage.SEVERITY_INFO)
            {
                style = (String)attr.get(org.apache.myfaces.jtracc.renderkit.JSFAttr.INFO_STYLE_ATTR);
                styleClass = (String)attr.get(org.apache.myfaces.jtracc.renderkit.JSFAttr.INFO_CLASS_ATTR);
            }
            else if (severity == FacesMessage.SEVERITY_WARN)
            {
                style = (String)attr.get(org.apache.myfaces.jtracc.renderkit.JSFAttr.WARN_STYLE_ATTR);
                styleClass = (String)attr.get(org.apache.myfaces.jtracc.renderkit.JSFAttr.WARN_CLASS_ATTR);
            }
            else if (severity == FacesMessage.SEVERITY_ERROR)
            {
                style = (String)attr.get(org.apache.myfaces.jtracc.renderkit.JSFAttr.ERROR_STYLE_ATTR);
                styleClass = (String)attr.get(org.apache.myfaces.jtracc.renderkit.JSFAttr.ERROR_CLASS_ATTR);
            }
            else if (severity == FacesMessage.SEVERITY_FATAL)
            {
                style = (String)attr.get(org.apache.myfaces.jtracc.renderkit.JSFAttr.FATAL_STYLE_ATTR);
                styleClass = (String)attr.get(JSFAttr.FATAL_CLASS_ATTR);
            }

            if (style == null)
            {
                style = (String)attr.get(org.apache.myfaces.jtracc.renderkit.JSFAttr.STYLE_CLASS_ATTR);
            }

            if (styleClass == null)
            {
                styleClass = (String)attr.get(org.apache.myfaces.jtracc.renderkit.JSFAttr.STYLE_CLASS_ATTR);
            }
        }

        return new String[] {style, styleClass};
    }


    protected String getTitle(UIComponent component)
    {
        if (component instanceof HtmlMessages)
        {
            return ((HtmlMessages)component).getTitle();
        }
        else
        {
            return (String)component.getAttributes().get(org.apache.myfaces.jtracc.renderkit.JSFAttr.TITLE_ATTR);
        }
    }

    protected boolean isTooltip(UIComponent component)
    {
        if (component instanceof HtmlMessages)
        {
            return ((HtmlMessages)component).isTooltip();
        }
        else
        {
            return org.apache.myfaces.jtracc.renderkit.RendererUtils.getBooleanAttribute(component, org.apache.myfaces.jtracc.renderkit.JSFAttr.TOOLTIP_ATTR, false);
        }
    }

    protected boolean isShowSummary(UIComponent component)
    {
        if (component instanceof UIMessages)
        {
            return ((UIMessages)component).isShowSummary();
        }
        else
        {
            return RendererUtils.getBooleanAttribute(component, JSFAttr.SHOW_SUMMARY_ATTR, false);
        }
    }

    protected boolean isShowDetail(UIComponent component)
    {
        if (component instanceof UIMessages)
        {
            return ((UIMessages)component).isShowDetail();
        }
        else
        {
            return org.apache.myfaces.jtracc.renderkit.RendererUtils.getBooleanAttribute(component, JSFAttr.SHOW_DETAIL_ATTR, false);
        }
    }

    protected boolean isGlobalOnly(UIComponent component)
    {
        if (component instanceof UIMessages)
        {
            return ((UIMessages)component).isGlobalOnly();
        }
        else
        {
            return org.apache.myfaces.jtracc.renderkit.RendererUtils.getBooleanAttribute(component, JSFAttr.GLOBAL_ONLY_ATTR, false);
        }
    }

    protected String getLayout(UIComponent component)
    {
        if (component instanceof HtmlMessages)
        {
            return ((HtmlMessages)component).getLayout();
        }
        else
        {
            return (String)component.getAttributes().get(JSFAttr.LAYOUT_ATTR);
        }
    }



    private static class MessagesIterator implements Iterator
    {
        private FacesContext _facesContext;
        private Iterator _globalMessagesIterator;
        private Iterator _clientIdsWithMessagesIterator;
        private Iterator _componentMessagesIterator = null;
        private String _clientId = null;

        public MessagesIterator(FacesContext facesContext, boolean globalOnly)
        {
            _facesContext = facesContext;
            if (globalOnly)
            {
                _globalMessagesIterator = facesContext.getMessages(null);
                _clientIdsWithMessagesIterator = NullIterator.instance();
            }
            else
            {
                _globalMessagesIterator = org.apache.myfaces.jtracc.util.NullIterator.instance();
                _clientIdsWithMessagesIterator = facesContext.getClientIdsWithMessages();
            }
            _componentMessagesIterator = null;
            _clientId = null;
        }

        public boolean hasNext()
        {
            return _globalMessagesIterator.hasNext() ||
                    _clientIdsWithMessagesIterator.hasNext() ||
                    (_componentMessagesIterator != null && _componentMessagesIterator.hasNext());
        }

        public Object next()
        {
            if (_globalMessagesIterator.hasNext())
            {
                return _globalMessagesIterator.next();
            }
            else if (_componentMessagesIterator != null && _componentMessagesIterator.hasNext())
            {
                return _componentMessagesIterator.next();
            }
            else
            {
                _clientId = (String)_clientIdsWithMessagesIterator.next();
                _componentMessagesIterator = _facesContext.getMessages(_clientId);
                return _componentMessagesIterator.next();
            }
        }

        public void remove()
        {
            throw new UnsupportedOperationException(this.getClass().getName() + " UnsupportedOperationException");
        }

        public String getClientId()
        {
            return _clientId;
        }
    }

}

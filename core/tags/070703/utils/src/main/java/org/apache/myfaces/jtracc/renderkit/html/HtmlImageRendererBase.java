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

import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.jtracc.renderkit.JSFAttr;
import org.apache.myfaces.jtracc.renderkit.html.HTML;


/**
 * @author Manfred Geiler (latest modification by $Author: grantsmith $)
 * @author Thomas Spiegl
 * @author Anton Koinov
 * @version $Revision$ $Date: 2005-05-11 18:45:06 +0200 (Wed, 11 May 2005) $
 */
public class HtmlImageRendererBase
        extends HtmlRenderer
{
     private static final Log log = LogFactory.getLog(HtmlImageRendererBase.class);

        public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
                throws IOException
        {
            org.apache.myfaces.jtracc.renderkit.RendererUtils.checkParamValidity(facesContext, uiComponent, UIGraphic.class);

            ResponseWriter writer = facesContext.getResponseWriter();

            String url;
            if (uiComponent instanceof HtmlGraphicImage)
            {
                url = ((HtmlGraphicImage)uiComponent).getUrl();
            }
            else
            {
                url = (String)uiComponent.getAttributes().get(JSFAttr.URL_ATTR);
            }

            if ((url != null) && (url.length() > 0))
            {
                writer.startElement(HTML.IMG_ELEM, uiComponent);

                HtmlRendererUtils.writeIdIfNecessary(writer, uiComponent, facesContext);

                String src = facesContext.getApplication()
                                .getViewHandler().getResourceURL(facesContext, url);
                writer.writeURIAttribute(HTML.SRC_ATTR,
                                         facesContext.getExternalContext().encodeResourceURL(src),
                                         null);

                HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, HTML.IMG_PASSTHROUGH_ATTRIBUTES);

                writer.endElement(org.apache.myfaces.jtracc.renderkit.html.HTML.IMG_ELEM);
            }
            else
            {
                if (log.isWarnEnabled()) log.warn("Graphic with id " + uiComponent.getClientId(facesContext) + " has no value (url).");
            }
        }

}

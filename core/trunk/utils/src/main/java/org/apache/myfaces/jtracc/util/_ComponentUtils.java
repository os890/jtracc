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
package org.apache.myfaces.jtracc.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.jtracc.renderkit.RendererUtils;
import org.apache.myfaces.jtracc.renderkit.html.util.FormInfo;

/**
 * @author Mathias Br&ouml;kelmann (latest modification by $Author: matzew $)
 * @version $Revision: 478085 $ $Date: 2006-11-22 08:27:52 +0000 (Wed, 22 Nov 2006) $
 * @deprecated use <code>RendererUtils</code> instead
 */
public final class _ComponentUtils
{

    private _ComponentUtils()
    {
    }

    /**
     * 
     * @deprecated use <code>RendererUtils.getStringValue</code> instead
     */
    public static String getStringValue(FacesContext context, ValueBinding vb)
    {
        return RendererUtils.getStringValue(context, vb);
    }

    /**
     * This method will be removed in a future release
     * @deprecated use <code>RendererUtils.findNestingForm</code> instead
     */
    public static FormInfo findNestingForm(UIComponent uiComponent, FacesContext facesContext)
    {
      return RendererUtils.findNestingForm(uiComponent, facesContext);
    }
}

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
package org.apache.myfaces.jtracc.taglib.html;

import org.apache.myfaces.jtracc.renderkit.JSFAttr;
import org.apache.myfaces.jtracc.renderkit.html.HTML;

import javax.faces.component.UIComponent;


/**
 * @author Manfred Geiler (latest modification by $Author: paulsp $)
 * @author Martin Marinschek
 * @version $Revision: 495124 $ $Date: 2007-01-11 05:25:08 +0000 (Thu, 11 Jan 2007) $
 */
public abstract class HtmlOutputLabelTagBase
    extends org.apache.myfaces.jtracc.taglib.html.HtmlComponentTagBase
{
    // UIComponent attributes --> already implemented in UIComponentTagBase

    // user role attributes --> already implemented in UIComponentTagBase

    // HTML universal attributes --> already implemented in HtmlComponentTagBase

    // HTML event handler attributes --> already implemented in HtmlComponentTagBase

    // HTML label attributes
    private String _accesskey;
    private String _onblur;
    private String _onfocus;

    // UIOutput attributes
    // value and converterId --> already implemented in UIComponentTagBase

    //HTMLOutputLabel attributes
    private String _for;
    private String _tabindex;
    
    public void release() {
        super.release();
        _accesskey=null;
        _onblur=null;
        _onfocus=null;
        _for=null;
        _tabindex=null;
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, HTML.ACCESSKEY_ATTR, _accesskey);
        setStringProperty(component, HTML.ONBLUR_ATTR, _onblur);
        setStringProperty(component, HTML.ONFOCUS_ATTR, _onfocus);

        setStringProperty(component, JSFAttr.FOR_ATTR, _for);
        setStringProperty(component, HTML.TABINDEX_ATTR, _tabindex);
   }

    public void setAccesskey(String accesskey)
    {
        _accesskey = accesskey;
    }

    public void setOnblur(String onblur)
    {
        _onblur = onblur;
    }

    public void setOnfocus(String onfocus)
    {
        _onfocus = onfocus;
    }

    public void setFor(String aFor)
    {
        _for = aFor;
    }

    public void setTabindex(String tabindex)
    {
        _tabindex = tabindex;
    }
}

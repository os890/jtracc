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

import org.apache.myfaces.jtracc.renderkit.html.HTML;

import javax.faces.component.UIComponent;

/**
 * @author Manfred Geiler (latest modification by $Author: grantsmith $)
 * @version $Revision: 482275 $ $Date: 2006-12-04 18:14:25 +0000 (Mon, 04 Dec 2006) $
 */
public abstract class HtmlFormTagBase
        extends org.apache.myfaces.jtracc.taglib.html.HtmlComponentTagBase
{
    //private static final Log log = LogFactory.getLog(HtmlFormTag.class);

    // UIComponent attributes --> already implemented in UIComponentTagBase

    // user role attributes --> already implemented in UIComponentTagBase

    // HTML universal attributes --> already implemented in HtmlComponentTagBase

    // HTML event handler attributes --> already implemented in HtmlComponentTagBase

    // HTML form attributes

    private String _accept;
    private String _acceptcharset;
    private String _enctype;
    private String _name;
    private String _onreset;
    private String _onsubmit;
    private String _target;

    // UIForm attributes --> none so far
    public void release() {
        super.release();
        _accept=null;
        _acceptcharset=null;
        _enctype=null;
        _name=null;
        _onreset=null;
        _onsubmit=null;
        _target=null;
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        setStringProperty(component, org.apache.myfaces.jtracc.renderkit.html.HTML.ACCEPT_ATTR, _accept);
        setStringProperty(component, org.apache.myfaces.jtracc.renderkit.html.HTML.ACCEPT_CHARSET_ATTR, _acceptcharset);
        setStringProperty(component, org.apache.myfaces.jtracc.renderkit.html.HTML.ENCTYPE_ATTR, _enctype);
        setStringProperty(component, HTML.NAME_ATTR, _name);
        setStringProperty(component, org.apache.myfaces.jtracc.renderkit.html.HTML.ONRESET_ATTR, _onreset);
        setStringProperty(component, org.apache.myfaces.jtracc.renderkit.html.HTML.ONSUMBIT_ATTR, _onsubmit);
        setStringProperty(component, org.apache.myfaces.jtracc.renderkit.html.HTML.TARGET_ATTR, _target);
    }

    public void setAccept(String accept)
    {
        _accept = accept;
    }

    public void setAcceptcharset(String acceptcharset)
    {
        _acceptcharset = acceptcharset;
    }

    public void setEnctype(String enctype)
    {
        _enctype = enctype;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public void setOnreset(String onreset)
    {
        _onreset = onreset;
    }

    public void setOnsubmit(String onsubmit)
    {
        _onsubmit = onsubmit;
    }

    public void setTarget(String target)
    {
        _target = target;
    }

}

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
import org.apache.myfaces.jtracc.renderkit.JSFAttr;

import javax.faces.component.UIComponent;


/**
 * Common base tag class for HtmlSelectOneMenu and HtmlSelectManyMenu components.
 *
 * @author Manfred Geiler (latest modification by $Author: paulsp $)
 * @author Martin Marinschek
 * @version $Revision: 495108 $ $Date: 2007-01-11 04:15:27 +0000 (Thu, 11 Jan 2007) $
 */
public abstract class HtmlSelectMenuTagBase
        extends org.apache.myfaces.jtracc.taglib.html.HtmlInputTagBase
{
    // UIComponent attributes --> already implemented in UIComponentTagBase

    // user role attributes --> already implemented in UIComponentTagBase

    // HTML universal attributes --> already implemented in HtmlComponentTagBase

    // HTML event handler attributes --> already implemented in HtmlComponentTagBase

    // HTML input attributes relevant for menu
    private String _accesskey;
    private String _datafld;
    private String _datasrc;
    private String _dataformatas;
    private String _disabled;
    private String _name;
    private String _onblur;
    private String _onchange;
    private String _onfocus;
    private String _onselect;
    private String _tabindex;

    // UIInput attributes
    // --> already implemented in HtmlInputTagBase

    // UISelectMany attributes
    //selectedValues cannot be set here, is set in JSP-parsing

    //HtmlSelectManyMenu Attributes
    private String _border;

    // HTMLSelectManyAttributes attributes
    private String _disabledClass;
    private String _enabledClass;
    

    public void release() {
        super.release();
        _accesskey=null;
        _datafld=null;
        _datasrc=null;
        _dataformatas=null;
        _disabled=null;
        _name=null;
        _onblur=null;
        _onchange=null;
        _onfocus=null;
        _onselect=null;
        _tabindex=null;
        _border=null;
        _disabledClass=null;
        _enabledClass=null;
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, HTML.ACCESSKEY_ATTR, _accesskey);
        setStringProperty(component, HTML.DATAFLD_ATTR, _datafld);
        setStringProperty(component,HTML.DATASRC_ATTR, _datasrc);
        setStringProperty(component, HTML.DATAFORMATAS_ATTR, _dataformatas);
        setBooleanProperty(component, HTML.DISABLED_ATTR, _disabled);
        setStringProperty(component, HTML.NAME_ATTR, _name);
        setStringProperty(component, HTML.ONBLUR_ATTR, _onblur);
        setStringProperty(component, HTML.ONCHANGE_ATTR, _onchange);
        setStringProperty(component, HTML.ONFOCUS_ATTR, _onfocus);
        setStringProperty(component, HTML.ONSELECT_ATTR, _onselect);
        setStringProperty(component, HTML.TABINDEX_ATTR, _tabindex);
        setStringProperty(component, JSFAttr.DISABLED_CLASS_ATTR, _disabledClass);
        setStringProperty(component, JSFAttr.ENABLED_CLASS_ATTR, _enabledClass);

        setIntegerProperty(component, HTML.BORDER_ATTR, _border);
   }

    public void setAccesskey(String accesskey)
    {
        _accesskey = accesskey;
    }
    public void setBorder(String border)
    {
        _border = border;
    }

    public void setDatafld(String datafld)
    {
        _datafld = datafld;
    }

    public void setDatasrc(String datasrc)
    {
        _datasrc = datasrc;
    }

    public void setDataformatas(String dataformatas)
    {
        _dataformatas = dataformatas;
    }

    public void setDisabled(String disabled)
    {
        _disabled = disabled;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public void setOnblur(String onblur)
    {
        _onblur = onblur;
    }

    public void setOnchange(String onchange)
    {
        _onchange = onchange;
    }

    public void setOnfocus(String onfocus)
    {
        _onfocus = onfocus;
    }

    public void setOnselect(String onselect)
    {
        _onselect = onselect;
    }

    public void setTabindex(String tabindex)
    {
        _tabindex = tabindex;
    }

    public void setDisabledClass(String disabledClass)
    {
        _disabledClass = disabledClass;
    }

    public void setEnabledClass(String enabledClass)
    {
        _enabledClass = enabledClass;
    }
}

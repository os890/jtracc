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
 * @author Martin Marinschek
 * @version $Revision: 472618 $ $Date: 2006-11-08 20:06:54 +0000 (Wed, 08 Nov 2006) $
 */
public abstract class HtmlInputSecretTagBase
        extends HtmlInputTagBase
{
    // UIComponent attributes --> already implemented in UIComponentTagBase

    // user role attributes --> already implemented in UIComponentTagBase

    // HTML universal attributes --> already implemented in HtmlComponentTagBase

    // HTML event handler attributes --> already implemented in HtmlComponentTagBase

    // HTML input attributes relevant for password-input
    private String _accesskey;
    private String _align;
    private String _alt;
    private String _datafld;
    private String _datasrc;
    private String _dataformatas;
    private String _disabled;
    private String _maxlength;
    private String _onblur;
    private String _onchange;
    private String _onfocus;
    private String _onselect;
    private String _readonly;
    private String _size;
    private String _tabindex;

    // UIOutput attributes
    // value and converterId --> already implemented in UIComponentTagBase

    // UIInput attributes
    // --> already implemented in HtmlInputTagBase

    // HTMLInputSecret attributes
    private String _redisplay;

    public void release() {
        super.release();
        _accesskey=null;
        _align=null;
        _alt=null;
        _datafld=null;
        _datasrc=null;
        _dataformatas=null;
        _disabled=null;
        _maxlength=null;
        _onblur=null;
        _onchange=null;
        _onfocus=null;
        _onselect=null;
        _readonly=null;
        _size=null;
        _tabindex=null;
        _redisplay=null;
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, org.apache.myfaces.jtracc.renderkit.html.HTML.ACCESSKEY_ATTR, _accesskey);
        setStringProperty(component, org.apache.myfaces.jtracc.renderkit.html.HTML.ALIGN_ATTR, _align);
        setStringProperty(component, org.apache.myfaces.jtracc.renderkit.html.HTML.ALT_ATTR, _alt);
        setStringProperty(component, org.apache.myfaces.jtracc.renderkit.html.HTML.DATAFLD_ATTR, _datafld);
        setStringProperty(component, org.apache.myfaces.jtracc.renderkit.html.HTML.DATASRC_ATTR, _datasrc);
        setStringProperty(component, org.apache.myfaces.jtracc.renderkit.html.HTML.DATAFORMATAS_ATTR, _dataformatas);
        setBooleanProperty(component, org.apache.myfaces.jtracc.renderkit.html.HTML.DISABLED_ATTR, _disabled);
        setIntegerProperty(component, org.apache.myfaces.jtracc.renderkit.html.HTML.MAXLENGTH_ATTR, _maxlength);
        setStringProperty(component, HTML.ONBLUR_ATTR, _onblur);
        setStringProperty(component, org.apache.myfaces.jtracc.renderkit.html.HTML.ONCHANGE_ATTR, _onchange);
        setStringProperty(component, org.apache.myfaces.jtracc.renderkit.html.HTML.ONFOCUS_ATTR, _onfocus);
        setStringProperty(component, org.apache.myfaces.jtracc.renderkit.html.HTML.ONSELECT_ATTR, _onselect);
        setBooleanProperty(component, org.apache.myfaces.jtracc.renderkit.html.HTML.READONLY_ATTR, _readonly);
        setIntegerProperty(component, HTML.SIZE_ATTR, _size);
        setStringProperty(component, org.apache.myfaces.jtracc.renderkit.html.HTML.TABINDEX_ATTR, _tabindex);

        setBooleanProperty(component, org.apache.myfaces.jtracc.renderkit.JSFAttr.REDISPLAY_ATTR, _redisplay);
   }

    public void setAccesskey(String accesskey)
    {
        _accesskey = accesskey;
    }

    public void setAlign(String align)
    {
        _align = align;
    }

    public void setAlt(String alt)
    {
        _alt = alt;
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

    public void setMaxlength(String maxlength)
    {
        _maxlength = maxlength;
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

    public void setReadonly(String readonly)
    {
        _readonly = readonly;
    }

    public void setSize(String size)
    {
        _size = size;
    }

    public void setTabindex(String tabindex)
    {
        _tabindex = tabindex;
    }

    public void setRedisplay(String redisplay)
    {
        _redisplay = redisplay;
    }
}

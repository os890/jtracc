/*
 * jtracc - i18n JSF component library
 * Copyright 2007, IRIAN Solutions GmbH Vienna, Austria
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package at.irian.i18n.jtracc.renderkit.translation.model;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.SelectItem;
import javax.faces.webapp.UIComponentTag;

import org.apache.myfaces.jtracc.util._ComponentUtils;

/**
 * Extends base SelectItem functionality by providing support for EL expressions
 * as valueBindings in both value and label properties.
 */
public class TranslationSelectItem extends SelectItem
{

    private static final long serialVersionUID = 8841594746464518886L;

    private Map _valueBindingMap = null;

    private String _label;
    private String _description;

    // CONSTRUCTORS
    public TranslationSelectItem()
    {
    }

    public TranslationSelectItem(Object value)
    {
        super( value );
    }

    public TranslationSelectItem(Object value, String label)
    {
        if (value == null)
        {
            throw new NullPointerException( "value" );
        }
        if (label == null)
        {
            throw new NullPointerException( "label" );
        }

        super.setValue( value );
        this.setLabel( label );
        this.setDescription( null );
        super.setDisabled( false );
    }

    public TranslationSelectItem(Object value, String label, String description)
    {
        if (value == null)
        {
            throw new NullPointerException( "value" );
        }
        if (label == null)
        {
            throw new NullPointerException( "label" );
        }

        super.setValue( value );
        this.setLabel( label );
        this.setDescription( description );
        super.setDisabled( false );
    }

    public TranslationSelectItem(Object value, String label,
                                 String description, boolean disabled)
    {
        if (value == null)
        {
            throw new NullPointerException( "value" );
        }
        if (label == null)
        {
            throw new NullPointerException( "label" );
        }

        super.setValue( value );
        this.setLabel( label );
        this.setDescription( description );
        super.setDisabled( disabled );
    }

    public String getDescription()
    {
        if (_description != null)
        {
            return _description;
        }
        ValueBinding vb = getValueBinding( "description" );
        return vb != null ? _ComponentUtils.getStringValue( FacesContext
                .getCurrentInstance(), vb ) : null;
    }

    public void setDescription(String description)
    {
        if (description == null)
        {
            this._description = null;
            return;
        }

        if (UIComponentTag.isValueReference( description ))
        {
            ValueBinding vb = FacesContext.getCurrentInstance()
                    .getApplication().createValueBinding( description );
            this.setValueBinding( "description", vb );
        }
        else
        {
            this._description = description;
        }
    }

    public String getLabel()
    {
        if (_label != null)
        {
            return _label;
        }
        ValueBinding vb = getValueBinding( "label" );
        return vb != null ? _ComponentUtils.getStringValue( FacesContext
                .getCurrentInstance(), vb ) : null;
    }

    public void setLabel(String label)
    {
        if (label == null)
        {
            this._label = null;
            return;
        }

        if (UIComponentTag.isValueReference( label ))
        {
            ValueBinding vb = FacesContext.getCurrentInstance()
                    .getApplication().createValueBinding( label );
            this.setValueBinding( "label", vb );
        }
        else
        {
            this._label = label;
        }
    }

    /**
     * Put the provided value-binding into a map of value-bindings associated
     * with this component.
     */
    public void setValueBinding(String name, ValueBinding binding)
    {
        if (name == null)
        {
            throw new NullPointerException( "name" );
        }
        if (_valueBindingMap == null)
        {
            _valueBindingMap = new HashMap();
        }
        _valueBindingMap.put( name, binding );
    }

    /**
     * Get the named value-binding associated with this component.
     * <p/>
     * Value-bindings are stored in a map associated with the component, though
     * there is commonly a property (setter/getter methods) of the same name
     * defined on the component itself which evaluates the value-binding when
     * called.
     */
    public ValueBinding getValueBinding(String name)
    {
        if (name == null)
        {
            throw new NullPointerException( "name" );
        }
        if (_valueBindingMap == null)
        {
            return null;
        }
        else
        {
            return (ValueBinding) _valueBindingMap.get( name );
        }
    }

}

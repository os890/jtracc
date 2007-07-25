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
package at.irian.i18n.jtracc.renderkit.translation.internal;

import at.irian.i18n.jtracc.persistence.PersistenceDelegationEntryBean;
import at.irian.i18n.jtracc.util.SettingsUtils;
import at.irian.i18n.jtracc.util.TranslationUtils;
import at.irian.i18n.jtracc.util.TranslationELUtils;
import at.irian.i18n.jtracc.renderkit.translation.model.HtmlTargetLocaleSelectOneMenu;
import org.apache.myfaces.jtracc.renderkit.html.HTML;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.html.*;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.*;

/**
 * Wrapper renderer that decorates the original Renderer. It delegates all
 * methods, except encodeBegin() and encodeEnd(), to the original Renderer.
 */
public class TranslationRendererWrapper extends Renderer
{
    // Wrapped Renderer.
    private Renderer wrappedRenderer;

    // For internal cache use.
    private String[] _translatableAttributeContent;

    /**
     * Do not allow a TranslationRendererWrapper to be created without providing
     * the wrapped Renderer.
     */
    private TranslationRendererWrapper()
    {
    }

    /**
     * Provide Renderer to be wrapped.
     */
    public TranslationRendererWrapper(Renderer wrappedRenderer)
    {
        this.wrappedRenderer = wrappedRenderer;
    }

    // Do delegation to the wrapped Renderer.
    public String convertClientId(FacesContext context, String clientId)
    {
        return this.wrappedRenderer.convertClientId( context, clientId );
    }

    // Do delegation to the wrapped Renderer.
    public void decode(FacesContext context, UIComponent component)
    {
        this.wrappedRenderer.decode( context, component );
    }

    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException
    {
        boolean addedContent = false;
        boolean isInput = true;

        // filter and check component
        if (TranslationUtils.isExcludedObject( component ) || !this.isTranslatable( context, component ))
        {
            setResults( context, component, addedContent, isInput );
            // delegate to wrapped renderer
            this.wrappedRenderer.encodeBegin( context, component );
            return;
        }

        HtmlPanelGrid grid = (HtmlPanelGrid) context.getApplication()
                .createComponent( HtmlPanelGrid.COMPONENT_TYPE );
        grid.setColumns( 6 );

        /*
        * create grid entries
        */
        String attributeContent;
        Object value;
        Map addedELTerms = new HashMap();

        // analyze each attribute which contains el-terms
        for (int i = 0; i < this._translatableAttributeContent.length; i++)
        {
            attributeContent = this._translatableAttributeContent[i];

            value = context.getApplication().createValueBinding( attributeContent ).getValue( context );

            // input components have el-terms without value
            if (value == null)
            {
                continue;
            }
            isInput = false;

            // process complex components to setup  a mapping of elContent and var name
            if (value instanceof List)
            {
                processComplexComponents( context, component, attributeContent );

                // avoid to add redundant el content of complex components
                addedContent = false;
            }
            // simple components
            else
            {
                if(addedContent == false)
                {
                    addedContent = addGridEntry( context, attributeContent, grid, addedELTerms );
                }
            }
        }
        this._translatableAttributeContent = null;

        if (isInput || !addedContent)
        {
            setResults( context, component, addedContent, isInput );
            // delegate to wrapped renderer
            this.wrappedRenderer.encodeBegin( context, component );
            return;
        }

        ResponseWriter out = context.getResponseWriter();

        //Container
        out.startElement( HTML.DIV_ELEM, null );
        out.writeAttribute( HTML.CLASS_ATTR, "translationContainer", null );
        out.writeAttribute( HTML.ONCLICK_ATTR, "showTranslationPane(this); dojo.event.browser.stopEvent(arguments[0] || window.event);", null );
        out.writeAttribute( HTML.ONMOUSEOVER_ATTR, "onMouseOverTRArea(this);", null );
        out.writeAttribute( HTML.ONMOUSEOUT_ATTR, "onMouseOutTRArea(this);", null );

        //Hitlayer
        out.startElement( HTML.DIV_ELEM, null );
        out.writeAttribute( HTML.CLASS_ATTR, "translationContainerOverlay", null );
        out.write( "&nbsp;" );
        out.endElement( HTML.DIV_ELEM );

        UIOutput link = TranslationUtils.createTranslationLink( context );

        HtmlPanelGroup group = (HtmlPanelGroup) context
                .getApplication().createComponent(
                HtmlPanelGroup.COMPONENT_TYPE );

        group.getChildren().add( grid );

        context.getViewRoot().getChildren().add( link );
        link.getChildren().add( group );

        //Link + Table
        link.encodeBegin( context );
        link.encodeChildren( context );
        link.encodeEnd( context );

        //Wrapped Component
        setResults( context, component, addedContent, isInput );
        this.wrappedRenderer.encodeBegin( context, component );
    }

    private void setResults(FacesContext context, UIComponent component, boolean addedContent, boolean isInput)
    {
        Results results = new Results();

        results.setAddedContent( addedContent );
        results.setInput( isInput );

        Map requestMap = context.getExternalContext().getRequestMap();
        Map mapping;
        String key = SettingsUtils.getComponentProperty( "encodeBegin_results" );

        if (requestMap.containsKey( key ))
        {
            mapping = (HashMap) requestMap.get( key );
        }
        else
        {
            mapping = new HashMap();
        }

        mapping.put( component.getClientId( context ), results );

        requestMap.put( key, mapping );
    }

    private Results getResults(FacesContext context, UIComponent component)
    {
        Map requestMap = context.getExternalContext().getRequestMap();
        Map mapping;
        String key = SettingsUtils.getComponentProperty( "encodeBegin_results" );

        if (requestMap.containsKey( key ))
        {
            mapping = (HashMap) requestMap.get( key );
        }
        else
        {
            return null;
        }

        return (Results) mapping.get( component.getClientId( context ) );
    }

    private class Results
    {
        private boolean addedContent = false;
        private boolean isInput = true;

        public boolean isAddedContent()
        {
            return addedContent;
        }

        public void setAddedContent(boolean addedContent)
        {
            this.addedContent = addedContent;
        }

        public boolean isInput()
        {
            return isInput;
        }

        public void setInput(boolean input)
        {
            isInput = input;
        }
    }

    // Do delegation to the wrapped Renderer.
    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException
    {
        // Do nothing, as span only works with encodeEnd.
        this.wrappedRenderer.encodeChildren( context, component );
    }

    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException
    {
        this.wrappedRenderer.encodeEnd( context, component );

        Results results = getResults( context, component );
        if (results.isInput() || !results.isAddedContent())
        {
            return;
        }

        ResponseWriter out = context.getResponseWriter();
        out.endElement( HTML.DIV_ELEM );
    }

    // Do delegation to the wrapped Renderer.
    public Object getConvertedValue(FacesContext context,
                                    UIComponent component, Object submittedValue)
            throws ConverterException
    {
        return this.wrappedRenderer.getConvertedValue( context, component, submittedValue );
    }

    // Do delegation to the wrapped Renderer.
    public boolean getRendersChildren()
    {
        return this.wrappedRenderer.getRendersChildren();
    }

    /**
     * Checks whether a component must be translated or not by trying to find an el-term within the component.<br>
     * Furthermore, the method sets the found el-term for further processing
     */
    private boolean isTranslatable(FacesContext facesContext, UIComponent component)
    {
        _translatableAttributeContent = TranslationELUtils.getComponentAttributesWithELTerms( facesContext, component );

        if (_translatableAttributeContent == null || _translatableAttributeContent.length == 0)
        {
            return false;
        }

        return true;
    }

    /**
     * Create mapping between elContent and var-attribute and add it to the session<br/>
     * Format: <b>[elContent]:[varContent]</b>
     * Name of var-attribute is configurable - e.g. var for complex standard components
     */
    private void processComplexComponents(FacesContext context, UIComponent component, String elContent)
    {
        String contentMapping = SettingsUtils.getComponentProperty( "content_mapping_key" );
        // mapping of value binding and content of jsp var attribute
        Map sessionMap = context.getExternalContext().getSessionMap();
        List mapping;
        if (sessionMap.containsKey( contentMapping ))
        {
            mapping = (ArrayList) sessionMap.get( contentMapping );
        }
        else
        {
            mapping = new ArrayList();
        }

        // for each attribute which provides variable names - standard components: var-attribute

        String varContent;
        Object varObject;
        Iterator vani = TranslationUtils.getVarAttributeNames().iterator();
        while (vani.hasNext())
        {
            varObject = component.getAttributes().get( vani.next() );

            if (!TranslationUtils.isEmtyString( (String) varObject ))
            {
                varContent = varObject.toString();

                String mappingEntry;
                if (!TranslationUtils.isEmtyString( varContent ))
                {
                    mappingEntry = elContent + ":" + varContent;
                    mapping.add( mappingEntry );
                    sessionMap.put( contentMapping, mapping );
                }
            }
        }
    }

    /**
     * Create grid entry - format: original value - inputbox to change value with ValueChangeListener - <b>hidden field</b> with el-term.
     * Each changeable el-term is displayed in one row.
     *
     * @param attributeContent Content of an attribute which contains found el terms
     * @param grid             Target grid for the grid entries
     *
     * @return True if at least one el-term is able to change the value of an property. <br/>
     *         Use the return value to avoid emty grids.
     */
    private boolean addGridEntry(FacesContext context, String attributeContent, HtmlPanelGrid grid, Map addedELTerms)
    {
        boolean addedContent = false;

        Iterator elti = TranslationELUtils.getChangeableELTerms( attributeContent ).iterator();

        // add one row for each changeable el-term
        String elTerm;

        while (elti.hasNext())
        {
            elTerm = (String) elti.next();

            // ignore elTerm if it alread exists
            if (addedELTerms.containsKey( elTerm ))
            {
                continue;
            }

            addedELTerms.put( elTerm, null );
            addedContent = true;

            /*
             * content of first column - locale markers
             */
            // has to be a session map - to continue translation mode
            Map sessionMap = context.getExternalContext().getSessionMap();
            String keyLocaleMappingKey = SettingsUtils.getComponentProperty( "key_locale_mapping_key" );
            Map keyLocaleMapping = (Map) sessionMap.get( keyLocaleMappingKey );

            if (sessionMap.containsKey( keyLocaleMappingKey ))
            {
                keyLocaleMapping = (Map) sessionMap.get( keyLocaleMappingKey );
            }

            String key;
            // TODO refactor
            key = elTerm.substring( 2, elTerm.indexOf( "." ) );

            Object o = context.getApplication().getVariableResolver().resolveVariable( context, key );

            // TODO refactor
            String propertyKey = elTerm.substring( elTerm.indexOf( "." ) + 1, elTerm.indexOf( "}" ) );
            if (o instanceof PersistenceDelegationEntryBean)
            {
                propertyKey = propertyKey + "_" + ( (PersistenceDelegationEntryBean) o ).getIndex();
            }

            // create source locale marker component
            String mappedLocale = null;
            if (keyLocaleMapping != null)
            {
                mappedLocale = (String) keyLocaleMapping.get( propertyKey );
            }
            UIOutput sourceLocale = TranslationUtils.createLocaleMarkerIcon( context, mappedLocale );

            // create target locale marker component
            HtmlTargetLocaleSelectOneMenu menu = (HtmlTargetLocaleSelectOneMenu)context.getViewRoot().findComponent( "_jtraccTargetLocaleSelectOneMenuId");
            UIOutput targetLocale = TranslationUtils.createLocaleMarkerIcon( context, (String) menu.getValue() );

            /*
             *  add source and target locale marker components
             */
            //add source locale
            grid.getChildren().add( sourceLocale );

            //add arrow
            UIOutput arrow = new HtmlOutputText();
            arrow.setValue( "->" );
            grid.getChildren().add( arrow );

            //add target locale
            grid.getChildren().add( targetLocale );


            /*
             * content of second column
             */
            UIOutput tsText = (HtmlOutputText) context.getApplication()
                    .createComponent( HtmlOutputText.COMPONENT_TYPE );

            Object value = context.getApplication().createValueBinding( elTerm ).getValue( context );

            // create labels for images
            String label = "";
            if (isImagePropertyKey( elTerm ))
            {
                label = "[" + getImageProperty( elTerm ) + "]: ";
            }

            tsText.setValue( label + value );
            grid.getChildren().add( tsText );

            /*
             * content of third column
             */
            UIInput tsInput = (HtmlInputText) context.getApplication().createComponent( HtmlInputText.COMPONENT_TYPE );
            tsInput.setValue( value );

            // ValueChangeListener
            tsInput.setValueChangeListener( context.getApplication()
                    .createMethodBinding( SettingsUtils.getComponentProperty( "el_indicator" ) + "{" + TranslationUtils.getMessageResolverBeanName() + ".changeMessage}",
                                          new Class[]{ValueChangeEvent.class} ) );
            grid.getChildren().add( tsInput );

            /*
             * content of fourth column
             */
            UIInput hiddenELContent = (HtmlInputHidden) context.getApplication()
                    .createComponent( HtmlInputHidden.COMPONENT_TYPE );
            hiddenELContent.setValue( elTerm );
            grid.getChildren().add( hiddenELContent );
        }

        return addedContent;
    }

    private boolean isImagePropertyKey(String elTerm)
    {
        // TODO refactor
        String key = elTerm.substring( elTerm.indexOf( "." ) + 1, elTerm.indexOf( "}" ) );

        return ( key.startsWith( SettingsUtils.getComponentProperty( "image_prefix" ) ) ||
                 key.startsWith( SettingsUtils.getComponentProperty( "image_prefix" ) + "width" ) ||
                 key.startsWith( SettingsUtils.getComponentProperty( "image_prefix" ) + "height" ) ||
                 key.startsWith( SettingsUtils.getComponentProperty( "image_prefix" ) + "alt" ) );
    }

    private String getImageProperty(String elTerm)
    {
        elTerm = elTerm.replace( SettingsUtils.getComponentProperty( "image_prefix" ), "" );
        // TODO refactor
        String key = elTerm.substring( elTerm.indexOf( "." ) + 1, elTerm.indexOf( "}" ) );

        return ( key.startsWith( "width" ) || key.startsWith( "height" ) || key.startsWith( "alt" ) ) ?
               key.substring( 0, key.indexOf( "_" ) ) : "img";
    }
}

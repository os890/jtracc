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
package at.irian.i18n.jtracc.util;

import at.irian.i18n.jtracc.persistence.PersistableMessagesBean;
import at.irian.i18n.jtracc.renderkit.translation.model.TranslationSelectItem;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Helper methods and routines for discovering translatable el contents
 */
public class TranslationELUtils
{
    /**
     * Utility method to obtain all translatable component value binding attribute names for the given
     * component.
     */
    private static List getComponentValueBindingAttributeNames(UIComponent component)
    {
        List availableValueBindingAttributeNames = new ArrayList();

        Iterator current = TranslationUtils.getTranslatableAttributeNames().iterator();

        while (current.hasNext())
        {
            String attributeName = (String) current.next();

            if (component.getValueBinding( attributeName ) != null)
            {
                availableValueBindingAttributeNames.add( attributeName );
            }
        }

        return availableValueBindingAttributeNames;
    }

    /*
     * recursive method which determine el-terms of an given object
     */
    //TODO: improve
    private static int resolveELTerms(Object o, Map visited, List foundELTerms) throws Exception
    {
        if (TranslationUtils.isExcludedObject( o ) || visited.containsKey( o ))
        {
            return 0;
        }

        visited.put( o, null );

        int elCount = 0;
        Class c = o.getClass();

        //inspect maps
        if (o instanceof java.util.Map)
        {
            Iterator vi = ( (Map) o ).values().iterator();

            while (vi.hasNext())
            {
                Object entry = vi.next();

                elCount += resolveELTerms( entry, visited, foundELTerms );
            }
            return elCount;
        }

        if (isELTerm( o ))
        {
            if (foundELTerms != null)
            {
                foundELTerms.add( o.toString() );
            }
            return ++elCount;
        }

        //analyze arrays
        if (c.isArray())
        {
            int length = Array.getLength( o );
            //check array [L -> no array of primitive types
            if (o.toString().startsWith( "[L" ))
            {
                for (int i = 0; i < length; i++)
                {
                    if (o.toString().startsWith( "[Ljava.lang.String" ))
                    {
                        if (isELTerm( Array.get( o, i ) ))
                        {
                            if (foundELTerms != null)
                            {
                                foundELTerms.add( o.toString() );
                            }
                            elCount++;
                        }
                    }
                    else
                    {
                        elCount += resolveELTerms( Array.get( o, i ), visited, foundELTerms );
                    }
                }
            }
            return elCount;
        }

        List attributes = findAllAttributes( c, new ArrayList() );
        Field[] fields = (Field[]) attributes.toArray( new Field[attributes.size()] );

        AccessibleObject.setAccessible( fields, true );
        for (int i = 0; i < fields.length; i++)
        {
            Field f = fields[i];
            if (f.get( o ) == null)
            {
                continue;
            }

            if (f.getType().equals( String.class ))
            {
                if (f.get( o ) != null && isELTerm( f.get( o ) ))
                {
                    if (foundELTerms != null)
                    {
                        foundELTerms.add( o.toString() );
                    }
                    elCount++;
                }
            }
            else if (!f.getType().isPrimitive())
            {
                elCount += resolveELTerms( f.get( o ), visited, foundELTerms );
            }
        }
        return elCount;
    }

    public static boolean containsELTerms(Object o)
    {
        int count;
        try
        {
            //TODO: extended tests
            count = resolveELTerms( o, new HashMap(), null );
        }
        catch (Exception e)
        {
            //TODO add exception handling
            count = 0;
        }
        return ( count > 0 );
    }

    private static List findAllAttributes(Class c, List attributes)
    {
        if (c == null)
        {
            return attributes;
        }
        findAllAttributes( c.getSuperclass(), attributes );

        Field[] fields = c.getDeclaredFields();
        for (int i = 0; i < fields.length; i++)
        {
            Field f = fields[i];

            if (!Modifier.isStatic( f.getModifiers() ))
            {
                attributes.add( f );
            }
        }

        return attributes;
    }

    public static String[] getComponentAttributesWithELTerms(FacesContext facesContext, UIComponent component)
    {
        List valueBindings = new ArrayList();

        if (component.getChildCount() == 0)
        {

            Iterator vbni = getComponentValueBindingAttributeNames( component ).iterator();

            while (vbni.hasNext())
            {
                String s = (String) vbni.next();

                ValueBinding vb = component.getValueBinding( s );
                valueBindings.add( vb.getExpressionString() );
            }
        }
        // components with children: UISelectMany + complex components
        else
        {
            Iterator ci = component.getChildren().iterator();
            while (ci.hasNext())
            {
                Object componentChild = ci.next();

                //UISelectItems
                if (componentChild instanceof UISelectItems)
                {
                    Iterator vbni = getComponentValueBindingAttributeNames( (UISelectItems) componentChild ).iterator();

                    while (vbni.hasNext())
                    {
                        String s = (String) vbni.next();

                        ValueBinding vb = ( (UIComponent) componentChild ).getValueBinding( s );
                        if (vb != null)
                        {
                            Object _values = vb.getValue( facesContext );

                            if (_values instanceof List)
                            {
                                for (Iterator iterator = ( (List) _values ).iterator(); iterator
                                        .hasNext();)
                                {
                                    Object selItem = iterator.next();

                                    // ... where there must be at least one
                                    // componentChild translatable.
                                    if (selItem instanceof TranslationSelectItem)
                                    {
                                        TranslationSelectItem transItem = (TranslationSelectItem) selItem;

                                        if (containsELTerms( transItem ))
                                        {
                                            //todo: change and use getComponentValueBindingAttributeNames
                                            //(change: reflection - inspect Object - getValueBinding Method => invoke if it exists

                                            valueBindings.add( transItem.getValueBinding(
                                                    "label" ).getExpressionString() );
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // UISelectItem components
                else if (componentChild instanceof UISelectItem)
                {
                    Iterator vbni = getComponentValueBindingAttributeNames( (UIComponent) componentChild ).iterator();

                    while (vbni.hasNext())
                    {
                        String s = (String) vbni.next();

                        ValueBinding vb = ( (UIComponent) componentChild ).getValueBinding( s );
                        valueBindings.add( vb.getExpressionString() );
                    }
                }
                // complex components
                else
                {
                    valueBindings = getComponentAttributesWithELTerms( component );
                    break;
                }
            }
        }
        return ( valueBindings != null ) ? (String[]) valueBindings.toArray( new String[valueBindings.size()] ) : null;
    }

    private static List getComponentAttributesWithELTerms(Object o)
    {
        List foundELTerms = new ArrayList();
        try
        {
            if (resolveELTerms( o, new HashMap(), foundELTerms ) > 0)
            {
                return foundELTerms;
            }
        }
        catch (Exception ex)
        {
            return null;
        }
        return null;
    }

    private static boolean isELTerm(Object o)
    {
        String s = o.toString();
        return ( s.contains( SettingsUtils.getComponentProperty( "el_indicator" ) + "{" ) && s.contains( "}" ) );
    }

    public static String createCustomizedELTerm(String elTerm)
    {
        // remove #{ and }
        elTerm = elTerm.substring( 2, elTerm.length() - 1 );

        String contentMappingKey = SettingsUtils.getComponentProperty( "content_mapping_key" );
        FacesContext context = FacesContext.getCurrentInstance();
        Map sessionMap = context.getExternalContext().getSessionMap();
        List mapping;
        if (sessionMap.containsKey( contentMappingKey ))
        {
            mapping = (ArrayList) sessionMap.get( contentMappingKey );
        }
        else
        {
            return null;
        }

        Iterator mi = mapping.iterator();
        while (mi.hasNext())
        {
            String entry = mi.next().toString();

            if (isCustomizedEL( entry ))
            {
                /*
                 * e.g.:
                 * elTerm = var.content -> cannot resolve this
                 * sessionMap contains: #{helloWorldBacking.manyEntries}:var
                 *  -> get attribute name of sessionMap entry = var
                 * the elTerm contains this attribute name + "." -> replace var with mapping of sessionMap entry:
                 * #{helloWorldBacking.manyEntries}:content
                 */
                if (elTerm.contains( separateVarCustomizedEL( entry ) + "." ))
                {
                    return elTerm.replace( separateVarCustomizedEL( entry ) + ".", separateELTermOfCustomizedEL( entry ) + ":" );
                }
            }

        }

        return null;
    }

    public static boolean isResolvableELTerm(String elTerm)
    {
        FacesContext context = FacesContext.getCurrentInstance();
        Object o = context.getApplication().createValueBinding( elTerm ).getValue( context );

        return ( o != null );
    }

    public static boolean isCustomizedEL(String elTerm)
    {
        return ( elTerm.contains( "}:" ) );
    }

    public static String separateELTermOfCustomizedEL(String elOfComplexComponents)
    {
        return elOfComplexComponents.split( ":" )[0];
    }

    public static String separateVarCustomizedEL(String elOfComplexComponents)
    {
        return elOfComplexComponents.split( ":" )[1];
    }

    /**
     * Extract changeable el-terms of a given attribute content
     *
     * @return changeable el-terms
     */
    public static List getChangeableELTerms(String attributeContent)
    {
        StringTokenizer st = new StringTokenizer( attributeContent, SettingsUtils.getComponentProperty( "el_indicator" ) + "{" );
        List results = new ArrayList();

        String part;
        String bean;
        FacesContext context = FacesContext.getCurrentInstance();
        Object resolvedBean;

        while (st.hasMoreTokens())
        {
            part = st.nextToken();

            if (part.contains( "}" ))
            {
                part = part.substring( 0, part.indexOf( "}" ) );
                if (part.contains( "." ))
                {
                    // get managed bean
                    bean = part.substring( 0, part.indexOf( "." ) );
                    resolvedBean = context.getApplication().getVariableResolver().resolveVariable( context, bean );

                    if (resolvedBean != null && isPropertyChangeable( resolvedBean, part ))
                    {
                        results.add( SettingsUtils.getComponentProperty( "el_indicator" ) + "{" + part + "}" );
                    }
                }
            }
        }
        return results;
    }

    /**
     * Try to change message and restore original value if it is possible
     *
     * @return true if the value of the message is changeable
     */
    private static boolean isPropertyChangeable(Object messagesBean, String pureELContent)
    {
        String property = getProperty( pureELContent );

        if (!TranslationUtils.isEmtyString( property ) && messagesBean instanceof PersistableMessagesBean)
        {
/*
            Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

            return ( (PersistableMessagesBean) messagesBean ).isMessageChangeable( property, locale );
*/
            return true;
        }

        return false;
    }

    /**
     * Extract the last property of a given el-term without #{ and }
     *
     * @return the last property of a given el-term
     */
    private static String getProperty(String pureELContent)
    {
        if (!TranslationUtils.isEmtyString( pureELContent ) && pureELContent.contains( "." ))
        {
            return pureELContent.substring( pureELContent.lastIndexOf( "." ) + 1, pureELContent.length() );
        }

        return null;
    }
}
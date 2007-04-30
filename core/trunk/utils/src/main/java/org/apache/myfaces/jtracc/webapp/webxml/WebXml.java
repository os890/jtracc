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
package org.apache.myfaces.jtracc.webapp.webxml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Manfred Geiler (latest modification by $Author: grantsmith $)
 * @version $Revision: 472630 $ $Date: 2006-11-08 20:40:03 +0000 (Wed, 08 Nov 2006) $
 */
public class WebXml
{
    private static final Log log = LogFactory.getLog(WebXmlParser.class);

    private Map _servlets = new HashMap();
    private Map _servletMappings = new HashMap();
    private Map _filters = new HashMap();
    private Map _filterMappings = new HashMap();
    
    private List _facesServletMappings = null;
    private List _facesExtensionsFilterMappings = null;

    void addServlet(String servletName, String servletClass)
    {
        if (_servlets.get(servletName) != null)
        {
            log.warn("Servlet " + servletName + " defined more than once, first definition will be used.");
        }
        else
        {
            _servlets.put(servletName, servletClass);
        }
    }

    void addFilter(String filterName, String filterClass)
    {
        if (_filters.get(filterName) != null)
        {
            log.warn("Filter " + filterName + " defined more than once, first definition will be used.");
        }
        else
        {
            _filters.put(filterName, filterClass);
        }
    }
    
    boolean containsServlet(String servletName)
    {
        return _servlets.containsKey(servletName);
    }

    boolean containsFilter(String filterName)
    {
        return _filters.containsKey(filterName);
    }
    
    void addServletMapping(String servletName, String urlPattern)
    {
        List mappings = (List)_servletMappings.get(servletName);
        if (mappings == null)
        {
            mappings = new ArrayList();
            _servletMappings.put(servletName, mappings);
        }
        mappings.add(urlPattern);
    }

    void addFilterMapping(String filterName, String urlPattern)
    {
        List mappings = (List)_filterMappings.get(filterName);
        if (mappings == null)
        {
            mappings = new ArrayList();
            _filterMappings.put(filterName, mappings);
        }
        mappings.add(urlPattern);
    }
    
    public List getFacesServletMappings()
    {
        if (_facesServletMappings != null) return _facesServletMappings;

        _facesServletMappings = new ArrayList();
        for (Iterator it = _servlets.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = (Map.Entry)it.next();
            String servletName = (String)entry.getKey();
            if (null == entry.getValue())
            {
                // the value is null in the case of jsp files listed as servlets
                // in cactus
                // <servlet>
                //   <servlet-name>JspRedirector</servlet-name>
                //   <jsp-file>/jspRedirector.jsp</jsp-file>
                // </servlet>
                continue;
            }
            Class servletClass = org.apache.myfaces.jtracc.util.ClassUtils.simpleClassForName((String)entry.getValue());
//            if (FacesServlet.class.isAssignableFrom(servletClass) ||
//                    DelegatedFacesServlet.class.isAssignableFrom(servletClass))
//            {
                List urlPatterns = (List)_servletMappings.get(servletName);
                if( urlPatterns != null )
                {
                    for (Iterator it2 = urlPatterns.iterator(); it2.hasNext(); )
                    {
                        String urlpattern = (String)it2.next();
                        _facesServletMappings.add(new org.apache.myfaces.jtracc.webapp.webxml.ServletMapping(servletName,
                                                                                                             servletClass,
                                                                                                             urlpattern));
                        if (log.isTraceEnabled())
                            log.trace("adding mapping for servlet + " + servletName + " urlpattern = " + urlpattern);
                    }
                }
//            }
//            else
//            {
//                if (log.isTraceEnabled()) log.trace("ignoring servlet + " + servletName + " " + servletClass + " (no FacesServlet)");
//            }
        }
        return _facesServletMappings;
    }

    /**
     * returns a list of {@see #org.apache.myfaces.jtracc.webapp.webxml.FilterMapping}s representing a
     * extensions filter entry
     */
    public List getFacesExtensionsFilterMappings()
    {
        if (_facesExtensionsFilterMappings != null) return _facesExtensionsFilterMappings;

        _facesExtensionsFilterMappings = new ArrayList();
        for (Iterator it = _filters.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = (Map.Entry)it.next();
            String filterName = (String)entry.getKey();
            String filterClassName = (String)entry.getValue();

            //JTRACC CHANGE
            if (!"org.apache.myfaces.jtracc.component.html.util.ExtensionsFilter".equals(filterClassName) &&
    			!"org.apache.myfaces.jtracc.webapp.filter.ExtensionsFilter".equals(filterClassName))
    		{
    			// not an extensions filter
    			continue;
    		}
            
            Class filterClass = org.apache.myfaces.jtracc.util.ClassUtils.simpleClassForName(filterClassName);
            List urlPatterns = (List)_filterMappings.get(filterName);
            if( urlPatterns != null )
            {
                for (Iterator it2 = urlPatterns.iterator(); it2.hasNext(); )
                {
                    String urlpattern = (String)it2.next();
                    _facesExtensionsFilterMappings.add(new org.apache.myfaces.jtracc.webapp.webxml.FilterMapping(
                    	filterName, filterClass, urlpattern));
                    if (log.isTraceEnabled())
                        log.trace("adding mapping for filter + " + filterName + " urlpattern = " + urlpattern);
                }
            }
        }
        return _facesExtensionsFilterMappings;
    }

    private static final String WEB_XML_ATTR = WebXml.class.getName();
    public static WebXml getWebXml(ExternalContext context)
    {
        WebXml webXml = (WebXml)context.getApplicationMap().get(WEB_XML_ATTR);
        if (webXml == null)
        {
            init(context);
            webXml = (WebXml)context.getApplicationMap().get(WEB_XML_ATTR);
        }
        return webXml;
    }

    /**
     * should be called when initialising Servlet
     * @param context
     */
    public static void init(ExternalContext context)
    {
        WebXmlParser parser = new WebXmlParser(context);
        WebXml webXml = parser.parse();
        context.getApplicationMap().put(WEB_XML_ATTR, webXml);
    }
}

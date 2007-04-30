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
package org.apache.myfaces.jtracc.renderkit.html.util;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A class which can interpret the URI generated by a corresponding ResourceHandler
 * implementation, locate that resource and write it to the servlet response stream.
 *
 * @author Mathias Broekelmann (latest modification by $Author: grantsmith $)
 * @version $Revision: 472792 $ $Date: 2006-11-09 07:34:47 +0100 (Do, 09 Nov 2006) $
 */
public interface ResourceLoader
{
    /**
     * Called by AddResource to render external resource data 
     * @param context TODO
     * @param request the request 
     * @param response the response to write the resource content to
     * @param resourceUri contains the uri part after the uri which 
     * is used to identify the resource loader
     * 
     * @throws IOException
     */
    public void serveResource(ServletContext context, HttpServletRequest request,
            HttpServletResponse response, String resourceUri) throws IOException;
}
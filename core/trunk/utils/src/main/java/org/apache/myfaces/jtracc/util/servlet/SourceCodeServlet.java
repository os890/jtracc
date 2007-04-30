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
 
package org.apache.myfaces.jtracc.util.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class SourceCodeServlet extends HttpServlet 
{
    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException
    {
        String webPage = req.getServletPath();
        
        // remove the '*.source' suffix that maps to this servlet
        int chopPoint = webPage.lastIndexOf(".source");
        
        webPage = webPage.substring(0, chopPoint);

        if(webPage.endsWith(".jsf"))
        {
            int jsfChopPoint = webPage.lastIndexOf(".jsf");

            webPage = webPage.substring(0, jsfChopPoint);

            webPage += ".jsp"; // replace jsf with jsp

            // get the actual file location of the requested resource
            String realPath = getServletConfig().getServletContext().getRealPath(webPage);

            outputFile(res, realPath);
        }
        else if(webPage.endsWith(".jsp"))
        {
            // get the actual file location of the requested resource
            String realPath = getServletConfig().getServletContext().getRealPath(webPage);

            outputFile(res, realPath);
        }
        else if(webPage.endsWith(".jspx"))
        {
            // get the actual file location of the requested resource
            String realPath = getServletConfig().getServletContext().getRealPath(webPage);

            outputFile(res, realPath);
        }
        else if(webPage.endsWith(".xhtml"))
        {
            // get the actual file location of the requested resource
            String realPath = getServletConfig().getServletContext().getRealPath(webPage);

            outputFile(res, realPath);
        }
        else
        {
            int beginChopPoint = webPage.lastIndexOf("/");
            int extensionChopPoint = webPage.lastIndexOf(".java");

            webPage = webPage.substring(beginChopPoint+1,extensionChopPoint);

            try
            {

                // get the actual file location of the requested resource; try it in classes first
                String realPath = getServletConfig().getServletContext().getRealPath("/WEB-INF/classes/"+webPage.replace('.','/')+".java");

                outputFile(res, realPath);
            }
            catch(Exception e)
            {
                //classes hasn't worked. How about src?
                String realPath = getServletConfig().getServletContext().getRealPath("/WEB-INF/src/"+webPage.replace('.','/')+".java");

                outputFile(res, realPath);
            }
            
        }
    }

    private void outputFile(HttpServletResponse res, String realPath)
            throws IOException
    {
        // output an HTML page
        res.setContentType("text/plain");

        // print some html
        ServletOutputStream out = res.getOutputStream();

        // print the file
        InputStream in = null;
        try
        {
            in = new BufferedInputStream(new FileInputStream(realPath));
            int ch;
            while ((ch = in.read()) !=-1)
            {
                out.print((char)ch);
            }
        }
        finally {
            if (in != null) in.close();  // very important
        }
    }
}

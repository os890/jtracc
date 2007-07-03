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

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Class for general I/O handling
 */
public class FileUtils
{
    public static void initFile(String propertyFilePath) throws IOException
    {
        File f = new File( propertyFilePath );
        if (f.exists() == false || f.length() == 0)
        {
            try
            {
                if (new File( new File( propertyFilePath ).getParentFile().getAbsolutePath() ).exists() == false)
                {
                    new File( new File( propertyFilePath ).getParentFile().getAbsolutePath() ).mkdirs();
                }
                new File( propertyFilePath ).createNewFile();
            }
            catch (IOException e)
            {
                FacesContext.getCurrentInstance().addMessage( null, new FacesMessage( FacesMessage.SEVERITY_ERROR, "file system error - can't save messages", null ) );
                throw e;
            }
        }
    }

    public static boolean isEmtyFile(String filePath) throws FileNotFoundException
    {
        File f = new File(filePath);

        if(!f.exists())
        {
            throw new FileNotFoundException( filePath );
        }

        return f.length() == 0;
    }
}

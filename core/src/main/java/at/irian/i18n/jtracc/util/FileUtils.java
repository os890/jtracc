package at.irian.i18n.jtracc.util;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Class for general I/O handling
 *
 * @author Gerhard Petracek
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

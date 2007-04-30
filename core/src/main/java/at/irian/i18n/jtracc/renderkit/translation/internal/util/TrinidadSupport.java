package at.irian.i18n.jtracc.renderkit.translation.internal.util;

import at.irian.i18n.jtracc.util.TranslationUtils;

/**
 * @author Thomas Spiegl
 */
public class TrinidadSupport
{
    public static void initRenderingContext()
    {
        try
        {
            TranslationUtils.loadClassForName( "org.apache.myfaces.trinidadinternal.renderkit.core.CoreRenderingContext" ).newInstance();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}

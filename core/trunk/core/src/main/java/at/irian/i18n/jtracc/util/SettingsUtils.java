package at.irian.i18n.jtracc.util;

import at.irian.i18n.jtracc.Settings;

import javax.faces.context.FacesContext;

/**
 * @author Gerhard Petracek
 */
public class SettingsUtils
{
    public static Settings getComponentSettings()
    {
        return (Settings) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get( Settings.BEAN_NAME );
    }

    public static String getComponentProperty(String property)
    {
        return getComponentSettings().getValue( property );
    }

    public static void setComponentProperty(String property, String value)
    {
        getComponentSettings().setValue( property, value );
    }
}

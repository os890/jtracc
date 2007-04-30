package at.irian.i18n.jtracc;

import javax.faces.event.ValueChangeEvent;
import java.util.List;
import java.util.Locale;

/**
 * Simple interface to resolve messages using a key to identify them. <br>
 * A typical implementation would be based on a ResourceBundle management
 * againts an external repository (properties file, database, plain text file,
 * etc.).
 *
 * @author Enrique Medina
 * @author Gerhard Petracek
 */
public interface MessageResolver
{
    public abstract String getMessage(String key, Locale locale);

    public abstract void setMessage(String key, String value, Locale locale);

    public abstract List getSupportedLocales();

    public abstract Locale getDefaultLocale();

    public abstract void changeMessage(ValueChangeEvent event);
}
package at.irian.i18n.jtracc.renderkit.translation.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import at.irian.i18n.jtracc.MessageResolver;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import java.util.Locale;

/**
 * Property resolver to detect properties belonging to translation
 * functionality.
 *
 * @author Enrique Medina
 */
public class TranslationPropertyResolver extends PropertyResolver
{

    /**
     * Logger available to subclasses
     */
    protected final Log logger = LogFactory.getLog( getClass() );

    /**
     * The original PropertyResolver
     */
    protected final PropertyResolver originalPropertyResolver;

    /**
     * Create a new TranslationPropertyResolver, using the given original
     * PropertyResolver.
     * <p/>
     * A JSF implementation will automatically pass its original resolver into
     * the constructor of a configured resolver, provided that there is a
     * corresponding constructor argument.
     *
     * @param originalPropertyResolver the original PropertyResolver
     */
    public TranslationPropertyResolver(PropertyResolver originalPropertyResolver)
    {
        this.originalPropertyResolver = originalPropertyResolver;
    }

    /**
     * If the base object is a <code>MessageResolver</code> class return
     * String.class as the <code>MessageResolver</code> returns strings.
     *
     * @param base     Base object from which to return a property type
     * @param property Property whose type is to be returned
     *
     * @return Type of property
     */
    public Class getType(Object base, Object property)
            throws EvaluationException, PropertyNotFoundException
    {
        if (base instanceof MessageResolver)
        {
            return String.class;
        }
        else
        {
            return this.originalPropertyResolver.getType( base, property );
        }
    }

    /**
     * Convert an index into a corresponding string, and delegate to
     * <code>getType(base, property)</code>.
     *
     * @param base  Base object from which to return a property type
     * @param index Index whose type is to be returned
     *
     * @return Type of property
     */
    public Class getType(Object base, int index) throws EvaluationException,
                                                        PropertyNotFoundException
    {
        if (base instanceof MessageResolver)
        {
            return getType( base, "" + index );
        }
        else
        {
            return this.originalPropertyResolver.getType( base, index );
        }
    }

    /**
     * Convert an index into a corresponding string, and delegate to
     * <code>getValue(base, property)</code>.
     *
     * @param base  Base object from which to return a property
     * @param index Index to be returned
     *
     * @return object in this context
     */
    public Object getValue(Object base, int index) throws EvaluationException,
                                                          PropertyNotFoundException
    {
        if (base instanceof MessageResolver)
        {
            return getValue( base, "" + index );
        }
        else
        {
            return this.originalPropertyResolver.getValue( base, index );
        }
    }

    /**
     * Looks up and returns the "property" of the object. If the base object is
     * a <code>MessageResolver</code> class call its
     * <code>getMessage(String key)</code> method.
     *
     * @param base     object that contains the property.
     * @param property Property to be returned.
     *
     * @return the message from the external message repository
     */
    public Object getValue(Object base, Object property)
            throws EvaluationException, PropertyNotFoundException
    {
        if (base instanceof MessageResolver)
        {
            MessageResolver messageResolver = (MessageResolver) base;
            Locale locale = FacesContext.getCurrentInstance().getViewRoot()
                    .getLocale();

            return messageResolver.getMessage( (String) property, locale );
        }
        else
        {
            return this.originalPropertyResolver.getValue( base, property );
        }
    }

    public boolean isReadOnly(Object base, int index)
            throws EvaluationException, PropertyNotFoundException
    {
        return this.originalPropertyResolver.isReadOnly( base, index );
    }

    public boolean isReadOnly(Object base, Object property)
            throws EvaluationException, PropertyNotFoundException
    {
        return this.originalPropertyResolver.isReadOnly( base, property );
    }

    /**
     * If the base object is a <code>MessageResolver</code> class, delegate to
     * the <code>setMessage(String key, String value)</code> method.
     *
     * @param base  Base object into which to store a property
     * @param index Index to be stored
     * @param value Value to be stored
     */
    public void setValue(Object base, int index, Object value)
            throws EvaluationException, PropertyNotFoundException
    {
        if (base instanceof MessageResolver)
        {
            setValue( base, "" + index, value );
        }
        else
        {
            this.originalPropertyResolver.setValue( base, index, value );
        }
    }

    /**
     * If the base object is a <code>MessageResolver</code> class, delegate to
     * the <code>setMessage(String key, String value)</code> method.
     *
     * @param base     Base object into which to store a property
     * @param property Index to be stored
     * @param value    Value to be stored
     */
    public void setValue(Object base, Object property, Object value)
            throws EvaluationException, PropertyNotFoundException
    {
        if (base instanceof MessageResolver)
        {
            MessageResolver messageResolver = (MessageResolver) base;
            Locale locale = FacesContext.getCurrentInstance().getViewRoot()

                    .getLocale();

            messageResolver.setMessage( (String) property, (String) value,
                                        locale );
        }
        else
        {
            this.originalPropertyResolver.setValue( base, property, value );
        }
    }
}
package at.irian.i18n.jtracc.persistence.util;

import at.irian.i18n.jtracc.persistence.PersistableMessagesBean;
import at.irian.i18n.jtracc.persistence.PersistenceDelegationEntryBean;
import at.irian.i18n.jtracc.persistence.beans.AbstractPersistableMessagesBean;
import at.irian.i18n.jtracc.persistence.beans.ListEntryMessagesBean;
import at.irian.i18n.jtracc.util.TranslationUtils;
import at.irian.i18n.jtracc.util.SettingsUtils;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Factory for messages beans - for usage in backing beans
 *
 * @author Gerhard Petracek
 */
public class PersistableMessagesBeanFactory
{
    /**
     * Logger available to subclasses
     */
    protected static final Log logger = LogFactory.getLog( PersistableMessagesBeanFactory.class );

    public static PersistenceDelegationEntryBean getPersistenceEntryBean() throws ClassNotFoundException
    {
        String beanName;
        String beanToCreate;

        beanName = SettingsUtils.getComponentProperty( "in_memory_persistence_delegation_bean_name" );
        beanToCreate = SettingsUtils.getComponentProperty( beanName );

        String persistenceDelegationInterface = "at.irian.i18n.jtracc.persistence.PersistenceDelegationEntryBean";
        boolean implementedInterface = false;

        try
        {
            Class persistenceBean = TranslationUtils.loadClassForName( beanToCreate );
            Class[] interfaces = persistenceBean.getInterfaces();

            // prove configured filter class
            for (int i = 0; i < interfaces.length; i++)
            {
                if (interfaces[i].getName().equals( persistenceDelegationInterface ))
                {
                    implementedInterface = true;
                }
            }

            if (!implementedInterface)
            {
                throw new ClassNotFoundException( persistenceDelegationInterface );
            }

            // configured bean
            return (PersistenceDelegationEntryBean) persistenceBean.newInstance();
        }
        catch (IllegalAccessException e)
        {
            logger.warn( "couldn't load persistence delegation entry bean - check value of property: " + beanName, e );
        }
        catch (InstantiationException e)
        {
            logger.warn( "couldn't load persistence delegation entry bean - check value of property: " + beanName, e );
        }

        // default beans
        return new ListEntryMessagesBean();
    }

    public static PersistableMessagesBean lookup(String managedBeanName)
    {
        FacesContext context = FacesContext.getCurrentInstance();
        return (AbstractPersistableMessagesBean) context.getApplication()
                .createValueBinding( SettingsUtils.getComponentProperty( "el_indicator" ) + "{" + managedBeanName + "}" ).getValue( context );
    }
}

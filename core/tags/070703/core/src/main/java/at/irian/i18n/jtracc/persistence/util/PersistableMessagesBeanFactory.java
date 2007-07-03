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

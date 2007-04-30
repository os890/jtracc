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
package at.irian.i18n.jtracc.persistence.beans;

import at.irian.i18n.jtracc.beans.InMemoryMessagesBean;
import at.irian.i18n.jtracc.persistence.PersistableMessagesBean;
import at.irian.i18n.jtracc.persistence.PersistenceDelegationEntryBean;

import java.util.Locale;

/**
 * Bean for entries of lists which delegates persistence functionality to a (central) bean
 * <p/>
 * !!!NEVER use/call specific JSF functionality (such as FacesContext) in the constructor of this class or of any subclass!!!
 */
public class ListEntryMessagesBean extends InMemoryMessagesBean implements PersistenceDelegationEntryBean
{
    private int index = 0;

    private PersistableMessagesBean persistableMessagesBean;

    protected void createKeyLocaleMapping(String key, String locale)
    {
        super.createKeyLocaleMapping( key + "_" + index, locale );
    }

    /**
     * @see at.irian.i18n.jtracc.beans.InMemoryMessagesBean
     */
    public void setMessage(String key, String value, Locale locale)
    {
        super.setMessage( key, value, locale );

        if (persistableMessagesBean != null)
        {
            persistableMessagesBean.setMessage( key + "_" + index, value, locale );

        }
    }

    /**
     * @see at.irian.i18n.jtracc.persistence.PersistenceDelegationBean
     */
    public void setPersistableMessagesBean(PersistableMessagesBean persistableMessagesBean)
    {
        this.persistableMessagesBean = persistableMessagesBean;
    }

    /**
     * Set index to differentiate keys of different entry beans. Key name in the property file: [key]_[index]
     *
     * @param index of current entry bean
     */
    public void setIndex(int index)
    {
        this.index = index;
    }

    public int getIndex()
    {
        return index;
    }

/*
    public boolean isMessageChangeable(String key, Locale locale)
    {
        return persistableMessagesBean.isMessageChangeable( key + "_" + index, locale );
    }
*/
}

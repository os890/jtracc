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
package at.irian.i18n.jtracc.beans;

import at.irian.i18n.jtracc.MessagesProvider;

import java.util.Properties;
import java.util.Map;
import java.util.Hashtable;

public class InMemoryMessagesCacheBean implements MessagesProvider
{
    private static Map properties = new Hashtable();

    public Properties get(String key)
    {
        return (Properties) properties.get( key );
    }

    public void set(String key, Properties messages)
    {
        properties.put(key, messages);
    }

    public boolean containsKey(String key)
    {
        return properties.containsKey( key );
    }

    public void remove(String key)
    {
        properties.remove( key );
    }
}

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
package at.irian.i18n.jtracc.renderkit.translation.model;

import at.irian.i18n.jtracc.MessageResolver;

public class MessageResolverBean
{
    public static final String MESSAGE_RESOLVER_MANAGED_BEAN_NAME = "myfaces_messageResolverBean";

    private MessageResolver _messageResolver;

    public void setMessageResolver(MessageResolver messageResolver)
    {
        _messageResolver = messageResolver;
    }

    public MessageResolver getMessageResolver()
    {
        check();
        return _messageResolver;
    }

    private void check()
    {
        if (_messageResolver == null)
        {
            throw new NullPointerException( "Add a managed-property \"messageResolver\"" +
                                            " to your managed-bean " + MESSAGE_RESOLVER_MANAGED_BEAN_NAME +
                                            ". The value returned by the property must implement " + MessageResolver.class.getName() );
        }
    }

}

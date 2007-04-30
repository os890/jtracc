package at.irian.i18n.jtracc.renderkit.translation.model;

import at.irian.i18n.jtracc.MessageResolver;

/**
 * @author Thomas Spiegl
 */
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

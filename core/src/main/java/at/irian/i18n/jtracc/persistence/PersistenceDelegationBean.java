package at.irian.i18n.jtracc.persistence;

/**
 * Formal marker interface for all messages beans, which delegate persistence calls
 *
 * @author Gerhard Petracek
 */
public interface PersistenceDelegationBean extends PersistableMessagesBean
{
    /**
     * @param persistableMessagesBean Bean for delegating persistence functionality
     */
    public void setPersistableMessagesBean(PersistableMessagesBean persistableMessagesBean);
}

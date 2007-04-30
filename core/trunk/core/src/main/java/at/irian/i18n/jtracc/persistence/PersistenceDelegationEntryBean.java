package at.irian.i18n.jtracc.persistence;

/**
 * Interface for all list entry messages beans, which delegate persistence calls
 *
 * @author Gerhard Petracek
 */
public interface PersistenceDelegationEntryBean extends PersistenceDelegationBean
{
    /**
     * Set index of a message which is wrapped with an entry bean
     *
     * @param index number of an entry
     */
    public void setIndex(int index);

    public int getIndex();
}

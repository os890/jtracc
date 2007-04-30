package at.irian.i18n.jtracc.util;

import java.util.Comparator;
import java.util.Locale;

/**
 * @author Gerhard Petracek
 */
public class LocaleComparator implements Comparator
{
    public int compare(Object o1, Object o2)
    {
        Locale lo1 = (Locale) o1;
        Locale lo2 = (Locale) o2;

        String l1 = ( lo1 ).getDisplayLanguage() + LocaleUtils.getLocaleString( lo1 );
        String l2 = ( lo2 ).getDisplayLanguage() + LocaleUtils.getLocaleString( lo2 );

        return l1.compareToIgnoreCase( l2 );
    }
}

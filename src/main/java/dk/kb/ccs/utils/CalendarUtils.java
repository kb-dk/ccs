package dk.kb.ccs.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for handling the dates.
 * @author jolf
 */
public class CalendarUtils {

    /**
     * Retrieve the text version of the given date.
     * @param d The date to convert into a text.
     * @return The text version of the date.
     */
    public static String getDateAsString(Date d) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        return format.format(d);
    }
}

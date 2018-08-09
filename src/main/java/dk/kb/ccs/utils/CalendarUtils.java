package dk.kb.ccs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for handling the dates.
 * @author jolf
 */
public class CalendarUtils {
    /** The logger.*/
    private static final Logger log = LoggerFactory.getLogger(CalendarUtils.class);

    /** The number of milliseconds for a month.*/
    public static final Long MILLIS_PER_MONTH = 2592000000L;
    /** The number of milliseconds for a day.*/
    public static final Long MILLIS_PER_DAY = 86400000L;
    /** The number of milliseconds for an HOUR.*/
    public static final Long MILLIS_PER_HOUR = 3600000L;
    
    /** The simple format for a date.*/
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    /** The alternative simple format for a date.*/
    public static final String FORMAT_DATE2 = "yyyy/MM/dd";
    /** The format for a date with time.*/
//    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss.SSSZ";
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * Parses a string as a date.
     * @param s The string to parse.
     * @return The date parsed from the string, or epoch if the string cannot be parsed.
     */
    public static Date getDateFromString(String s) {
        SimpleDateFormat format;
        try {
            format = new SimpleDateFormat(FORMAT_DATETIME);
            return format.parse(s);
        } catch (ParseException e) {
            log.debug("Cannot parse '" + s + "' with format: " + FORMAT_DATETIME);
        }
        try {
            format = new SimpleDateFormat(FORMAT_DATE);
            return format.parse(s);
        } catch (ParseException e) {
            log.debug("Cannot parse '" + s + "' with format: " + FORMAT_DATE);
        }
        try {
            format = new SimpleDateFormat(FORMAT_DATE2);
            return format.parse(s);
        } catch (ParseException e) {
            log.debug("Cannot parse '" + s + "' with format: " + FORMAT_DATE2);
        }
        try {
            Long l = Long.parseLong(s);
            return new Date(l);
        } catch (NumberFormatException e) {
            log.warn("Cannot parse the text '" + s + "' as a date or a long. Returning epoch.", e);
            return new Date(0);
        }
    }

    /**
     * Retrieve the text version of the given datetime.
     * @param d The date to convert into a text.
     * @return The text version of the datetime.
     */
    public static String getDateTimeAsString(Date d) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATETIME);
        return format.format(d);
    }

    /**
     * Retrieve the text version of the given datetime.
     * @param d The date in millis since epoch.
     * @return The text version of the datetime.
     */
    public static String getDateTimeAsString(Long millis) {
        return getDateTimeAsString(new Date(millis));
    }
    
    /**
     * Retrieve the text version of the given date.
     * @param d The date to convert into a text.
     * @return The text version of the date.
     */
    public static String getDateAsString(Date d) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE);
        return format.format(d);
    }
}

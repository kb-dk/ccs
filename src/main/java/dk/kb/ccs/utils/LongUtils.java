package dk.kb.ccs.utils;

/**
 * Utility class for method regarding Long objects.
 * @author jolf
 */
public class LongUtils {

    /**
     * Retrieves the Long value of a object of undetermined type.
     * @param o The long in a undetermined type.
     * @return The long value of the object.
     */
    public static Long getLong(Object o) {
        if(o instanceof String) {
            return Long.parseLong((String) o);
        } 
        if(o instanceof Integer) {
            return Long.valueOf((Integer) o);
        }
        return (Long) o;
    }
}

package dk.kb.ccs.utils;

/**
 * Utility class for dealing with system properties.
 * @author jolf
 */
public class PropertyUtils {

    /**
     * Retrieves the version of the CCS.
     * @return The version of the CCS, or 'undetermined' if the property cannot be extracted.
     */
    public static String getVersion() {
        String res = PropertyUtils.class.getPackage().getImplementationVersion();
        if(res == null) {
            res = "undetermined";
        }
        return res;
    }
}

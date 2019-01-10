package dk.kb.ccs.utils;

/**
 * Utility class for dealing with system properties.
 * @author jolf
 */
public class PropertyUtils {
    /** The default value for the undetermined version.*/
    protected static final String UNDETERMINED_VERSION = "undetermined";
    
    /**
     * Retrieves the version of the CCS.
     * @return The version of the CCS, or 'undetermined' if the property cannot be extracted.
     */
    public static String getVersion() {
        String res = PropertyUtils.class.getPackage().getImplementationVersion();
        if(res == null) {
            res = UNDETERMINED_VERSION;
        }
        return res;
    }
}

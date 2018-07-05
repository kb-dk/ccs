package dk.kb.ccs.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import dk.kb.cumulus.utils.ArgumentCheck;

/**
 * Utility class for dealings with Strings.
 */
public class StringUtils {
    /** The result of an empty string.*/
    protected static final String EMPTY_LIST = "[]";
    
    /**
     * Method for changing a list of strings, into a single string.
     * @param list The list of string.
     * @param separator The separator between each string in the list.
     * @return The strings in the list combined to a single string, but separated by the separator.
     */
    public static String listToString(List<String> list, String separator) {
        ArgumentCheck.checkNotNull(separator, "String separator");
        if(list == null || list.isEmpty()) {
            return EMPTY_LIST;
        }
        StringBuilder res = new StringBuilder();
        for(String s : list) {
            res.append(s);
            res.append(separator);
        }
        return res.substring(0, res.length()-1).toString();
    }
    
    /**
     * Method for extracting a JSON array as a single string.
     * @param array The JSON array.
     * @param separator The separator between the elements of the array.
     * @return The string with the elements.
     */
    public static String extractJSONArray(JSONArray array, String separator) {
        ArgumentCheck.checkNotNull(separator, "String separator");
        if(array == null || array.length() == 0) {
            return EMPTY_LIST;
        }
        List<String> res = new ArrayList<String>();
        
        for(int i = 0; i < array.length(); i++) {
            String s = array.getString(i);
            if(!res.contains(s)) {
                res.add(s);
            }
        }
        return listToString(res, separator);
    }
    /**
     * Method for extracting a JSON array as a single string.
     * @param array The JSON array.
     * @param separator The separator between the elements of the array.
     * @return The string with the elements.
     */
    public static String extractSolrArray(List<String> array, String separator) {
        ArgumentCheck.checkNotNull(separator, "String separator");
        if(array == null || array.size() == 0) {
            return EMPTY_LIST;
        }
        List<String> res = new ArrayList<String>();
        
        for(int i = 0; i < array.size(); i++) {
            String s = array.get(i);
            if(!res.contains(s)) {
                res.add(s);
            }
        }
        return listToString(res, separator);
    }
}

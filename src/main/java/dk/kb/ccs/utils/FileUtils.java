package dk.kb.ccs.utils;

import java.io.File;
import java.io.IOException;

/**
 * Class with utility methods regarding Files.
 * 
 * @author jolf
 */
public class FileUtils {
    /**
     * Retrieves a writable file at the given path.
     * Will throw an exception, if it fails to instantiate the file, or if it is not writable. 
     * @param path The path to the file.
     * @return The writable file.
     */
    public static File getWritableFile(String path) {
        File res = new File(path);
        
        try {
            if(!res.exists()) {
                res.createNewFile();
            }
            if(!res.canWrite()) {
                throw new IllegalStateException("Cannot write to the file at '" + path + "'.");
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot instantiate a writable file at '"
                    + path + "'.", e);
        }
        
        return res;
    }
}

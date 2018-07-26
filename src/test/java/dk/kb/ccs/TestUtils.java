package dk.kb.ccs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Assume;

public class TestUtils {

    public static Configuration getTestConfiguration() {
        Configuration conf = null;
        try {
            File f = new File("ccs.yml");
            Assume.assumeTrue("Local configuration file exists", f.isFile());
            conf = new Configuration(f.getAbsolutePath());
        } catch (IOException e) {
            Assume.assumeNoException(e);
        }
        return conf;
    }
    
    protected static File tmpDir = new File("tmp");
    
    public static File getTmpDir() {
        if(!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        
        return tmpDir;
    }
    
    public static void cleanTmpDir() throws IOException {
        delete(tmpDir);
    }
    
    public static void delete(File f) throws IOException {
        if(f.listFiles() != null) {
            for(File file : f.listFiles()) {
                delete(file);
            }
            f.delete();
        } else {
            Files.deleteIfExists(f.toPath());
        }
    }
}

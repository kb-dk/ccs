package dk.kb.ccs;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConfigurationTest {
    
    @Test
    public void testInstantiation() throws Exception {
        File f = new File("src/test/resources/ccs.yml");
        Assert.assertTrue(f.isFile());
        
        Configuration conf = new Configuration(f.getAbsolutePath());
        
        Assert.assertNotNull(conf);
    }
}

package dk.kb.ccs;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import dk.kb.ccs.conf.Configuration;
import dk.kb.ccs.solr.CcsRecord;
import dk.kb.ccs.solr.CcsRecordTest;

@SpringBootTest
public class CumulusWrapperTest {
    Configuration conf;
    CumulusWrapper wrapper; 
    
    @Before
    public void setup() {
        conf = TestUtils.getTestConfiguration();
        
        wrapper = new CumulusWrapper();
        wrapper.conf = conf;
        wrapper.initialize();
    }
    
    @After
    public void tearDown() throws Exception {
        wrapper.getCumulusServer().close();
    }
    
    @Test
    public void testUpdate() throws Exception {
        
        CcsRecord record = CcsRecordTest.getTestRecord();
        
        wrapper.ccsUpdate(record);
    }
}

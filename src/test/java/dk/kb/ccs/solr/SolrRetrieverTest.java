package dk.kb.ccs.solr;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import dk.kb.ccs.Configuration;
import dk.kb.ccs.CumulusWrapper;
import dk.kb.ccs.TestUtils;

@SpringBootTest
public class SolrRetrieverTest {
    
    @Test
    public void testInstantiation() throws Exception {
        Configuration conf = TestUtils.getTestConfiguration();
        CumulusWrapper cumulusWrapper = Mockito.mock(CumulusWrapper.class);
        
        SolrRetriever solr = new SolrRetriever();
        solr.conf = conf;
        solr.cumulusWrapper = cumulusWrapper;
        
        List<String> ids = solr.getIDsForCrowd(new Date(0));
        System.err.println(ids);
    }
}


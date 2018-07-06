package dk.kb.ccs.workflow.steps;

import java.util.Arrays;
import java.util.UUID;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import dk.kb.ccs.CumulusWrapper;
import dk.kb.ccs.solr.CcsRecord;
import dk.kb.ccs.solr.SolrRetriever;

@SpringBootTest
public class CrowdReturnStepTest {

    @Test
    public void testRun() throws Exception {
        CumulusWrapper cumulusWrapper = Mockito.mock(CumulusWrapper.class);
        SolrRetriever solrRetriever = Mockito.mock(SolrRetriever.class);
        CcsRecord record = Mockito.mock(CcsRecord.class);
        
        CrowdReturnStep step = new CrowdReturnStep(cumulusWrapper, solrRetriever);
        
        String id = UUID.randomUUID().toString();
        Mockito.when(solrRetriever.findIDsForCrowd()).thenReturn(Arrays.asList(id));
        Mockito.when(solrRetriever.getRecordForId(Mockito.eq(id))).thenReturn(record);
        
        step.run();
        
        Mockito.verify(solrRetriever).findIDsForCrowd();
        Mockito.verify(solrRetriever).getRecordForId(Mockito.eq(id));
        Mockito.verify(solrRetriever).updateRecord(Mockito.eq(id));
        Mockito.verifyNoMoreInteractions(solrRetriever);
        
        Mockito.verify(cumulusWrapper).ccsUpdate(Mockito.eq(record));
        Mockito.verifyNoMoreInteractions(cumulusWrapper);
        
        Mockito.verifyZeroInteractions(record);
    }
}

package dk.kb.ccs.workflow.steps;

import java.util.Date;
import java.util.UUID;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;

import dk.kb.ccs.CumulusWrapper;
import dk.kb.ccs.reporting.Reporter;
import dk.kb.ccs.solr.CcsRecord;
import dk.kb.ccs.solr.SolrRetriever;
import dk.kb.ccs.solr.SolrSearchResult;

@SpringBootTest
public class CrowdReturnStepTest {

    @Test
    public void testRun() throws Exception {
        CumulusWrapper cumulusWrapper = Mockito.mock(CumulusWrapper.class);
        SolrRetriever solrRetriever = Mockito.mock(SolrRetriever.class);
        Reporter reporter = Mockito.mock(Reporter.class);
        CcsRecord record = Mockito.mock(CcsRecord.class);
        
        CrowdReturnStep step = new CrowdReturnStep(cumulusWrapper, solrRetriever, reporter);
        
        String id = UUID.randomUUID().toString();
        SolrSearchResult res = new SolrSearchResult();
        res.addId(id);
        Mockito.when(solrRetriever.findIDsForCrowd()).thenReturn(res);
        Mockito.when(solrRetriever.getRecordForId(Mockito.eq(id))).thenReturn(record);
        
        step.run();
        
        Mockito.verify(solrRetriever).findIDsForCrowd();
        Mockito.verify(solrRetriever).getRecordForId(Mockito.eq(id));
        Mockito.verify(solrRetriever).updateRecord(Mockito.eq(id), Mockito.eq(SolrRetriever.SOLR_MODIFY_USER_CROWD_SERVICE));
        Mockito.verifyNoMoreInteractions(solrRetriever);
        
        Mockito.verify(cumulusWrapper).ccsUpdate(Mockito.eq(record));
        Mockito.verifyNoMoreInteractions(cumulusWrapper);
        
        Mockito.verify(reporter).addResult(Mockito.any(Date.class), Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(reporter);
        
        Mockito.verifyZeroInteractions(record);
    }
    

    @Test
    public void testFailure() throws Exception {
        CumulusWrapper cumulusWrapper = Mockito.mock(CumulusWrapper.class);
        SolrRetriever solrRetriever = Mockito.mock(SolrRetriever.class);
        Reporter reporter = Mockito.mock(Reporter.class);
        CcsRecord record = Mockito.mock(CcsRecord.class);
        
        CrowdReturnStep step = new CrowdReturnStep(cumulusWrapper, solrRetriever, reporter);
        
        String id = UUID.randomUUID().toString();
        SolrSearchResult res = new SolrSearchResult();
        res.addId(id);
        Mockito.when(solrRetriever.findIDsForCrowd()).thenReturn(res);
        Mockito.when(solrRetriever.getRecordForId(Mockito.eq(id))).thenReturn(record);
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                throw new IllegalStateException("TEST CUMULUS FAILURE");
            }
        }).when(cumulusWrapper).ccsUpdate(Mockito.any(CcsRecord.class));
        
        step.run();
        
        Mockito.verify(solrRetriever).findIDsForCrowd();
        Mockito.verify(solrRetriever).getRecordForId(Mockito.eq(id));
        Mockito.verify(solrRetriever).updateRecord(Mockito.eq(id), Mockito.eq(SolrRetriever.SOLR_MODIFY_USER_FAILURE));
        Mockito.verifyNoMoreInteractions(solrRetriever);
        
        Mockito.verify(cumulusWrapper).ccsUpdate(Mockito.eq(record));
        Mockito.verifyNoMoreInteractions(cumulusWrapper);
        
        Mockito.verify(reporter).addResult(Mockito.any(Date.class), Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(reporter);
        
        Mockito.verifyZeroInteractions(record);
    }
}

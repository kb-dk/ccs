package dk.kb.ccs.solr;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import dk.kb.ccs.Configuration;
import dk.kb.ccs.TestUtils;

@SpringBootTest
public class SolrRetrieverTest {
    
    @Test
    public void testFindingIDsForCrowd() throws Exception {
        Configuration conf = TestUtils.getTestConfiguration();
        
        SolrRetriever solr = new SolrRetriever();
        solr.conf = conf;
        
        SolrSearchResult ids = solr.findIDsForCrowd();
        System.err.println(ids.getIds());
        System.err.println(ids.hasMoreResults);
    }

    @Test
    public void testGetRecordForId() throws Exception {
        Configuration conf = TestUtils.getTestConfiguration();
        
        SolrRetriever solr = new SolrRetriever();
        solr.conf = conf;
        
        CcsRecord record = solr.getRecordForId("/images/luftfo/2011/maj/luftfoto/object182167");
        Assert.assertNotNull(record);
        Assert.assertEquals("Luftfoto OM", record.getCatalogName());
        Assert.assertEquals("A088673_13.tif", record.getRecordName());
        Assert.assertEquals("Jensen, Iris & Børge, Langsted - 1988 -", record.getTitel());
        Assert.assertEquals("Iris og Børge Jensen, gårdejere", record.getPerson());
        Assert.assertEquals("Overgaard", record.getBygningsnavn());
        Assert.assertEquals("Danmark, Fyn, Langsted, Højrupgyden 11", record.getSted());
        Assert.assertEquals("Højrupgyden", record.getVejnavn());
        Assert.assertEquals("11", record.getHusnummer());
        Assert.assertEquals("Langsted", record.getLokalitet());
        Assert.assertNull(record.getPostnummer());
        Assert.assertEquals("Langsted", record.getBy());
        Assert.assertNull(record.getSogn());
        Assert.assertEquals("", record.getMatrikelnummer());
        Assert.assertNull(record.getNote());
        // TODO: Needs to included in the JSON record from SOLR.
        Assert.assertEquals("hest", record.getKommentar());
        
        // Cannot guarantee the order.
        String emneOrd = record.getEmneord();
        Assert.assertTrue(emneOrd.contains("Iris og Børge Jensen"));
        Assert.assertTrue(emneOrd.contains("gårdejere"));
        Assert.assertTrue(emneOrd.contains("brændestabel"));
        Assert.assertTrue(emneOrd.contains("urtehave"));
        Assert.assertTrue(emneOrd.contains("gæs"));
        Assert.assertTrue(emneOrd.contains("prydhave"));
        Assert.assertTrue(emneOrd.contains("havemøbler"));
        Assert.assertTrue(emneOrd.contains("dam"));
        Assert.assertTrue(emneOrd.contains("landbrugsredskaber"));
        
        Assert.assertEquals("55.27572170005405,10.177644300083102", record.getGeoreference());
    }
}


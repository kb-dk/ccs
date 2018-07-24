package dk.kb.ccs.solr;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import dk.kb.ccs.utils.StreamUtils;

@SpringBootTest
public class CcsRecordTest {

    public static CcsRecord getTestRecord() throws Exception{
        File f = new File("src/test/resources/luftfoto-solr.json");
        Assert.assertTrue(f.isFile());
        
        String content = StreamUtils.extractInputStreamAsString(new FileInputStream(f));
        JSONObject json = new JSONObject(content);
        JSONObject response = json.getJSONObject("response");
        JSONObject recordJson = response.getJSONArray("docs").getJSONObject(0);

        return new CcsRecord(recordJson);
    }
    
    
    @Test
    public void testInstantiation() throws Exception {
        CcsRecord record = getTestRecord();
        
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
        Assert.assertEquals("1234", record.getPostnummer());
        Assert.assertEquals("Langsted", record.getBy());
        Assert.assertEquals("sogn", record.getSogn());
        Assert.assertEquals("1234567890", record.getMatrikelnummer());
        Assert.assertEquals("noten står her", record.getNote());
        // TODO: Needs to included in the JSON record from SOLR.
        Assert.assertNull(record.getKommentar());
        
        Assert.assertEquals("Iris og Børge Jensen, gårdejere,brændestabel,urtehave,gæs,prydhave,havemøbler,dam,landbrugsredskaber", record.getEmneord());
        Assert.assertEquals("55.27572170005405,10.177644300083102", record.getGeoreference());
    }
}


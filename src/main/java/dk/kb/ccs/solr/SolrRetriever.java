package dk.kb.ccs.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import dk.kb.ccs.Configuration;
import dk.kb.ccs.CumulusWrapper;

public class SolrRetriever {
    /** The log.*/
    protected static final Logger log = LoggerFactory.getLogger(SolrRetriever.class);
    
    /** TODO: extract the catalog name from the SOLR data instead.*/
    protected static final String CATALOG_NAME = "luftfoto";
    
    /** The configuration. Auto-wired.*/
    @Autowired
    protected Configuration conf;

    /**
     * Retrieves the 
     * @return
     * @throws IOException
     */
    public List<String> getIDsForCrowd() throws IOException {
        SolrClient client = new HttpSolrClient.Builder(conf.getSolrUrl()).build();
        
        SolrQuery query = new SolrQuery();
//        query.setQuery("cobject_last_modified_by_ssi:crowd");
        query.setQuery("id:\"/images/luftfo/2011/maj/luftfoto/object182167\"");
        query.addFilterQuery(conf.getSolrFilterQuery());
        query.setFields("id");
        query.setStart(0);
        query.set("defType", "edismax");
        
        try {
            List<String> res = new ArrayList<String>();
            QueryResponse response = client.query(query);
            SolrDocumentList results = response.getResults();
            
            for (int i = 0; i < results.size(); ++i) {
                res.add((String) results.get(i).getFieldValue("id"));
            }
            return res;
        } catch (SolrServerException e) {
            throw new IOException("Issue extracting data from Solr.", e);
        }
    }
    
    public CcsRecord getRecordForId(String id) throws IOException {
        SolrClient client = new HttpSolrClient.Builder(conf.getSolrUrl()).build();
        
        SolrQuery query = new SolrQuery();
        query.setQuery("id:" + id);
        query.addFilterQuery(conf.getSolrFilterQuery());
        query.setStart(0);
        query.set("defType", "edismax");
        
        try {
            QueryResponse response = client.query(query);
            SolrDocumentList results = response.getResults();
            
            if(results.size() == 0) {
                log.warn("Could not find the document for '" + id + "'. Returning a null.");
                return null;
            } else if(results.size() > 1) {
                log.warn("Found more than one document for id '" + id + "'. Returning the first.");
            }
            return new CcsRecord(results.get(0), CATALOG_NAME);
        } catch (Exception e) {
            throw new IOException("Issue extracting data from Solr.", e);
        }
    }
    
    public void updateRecord(String id) {
        // TODO
    }
    
}

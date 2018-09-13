package dk.kb.ccs.solr;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.kb.ccs.conf.Configuration;

/**
 * Class for handling the interactions with SOLR.
 * @author jolf
 */
@Component
public class SolrRetriever {
    /** The log.*/
    protected final Logger log = LoggerFactory.getLogger(SolrRetriever.class);
    
    /** The username in SOLR for the CumulusCrowdService.*/
    public static final String SOLR_MODIFY_USER_CROWD_SERVICE = "ccs";
    /** The username in SOLR for the failure case.*/
    public static final String SOLR_MODIFY_USER_FAILURE = "failure";
    /** The username in SOLR for the crowd-sourced materials.*/
    public static final String SOLR_MODIFY_USER_CROWD = "crowd";
    
    /** The SOLR update action for setting a new value.*/
    protected static final String SOLR_UPDATE_ACTION_SET = "set";
    /** The SOLR argument for definition type.*/
    protected static final String SOLR_ARGUMENT_DEFINITION_TYPE = "defType";
    /** The definition type edismax.*/
    protected static final String DEF_TYPE_EDISMAX = "edismax";
    
    /** The field name for the modify user.*/
    protected static final String FIELD_MODIFY_USER = "cobject_last_modified_by_ssi";
    /** The field name for the id.*/
    protected static final String FIELD_ID = "id";
    
    /** The configuration. Auto-wired.*/
    @Autowired
    protected Configuration conf;

    /**
     * Retrieves the list of IDs of crowd sourced records from the SOLR index.
     * It should not find the records, which have any other last_modified_by than 'ccs'. 
     * @return The list of IDs for the crowd sourced records.
     * @throws IOException If it fails.
     */
    public SolrSearchResult findIDsForCrowd() throws IOException {
        try (SolrClient client = new HttpSolrClient.Builder(conf.getSolrUrl()).build()) {
            SolrQuery query = new SolrQuery();
            query.setQuery(FIELD_MODIFY_USER + ":" + SOLR_MODIFY_USER_CROWD);
            query.addFilterQuery(conf.getSolrFilterQuery());
            query.setFields(FIELD_ID);
            query.setStart(0);
            query.setRows(conf.getSolrMaxResults());
            query.set(SOLR_ARGUMENT_DEFINITION_TYPE, DEF_TYPE_EDISMAX);
            
            SolrSearchResult res = new SolrSearchResult();
            QueryResponse response = client.query(query);
            SolrDocumentList results = response.getResults();
            log.info("Found # of solr results: " + results.size());
            
            for (int i = 0; i < results.size(); ++i) {
                res.addId((String) results.get(i).getFieldValue(FIELD_ID));
            }
            res.setHasMoreIds(results.getNumFound() > results.size());
            return res;
        } catch (SolrServerException e) {
            throw new IOException("Issue extracting data from Solr.", e);
        }
    }
    
    /**
     * Retrieves the SOLR record for a given ID, and then returns it as a CcsRecord.
     * @param id The ID for the solr record to find.
     * @return The CcsRecord for the SOLR record with the given ID.
     * @throws IOException If it fails to retrieve data from SOLR.
     */
    public CcsRecord getRecordForId(String id) throws IOException {
        try (SolrClient client = new HttpSolrClient.Builder(conf.getSolrUrl()).build()) {
            SolrQuery query = new SolrQuery();
            query.setQuery(FIELD_ID + ":" + id);
            query.addFilterQuery(conf.getSolrFilterQuery());
            query.setStart(0);
            query.set(SOLR_ARGUMENT_DEFINITION_TYPE, DEF_TYPE_EDISMAX);
            QueryResponse response = client.query(query);
            SolrDocumentList results = response.getResults();
            
            log.info("SOLR query: " + query.toString());
            if(results.size() == 0) {
                log.warn("Could not find the document for '" + id + "'. Returning a null.");
                return null;
            } else if(results.size() > 1) {
                log.warn("Found more than one document for id '" + id + "'. Returning the first.");
            }
            log.info("Creating a CCS record from: " + results.get(0));
            
            return new CcsRecord(results.get(0));
        } catch (SolrServerException e) {
            throw new IOException("Issue extracting data from Solr.", e);
        }
    }
    
    /**
     * Updates the SOLR record with the given id, so it has this service as the 'last_modified_by' user. 
     * @param id The ID of the SOLR record to update.
     * @param updateUser The solr modify user to update the record.
     * @throws IOException If it fails to update.
     */
    public void updateRecord(String id, String updateUser) throws IOException {
        try (SolrClient client = new HttpSolrClient.Builder(conf.getSolrUrl()).build()) {
            SolrInputDocument updateDoc = new SolrInputDocument();
            
            updateDoc.addField(FIELD_ID, id);
            
            Map<String,Object> fieldModifier = new HashMap<>(1);
            fieldModifier.put(SOLR_UPDATE_ACTION_SET, updateUser);
            updateDoc.addField(FIELD_MODIFY_USER, fieldModifier);
            
            UpdateRequest request = new UpdateRequest();
            request.add(updateDoc);
            request.process(client);
            client.commit();
        } catch (SolrServerException e) {
            throw new IOException("Issue occured while updating Solr document.", e);
        }
    }
}

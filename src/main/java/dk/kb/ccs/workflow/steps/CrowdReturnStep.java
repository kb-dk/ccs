package dk.kb.ccs.workflow.steps;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.kb.ccs.CumulusWrapper;
import dk.kb.ccs.reporting.Reporter;
import dk.kb.ccs.solr.CcsRecord;
import dk.kb.ccs.solr.SolrRetriever;
import dk.kb.ccs.solr.SolrSearchResult;

/**
 * Step for retrieving changed records from COP, extracts the crowd-sourced fields from  
 * each COP record, then find the related Cumulus record, and return the fields to the Cumulus record.
 */
public class CrowdReturnStep extends WorkflowStep {
    /** The log.*/
    protected static final Logger log = LoggerFactory.getLogger(CrowdReturnStep.class);
    
    /** The Cumulus retriever, for finding the Cumulus records for the COP records.*/
    protected final CumulusWrapper cumulusWrapper;
    /** The SOLR retriever for searchin, find and updating data.*/
    protected final SolrRetriever solrRetriever;
    /** The Reporter for reporting the results of each run.*/
    protected final Reporter report;
    
    /**
     * Constructor.
     * @param cumulusWrapper The CumulusRetriever for interacting with Cumulus. 
     * @param solrRetriever The SolrRetriever for interacting with SOLR.
     * @param report The reporter for reporting the results of each run.
     */
    public CrowdReturnStep(CumulusWrapper cumulusWrapper, SolrRetriever solrRetriever, Reporter report) {
        this.cumulusWrapper = cumulusWrapper;
        this.solrRetriever = solrRetriever;
        this.report = report;
    }
    
    @Override
    protected void runStep() throws IOException {
        SolrSearchResult res = solrRetriever.findIDsForCrowd();
        for(String id : res.getIds()) {
            handleCrowdSourcedRecord(id);
        }
        report.addResult(Long.valueOf(res.getIds().size()));
    }
    
    /**
     * Handles the crowdsourced record for the given id.
     * It retrieves the SOLR record, finds and updates the related Cumulus record, 
     * and then updates the SOLR record to not being found again.
     * @param id The ID of the crowd sourced SOLR record to retrieve and update.
     * @throws IOException If it fails to find or update the record.
     */
    protected void handleCrowdSourcedRecord(String id) throws IOException {
        log.info("Handling the crowd sourced material for id '" + id + "'.");
        CcsRecord record = solrRetriever.getRecordForId(id);
        cumulusWrapper.ccsUpdate(record);
        solrRetriever.updateRecord(id);
    }
    
    @Override
    public String getName() {
        return "Crowd Return Step";
    }
}

package dk.kb.ccs.workflow.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.kb.ccs.CumulusWrapper;

/**
 * Step for retrieving changed records from COP, extracts the crowd-sourced fields from  
 * each COP record, then find the related Cumulus record, and return the fields to the Cumulus record.
 */
public class CrowdReturnStep extends WorkflowStep {
    /** The log.*/
    protected static final Logger log = LoggerFactory.getLogger(CrowdReturnStep.class);
    
    /** The Cumulus retriever, for finding the Cumulus records for the COP records.*/
    protected final CumulusWrapper retriever;
    
    /**
     * Constructor.
     * @param retriever The CumulusRetriever.
     */
    public CrowdReturnStep(CumulusWrapper retriever) {
        this.retriever = retriever;
    }
    
    @Override
    protected void runStep() {
        // TODO: what to do?
    }
    
    @Override
    public String getName() {
        return "Crowd Return Step";
    }

}

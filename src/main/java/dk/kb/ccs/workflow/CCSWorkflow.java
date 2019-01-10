package dk.kb.ccs.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.kb.ccs.CumulusWrapper;
import dk.kb.ccs.reporting.Reporter;
import dk.kb.ccs.solr.SolrRetriever;
import dk.kb.ccs.workflow.steps.CrowdReturnStep;

/**
 * The CumulusCrowdService workflow for the backflow.
 */
@Component
public class CCSWorkflow extends Workflow {
    /** The Cumulus retriever.*/
    @Autowired
    protected CumulusWrapper cumulusWrapper;
    /** The SOLR retriever.*/
    @Autowired
    protected SolrRetriever solrRetriever;
    /** The Reporter.*/
    @Autowired
    protected Reporter reporter;

    @Override
    protected void initSteps() {
        steps.add(new CrowdReturnStep(cumulusWrapper, solrRetriever, reporter));
    }

    @Override
    protected Long getInterval() {
        return conf.getCcsWorkflowInterval();
    }

    @Override
    public String getName() {
        return "Cumulus Crowd Service Backflow Workflow";
    }    
}

package dk.kb.ccs.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.kb.ccs.SendMail;
import dk.kb.ccs.reporting.Reporter;
import dk.kb.ccs.workflow.steps.SendMailStep;

/**
 * Workflow for automatically sending the mail report.
 * @author jolf
 */
@Component
public class MailWorkflow extends Workflow {
    /** The Cumulus retriever.*/
    @Autowired
    protected SendMail mailer;
    /** The Reporter.*/
    @Autowired
    protected Reporter reporter;

    @Override
    protected void initSteps() {
        steps.add(new SendMailStep(reporter, mailer, getInterval()));
    }

    @Override
    protected Long getInterval() {
        return conf.getMailWorkflowInterval();
    }

    @Override
    public String getName() {
        return "Mail Workflow";
    }
}

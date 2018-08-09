package dk.kb.ccs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import dk.kb.ccs.workflow.CCSWorkflow;
import dk.kb.ccs.workflow.MailWorkflow;

/**
 * Created by jolf on 03-07-2018.
 */
@Controller
public class WorkflowController {

    /** The CCS workflow.*/
    @Autowired
    protected CCSWorkflow ccsWorkflow;
    /** The Send mail workflow.*/
    @Autowired
    protected MailWorkflow mailWorkflow;

    /**
     * View for the workflows.
     * @param model The model.
     * @return The path to the workflow.
     */
    @RequestMapping("/workflow")
    public String getWorkflow(Model model) {
        model.addAttribute("ccsWorkflow", ccsWorkflow);
        model.addAttribute("mailWorkflow", mailWorkflow);
        
        return "workflow";
    }
    
    /**
     * The run method for the workflows.
     * Matches the name of the workflow, and starts the given workflow.
     * Will default run the CCS workflow, if the name doesn't match.
     * @param name The name of the workflow to run.
     * @return The redirect back to the workflow view, when the given workflow is started.
     */
    @RequestMapping("/workflow/run")
    public RedirectView runWorkflow(@RequestParam(value="name",required=false) String name) {
        if(name != null && name.equals("mailWorkflow")) {
            mailWorkflow.startManually();
        } else {
            ccsWorkflow.startManually();
        }
        
        try {
            synchronized(this) {
                this.wait(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RedirectView("../workflow",true);
    }
}

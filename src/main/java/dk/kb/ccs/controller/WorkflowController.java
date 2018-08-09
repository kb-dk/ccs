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

    @RequestMapping("/workflow")
    public String getWorkflow(Model model) {
        model.addAttribute("ccsWorkflow", ccsWorkflow);
        model.addAttribute("mailWorkflow", mailWorkflow);
        
        return "workflow";
    }
    
    @RequestMapping("/workflow/run")
    public RedirectView runWorkflow(@RequestParam(value="name",required=false) String name) {
        if(name.equals("mailWorkflow")) {
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

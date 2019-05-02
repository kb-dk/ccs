package dk.kb.ccs.controller;

import dk.kb.ccs.CcsConstants;
import dk.kb.ccs.conf.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Setting the default start path for the view.
 */
@Controller
public class IndexController {

    /** The ginnugagap index path.*/
    protected static final String PATH = "ccs";

    /** The configuration.*/
    @Autowired
    protected Configuration conf;
    /** The constants for this application.*/
    @Autowired
    protected CcsConstants constants;

    /**
     * Index controller, for redirecting towards the workflow site.
     * @return The redirect toward the workflow site.
     */
    @RequestMapping("/")
    public RedirectView getIndex() {
        return new RedirectView(PATH,true);
    }

    /**
     * The default view for Cumulus Crowd Service, with the configuration.
     * @param model The model.
     * @return The path to the jsp page.
     */
    @RequestMapping("/" + PATH)
    public String ccs(Model model) {
        model.addAttribute("solrUrl", conf.getSolrUrl());
        model.addAttribute("solrFilterQuery", conf.getSolrFilterQuery());
        model.addAttribute("solrMaxResults", conf.getSolrMaxResults());
        model.addAttribute("ccsWorkflowInterval", conf.getCcsWorkflowInterval());
        model.addAttribute("mailWorkflowInterval", conf.getMailWorkflowInterval());
        model.addAttribute("mailConf", conf.getMailConf());
        model.addAttribute("cumulusUrl", conf.getCumulusConf().getServerUrl());
        model.addAttribute("cumulusCatalog", conf.getCumulusCatalog());
        model.addAttribute("reportFile", conf.getReportFilePath());
        model.addAttribute("applicationName", constants.getApplicationName());
        model.addAttribute("version", constants.getBuildVersion());

        return PATH;
    }
}

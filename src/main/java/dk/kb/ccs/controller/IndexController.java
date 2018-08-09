package dk.kb.ccs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Setting the default start path for the view.
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public RedirectView getIndex() {
        return new RedirectView("workflow",true);
    }
}

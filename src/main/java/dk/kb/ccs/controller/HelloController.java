package dk.kb.ccs.controller;

import dk.kb.ccs.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by dgj on 27-06-2018.
 */

@Controller
public class HelloController {

    private static Logger log = LoggerFactory.getLogger(Application.class);


    @RequestMapping("/hello")
    public String hello() {
        log.info("Saying Hello");
        return "hello";
    }

}

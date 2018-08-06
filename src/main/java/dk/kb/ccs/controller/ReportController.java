package dk.kb.ccs.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import dk.kb.ccs.reporting.Reporter;
import dk.kb.ccs.utils.CalendarUtils;

/**
 * Created by jolf on 03-07-2018.
 */
@Controller
public class ReportController {

    /** The workflow.*/
    @Autowired
    protected Reporter reporter;

    @RequestMapping(value="/report")
    public String getWorkflow(@RequestParam(value="fromDate",required=false) String fromDate, 
            @RequestParam(value="toDate",required=false) String toDate,
            Model model) {
        Date dateFrom = new Date(fromDate == null ? 0 : Long.parseLong(fromDate));
        Date dateTo = new Date(toDate == null ? System.currentTimeMillis() : Long.parseLong(toDate));
        model.addAttribute("fromDate", CalendarUtils.getDateAsString(dateFrom));
        model.addAttribute("toDate", CalendarUtils.getDateAsString(dateTo));
        try {
            model.addAttribute("count", reporter.getSummary(dateFrom, dateTo));
        } catch (Exception e) {
            model.addAttribute("count", -1L);
        }
        
        return "report";
    }
    
    @RequestMapping(value="/report/copmile")
    public RedirectView sendMail(@RequestParam(value="fromDate",required=false) String fromDate, 
            @RequestParam(value="toDate",required=false) String toDate,
            @RequestParam(value="sendMail", defaultValue="false") String sendMail,
            Model model) {
        Date dateFrom = new Date(fromDate == null ? 0 : Long.parseLong(fromDate));
        Date dateTo = new Date(toDate == null ? System.currentTimeMillis() : Long.parseLong(toDate));
        if(Boolean.valueOf(sendMail)) {
            // TODO send the mail!!!
            return new RedirectView("../report",true);            
        } else {
            return new RedirectView("../report?fromDate=" + dateFrom.getTime() + "&toDate=" + dateTo.getTime(), true);
        }
    }
}

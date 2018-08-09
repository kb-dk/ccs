package dk.kb.ccs.controller;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import dk.kb.ccs.SendMail;
import dk.kb.ccs.conf.Configuration;
import dk.kb.ccs.reporting.Reporter;
import dk.kb.ccs.utils.CalendarUtils;

/**
 * Created by jolf on 03-07-2018.
 */
@Controller
public class ReportController {
    /** The log.*/
    protected static final Logger log = LoggerFactory.getLogger(ReportController.class);

    /** The workflow.*/
    @Autowired
    protected Reporter reporter;
    /** The send mail component.*/
    @Autowired
    protected SendMail mailer;
    /** The configuration.*/
    @Autowired
    protected Configuration conf;

    @RequestMapping(value="/report")
    public String getWorkflow(@RequestParam(value="fromDate",required=false) String fromDate, 
            @RequestParam(value="toDate",required=false) String toDate,
            Model model) {
        Date dateFrom = fromDate == null ? new Date(System.currentTimeMillis() - conf.getCcsWorkflowInterval()) : 
            CalendarUtils.getDateFromString(fromDate);
        Date dateTo = toDate == null ? new Date(System.currentTimeMillis() + CalendarUtils.MILLIS_PER_DAY) : 
            CalendarUtils.getDateFromString(toDate);
        model.addAttribute("fromDateTime", CalendarUtils.getDateTimeAsString(dateFrom));
        model.addAttribute("toDateTime", CalendarUtils.getDateTimeAsString(dateTo));
        model.addAttribute("fromDate", CalendarUtils.getDateAsString(dateFrom));
        model.addAttribute("toDate", CalendarUtils.getDateAsString(dateTo));
        try {
            model.addAttribute("count", reporter.getSummary(dateFrom, dateTo));
        } catch (Exception e) {
            model.addAttribute("count", -1L);
        }
        
        return "report";
    }
    
    @RequestMapping(value="/report/run")
    public RedirectView sendMail(@RequestParam(value="fromDate",required=false) String fromDate, 
            @RequestParam(value="toDate",required=false) String toDate,
            @RequestParam(value="sendMail", defaultValue="false") String sendMail,
            Model model) throws IOException {
        Date dateFrom = fromDate == null ? new Date(System.currentTimeMillis() - CalendarUtils.MILLIS_PER_MONTH) : 
            CalendarUtils.getDateFromString(fromDate);
        Date dateTo = toDate == null ? new Date(System.currentTimeMillis() + CalendarUtils.MILLIS_PER_DAY) : 
            CalendarUtils.getDateFromString(toDate);
        if(Boolean.valueOf(sendMail)) {
            String report = reporter.getReport(dateFrom, dateTo);
            String subject = "CumulusCrowdService report";
            log.info("Sending the mail with subject: '" + subject + "': \n" + report);
            mailer.sendMail(subject, report);
        }
        return new RedirectView("../report?fromDate=" + dateFrom.getTime() + "&toDate=" + dateTo.getTime(), true);
    }
}

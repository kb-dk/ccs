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
import dk.kb.ccs.reporting.MailReport;
import dk.kb.ccs.reporting.Reporter;
import dk.kb.ccs.utils.CalendarUtils;

/**
 * Created by jolf on 03-07-2018.
 */
@Controller
public class ReportController {
    /** The log.*/
    protected final Logger log = LoggerFactory.getLogger(ReportController.class);

    /** The workflow.*/
    @Autowired
    protected Reporter reporter;
    /** The send mail component.*/
    @Autowired
    protected SendMail mailer;
    /** The configuration.*/
    @Autowired
    protected Configuration conf;

    /**
     * The controller method for the report view.
     * Delivers the count of entries return to Cumulus by the backflow workflow within the given interval,
     * and the interval dates.
     * @param fromDate The begining of the interval.
     * @param toDate The end of the interval.
     * @param model The model.
     * @return The report path.
     */
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
    
    /**
     * Run method for sending the mail, or redirecting back to the view with the report interval.
     * @param fromDate The lower date interval limit.
     * @param toDate The upper date interval limit.
     * @param sendMail Whether or not to send the mail, or just redirect with to the report view.
     * @param model The model.
     * @return The redirect back to the report view, with the parameters for the interval.
     * @throws IOException If it fails to send the mail, or retrieve the report for the mail.
     */
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
            MailReport report = reporter.getReport(dateFrom, dateTo);
            String subject = "CumulusCrowdService report";
            log.info("Sending the mail with subject: '" + subject + "': \n" + report);
            mailer.sendReport(subject, report);
        }
        return new RedirectView("../report?fromDate=" + dateFrom.getTime() + "&toDate=" + dateTo.getTime(), true);
    }
}

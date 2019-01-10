package dk.kb.ccs.reporting;

import java.util.Date;
import java.util.List;

import dk.kb.ccs.utils.CalendarUtils;
import dk.kb.ccs.utils.PropertyUtils;

/**
 * Container for the mail for the report.
 * @author jolf
 */
public class MailReport {
    /** Entries for the report mail.*/
    protected final List<BackflowEntry> entries;
    /** The earliest date.*/
    protected final Date earliestDate;
    /** The latest date.*/
    protected final Date latestDate;
    
    /**
     * Constructor.
     * @param earliestDate The earliest date.
     * @param latestDate The latest date.
     * @param entries The entries for the report.
     */
    public MailReport(Date earliestDate, Date latestDate, List<BackflowEntry> entries) {
        this.entries = entries;
        this.earliestDate = new Date(earliestDate.getTime());
        this.latestDate = new Date(latestDate.getTime());
    }
    
    /**
     * Compiles the mail body content, as a summary of the report.
     * @return The mail summary.
     */
    public String getMailBodyContent() {
        StringBuilder res = new StringBuilder();
        res.append("Report from the CumulusCrowdService (v. " + PropertyUtils.getVersion() + ").\n");
        res.append("\nTotal number of entries returned to Cumulus in the interval: '" 
                + BackflowEntry.calculateSummary(entries) + "'");
        res.append("\nStart date for interval: '" + CalendarUtils.getDateAsString(earliestDate) + "'");
        res.append("\nEnd date for interval: '" + CalendarUtils.getDateAsString(latestDate) + "'");
        return res.toString();
    }
    
    /**
     * Creates the text for the attachment.
     * @return The attachment content. 
     */
    public String getAttachment() {
        StringBuilder res = new StringBuilder();
        res.append("StartDate;EndDate;Count;\n");
        for(BackflowEntry entry : entries) {
            res.append(CalendarUtils.getDateTimeAsString(entry.getStartDate()) + ";" 
                    + CalendarUtils.getDateTimeAsString(entry.getEndDate()) + ";" 
                    + entry.getCount() + ";\n");
        }
        
        return res.toString();
    }
}

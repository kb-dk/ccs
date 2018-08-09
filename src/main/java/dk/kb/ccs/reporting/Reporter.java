package dk.kb.ccs.reporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.kb.ccs.conf.Configuration;
import dk.kb.ccs.utils.StreamUtils;

/**
 * Class for dealing with the reports.
 * 
 * It wraps a file which contains the entries for each run of the workflow.
 * Everytime the workflow is run, a result entry is made in the report file (unless not results where found).
 * 
 * The file will have a line for each entry. Each line will consist of the start date, the end date and the count,
 * all separated by hashes.
 * E.g.:
 * startDate##endDate##count
 * 
 * @author jolf
 */
@Component
public class Reporter {
    /** The log.*/
    protected final Logger log = LoggerFactory.getLogger(Reporter.class);
    
    /** The separator character set for the report file.*/
    protected static final String SEPARATOR = "##";
    
    /**
     * The configuration.
     */
    @Autowired
    Configuration conf;
    
    /** The file with the reports.*/
    File reportFile;
    
    /**
     * Initializes this component.
     */
    @PostConstruct
    protected void initialize() {
        reportFile = new File(conf.getReportFilePath());
    }
    
    /**
     * Adds a result to the report, which is started at the start-data and ended at the current timestamp.
     * @param startDate The startDate for the backflow.
     * @param itemCount The number of items handled.
     */
    public void addResult(Date startDate, Long itemCount) {
        if(itemCount < 0) {
            log.info("Will not handle negative results.");
            return;
        }
        
        Date now = new Date();
        
        synchronized(reportFile) {
            try(OutputStream out = new FileOutputStream(reportFile, true)) {
                String line = startDate.getTime() + SEPARATOR + now.getTime() + SEPARATOR + itemCount + "\n";
                out.write(line.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                log.warn("Error occured while reporting the results at '" + now + "'", e);
                throw new IllegalStateException("Error occured while reporting the results at '" + now + "'", e);
            } 
        }
    }
    
    /**
     * Retrieves the summary of the results of the given dates.
     * @param earliestDate The date.
     * @param latestDate The latest date.
     * @return The sum of all the results within the given interval.
     * @throws IOException If it fails to read the report file.
     */
    public Long getSummary(Date earliestDate, Date latestDate) throws IOException {
        List<BackflowEntry> entries = getEntriesForInterval(earliestDate, latestDate);
        return BackflowEntry.calculateSummary(entries);
    }
    
    /**
     * Creates a report for returned entries to Cumulus for a given interval.
     * @param earliestDate The earliest date for the interval.
     * @param latestDate The latest date for the interval.
     * @return The mail report for the given interval.
     * @throws IOException If it fails to read the file for the report.
     */
    public MailReport getReport(Date earliestDate, Date latestDate) throws IOException {
        List<BackflowEntry> entries = getEntriesForInterval(earliestDate, latestDate);
        return new MailReport(earliestDate, latestDate, entries);
    }
    
    /**
     * Retrieves the entries for given interval.
     * These entries are returned as a mapping between their dates and their counts.
     * @param earliestDate The lower date limit of the interval.
     * @param latestDate The upper date limit of the interval.
     * @return The list of backflow entries for the interval.
     * @throws IOException If it fails to read the report file.
     */
    protected List<BackflowEntry> getEntriesForInterval(Date earliestDate, Date latestDate) throws IOException {
        Long earliest = 0L;
        if(earliestDate != null) {
            earliest = earliestDate.getTime();
        }
        long latest = Long.MAX_VALUE;
        if(latestDate != null) {
            latest = latestDate.getTime();
        }
        
        if(earliest > latest) {
            log.warn("Cannot retrieve report, when the earliest date is later than the latest date! "
                    + "Returning an empty list.");
            return Collections.emptyList();
        }
        
        List<String> lines;
        synchronized(reportFile) {
            try(InputStream in = new FileInputStream(reportFile)) {
                lines = StreamUtils.extractInputStreamAsLines(in);
            }
        }
        
        return getMap(lines, earliest, latest);
    }
    
    /**
     * Retrieves the lines as a map between the date and the count.
     * @param lines The lines from the report file.
     * @param earliestStart The earliest value for the start date.
     * @param latestStart The latest value for the start date.
     * @return The mapping between the dates and their count from the lines.
     */
    protected List<BackflowEntry> getMap(List<String> lines, Long earliestStart, Long latestStart) {
        List<BackflowEntry> res = new ArrayList<BackflowEntry>();
        
        for(String line : lines) {
            String[] split = line.split(SEPARATOR);
            if(split.length < 3) {
                log.warn("The report line '" + line + "' does not contain the separator, '" + SEPARATOR 
                        + "'. It will be ignored.");
                continue;
            }
            if(split.length > 3) {
                log.warn("The report line '" + line + "' has elements than required. "
                        + "Only the first three elements are used.");
            }

            try {
                Long startDate = Long.decode(split[0]);
                Long endDate = Long.decode(split[1]);
                Long count = Long.decode(split[2]);
                if(startDate > earliestStart && startDate < latestStart) {
                    res.add(new BackflowEntry(startDate, endDate, count));
                }
            } catch (NumberFormatException e) {
                log.warn("Cannot handle line '" + line + "'. It will be ignored.", e);
            }
        }
        return res;
    }
}

package dk.kb.ccs.reporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.kb.ccs.Configuration;
import dk.kb.ccs.utils.StreamUtils;

/**
 * Class for dealing with the reports.
 * 
 * It wraps a file which contains the entries for each run of the workflow.
 * Everytime the workflow is run, a result entry is made in the report file (unless not results where found).
 * 
 * The file will have a line for each entry. Each line will consist of the date and the count.
 * 
 * @author jolf
 */
@Component
public class Reporter {
    /** The log.*/
    protected static final Logger log = LoggerFactory.getLogger(Reporter.class);
    
    /** The separator character set for the report file.*/
    protected static final String SEPARATOR = "##";
    
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
     * Adds a result to the report, which is at the current timestamp.
     * @param itemCount The number of items handled.
     */
    public void addResult(Long itemCount) {
        if(itemCount == 0) {
            log.info("Will not handle the file for a ");
            return;
        }
        
        Date now = new Date();
        
        synchronized(reportFile) {
            try(OutputStream out = new FileOutputStream(reportFile, true)) {
                String line = now.getTime() + SEPARATOR + itemCount + "\n";
                out.write(line.getBytes());
            } catch (IOException e) {
                log.warn("Error occured while reporting the results at '" + now + "'", e);
                throw new IllegalStateException("Error occured while reporting the results at '" + now + "'", e);
            } 
        }
    }
    
    /**
     * Retrieves the entries for given interval.
     * These entries are returned as a mapping between their dates and their counts.
     * @param earliestDate The lower date limit of the interval.
     * @param latestDate The upper date limit of the interval.
     * @return The map of entries for the interval.
     * @throws IOException If it fails to read the report file.
     */
    public Map<Long, Long> getEntriesForInterval(Date earliestDate, Date latestDate) throws IOException {
        if(earliestDate.getTime() > latestDate.getTime()) {
            log.warn("Cannot retrieve report, when the earliest date is later than the latest date! "
                    + "Returning an empty map.");
            return Collections.emptyMap();
        }
        
        List<String> lines;
        synchronized(reportFile) {
            try(InputStream in = new FileInputStream(reportFile)) {
                lines = StreamUtils.extractInputStreamAsLines(in);
            }
        }
        
        Long earliest = 0L;
        if(earliestDate != null) {
            earliest = earliestDate.getTime();
        }
        long latest = Long.MAX_VALUE;
        if(latestDate != null) {
            latest = latestDate.getTime();
        }
        
        Map<Long, Long> res = new HashMap<Long, Long>();
        Map<Long, Long> map = getMap(lines);
        
        for(Map.Entry<Long, Long> entry : map.entrySet()) {
            if(entry.getKey() > earliest && entry.getKey() < latest) {
                res.put(entry.getKey(), entry.getValue());
            }
        }
        
        return res;
    }
    
    /**
     * Retrieves the lines as a map between the date and the count.
     * @param lines The lines from the report file.
     * @return The mapping between the dates and their count from the lines.
     */
    protected Map<Long, Long> getMap(List<String> lines) {
        Map<Long, Long> res = new HashMap<Long, Long>();
        
        for(String line : lines) {
             String[] split = line.split(SEPARATOR);
             if(split.length < 2) {
                 log.warn("The report line '" + line + "' does not contain the separator, '" + SEPARATOR 
                         + "'. It will be ignored.");
                 continue;
             }
             if(split.length > 2) {
                 log.warn("The report line '" + line + "' has more than one separator. "
                         + "Only the first two elements are used.");
             }
             
             try {
                 Long date = Long.decode(split[0]);
                 Long count = Long.decode(split[1]);
                 res.put(date, count);
             } catch (NumberFormatException e) {
                 log.warn("Cannot handle line '" + line + "'.", e);
             }
        }
        return res;
    }
}

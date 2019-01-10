package dk.kb.ccs.reporting;

import java.util.List;

/**
 * Class for encapsulating the backflow entries.
 * @author jolf
 */
public class BackflowEntry {
    /** The start date for the backflow (in millis since epoch).*/
    protected final Long startDate;
    /** The end date for the backflow (in millis since epoch).*/
    protected final Long endDate;
    /** The number of entries for the given run of the backflow.*/
    protected final Long count;
    
    /**
     * Constructor.
     * @param startDate The start date for the backflow (in millis since epoch).
     * @param endDate The end date for the backflow (in millis since epoch).
     * @param count The number of entries for the given run of the backflow.
     */
    public BackflowEntry(Long startDate, Long endDate, Long count) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.count = count;
    }
    
    /**
     * @return The start date for the backflow (in millis since epoch).
     */
    public Long getStartDate() {
        return startDate;
    }
    
    /**
     * @return The end date for the backflow (in millis since epoch).
     */
    public Long getEndDate() {
        return endDate;
    }
    
    /**
     * @return The number of entries for the given run of the backflow.
     */
    public Long getCount() {
        return count;
    }
    
    /**
     * Method for calculating the summary of the value elements of a map.
     * @param entries The map to have the summary calculated.
     * @return The summary of the values of the map.
     */
    public static long calculateSummary(List<BackflowEntry> entries) {
        Long res = 0L;
        for(BackflowEntry entry : entries) {
            res += entry.getCount();
        }
        return res;        
    }
}

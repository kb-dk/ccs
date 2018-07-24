package dk.kb.ccs.solr;

import java.util.ArrayList;
import java.util.List;

/**
 * Container class for the results of a solr search.
 * Contains the IDs and whether or not more results exists.
 */
public class SolrSearchResult {
    /** The list of IDs.*/
    List<String> ids = new ArrayList<String>();
    /** Whether or not more results exists.*/
    boolean hasMoreResults = false;
    
    /**
     * Constructor.
     */
    public SolrSearchResult() {}
    
    /**
     * Adds an ID.
     * @param id The ID to add.
     */
    public void addId(String id) {
        ids.add(id);
    }
    
    /**
     * @return The list of IDs from the SOLR search result.
     */
    public List<String> getIds() {
        return ids;
    }
    
    /**
     * Sets whether or not more results exists.
     * @param hasMoreResults More results.
     */
    public void setHasMoreIds(boolean hasMoreResults) {
        this.hasMoreResults = hasMoreResults;
    }
    
    /**
     * @return Whether or not more results exists.
     */
    public boolean getHasMoreResults() {
        return hasMoreResults;
    }
}

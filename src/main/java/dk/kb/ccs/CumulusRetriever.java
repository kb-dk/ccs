package dk.kb.ccs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.kb.cumulus.CumulusRecord;
import dk.kb.cumulus.CumulusServer;

/**
 * Class for accessing Cumulus and retrieving the CumulusRecords.
 */
@Component
public class CumulusRetriever {
    
    /** The Cumulus server.*/
    protected CumulusServer server;
    
    /** The configuration. Auto-wired.*/
    @Autowired
    protected Configuration conf;
    
    /**
     * Initializes this component.
     */
    @PostConstruct
    protected void initialize() {
        setCumulusServer(new CumulusServer(conf.getCumulusConf()));
    }
    
    /**
     * Sets the server. 
     * Made as separate function to make testing possible.
     * @param server The Cumulus server.
     */
    protected void setCumulusServer(CumulusServer server) {
        this.server = server;        
    }
    
    /**
     * Method for retrieving the Cumulus server.
     * @return
     */
    public CumulusServer getCumulusServer() {
        return server;
    }
    
    /**
     * Finds the CumulusRecord with the given name in the given catalog.
     * @param catalogName The name of the catalog.
     * @param filename The name of the file.
     * @return The record with the given name from the given catalog.
     */
    public CumulusRecord findRecord(String catalogName, String filename) {
        CumulusRecord record = server.findCumulusRecordByName(catalogName, filename);
        if(record == null) {
            throw new IllegalStateException("Cannot find the file '" + filename + "' from the catalog '"
                    + catalogName + "'");
        }
        return record;
    }
} 

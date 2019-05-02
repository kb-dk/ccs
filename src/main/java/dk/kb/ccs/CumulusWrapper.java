package dk.kb.ccs;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.kb.ccs.conf.Configuration;
import dk.kb.ccs.solr.CcsRecord;
import dk.kb.cumulus.CumulusRecord;
import dk.kb.cumulus.CumulusServer;

/**
 * Class for accessing Cumulus and retrieving the CumulusRecords.
 */
@Component
public class CumulusWrapper {
    /** The log.*/
    protected final Logger log = LoggerFactory.getLogger(CumulusWrapper.class);
    
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
     * Close the Cumulus client.
     */
    @PreDestroy
    protected void tearDown() {
        try {
            server.close();
        } catch (Exception e) {
            log.error("Issue while closing the Cumulus client.", e);
        }
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
     * @return The cumulus server.
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
    
    /**
     * Updates a Cumulus record with the crowd data from Solr.
     * @param ccsRecord The CumulusCrowdService record to use for finding and updating a Cumulus record.
     */
    public void ccsUpdate(CcsRecord ccsRecord) {
        CumulusRecord record = findRecord(ccsRecord.getCatalogName(), ccsRecord.getRecordName());

        boolean updated = false;
        updated = setFieldValueInRecord(record, "Crowd_Titel", ccsRecord.getTitel()) || updated;
        updated = setFieldValueInRecord(record, "Crowd_Person", ccsRecord.getPerson()) || updated;
        updated = setFieldValueInRecord(record, "Crowd_Bygningsnavn", ccsRecord.getBygningsnavn()) || updated;
        updated = setFieldValueInRecord(record, "Crowd_Sted", ccsRecord.getSted()) || updated;
        updated = setFieldValueInRecord(record, "Crowd_Vejnavn", ccsRecord.getVejnavn()) || updated;
        updated = setFieldValueInRecord(record, "Crowd_Husnummer", ccsRecord.getHusnummer()) || updated;
        updated = setFieldValueInRecord(record, "Crowd_Lokalitet", ccsRecord.getLokalitet()) || updated;
        updated = setFieldValueInRecord(record, "Crowd_Postnummer", ccsRecord.getPostnummer()) || updated;
        updated = setFieldValueInRecord(record, "Crowd_By", ccsRecord.getBy()) || updated;
        updated = setFieldValueInRecord(record, "Crowd_Sogn", ccsRecord.getSogn()) || updated;
        updated = setFieldValueInRecord(record, "Crowd_Matrikelnummer", ccsRecord.getMatrikelnummer())
                || updated;
        updated = setFieldValueInRecord(record, "Crowd_Note", ccsRecord.getNote()) || updated;
        updated = setFieldValueInRecord(record, "Crowd_Kommentar", ccsRecord.getKommentar()) || updated;
        updated = setFieldValueInRecord(record, "Crowd_Emneord", ccsRecord.getEmneord()) || updated;
        updated = setFieldValueInRecord(record, "Crowd_Georeference", ccsRecord.getGeoreference()) || updated;

        if(updated) {
            log.info("Updated record: " + ccsRecord.getRecordName() + " " + record.toString());
        } else {
            log.info("No updating for record: " + ccsRecord.getRecordName());
        }
    }
    
    /**
     * Support method for avoid trying to update with nulls.
     * @param record The record to update.
     * @param fieldName The field to update.
     * @param value The value to update with.
     * @return Whether or not it is updated.
     */
    protected boolean setFieldValueInRecord(CumulusRecord record, String fieldName, String value) {
        if(value == null) {
            return false;
        }
        record.setStringValueInField(fieldName, value);
        return true;
    }
}

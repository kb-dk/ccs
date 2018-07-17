package dk.kb.ccs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.kb.ccs.solr.CcsRecord;
import dk.kb.cumulus.CumulusRecord;
import dk.kb.cumulus.CumulusServer;

/**
 * Class for accessing Cumulus and retrieving the CumulusRecords.
 */
@Component
public class CumulusWrapper {
    
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
    
    /**
     * Updates a Cumulus record with the crowd data from Solr.
     * @param ccsRecord The CumulusCrowdService record to use for finding and updating a Cumlus record.
     */
    public void ccsUpdate(CcsRecord ccsRecord) {
       CumulusRecord record = findRecord(ccsRecord.getCatalogName(), ccsRecord.getRecordName());
       
       setFieldValueInRecord(record, "Crowd_Titel", ccsRecord.getTitel());
       setFieldValueInRecord(record, "Crowd_Person", ccsRecord.getPerson());
       setFieldValueInRecord(record, "Crowd_Bygningsnavn", ccsRecord.getBygningsnavn());
       setFieldValueInRecord(record, "Crowd_Sted", ccsRecord.getSted());
       setFieldValueInRecord(record, "Crowd_Vejnavn", ccsRecord.getVejnavn());
       setFieldValueInRecord(record, "Crowd_Husnummer", ccsRecord.getHusnummer());
       setFieldValueInRecord(record, "Crowd_Lokalitet", ccsRecord.getLokalitet());
       setFieldValueInRecord(record, "Crowd_Postnummer", ccsRecord.getPostnummer());
       setFieldValueInRecord(record, "Crowd_By", ccsRecord.getBy());
       setFieldValueInRecord(record, "Crowd_Sogn", ccsRecord.getSogn());
       setFieldValueInRecord(record, "Crowd_Matrikelnummer", ccsRecord.getMatrikelnummer());
       setFieldValueInRecord(record, "Crowd_Note", ccsRecord.getNote());
       setFieldValueInRecord(record, "Crowd_Kommentar", ccsRecord.getKommentar());
       setFieldValueInRecord(record, "Crowd_Emneord", ccsRecord.getEmneord());
       setFieldValueInRecord(record, "Crowd_Georeference", ccsRecord.getGeoreference());
    }
    
    /**
     * Support method for avoid trying to update with nulls.
     * @param record The record to update.
     * @param fieldName The field to update.
     * @param value The value to update with.
     */
    protected void setFieldValueInRecord(CumulusRecord record, String fieldName, String value) {
        if(value == null) {
            return;
        }
        record.setStringValueInField(fieldName, value);
    }
}

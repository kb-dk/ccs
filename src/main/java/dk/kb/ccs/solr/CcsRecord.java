package dk.kb.ccs.solr;

import java.util.List;

import org.apache.solr.common.SolrDocument;
import org.json.JSONArray;
import org.json.JSONObject;

import dk.kb.ccs.utils.StringUtils;
import dk.kb.cumulus.utils.ArgumentCheck;

/**
 * The record for the CumulusCrowdService.
 * It envelopes the metadata fields from the Solr, to make the ready for being imported into Cumulus.
 * 
 * @author jolf
 */
public class CcsRecord {
    /** The JSON field name for the record name.*/
    public static final String JSON_FIELD_FOR_RECORD_NAME = "local_id_ssi";
    /** The JSON field name for the title.*/
    public static final String JSON_FIELD_FOR_TITEL = "title_tdsim";
    /** The JSON field name for the person.*/
    public static final String JSON_FIELD_FOR_PERSON = "cobject_person_ssim";
    /** The JSON field name for the bygningsnavn.*/
    public static final String JSON_FIELD_FOR_BYGNINGSNAVN = "area_building_tsim";
    /** The JSON field name for the sted.*/
    public static final String JSON_FIELD_FOR_STED = "cobject_location_ssim";
    /** The JSON field name for the vejnavn.*/
    public static final String JSON_FIELD_FOR_VEJNAVN = "citySection_street_tsim";
    /** The JSON field name for the husnummer.*/
    public static final String JSON_FIELD_FOR_HUSNUMMER = "citySection_housenumber_tsim";
    /** The JSON field name for the lokalitet.*/
    public static final String JSON_FIELD_FOR_LOKALITET = "cobject_building_ssim";
    /** The JSON field name for the postnummer.*/
    public static final String JSON_FIELD_FOR_POSTNUMMER = "citySection_zipcode_tsim";
    /** The JSON field name for the by.*/
    public static final String JSON_FIELD_FOR_BY = "area_area_tsim";
    /** The JSON field name for the sogn.*/
    public static final String JSON_FIELD_FOR_SOGN = "area_parish_tsim";
    /** The JSON field name for the matrikelnummer.*/
    public static final String JSON_FIELD_FOR_MATRIKELNUMMER = "area_cadastre_tsim";
    /** The JSON field name for the note.*/
    public static final String JSON_FIELD_FOR_NOTE = "description_tsim";
    /** The JSON field name for the kommentar.*/
    public static final String JSON_FIELD_FOR_KOMMENTAR = "cobject_annotation_comments_tds";
    /** The JSON field name for the emneord.*/
    public static final String JSON_FIELD_FOR_EMNEORD = "subject_tdsim";
    /** The JSON field name for the georeference.*/
    public static final String JSON_FIELD_FOR_GEOREFERENCE = "dcterms_spatial";
    /** The JSON field name for the cumulus catalog.*/
    public static final String JSON_FIELD_FOR_CUMULUS_CATALOG = "cumulus_catalog_ss";
    
    /** The array separator.*/
    public static final String ARRAY_STRING_SEPARATOR = ",";
    
    /** The default Cumulus catalog for the records.*/
    public static final String DEFAULT_CUMULUS_CATALOG = "Luftfoto OM";
    
    /** The name of the record.*/
    protected final String recordName;
    /** The name of the catalog.*/
    protected String catalogName;

    /** The titel, imported as crowd_titel.*/
    protected String titel;
    /** The person, imported as crowd_person.*/
    protected String person;
    /** The bygningsnavn, imported as crowd_bygningsnavn.*/
    protected String bygningsnavn;
    /** The sted, imported as crowd_sted.*/
    protected String sted;
    /** The vejnavn, imported as crowd_vejnavn.*/
    protected String vejnavn;
    /** The husnummer, imported as crowd_husnummer.*/
    protected String husnummer;
    /** The lokalitet, imported as crowd_lokalitet.*/
    protected String lokalitet;
    /** The postnummer, imported as crowd_postnummer.*/
    protected String postnummer;
    /** The by, imported as crowd_by.*/
    protected String by;
    /** The sogn, imported as crowd_sogn.*/
    protected String sogn;
    /** The materikelnummer, imported as crowd_matrikelnummer.*/
    protected String matrikelnummer;
    /** The note, imported as crowd_note.*/
    protected String note;
    /** The kommentar, imported as crowd_kommentar.*/
    protected String kommentar;
    /** The emneord, imported as crowd_emneord.*/
    protected String emneord;
    /** The georeference, imported as crowd_georeference.*/
    protected String georeference;

    /**
     * Constructor. For JSON results.
     * @param solrData The JSON solr data object.
     * @param catalogName The name of the catalog
     */
    public CcsRecord(JSONObject solrData) {
        ArgumentCheck.checkTrue(solrData.has(JSON_FIELD_FOR_RECORD_NAME), 
                "JSONObject solrData must contain the field '" + JSON_FIELD_FOR_RECORD_NAME + "'");
        
        this.recordName = solrData.getString(JSON_FIELD_FOR_RECORD_NAME);
        this.catalogName = getJSONFieldValue(solrData, JSON_FIELD_FOR_CUMULUS_CATALOG);
        if(catalogName == null) {
            this.catalogName = DEFAULT_CUMULUS_CATALOG;
        }
        
        this.titel = getJSONFieldValue(solrData, JSON_FIELD_FOR_TITEL);
        this.person = getJSONFieldValue(solrData, JSON_FIELD_FOR_PERSON);
        this.bygningsnavn = getJSONFieldValue(solrData, JSON_FIELD_FOR_BYGNINGSNAVN);
        this.sted = getJSONFieldValue(solrData, JSON_FIELD_FOR_STED);
        this.vejnavn = getJSONFieldValue(solrData, JSON_FIELD_FOR_VEJNAVN);
        this.husnummer = getJSONFieldValue(solrData, JSON_FIELD_FOR_HUSNUMMER);
        this.lokalitet = getJSONFieldValue(solrData, JSON_FIELD_FOR_LOKALITET);
        this.postnummer = getJSONFieldValue(solrData, JSON_FIELD_FOR_POSTNUMMER);
        this.by = getJSONFieldValue(solrData, JSON_FIELD_FOR_BY);
        this.sogn = getJSONFieldValue(solrData, JSON_FIELD_FOR_SOGN);
        this.matrikelnummer = getJSONFieldValue(solrData, JSON_FIELD_FOR_MATRIKELNUMMER);
        this.note = getJSONFieldValue(solrData, JSON_FIELD_FOR_NOTE);
        this.kommentar = getJSONFieldValue(solrData, JSON_FIELD_FOR_KOMMENTAR);
        this.emneord = getJSONFieldValue(solrData, JSON_FIELD_FOR_EMNEORD);
        this.georeference = getJSONFieldValue(solrData, JSON_FIELD_FOR_GEOREFERENCE);
    }
    
    /**
     * Constructor. For Solr results.
     * @param solrData The Solrj data object.
     * @param catalogName The name of the catalog
     */
    public CcsRecord(SolrDocument solrData) {
        ArgumentCheck.checkNotNull(solrData, "SolrDocument solrData");
        
        this.recordName = (String) solrData.getFieldValue(JSON_FIELD_FOR_RECORD_NAME);
        this.catalogName = getSolrFieldValue(solrData, JSON_FIELD_FOR_CUMULUS_CATALOG);
        if(catalogName == null) {
            this.catalogName = DEFAULT_CUMULUS_CATALOG;
        }

        this.titel = getSolrFieldValue(solrData, JSON_FIELD_FOR_TITEL);
        this.person = getSolrFieldValue(solrData, JSON_FIELD_FOR_PERSON);
        this.bygningsnavn = getSolrFieldValue(solrData, JSON_FIELD_FOR_BYGNINGSNAVN);
        this.sted = getSolrFieldValue(solrData, JSON_FIELD_FOR_STED);
        this.vejnavn = getSolrFieldValue(solrData, JSON_FIELD_FOR_VEJNAVN);
        this.husnummer = getSolrFieldValue(solrData, JSON_FIELD_FOR_HUSNUMMER);
        this.lokalitet = getSolrFieldValue(solrData, JSON_FIELD_FOR_LOKALITET);
        this.postnummer = getSolrFieldValue(solrData, JSON_FIELD_FOR_POSTNUMMER);
        this.by = getSolrFieldValue(solrData, JSON_FIELD_FOR_BY);
        this.sogn = getSolrFieldValue(solrData, JSON_FIELD_FOR_SOGN);
        this.matrikelnummer = getSolrFieldValue(solrData, JSON_FIELD_FOR_MATRIKELNUMMER);
        this.note = getSolrFieldValue(solrData, JSON_FIELD_FOR_NOTE);
        this.kommentar = getSolrFieldValue(solrData, JSON_FIELD_FOR_KOMMENTAR);
        this.emneord = getSolrFieldValue(solrData, JSON_FIELD_FOR_EMNEORD);
        this.georeference = getSolrFieldValue(solrData, JSON_FIELD_FOR_GEOREFERENCE);
    }

    /**
     * Extracts String value of a given JSON field. Returns null, if the field does not exist.
     * @param solrData The JSON object to extract the string value from.
     * @param path The path to the JSON element.
     * @return The String value of the JSON element, or null if the element does not exist.
     */
    protected String getJSONFieldValue(JSONObject solrData, String path) {
        if(solrData.has(path)) {
            Object o = solrData.get(path);
            if(o instanceof JSONArray) {
                JSONArray array = (JSONArray) o;
                if(array.length() == 0) {
                    return null;
                } else if(array.length() == 1) {
                    return array.getString(0);
                }
                
                return StringUtils.extractJSONArray(array, ARRAY_STRING_SEPARATOR);
            } else {
                return (String) o;
            }
        } else {
            return null;
        }
    }

    /**
     * Extracts String value of a given JSON field. Returns null, if the field does not exist.
     * @param solrData The JSON object to extract the string value from.
     * @param path The path to the JSON element.
     * @return The String value of the JSON element, or null if the element does not exist.
     */
    protected String getSolrFieldValue(SolrDocument solrData, String path) {
        if(solrData.containsKey(path)) {
            Object o = solrData.get(path);
            if(o instanceof List) {
                List<String> array = (List<String>) o;
                if(array.size() == 0) {
                    return null;
                } else if(array.size() == 1) {
                    return array.get(0);
                }
                
                return StringUtils.extractSolrArray(array, ARRAY_STRING_SEPARATOR);
            }
            return o.toString();
        } else {
            return null;
        }
    }
    
    /** @return The name of the record.*/
    public String getRecordName() {
        return recordName;
    }
    /** @return The name of the catalog.*/
    public String getCatalogName() {
        return catalogName;
    }

    /** @return The titel, imported as crowd_titel.*/
    public String getTitel() {
        return titel;
    }
    /** @return The person, imported as crowd_person.*/
    public String getPerson() {
        return person;
    }
    /** @return The bygningsnavn, imported as crowd_bygningsnavn.*/
    public String getBygningsnavn() {
        return bygningsnavn;
    }
    /** @return The sted, imported as crowd_sted.*/
    public String getSted() {
        return sted;
    }
    /** @return The vejnavn, imported as crowd_vejnavn.*/
    public String getVejnavn() {
        return vejnavn;
    }
    /** @return The husnummer, imported as crowd_husnummer.*/
    public String getHusnummer() {
        return husnummer;
    }
    /** @return The lokalitet, imported as crowd_lokalitet.*/
    public String getLokalitet() {
        return lokalitet;
    }
    /** @return The postnummer, imported as crowd_postnummer.*/
    public String getPostnummer() {
        return postnummer;
    }
    /** @return The by, imported as crowd_by.*/
    public String getBy() {
        return by;
    }
    /** @return The sogn, imported as crowd_sogn.*/
    public String getSogn() {
        return sogn;
    }
    /** @return The matrikelnummer, imported as crowd_materikelnummer.*/
    public String getMatrikelnummer() {
        return matrikelnummer;
    }
    /** @return The note, imported as crowd_note.*/
    public String getNote() {
        return note;
    }
    /** @return The kommentar, imported as crowd_kommentar.*/
    public String getKommentar() {
        return kommentar;
    }
    /** @return The emneord, imported as crowd_emneord.*/
    public String getEmneord() {
        return emneord;
    }
    /** @return The georeference, imported as crowd_georeference.*/
    public String getGeoreference() {
        return georeference;
    }
}

package dk.kb.ccs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import dk.kb.cumulus.config.CumulusConfiguration;
import dk.kb.cumulus.utils.ArgumentCheck;


/**
 * The configuration for the CCS.
 * @author jolf
 * 
 * The configuration file must be a YAML in the following format:
 * CCS:
 *   cumulus:
 *     server: $ URL for the Cumulus server
 *     username: $ Cumulus user name
 *     password: $ Cumulus user password
 *     catalog: $ List of Cumulus Catalogs
 *       - cat1
 *       - cat2
 *       - ...
 *   workflow_interval: $ interval for how often to run the workflows
 *   solr_url: $ The base URL for the SOLR search
 *   solr_filter_query: $ The filter query for the SOLR search
 *   
 *   MORE TO FOLLOW!!!
 */
@Component
public class Configuration {
    /** The log.*/
    protected static final Logger log = LoggerFactory.getLogger(Configuration.class);

    /** The root element of the YAML configuration.*/
    protected static final String CONF_ROOT = "ccs";
    
    /** Cumulus node-element.*/
    protected static final String CONF_CUMULUS = "cumulus";
    /** The cumulus server url leaf-element.*/
    protected static final String CONF_CUMULUS_SERVER = "server";
    /** The cumulus server username leaf-element.*/
    protected static final String CONF_CUMULUS_USERNAME = "username";
    /** The cumulus server password leaf-element.*/
    protected static final String CONF_CUMULUS_PASSWORD = "password";
    /** The cumulus catalogs array leaf-element.*/
    protected static final String CONF_CUMULUS_CATALOG = "catalog";

    /** The interval for how often the workflow should run (time in millis).*/
    protected static final String CONF_WORKFLOW_INTERVAL = "workflow_interval";
    /** The base URL for the SOLR.*/
    protected static final String CONF_SOLR_URL = "solr_url";
    /** The filter query for the SOLR searches.*/
    protected static final String CONF_SOLR_FILTER_QUERY = "solr_filter_query";
    
    /** Whether Cumulus should have write access. */
    protected static final boolean CUMULUS_WRITE_ACCESS = true;

    /** The configuration for Cumulus.*/
    protected final CumulusConfiguration cumulusConf;
    /** The interval for running the workflow.*/
    protected final Long workflowInterval;
    /** The URL for the SOLR.*/
    protected final String solrUrl;
    /** The filter query for the SOLR search.*/
    protected final String solrFilterQuery;

    /** 
     * Constructor.
     * @param path The path to the YAML file.
     * @throws IOException If it cannot load the configuration from the YAML file.
     */
    @Autowired
    public Configuration(@Value("#{ @environment['CCS_CONF'] ?: 'ccs.yml'}") String path) throws IOException {
        File confFile = new File(path);
        
        if(!confFile.isFile()) {
            throw new IllegalArgumentException("No configuration file at: " + confFile.getAbsolutePath());
        }
        
        try (InputStream in = new FileInputStream(confFile)) {
            Object o = new Yaml().load(in);
            if(!(o instanceof LinkedHashMap)) {
                throw new IllegalArgumentException("The file '" + confFile + "' does not contain a valid AIM configuration.");
            }
            LinkedHashMap<String, Object> rootMap = (LinkedHashMap<String, Object>) o;
            ArgumentCheck.checkTrue(rootMap.containsKey(CONF_ROOT), 
                    "Configuration must contain the '" + CONF_ROOT + "' element.");
            
            LinkedHashMap<String, Object> confMap = (LinkedHashMap<String, Object>) rootMap.get(CONF_ROOT);
            
            ArgumentCheck.checkTrue(confMap.containsKey(CONF_WORKFLOW_INTERVAL), 
                    "Configuration must contain the '" + CONF_WORKFLOW_INTERVAL + "' element.");
            ArgumentCheck.checkTrue(confMap.containsKey(CONF_SOLR_URL), 
                    "Configuration must contain the '" + CONF_SOLR_URL + "' element.");
            ArgumentCheck.checkTrue(confMap.containsKey(CONF_SOLR_FILTER_QUERY), 
                    "Configuration must contain the '" + CONF_SOLR_FILTER_QUERY + "' element.");
            ArgumentCheck.checkTrue(confMap.containsKey(CONF_CUMULUS), 
                    "Configuration must contain the '" + CONF_CUMULUS + "' element.");
            
            this.workflowInterval = Long.valueOf((Integer) confMap.get(CONF_WORKFLOW_INTERVAL));
            this.solrUrl = (String) confMap.get(CONF_SOLR_URL);
            this.solrFilterQuery = (String) confMap.get(CONF_SOLR_FILTER_QUERY);
            this.cumulusConf = loadCumulusConfiguration((Map<String, Object>) confMap.get(CONF_CUMULUS));
        }
    }
    
    /**
     * Method for extracting the Cumulus configuration from the YAML map.
     * @param map The map with the Cumulus configuration elements.
     * @return The Cumulus configuration.
     */
    protected CumulusConfiguration loadCumulusConfiguration(Map<String, Object> map) {
        ArgumentCheck.checkTrue(map.containsKey(CONF_CUMULUS_SERVER), 
                "Missing Cumulus element '" + CONF_CUMULUS_SERVER + "'");
        ArgumentCheck.checkTrue(map.containsKey(CONF_CUMULUS_USERNAME), 
                "Missing Cumulus element '" + CONF_CUMULUS_USERNAME + "'");
        ArgumentCheck.checkTrue(map.containsKey(CONF_CUMULUS_PASSWORD), 
                "Missing Cumulus element '" + CONF_CUMULUS_PASSWORD + "'");
        ArgumentCheck.checkTrue(map.containsKey(CONF_CUMULUS_CATALOG), 
                "Missing Cumulus element '" + CONF_CUMULUS_CATALOG + "'");
        
        return new CumulusConfiguration(CUMULUS_WRITE_ACCESS, (String) map.get(CONF_CUMULUS_SERVER), 
                (String) map.get(CONF_CUMULUS_USERNAME), (String) map.get(CONF_CUMULUS_PASSWORD), 
                (List<String>) map.get(CONF_CUMULUS_CATALOG));
    }
    
    /** @return The configuration for Cumulus.*/
    public CumulusConfiguration getCumulusConf() {
        return cumulusConf;
    }
    
    /** @return The interval for running the workflow.*/
    public Long getWorkflowInterval() {
        return workflowInterval;
    }
    
    /** @return The URL for the SOLR.*/
    public String getSolrUrl() {
        return solrUrl;
    }
    
    /** @return The filter query for the SOLR search.*/
    public String getSolrFilterQuery() {
        return solrFilterQuery;
    }
    
    /**
     * We only allow 1 catalog, so our instantiation of the Cumulus configuration has only one catalog in its 
     * list of catalogs. Thus returning the first and only catalog.
     * @return The Cumulus catalog.
     */
    public String getCumulusCatalog() {
        return cumulusConf.getCatalogs().get(0);
    }
}

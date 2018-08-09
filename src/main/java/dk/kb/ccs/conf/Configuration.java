package dk.kb.ccs.conf;

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

import dk.kb.ccs.utils.LongUtils;
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
 *   mail:
 *     to: $LIST OF RECEIVERS
 *       - receiver1
 *       - receiver2
 *       - ...
 *     from: $FROM
 *   ccs_workflow_interval: $ interval for how often to run the CCS workflows
 *   mail_workflow_interval: $ interval for how often to run the MAIL workflows
 *   solr_url: $ The base URL for the SOLR search
 *   solr_filter_query: $ The filter query for the SOLR search
 *   solr_max_results: $ The maximum number of results of the SOLR searches.
 *   report_file: $ The file with the reports.
 */
@Component
public class Configuration {
    /** The log.*/
    protected final Logger log = LoggerFactory.getLogger(Configuration.class);

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
    
    /** The mail node element of the configuration.*/
    protected static final String CONF_MAIL = "mail";
    /** The mail receivers list (who to send the mail to).*/
    protected static final String CONF_MAIL_TO = "to";
    /** The mail sender (who the mail is from).*/
    protected static final String CONF_MAIL_FROM = "from";

    /** The interval for how often the CCS workflow should run (time in millis).*/
    protected static final String CONF_CCS_WORKFLOW_INTERVAL = "ccs_workflow_interval";
    /** The interval for how often the Mail workflow should run (time in millis).*/
    protected static final String CONF_MAIL_WORKFLOW_INTERVAL = "mail_workflow_interval";
    /** The base URL for the SOLR.*/
    protected static final String CONF_SOLR_URL = "solr_url";
    /** The filter query for the SOLR searches.*/
    protected static final String CONF_SOLR_FILTER_QUERY = "solr_filter_query";
    /** The maximum number of search results of a SOLR search.*/
    protected static final String CONF_SOLR_MAX_RESULTS = "solr_max_results";
    /** The file with the reports.*/
    protected static final String CONF_REPORT_FILE = "report_file";
    
    /** Whether Cumulus should have write access. */
    protected static final boolean CUMULUS_WRITE_ACCESS = true;

    /** The configuration for Cumulus.*/
    protected final CumulusConfiguration cumulusConf;
    /** The configuration for the mails.*/
    protected final MailConfiguration mailConf;
    /** The interval for running the CCS workflow.*/
    protected final Long ccsWorkflowInterval;
    /** The interval for running the Mail workflow.*/
    protected final Long mailWorkflowInterval;
    /** The URL for the SOLR.*/
    protected final String solrUrl;
    /** The filter query for the SOLR search.*/
    protected final String solrFilterQuery;
    /** The maximum number of solr search results.*/
    protected final Integer solrMaxResults;
    /** The path for the report file.*/
    protected final String reportFilePath;

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
                throw new IllegalArgumentException("The file '" + confFile 
                        + "' does not contain a valid CCS configuration.");
            }
            LinkedHashMap<String, Object> rootMap = (LinkedHashMap<String, Object>) o;
            ArgumentCheck.checkTrue(rootMap.containsKey(CONF_ROOT), 
                    "Configuration must contain the '" + CONF_ROOT + "' element.");
            
            LinkedHashMap<String, Object> confMap = (LinkedHashMap<String, Object>) rootMap.get(CONF_ROOT);
            
            ArgumentCheck.checkTrue(confMap.containsKey(CONF_CCS_WORKFLOW_INTERVAL), 
                    "Configuration must contain the '" + CONF_CCS_WORKFLOW_INTERVAL + "' element.");
            ArgumentCheck.checkTrue(confMap.containsKey(CONF_MAIL_WORKFLOW_INTERVAL), 
                    "Configuration must contain the '" + CONF_MAIL_WORKFLOW_INTERVAL + "' element.");
            ArgumentCheck.checkTrue(confMap.containsKey(CONF_SOLR_URL), 
                    "Configuration must contain the '" + CONF_SOLR_URL + "' element.");
            ArgumentCheck.checkTrue(confMap.containsKey(CONF_SOLR_FILTER_QUERY), 
                    "Configuration must contain the '" + CONF_SOLR_FILTER_QUERY + "' element.");
            ArgumentCheck.checkTrue(confMap.containsKey(CONF_SOLR_MAX_RESULTS), 
                    "Configuration must contain the '" + CONF_SOLR_MAX_RESULTS + "' element.");
            ArgumentCheck.checkTrue(confMap.containsKey(CONF_REPORT_FILE), 
                    "Configuration must contain the '" + CONF_REPORT_FILE + "' element.");
            ArgumentCheck.checkTrue(confMap.containsKey(CONF_CUMULUS), 
                    "Configuration must contain the '" + CONF_CUMULUS + "' element.");
            ArgumentCheck.checkTrue(confMap.containsKey(CONF_MAIL), 
                    "Configuration must contain the '" + CONF_MAIL + "' element.");
            
            this.ccsWorkflowInterval = LongUtils.getLong(confMap.get(CONF_CCS_WORKFLOW_INTERVAL));
            this.mailWorkflowInterval = LongUtils.getLong(confMap.get(CONF_MAIL_WORKFLOW_INTERVAL));
            this.solrMaxResults = (Integer) confMap.get(CONF_SOLR_MAX_RESULTS);
            this.solrUrl = (String) confMap.get(CONF_SOLR_URL);
            this.reportFilePath = (String) confMap.get(CONF_REPORT_FILE);
            this.solrFilterQuery = (String) confMap.get(CONF_SOLR_FILTER_QUERY);
            this.cumulusConf = loadCumulusConfiguration((Map<String, Object>) confMap.get(CONF_CUMULUS));
            this.mailConf = new MailConfiguration((Map<String, Object>) confMap.get(CONF_MAIL));
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
    
    /** @return The configuration for the mails.*/
    public MailConfiguration getMailConf() {
        return mailConf;
    }
    
    /** @return The interval for running the workflow.*/
    public Long getCcsWorkflowInterval() {
        return ccsWorkflowInterval;
    }
    
    /** @return The interval for running the Mail workflow.*/
    public Long getMailWorkflowInterval() {
        return mailWorkflowInterval;
    }
    
    /** @return The URL for the SOLR.*/
    public String getSolrUrl() {
        return solrUrl;
    }
    
    /** @return The filter query for the SOLR search.*/
    public String getSolrFilterQuery() {
        return solrFilterQuery;
    }
    
    /** @return The maximum number of solr search results.*/
    public Integer getSolrMaxResults() {
        return solrMaxResults;
    }
    
    /** @return The path for the report file.*/
    public String getReportFilePath() {
        return reportFilePath;
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

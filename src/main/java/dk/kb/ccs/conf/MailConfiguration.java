package dk.kb.ccs.conf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Configuration for the mail.
 * Wraps the 'to' and 'from' elements.
 * @author jolf
 */
public class MailConfiguration {
    /** The list of receivers, which the mail is sent to.*/
    protected final List<String> to;
    /** The sender of the mail.*/
    protected final String from;

    /**
     * Constructor.
     * @param confMap The map of the configuration.
     */
    protected MailConfiguration(Map<String, Object> confMap) {
        to = (List<String>) confMap.get(Configuration.CONF_MAIL_TO);
        from = (String) confMap.get(Configuration.CONF_MAIL_FROM);
    }
    
    /** @return The list of receivers, which the mail is sent to.*/
    public List<String> getTo() {
        return new ArrayList<String>(to);
    }
    
    /** @return The sender of the mail.*/
    public String getFrom() {
        return from;
    }
    
}

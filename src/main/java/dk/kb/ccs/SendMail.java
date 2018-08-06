package dk.kb.ccs;

import java.net.InetAddress;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.kb.ccs.conf.Configuration;

@Component
public class SendMail {
    /** The log.*/
    protected static final Logger log = LoggerFactory.getLogger(Configuration.class);

    /** The receivers of the mails.*/
    private List<String> receivers;
    /** The sender of the mails.*/
    private String sender;
    /** The host of the machine, where the mail is being sent.*/
    private String host;

    /** The configuration. Auto-wired.*/
    @Autowired
    protected Configuration conf;
    
    /**
     * Initializes this component.
     */
    @PostConstruct
    protected void initialize() {
        this.receivers = conf.getMailConf().getTo();
        this.sender = conf.getMailConf().getFrom();
        String myHost = null;
        try {
            myHost = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            log.warn("Issue occured while trying to retrieve the name of the local host.", e);
            myHost = "localhost";
        }
        
        this.host = myHost;
    }

    /**
     * Method for sending a mail.
     * It will be sent to all the receivers in the configuration, and it will be from the sender in the configuration.
     * @param subject The subject of the mail.
     * @param content The content of the mail.
     */
    public void sendmail(String subject, String content) {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            for(String receiver : receivers) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            }
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
        } catch (MessagingException e) {
            log.error("Encountered an error while trying to send a mail with the subject: " + subject, e);
        }
    }
}


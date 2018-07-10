package dk.kb.ccs;

/**
 * Created by nkh on 10-07-2018.
 */

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class SendMail {
    InputStream inputStream;

    public static void main(String [] args) {
        Properties prop = new Properties();
        String propFileName = "application.properties";

        String to = prop.getProperty("mail.to");
        String from = prop.getProperty("mail.from");
        String host = "post.kb.dk";
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("This is the my test subject!");
            message.setText("This is my message");
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}


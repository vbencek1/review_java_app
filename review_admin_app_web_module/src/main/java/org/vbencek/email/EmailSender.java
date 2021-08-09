package org.vbencek.email;

import java.util.Properties;
import java.util.Random;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.vbencek.properties.PropertiesLoader;

/**
 * Class that's used to send email
 * @author Tino
 */
public class EmailSender {

    private String username = "";
    private String password = "";
    private Session session;
 
    
    private void setProperties() {
        PropertiesLoader propLoader=new PropertiesLoader();
        username=propLoader.getProperty("email.username");
        password=propLoader.getProperty("email.password");
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", propLoader.getProperty("mail.smtp.starttls.enable"));
        props.put("mail.smtp.auth", propLoader.getProperty("mail.smtp.auth"));
        props.put("mail.smtp.host", propLoader.getProperty("mail.smtp.host"));
        props.put("mail.smtp.port", propLoader.getProperty("mail.smtp.port"));
        props.put("mail.smtp.ssl.trust", propLoader.getProperty("mail.smtp.ssl.trust"));
        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void sendMessage(String emailTo, String subject, String text) {
        setProperties();
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emailTo));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
            System.out.println("Email Sender: Message sent!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

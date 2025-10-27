package iuh.fit.se.group1.service;

import iuh.fit.se.group1.util.PropertiesReader;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailSenderService {

    private static final Logger log = LoggerFactory.getLogger(EmailSenderService.class);
    private final String username;
    private final String password;
    private final String senderName;
    private final Properties mailProps;

    public EmailSenderService() {
        PropertiesReader reader = PropertiesReader.getInstance();

        this.username = reader.get("daotien.mail.email");
        this.password = reader.get("daotien.mail.password");
        this.senderName = reader.get("daotien.name");

        // Load toàn bộ mail.* properties (để linh hoạt cho các SMTP khác sau này)
        this.mailProps = new Properties();
        for (String key : reader.getAll().stringPropertyNames()) {
            if (key.startsWith("mail.")) {
                mailProps.put(key, reader.get(key));
            }
        }
    }

    private Session getSession() {
        return Session.getInstance(mailProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    /** 📨 Gửi email văn bản thuần */
    public void sendMail(String to, String subject, String content) {
        try {
            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(username, senderName));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
            System.out.println("✅ Email sent to: " + to);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("❌ Failed to send plain email: " + e.getMessage(), e);
        }
    }

    /** Gửi email HTML */
    public void sendHtmlMail(String to, String subject, String htmlContent) {
        try {
            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(username, senderName));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=" + mailProps.getProperty("mail.smtp.charset", "UTF-8"));
            Transport.send(message);
            log.info("Send email to: {}", to);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to send HTML email: " + e.getMessage(), e);
        }
    }
}

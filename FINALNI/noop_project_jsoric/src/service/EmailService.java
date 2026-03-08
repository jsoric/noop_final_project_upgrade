package service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Service responsible for sending emails via SMTP (Mailtrap sandbox configuration).
 */
public class EmailService {

    private static final String SMTP_HOST = "sandbox.smtp.mailtrap.io";
    private static final int SMTP_PORT = 2525;

    private static final String USERNAME = "2e59558b0ab25b";
    private static final String PASSWORD = "7c471c10ffad4b";

    private static final String FROM_EMAIL = "noreply@myapp.test";

    /**
     * Sends a generated password to an employee email address.
     *
     * @param name recipient display name
     * @param toEmail recipient email
     * @param password password to send
     */
    public void sendPassword(String name, String toEmail, String password) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(SMTP_PORT));

        Session session = Session.getInstance(
                props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                }
        );

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject("Your login Password");
            message.setText(
                    "Welcome! " + name + "\n\n" +
                            "Your login Password is: " + password + "\n\n"

            );

            Transport.send(message);
            System.out.println("Email captured in Mailtrap sandbox");

        } catch (MessagingException e) {
            System.err.println("Email NOT sent (Mailtrap limit).");
        }
    }
}
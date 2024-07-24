package dev.andreasgeorgatos.pointofservice.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;


    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendRegistrationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Verification email");
        message.setText("Your token to verify your e-mail address and your account is: " + token.substring(0, 36));

        javaMailSender.send(message);
    }

    public void sendSuccessfulVerificationEmail(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);

        message.setSubject("Successfully verified your e-mail");
        message.setText("This e-mail is for the purpose of notifying you that your account has been verified and you can now use all the features of the API and its apps, based on your authority levels");
        javaMailSender.send(message);
    }
}

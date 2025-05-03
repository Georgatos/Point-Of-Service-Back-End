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
    public EmailService(JavaMailSender javaMailSender)
    {
        this.javaMailSender = javaMailSender;
    }

    public void sendRegistrationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Verification email");
        message.setText("Your token to verify your e-mail address and your account is: " + token.substring(0, 36) + " and it will last for 30 minutes");

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

    public void sendForgotPasswordEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Reset your password");
        // Better approach - use a link
        message.setText("You are getting this e-mail because someone has requested a reset password. "
            + "If you did not request this, please ignore this email. "
            + "Otherwise, please click the following link to reset your password: "
            + "https://yourapp.com/reset-password?token=" + token);
        javaMailSender.send(message);
    }

    public void sendNotificationDeletedCode(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);

        message.setSubject("The verification code has been deleted");
        message.setText("We are here to inform that your verification code has been erased from our system, and thus your account too, please create a new account if you would like to use our services.");
        javaMailSender.send(message);
    }


    public void passwordChanged(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);

        message.setSubject("Your password has been updated.");
        message.setText("We are sending you this e-mail to notify you that your password has been successfully updated.\nIn case you weren't the one who changed your account, please take measures imminently.");
        javaMailSender.send(message);
    }
}
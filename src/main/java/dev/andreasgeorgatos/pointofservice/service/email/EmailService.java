package dev.andreasgeorgatos.pointofservice.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service responsible for sending various application-related emails,
 * such as registration verification, password reset, and notifications.
 */
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.base-url}")
    private String appBaseUrl;

    /**
     * Constructs an {@code EmailService} with the specified {@link JavaMailSender}.
     *
     * @param javaMailSender The mail sender utility for dispatching emails.
     */
    @Autowired
    public EmailService(JavaMailSender javaMailSender)
    {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Sends a registration verification email to the user.
     * The email contains a token that the user needs to verify their account.
     * The token is composed of a UUID and a timestamp.
     *
     * @param to The recipient's email address.
     * @param token The verification token (UUID + timestamp). The email will only include the UUID part (first 36 characters).
     */
    public void sendRegistrationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Verification email");
        message.setText("Your token to verify your e-mail address and your account is: " + token.substring(0, 36) + " and it will last for 30 minutes");

        javaMailSender.send(message);
    }

    /**
     * Sends an email notification upon successful account verification.
     *
     * @param to The recipient's email address.
     */
    public void sendSuccessfulVerificationEmail(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);

        message.setSubject("Successfully verified your e-mail");
        message.setText("This e-mail is for the purpose of notifying you that your account has been verified and you can now use all the features of the API and its apps, based on your authority levels");
        javaMailSender.send(message);
    }

    /**
     * Sends a password reset email to the user.
     * The email contains a link with a reset token to allow the user to set a new password.
     * The link is constructed using the configurable {@code app.base-url}.
     *
     * @param email The recipient's email address.
     * @param token The password reset token.
     */
    public void sendForgotPasswordEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Reset your password");
        String resetLink = appBaseUrl + "/reset-password?token=" + token;
        message.setText("You are getting this e-mail because someone has requested a reset password. "
            + "If you did not request this, please ignore this email. "
            + "Otherwise, please click the following link to reset your password: "
            + resetLink);
        javaMailSender.send(message);
    }

    /**
     * Sends a notification that a user's verification code (and potentially their unverified account)
     * has been removed from the system due to expiry.
     *
     * @param email The recipient's email address.
     */
    public void sendNotificationDeletedCode(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);

        message.setSubject("The verification code has been deleted");
        message.setText("Your pending account verification or password reset request has expired, and the associated temporary code has been removed from our system. If you were trying to verify your account, please attempt registration again. If you were trying to reset your password, please request a new password reset.");
        javaMailSender.send(message);
    }

    /**
     * Sends an email notification when a user's password has been successfully changed.
     *
     * @param email The recipient's email address.
     */
    public void passwordChanged(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);

        message.setSubject("Your password has been updated.");
        message.setText("We are sending you this e-mail to notify you that your password has been successfully updated.\nIn case you weren't the one who changed your account, please take measures imminently.");
        javaMailSender.send(message);
    }
}
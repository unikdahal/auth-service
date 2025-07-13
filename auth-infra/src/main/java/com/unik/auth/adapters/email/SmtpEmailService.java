package com.unik.auth.adapters.email;

import com.unik.auth.domain.entities.BaseUser;
import com.unik.auth.ports.output.NotificationServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmtpEmailService<U extends BaseUser<R>, R> implements NotificationServicePort<U, R> {
    private final JavaMailSender mailSender;

    @Value("${notification.email.from:noreply@example.com}")
    private String fromEmail;

    @Value("${notification.email.welcome.subject:Welcome to our platform!}")
    private String welcomeSubject;

    @Value("${notification.email.welcome.text:Hello {displayName},\n\nWelcome to our platform! Your account has been created successfully.\n\nRegards,\nThe Team}")
    private String welcomeText;

    @Value("${notification.email.password-change.subject:Your password has been changed}")
    private String passwordChangeSubject;

    @Value("${notification.email.password-change.text:Hello {displayName},\n\nYour password has been changed successfully. If you did not make this change, please contact support immediately.\n\nRegards,\nThe Team}")
    private String passwordChangeText;

    @Value("${notification.email.password-reset.subject:Password Reset Request}")
    private String passwordResetSubject;

    @Value("${notification.email.password-reset.text:Hello {displayName},\n\nYou have requested a password reset. Use the following temporary password or link to reset your password:\n\n{temporaryPassword}\n\nIf you did not request this reset, please ignore this email.\n\nRegards,\nThe Team}")
    private String passwordResetText;

    @Value("${notification.email.account-status.subject:Account Status Update}")
    private String accountStatusSubject;

    @Value("${notification.email.account-status.text:Hello {displayName},\n\nYour account status has been updated to: {status}.\n\nIf you have any questions, please contact support.\n\nRegards,\nThe Team}")
    private String accountStatusText;

    @Value("${notification.email.login.subject:New Login Detected}")
    private String loginSubject;

    @Value("${notification.email.login.text:Hello {displayName},\n\nA new login to your account has been detected.\n\nIP Address: {ipAddress}\nBrowser/Device: {userAgent}\n\nIf this was not you, please contact support immediately.\n\nRegards,\nThe Team}")
    private String loginText;

    @Value("${notification.email.security-alert.subject:Security Alert}")
    private String securityAlertSubject;

    @Value("${notification.email.security-alert.text:Hello {displayName},\n\nA security alert has been triggered for your account.\n\nAlert Type: {alertType}\nDetails: {details}\n\nIf you did not perform this action, please contact support immediately.\n\nRegards,\nThe Team}")
    private String securityAlertText;

    @Value("${notification.email.enabled:false}")
    private boolean emailEnabled;

    @Value("${notification.email.signature:Regards,\nThe Team}")
    private String emailSignature;

    public SmtpEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendWelcomeNotification(U user) {
        if (!emailEnabled) {
            log.info("Email notifications disabled. Skipping welcome email to: {}", user.getEmail());
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject(welcomeSubject);

            // Replace template placeholders
            String text = welcomeText
                .replace("{displayName}", user.getDisplayName())
                .replace("{username}", user.getUsername())
                .replace("{email}", user.getEmail());

            message.setText(text);
            mailSender.send(message);
            log.info("Welcome email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send welcome email to: {}", user.getEmail(), e);
        }
    }

    @Override
    public void sendPasswordChangeNotification(U user) {
        if (!emailEnabled) {
            log.info("Email notifications disabled. Skipping password change email to: {}", user.getEmail());
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject(passwordChangeSubject);
            message.setText("Hello " + user.getDisplayName() + ",\n\nYour password has been changed successfully. If you did not make this change, please contact support immediately.\n\nRegards,\nThe Team");
            mailSender.send(message);
            log.info("Password change email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send password change email to: {}", user.getEmail(), e);
        }
    }

    @Override
    public void sendPasswordResetNotification(U user, String temporaryPassword) {
        if (!emailEnabled) {
            log.info("Email notifications disabled. Skipping password reset email to: {}", user.getEmail());
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject(passwordResetSubject);
            message.setText("Hello " + user.getDisplayName() + ",\n\nYou have requested a password reset. Use the following temporary password or link to reset your password:\n\n" + temporaryPassword + "\n\nIf you did not request this reset, please ignore this email.\n\nRegards,\nThe Team");
            mailSender.send(message);
            log.info("Password reset email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", user.getEmail(), e);
        }
    }

    @Override
    public void sendAccountStatusNotification(U user, String status) {
        if (!emailEnabled) {
            log.info("Email notifications disabled. Skipping account status email to: {}", user.getEmail());
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject(accountStatusSubject);
            message.setText("Hello " + user.getDisplayName() + ",\n\nYour account status has been updated to: " + status + ".\n\nIf you have any questions, please contact support.\n\nRegards,\nThe Team");
            mailSender.send(message);
            log.info("Account status email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send account status email to: {}", user.getEmail(), e);
        }
    }

    @Override
    public void sendLoginNotification(U user, String ipAddress, String userAgent) {
        if (!emailEnabled) {
            log.info("Email notifications disabled. Skipping login notification email to: {}", user.getEmail());
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject(loginSubject);
            message.setText("Hello " + user.getDisplayName() + ",\n\nA new login to your account has been detected.\n\nIP Address: " + ipAddress + "\nBrowser/Device: " + userAgent + "\n\nIf this was not you, please contact support immediately.\n\nRegards,\nThe Team");
            mailSender.send(message);
            log.info("Login notification email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send login notification email to: {}", user.getEmail(), e);
        }
    }

    @Override
    public void sendSecurityAlertNotification(U user, String alertType, String details) {
        if (!emailEnabled) {
            log.info("Email notifications disabled. Skipping security alert email to: {}", user.getEmail());
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject(securityAlertSubject + ": " + alertType);
            message.setText("Hello " + user.getDisplayName() + ",\n\nA security alert has been triggered for your account.\n\nAlert Type: " + alertType + "\nDetails: " + details + "\n\nIf you did not perform this action, please contact support immediately.\n\nRegards,\nThe Team");
            mailSender.send(message);
            log.info("Security alert email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send security alert email to: {}", user.getEmail(), e);
        }
    }
}

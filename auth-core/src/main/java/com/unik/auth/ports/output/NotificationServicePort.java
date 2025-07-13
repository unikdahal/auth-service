package com.unik.auth.ports.output;

import com.unik.auth.domain.entities.BaseUser;

/**
 * Generic notification service port for sending various user-related notifications.
 *
 * @param <U> the user type
 * @param <R> the role type
 */
public interface NotificationServicePort<U extends BaseUser<R>, R> {
    /**
     * Sends a welcome notification to the user (e.g., after registration).
     * @param user the user entity
     */
    void sendWelcomeNotification(U user);

    /**
     * Sends a notification to the user when their password is changed.
     * @param user the user entity
     */
    void sendPasswordChangeNotification(U user);

    /**
     * Sends a password reset notification to the user with a temporary password or link.
     * @param user the user entity
     * @param temporaryPassword the temporary password or reset link
     */
    void sendPasswordResetNotification(U user, String temporaryPassword);

    /**
     * Sends a notification to the user about account status changes (e.g., enabled, disabled, locked).
     * @param user the user entity
     * @param status the new account status
     */
    void sendAccountStatusNotification(U user, String status);

    /**
     * Sends a login notification to the user, including IP address and user agent for security.
     * @param user the user entity
     * @param ipAddress the IP address of the login
     * @param userAgent the user agent string
     */
    void sendLoginNotification(U user, String ipAddress, String userAgent);

    /**
     * Sends a security alert notification to the user (e.g., suspicious activity).
     * @param user the user entity
     * @param alertType the type of security alert
     * @param details additional details about the alert
     */
    void sendSecurityAlertNotification(U user, String alertType, String details);
}

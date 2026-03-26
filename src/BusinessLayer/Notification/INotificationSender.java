package BusinessLayer.Notification;

/**
 * Bridge implementor for notification channel delivery.
 */
public interface INotificationSender {
    void send(String destination, String subject, String message);
}
package DataLayer.DataAccess;

import BusinessLayer.Notification.INotificationSender;

public class NoOpNotificationSender implements INotificationSender {

    @Override
    public void send(String destination, String subject, String message) {
        // Intentionally no-op.
    }
}
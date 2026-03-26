package DataLayer.DataAccess;

import BusinessLayer.Notification.INotificationSender;

public class ConsoleEmailNotificationSender implements INotificationSender {

    @Override
    public void send(String destination, String subject, String message) {
        System.out.println("[EMAIL] To=" + destination + " | Subject=" + subject + " | " + message);
    }
}
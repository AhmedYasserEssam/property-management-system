package BusinessLayer.Notification;

import BusinessLayer.Domain.MaintenanceRequest;

/**
 * Refined abstraction base that delegates delivery to a sender implementor.
 */
public abstract class BaseMaintenanceNotifier implements MaintenanceNotifier {
    protected final INotificationSender notificationSender;

    protected BaseMaintenanceNotifier(INotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    protected void send(MaintenanceRequest request, String subjectPrefix) {
        String destination = request.getAssignedTeam();
        String subject = subjectPrefix + " Request " + request.getRequestID();
        String body = "Unit=" + request.getUnitID()
                + ", Priority=" + request.getPriority()
                + ", Status=" + request.getStatus()
                + ", Issue=" + request.getIssueDescription();
        notificationSender.send(destination, subject, body);
    }
}
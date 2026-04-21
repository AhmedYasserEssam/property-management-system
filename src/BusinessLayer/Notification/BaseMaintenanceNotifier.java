package BusinessLayer.Notification;

import BusinessLayer.Domain.MaintenanceRequest;

/**
 * Refined abstraction base that delegates delivery to a sender implementor.
 */
public abstract class BaseMaintenanceNotifier implements MaintenanceNotifier {
    protected final NotificationSenderContext notificationSenderContext;

    protected BaseMaintenanceNotifier(NotificationSenderContext notificationSenderContext) {
        this.notificationSenderContext = notificationSenderContext;
    }

    protected BaseMaintenanceNotifier(INotificationSender notificationSender) {
        this(new NotificationSenderContext(notificationSender));
    }

    protected void send(MaintenanceRequest request, String subjectPrefix) {
        String destination = request.getAssignedTeam();
        String subject = subjectPrefix + " Request " + request.getRequestID();
        String body = "Unit=" + request.getUnitID()
                + ", Priority=" + request.getPriority()
                + ", Status=" + request.getStatus()
                + ", Issue=" + request.getIssueDescription();
        notificationSenderContext.send(destination, subject, body);
    }
}
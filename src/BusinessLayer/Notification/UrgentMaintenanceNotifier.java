package BusinessLayer.Notification;

import BusinessLayer.Domain.MaintenanceRequest;

/**
 * Refined abstraction for urgent maintenance notifications.
 */
public class UrgentMaintenanceNotifier extends BaseMaintenanceNotifier {
    private final INotificationSender preferredSender;

    public UrgentMaintenanceNotifier(NotificationSenderContext notificationSenderContext,
                                     INotificationSender preferredSender) {
        super(notificationSenderContext);
        this.preferredSender = preferredSender;
    }

    public UrgentMaintenanceNotifier(INotificationSender notificationSender) {
        super(notificationSender);
        this.preferredSender = null;
    }

    @Override
    public void notifyAssignedTeam(MaintenanceRequest request) {
        if (preferredSender != null) {
            notificationSenderContext.setStrategy(preferredSender);
        }
        send(request, "URGENT");
    }
}
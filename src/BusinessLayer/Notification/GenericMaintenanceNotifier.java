package BusinessLayer.Notification;

import BusinessLayer.Domain.MaintenanceRequest;

/**
 * Fallback notifier used when no request-specific notifier is registered.
 */
public class GenericMaintenanceNotifier extends BaseMaintenanceNotifier {
    private final INotificationSender preferredSender;

    public GenericMaintenanceNotifier(NotificationSenderContext notificationSenderContext,
                                      INotificationSender preferredSender) {
        super(notificationSenderContext);
        this.preferredSender = preferredSender;
    }

    public GenericMaintenanceNotifier(INotificationSender notificationSender) {
        super(notificationSender);
        this.preferredSender = null;
    }

    @Override
    public void notifyAssignedTeam(MaintenanceRequest request) {
        if (preferredSender != null) {
            notificationSenderContext.setStrategy(preferredSender);
        }
        send(request, "Maintenance");
    }
}
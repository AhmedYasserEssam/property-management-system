package BusinessLayer.Notification;

import BusinessLayer.Domain.MaintenanceRequest;

/**
 * Fallback notifier used when no request-specific notifier is registered.
 */
public class GenericMaintenanceNotifier extends BaseMaintenanceNotifier {

    public GenericMaintenanceNotifier(INotificationSender notificationSender) {
        super(notificationSender);
    }

    @Override
    public void notifyAssignedTeam(MaintenanceRequest request) {
        send(request, "Maintenance");
    }
}
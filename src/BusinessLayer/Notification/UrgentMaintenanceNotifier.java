package BusinessLayer.Notification;

import BusinessLayer.Domain.MaintenanceRequest;

/**
 * Refined abstraction for urgent maintenance notifications.
 */
public class UrgentMaintenanceNotifier extends BaseMaintenanceNotifier {

    public UrgentMaintenanceNotifier(INotificationSender notificationSender) {
        super(notificationSender);
    }

    @Override
    public void notifyAssignedTeam(MaintenanceRequest request) {
        send(request, "URGENT");
    }
}
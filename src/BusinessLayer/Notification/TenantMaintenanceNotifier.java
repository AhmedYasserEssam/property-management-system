package BusinessLayer.Notification;

import BusinessLayer.Domain.MaintenanceRequest;

/**
 * Refined abstraction for tenant-reported maintenance notifications.
 */
public class TenantMaintenanceNotifier extends BaseMaintenanceNotifier {

    public TenantMaintenanceNotifier(INotificationSender notificationSender) {
        super(notificationSender);
    }

    @Override
    public void notifyAssignedTeam(MaintenanceRequest request) {
        send(request, "Tenant Reported");
    }
}
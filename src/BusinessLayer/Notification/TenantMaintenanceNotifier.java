package BusinessLayer.Notification;

import BusinessLayer.Domain.MaintenanceRequest;

/**
 * Refined abstraction for tenant-reported maintenance notifications.
 */
public class TenantMaintenanceNotifier extends BaseMaintenanceNotifier {
    private final INotificationSender preferredSender;

    public TenantMaintenanceNotifier(NotificationSenderContext notificationSenderContext,
                                     INotificationSender preferredSender) {
        super(notificationSenderContext);
        this.preferredSender = preferredSender;
    }

    public TenantMaintenanceNotifier(INotificationSender notificationSender) {
        super(notificationSender);
        this.preferredSender = null;
    }

    @Override
    public void notifyAssignedTeam(MaintenanceRequest request) {
        if (preferredSender != null) {
            notificationSenderContext.setStrategy(preferredSender);
        }
        send(request, "Tenant Reported");
    }
}
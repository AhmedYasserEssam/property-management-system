package BusinessLayer.Notification;

import BusinessLayer.Domain.MaintenanceRequest;

/**
 * Bridge abstraction for maintenance notifications.
 */
public interface MaintenanceNotifier {
    void notifyAssignedTeam(MaintenanceRequest request);
}
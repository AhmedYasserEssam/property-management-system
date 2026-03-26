package BusinessLayer.Controller;

import BusinessLayer.Domain.MaintenanceRequest;

/**
 * Bridge implementor interface for maintenance request use cases.
 */
public interface IMaintenanceController {
    MaintenanceRequest submitRequest(String type, String unitID, String description);

    boolean setStatus(int reqID, String status);
}
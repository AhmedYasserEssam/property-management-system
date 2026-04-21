package PresentationLayer.UI;

import BusinessLayer.Controller.IMaintenanceController;

/**
 * Bridge abstraction for maintenance status updates.
 */
public class StatusUI {
    private final IMaintenanceController maintenanceController;

    public StatusUI(IMaintenanceController maintenanceController) {
        this.maintenanceController = maintenanceController;
    }

    public boolean updateStatus(int reqID, String status) {
        return maintenanceController.setStatus(reqID, status);
    }

}
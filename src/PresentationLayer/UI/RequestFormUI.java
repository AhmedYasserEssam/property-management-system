package PresentationLayer.UI;

import BusinessLayer.Controller.IMaintenanceController;
import BusinessLayer.Domain.MaintenanceRequest;

public class RequestFormUI {
    protected final IMaintenanceController maintenanceController;

    /**
     * Bridge constructor.
     */
    public RequestFormUI(IMaintenanceController maintenanceController) {
        this.maintenanceController = maintenanceController;
    }

    /**
     * 
     */
    public void reportIssue(int unitID, String description) {
        reportIssue("TENANT_REPORTED", unitID, description);
    }

    public MaintenanceRequest reportIssue(String type, int unitID, String description) {
        return maintenanceController.submitRequest(type, String.valueOf(unitID), description);
    }

}
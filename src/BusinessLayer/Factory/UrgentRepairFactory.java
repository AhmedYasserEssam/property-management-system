package BusinessLayer.Factory;

import BusinessLayer.Domain.MaintenanceRequest;
import BusinessLayer.Domain.UrgenMaintenanceRequest;

public class UrgentRepairFactory extends MaintenanceRequestFactory {
    @Override
    public MaintenanceRequest createRequest(String unitID, String description) {
        return new UrgenMaintenanceRequest(unitID, description);
    }
}
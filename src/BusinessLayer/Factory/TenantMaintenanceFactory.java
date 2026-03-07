package BusinessLayer.Factory;

import BusinessLayer.Domain.MaintenanceRequest;
import BusinessLayer.Domain.TenantMaintenanceRequest;

public class TenantMaintenanceFactory extends MaintenanceRequestFactory {
    @Override
    public MaintenanceRequest createRequest(String unitID, String description) {
        return new TenantMaintenanceRequest(unitID, description);
    }
}
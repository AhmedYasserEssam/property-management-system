package BusinessLayer.Factory;

import java.time.LocalDateTime;

import BusinessLayer.Domain.MaintenanceRequest;
import BusinessLayer.Domain.TenantMaintenanceRequest;
import BusinessLayer.Domain.RequestMetaData;

public class TenantMaintenanceFactory extends MaintenanceRequestFactory {
    public TenantMaintenanceFactory(RequestMetaData metadata) {
        super(metadata);
    }

    @Override
    public MaintenanceRequest reconstruct(String requestID, String unitID, String issueDescription,
            LocalDateTime requestDate, String status) {
        return new TenantMaintenanceRequest(requestID, unitID, issueDescription, requestDate, status, metadata);
    }

    @Override
    public MaintenanceRequest createRequest(String unitID, String description) {
        return new TenantMaintenanceRequest(unitID, description, metadata);
    }

}

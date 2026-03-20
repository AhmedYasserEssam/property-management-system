package BusinessLayer.Factory;

import java.time.LocalDateTime;

import BusinessLayer.Domain.MaintenanceRequest;
import BusinessLayer.Domain.UrgentMaintenanceRequest;
import BusinessLayer.Domain.RequestMetaData;

public class UrgentMaintenanceFactory extends MaintenanceRequestFactory {
    public UrgentMaintenanceFactory(RequestMetaData requestType) {
        super(requestType);
    }

    @Override
    public MaintenanceRequest reconstruct(String requestID, String unitID,
            String issueDescription,
            LocalDateTime requestDate, String status) {
        return new UrgentMaintenanceRequest(requestID, unitID, issueDescription, requestDate, status, metadata);
    }

    @Override
    public MaintenanceRequest createRequest(String unitID, String description) {
        return new UrgentMaintenanceRequest(unitID, description, metadata);
    }
}
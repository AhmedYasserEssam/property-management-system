package BusinessLayer.Factory;

import java.time.LocalDateTime;

import BusinessLayer.Domain.MaintenanceRequest;
import BusinessLayer.Domain.UrgentMaintenanceRequest;

public class UrgentMaintenanceFactory extends MaintenanceRequestFactory {
    @Override
    public MaintenanceRequest reconstruct(String requestID, String unitID,
            String issueDescription,
            LocalDateTime requestDate, String status) {
        return new UrgentMaintenanceRequest(requestID, unitID, issueDescription, requestDate, status);
    }

    @Override
    public MaintenanceRequest createRequest(String unitID, String description) {
        return new UrgentMaintenanceRequest(unitID, description);
    }
}
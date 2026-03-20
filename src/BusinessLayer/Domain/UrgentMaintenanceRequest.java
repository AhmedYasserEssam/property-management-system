package BusinessLayer.Domain;

import java.time.LocalDateTime;

public class UrgentMaintenanceRequest extends MaintenanceRequest {

    public UrgentMaintenanceRequest(String unitID, String issueDescription, RequestMetaData metadata) {
        super(unitID, issueDescription, metadata);
    }

    public UrgentMaintenanceRequest(String requestID, String unitID, String issueDescription,
            LocalDateTime requestDate, String status, RequestMetaData metadata) {
        super(requestID, unitID, issueDescription, requestDate, status, metadata);
    }
}
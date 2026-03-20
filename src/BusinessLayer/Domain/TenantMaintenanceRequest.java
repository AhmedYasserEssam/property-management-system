package BusinessLayer.Domain;

import java.time.LocalDateTime;

public class TenantMaintenanceRequest extends MaintenanceRequest {
    public TenantMaintenanceRequest(String unitID, String issueDescription,
            RequestMetaData metadata) {
        super(unitID, issueDescription, metadata);
    }

    public TenantMaintenanceRequest(String requestID, String unitID,
            String issueDescription,
            LocalDateTime requestDate, String status,
            RequestMetaData metadata) {
        super(requestID, unitID, issueDescription, requestDate, status, metadata);
    }
}

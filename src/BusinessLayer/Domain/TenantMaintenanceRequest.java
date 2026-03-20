package BusinessLayer.Domain;

import java.time.LocalDateTime;

public class TenantMaintenanceRequest extends MaintenanceRequest {
    public TenantMaintenanceRequest(String unitID, String issueDescription,
            RequestType requestType) {
        super(unitID, issueDescription, requestType);
    }

    public TenantMaintenanceRequest(String requestID, String unitID,
            String issueDescription,
            LocalDateTime requestDate, String status,
            RequestType requestType) {
        super(requestID, unitID, issueDescription, requestDate, status, requestType);
    }
}

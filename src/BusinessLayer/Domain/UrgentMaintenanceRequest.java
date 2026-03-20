package BusinessLayer.Domain;

import java.time.LocalDateTime;

public class UrgentMaintenanceRequest extends MaintenanceRequest {

    public UrgentMaintenanceRequest(String unitID, String issueDescription, RequestType requestType) {
        super(unitID, issueDescription, requestType);
    }

    public UrgentMaintenanceRequest(String requestID, String unitID, String issueDescription,
            LocalDateTime requestDate, String status, RequestType requestType) {
        super(requestID, unitID, issueDescription, requestDate, status, requestType);
    }
}
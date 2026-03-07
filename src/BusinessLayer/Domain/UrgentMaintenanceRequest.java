package BusinessLayer.Domain;

import java.time.LocalDateTime;

public class UrgentMaintenanceRequest extends MaintenanceRequest {

    public UrgentMaintenanceRequest(String unitID, String issueDescription) {
        super(unitID, issueDescription);
    }

    public UrgentMaintenanceRequest(String requestID, String unitID, String issueDescription,
            LocalDateTime requestDate, String status) {
        super(requestID, unitID, issueDescription, requestDate, status);
    }

    @Override
    public String getPriority() {
        return "CRITICAL";
    }

    @Override
    public String getAssignedTeam() {
        return "Emergency Crew";
    }

    @Override
    public String getInitialStatus() {
        return "DISPATCHED";
    }

    @Override
    public String getRequestType() {
        return "URGENT";
    }

}
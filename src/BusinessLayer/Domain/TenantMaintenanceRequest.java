package BusinessLayer.Domain;

import java.time.LocalDateTime;

public class TenantMaintenanceRequest extends MaintenanceRequest {
    public TenantMaintenanceRequest(String unitID, String issueDescription) {
        super(unitID, issueDescription);
    }

    public TenantMaintenanceRequest(String requestID, String unitID, String issueDescription,
            LocalDateTime requestDate, String status) {
        super(requestID, unitID, issueDescription, requestDate, status);
    }

    @Override
    public String getPriority() {
        return "MEDIUM";
    }

    @Override
    public String getAssignedTeam() {
        return "General Maintenance";
    }

    @Override
    public String getInitialStatus() {
        return "PENDING_REVIEW";
    }

    @Override
    public String getRequestType() {
        return "TENANT_REPORTED";
    }

}

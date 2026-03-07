package BusinessLayer.Domain;

public class TenantMaintenanceRequest extends MaintenanceRequest {
    public TenantMaintenanceRequest(String unitID, String issueDescription) {
        super(unitID, issueDescription);
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

}

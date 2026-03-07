package BusinessLayer.Domain.MaintenanceRequests;

public class TenantMaintenanceRequest extends MaintenanceRequest {
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

package BusinessLayer.Domain.MaintenanceRequests;

public class UrgenMaintenanceRequest extends MaintenanceRequest {
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
}
package BusinessLayer.Domain;

public class UrgenMaintenanceRequest extends MaintenanceRequest {

    public UrgenMaintenanceRequest(String unitID, String issueDescription) {
        super(unitID, issueDescription);
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
}
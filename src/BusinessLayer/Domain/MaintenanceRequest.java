package BusinessLayer.Domain;

import java.time.LocalDateTime;

public abstract class MaintenanceRequest {

    private String requestID;
    private String unitID;
    private String issueDescription;
    private LocalDateTime requestDate;
    private String status;

    public MaintenanceRequest(String unitID, String issueDescription) {
        this.unitID = unitID;
        this.issueDescription = issueDescription;
        this.status = "TO BE DETERMINED";
    }

    public abstract String getPriority();

    public abstract String getAssignedTeam();

    public abstract String getInitialStatus();
}

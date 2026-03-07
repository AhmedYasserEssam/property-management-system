package BusinessLayer.Domain;

import java.time.LocalDateTime;

public abstract class MaintenanceRequest {

    private String requestID;
    private String unitID;
    private String issueDescription;
    private LocalDateTime requestDate;
    private String status;

    public MaintenanceRequest(String requestID, String unitID, String issueDescription, LocalDateTime requestDate,
            String status) {
        this.requestID = requestID;
        this.unitID = unitID;
        this.issueDescription = issueDescription;
        this.requestDate = requestDate;
        this.status = status;
    }

    public MaintenanceRequest(String unitID, String issueDescription) {
        this.unitID = unitID;
        this.issueDescription = issueDescription;
        this.status = "TO BE DETERMINED";
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getUnitID() {
        return unitID;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    public abstract String getPriority();

    public abstract String getAssignedTeam();

    public abstract String getInitialStatus();

    public abstract String getRequestType();
}

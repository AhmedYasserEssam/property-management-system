package BusinessLayer.Domain;

import java.time.LocalDateTime;

class MaintenanceRequest {

    private String requestID;
    private String unitID;
    private String issueDescription;
    private LocalDateTime requestDate;
    private String status;
    private final RequestType requestType;

    public MaintenanceRequest(String requestID, String unitID, String issueDescription, LocalDateTime requestDate,
            String status, RequestType requestType) {
        this.requestID = requestID;
        this.unitID = unitID;
        this.issueDescription = issueDescription;
        this.requestDate = requestDate;
        this.status = status;
        this.requestType = requestType;
    }

    public MaintenanceRequest(String unitID, String issueDescription, RequestType requestType) {
        this.unitID = unitID;
        this.issueDescription = issueDescription;
        this.status = "TO BE DETERMINED";
        this.requestType = requestType;

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

    public String getPriority() {
        return requestType.getPriority();
    }

    public String getAssignedTeam() {
        return requestType.getAssignedTeam();
    }

    public String getInitialStatus() {
        return requestType.getInitialStatus();
    }

    public String getRequestType() {
        return requestType.getRequestType();
    }
}

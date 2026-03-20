package BusinessLayer.Domain;

import java.time.LocalDateTime;

public class MaintenanceRequest {

    private String requestID;
    private String unitID;
    private String issueDescription;
    private LocalDateTime requestDate;
    private String status;
    private final RequestMetaData metadata;

    public MaintenanceRequest(String requestID, String unitID, String issueDescription, LocalDateTime requestDate,
            String status, RequestMetaData metadata) {
        this.requestID = requestID;
        this.unitID = unitID;
        this.issueDescription = issueDescription;
        this.requestDate = requestDate;
        this.status = status;
        this.metadata = metadata;
    }

    public MaintenanceRequest(String unitID, String issueDescription, RequestMetaData metadata) {
        this.unitID = unitID;
        this.issueDescription = issueDescription;
        this.status = "TO BE DETERMINED";
        this.metadata = metadata;

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
        return metadata.getPriority();
    }

    public String getAssignedTeam() {
        return metadata.getAssignedTeam();
    }

    public String getInitialStatus() {
        return metadata.getInitialStatus();
    }
}

package BusinessLayer.Domain;

import java.time.LocalDateTime;

/**
 * Maintenance request domain entity.
 */
public class MaintenanceRequest {
    private int requestID;
    private int unitID;
    private String issueDescription;
    private LocalDateTime requestDate;
    private String status;

    public MaintenanceRequest() {
    }

    public MaintenanceRequest(int requestID, int unitID, String issueDescription, LocalDateTime requestDate, String status) {
        this.requestID = requestID;
        this.unitID = unitID;
        this.issueDescription = issueDescription;
        this.requestDate = requestDate;
        this.status = status;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public int getUnitID() {
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

}
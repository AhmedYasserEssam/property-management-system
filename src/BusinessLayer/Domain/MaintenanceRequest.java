package BusinessLayer.Domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class MaintenanceRequest {

    public static final String STATUS_TO_BE_DETERMINED = "TO BE DETERMINED";
    public static final String STATUS_COMPLETED = "COMPLETED";

    private String requestID;
    private String unitID;
    private String issueDescription;
    private LocalDateTime requestDate;
    private String status;
    private final RequestMetaData metadata;

    public MaintenanceRequest(String requestID, String unitID, String issueDescription, LocalDateTime requestDate,
            String status, RequestMetaData metadata) {
        this.requestID = requestID;
        this.unitID = requireNonBlank(unitID, "unitID");
        this.issueDescription = requireNonBlank(issueDescription, "issueDescription");
        this.requestDate = Objects.requireNonNull(requestDate, "requestDate cannot be null");
        this.status = requireNonBlank(status, "status");
        this.metadata = Objects.requireNonNull(metadata, "metadata cannot be null");
    }

    public MaintenanceRequest(String unitID, String issueDescription, RequestMetaData metadata) {
        this.unitID = requireNonBlank(unitID, "unitID");
        this.issueDescription = requireNonBlank(issueDescription, "issueDescription");
        this.requestDate = LocalDateTime.now();
        this.status = STATUS_TO_BE_DETERMINED;
        this.metadata = Objects.requireNonNull(metadata, "metadata cannot be null");

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
        this.status = requireNonBlank(status, "status");
    }

    public String getPriority() {
        return metadata.getPriority();
    }

    public String getRequestType() {
        return metadata.getRequestType();
    }

    public RequestMetaData getMetadata() {
        return metadata;
    }

    public String getAssignedTeam() {
        return metadata.getAssignedTeam();
    }

    public String getInitialStatus() {
        return metadata.getInitialStatus();
    }

    private String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        return value;
    }
}

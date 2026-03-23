package BusinessLayer.Domain;

import java.util.Objects;

public class RequestMetaData {
    private final String requestType;
    private final String priority;
    private final String assignedTeam;
    private final String initialStatus;

    public RequestMetaData(String requestType, String priority,
            String assignedTeam, String initialStatus) {
        this.requestType = normalize(requestType);
        this.priority = normalize(priority);
        this.assignedTeam = normalize(assignedTeam);
        this.initialStatus = normalize(initialStatus);
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().replace("|", "||");
    }

    public String getPriority() {
        return priority;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getAssignedTeam() {
        return assignedTeam;
    }

    public String getInitialStatus() {
        return initialStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequestMetaData)) {
            return false;
        }
        RequestMetaData that = (RequestMetaData) o;
        return requestType.equals(that.requestType)
            && priority.equals(that.priority)
            && assignedTeam.equals(that.assignedTeam)
            && initialStatus.equals(that.initialStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestType, priority, assignedTeam, initialStatus);
    }

    @Override
    public String toString() {
        return String.format("RequestMetaData[type=%s, priority=%s, team=%s, status=%s]",
            requestType,
            priority,
            assignedTeam,
            initialStatus);
    }
}

package BusinessLayer.Domain;

public class RequestType {
    private final String requestType;
    private final String priority;
    private final String assignedTeam;
    private final String initialStatus;

    public RequestType(String requestType, String priority,
            String assignedTeam, String initialStatus) {
        this.requestType = requestType;
        this.priority = priority;
        this.assignedTeam = assignedTeam;
        this.initialStatus = initialStatus;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getPriority() {
        return priority;
    }

    public String getAssignedTeam() {
        return assignedTeam;
    }

    public String getInitialStatus() {
        return initialStatus;
    }
}

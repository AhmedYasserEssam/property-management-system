package BusinessLayer.Domain;

public class RequestMetaData {
    private final String priority;
    private final String assignedTeam;
    private final String initialStatus;

    public RequestMetaData(String priority,
            String assignedTeam, String initialStatus) {
        this.priority = priority;
        this.assignedTeam = assignedTeam;
        this.initialStatus = initialStatus;
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

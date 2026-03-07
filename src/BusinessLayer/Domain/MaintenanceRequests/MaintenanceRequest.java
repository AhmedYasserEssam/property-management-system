package BusinessLayer.Domain.MaintenanceRequests;

import java.util.Date;

public abstract class MaintenanceRequest {

    private int requestID;
    private String issueDescription;
    private Date requestDate;
    private String status;

    public abstract String getPriority();

    public abstract String getAssignedTeam();

    public abstract String getInitialStatus();
}

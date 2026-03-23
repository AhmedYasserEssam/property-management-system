package BusinessLayer.Domain;

import java.util.Objects;

/**
 * Base decorator that preserves the MaintenanceRequest API while delegating to a wrapped request.
 */
public abstract class MaintenanceRequestDecorator extends MaintenanceRequest {

    private static final RequestMetaData DECORATOR_METADATA = new RequestMetaData(
            "DECORATOR",
            "DECORATOR",
            "DECORATOR",
            "DECORATOR");

    protected final MaintenanceRequest delegate;

    protected MaintenanceRequestDecorator(MaintenanceRequest delegate) {
        // Parent state is intentionally inert; behavior comes exclusively from the wrapped request.
        super("DECORATOR", "DECORATOR", DECORATOR_METADATA);
        this.delegate = Objects.requireNonNull(delegate, "delegate request cannot be null");
    }

    @Override
    public String getRequestID() {
        return delegate.getRequestID();
    }

    @Override
    public void setRequestID(String requestID) {
        delegate.setRequestID(requestID);
    }

    @Override
    public String getUnitID() {
        return delegate.getUnitID();
    }

    @Override
    public String getIssueDescription() {
        return delegate.getIssueDescription();
    }

    @Override
    public java.time.LocalDateTime getRequestDate() {
        return delegate.getRequestDate();
    }

    @Override
    public String getStatus() {
        return delegate.getStatus();
    }

    @Override
    public void updateStatus(String status) {
        delegate.updateStatus(status);
    }

    @Override
    public String getPriority() {
        return delegate.getPriority();
    }

    @Override
    public String getRequestType() {
        return delegate.getRequestType();
    }

    @Override
    public RequestMetaData getMetadata() {
        return delegate.getMetadata();
    }

    @Override
    public String getAssignedTeam() {
        return delegate.getAssignedTeam();
    }

    @Override
    public String getInitialStatus() {
        return delegate.getInitialStatus();
    }

    protected boolean isCompleted() {
        return MaintenanceRequest.STATUS_COMPLETED.equalsIgnoreCase(delegate.getStatus());
    }
}

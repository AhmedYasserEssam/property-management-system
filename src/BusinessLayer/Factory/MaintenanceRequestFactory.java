package BusinessLayer.Factory;

import java.time.LocalDateTime;
import BusinessLayer.Domain.RequestMetaData;
import BusinessLayer.Domain.MaintenanceRequest;

public abstract class MaintenanceRequestFactory {
    protected final RequestMetaData metadata;

    protected MaintenanceRequestFactory(RequestMetaData metadata) {
        this.metadata = metadata;
    }

    public abstract MaintenanceRequest createRequest(String unitID, String description);

    public abstract MaintenanceRequest reconstruct(String requestID, String unitID, String issueDescription,
            LocalDateTime requestDate, String status);

    /**
     * Template method for creating a new request.
     * Invariant algorithm: create -> validate -> set initial status -> return.
     */
    public final MaintenanceRequest submitRequest(String unitID, String description) {
        MaintenanceRequest request = createRequest(unitID, description);
        validateCreatedRequest(request, unitID, description);
        request.updateStatus(request.getInitialStatus());
        return request;
    }

    /**
     * Template method for rebuilding a request from persistent storage.
     */
    public final MaintenanceRequest reconstructRequest(String requestID, String unitID, String issueDescription,
            LocalDateTime requestDate, String status) {
        MaintenanceRequest request = reconstruct(requestID, unitID, issueDescription, requestDate, status);
        validateReconstructedRequest(request);
        return request;
    }

    protected void validateCreatedRequest(MaintenanceRequest request, String unitID, String description) {
        if (request == null) {
            throw new IllegalStateException("createRequest returned null");
        }
    }

    protected void validateReconstructedRequest(MaintenanceRequest request) {
        if (request == null) {
            throw new IllegalStateException("reconstruct returned null");
        }
    }
}

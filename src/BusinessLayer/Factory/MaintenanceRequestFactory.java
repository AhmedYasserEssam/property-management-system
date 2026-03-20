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

    public MaintenanceRequest submitRequest(String unitID, String description) {
        MaintenanceRequest r = createRequest(unitID, description);
        r.updateStatus(r.getInitialStatus());
        return r;
    }
}

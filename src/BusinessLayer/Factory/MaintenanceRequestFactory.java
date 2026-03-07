package BusinessLayer.Factory;

import java.util.Date;

import BusinessLayer.Domain.MaintenanceRequest;

public abstract class MaintenanceRequestFactory {
    public abstract MaintenanceRequest createRequest(String unitID, String description);

    public MaintenanceRequest submitRequest(String unitID, String description) {
        MaintenanceRequest r = createRequest(unitID, description);
        r.updateStatus(r.getInitialStatus());
        return r;
    }
}

package BusinessLayer.Controller;

import BusinessLayer.Domain.MaintenanceRequest;
import BusinessLayer.Factory.MaintenanceRequestFactoryResolver;
import BusinessLayer.Repository.IMaintenanceRepository;

import java.util.Optional;

/**
 * Orchestrates maintenance request use cases.
 */
public class MaintenanceController {
    private final IMaintenanceRepository maintenanceRepository;
    private final MaintenanceRequestFactoryResolver factoryResolver;

    public MaintenanceController(IMaintenanceRepository maintenanceRepository,
                                 MaintenanceRequestFactoryResolver factoryResolver) {
        this.maintenanceRepository = maintenanceRepository;
        this.factoryResolver = factoryResolver;
    }

    public MaintenanceRequest submitRequest(String type, String unitID, String description) {
        MaintenanceRequest request = factoryResolver.resolve(type).submitRequest(unitID, description);
        maintenanceRepository.save(request);
        return request;
    }

    public boolean setStatus(int reqID, String status) {
        Optional<MaintenanceRequest> existing = maintenanceRepository.findByID(reqID);
        if (existing.isEmpty()) {
            return false;
        }

        MaintenanceRequest request = existing.get();
        request.updateStatus(status);
        maintenanceRepository.update(request);
        return true;
    }
}

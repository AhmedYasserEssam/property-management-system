package BusinessLayer.Controller;

import BusinessLayer.Domain.MaintenanceRequest;
import BusinessLayer.Factory.MaintenanceRequestFactoryResolver;
import BusinessLayer.Notification.GenericMaintenanceNotifier;
import BusinessLayer.Notification.MaintenanceNotifierResolver;
import BusinessLayer.Repository.IMaintenanceRepository;
import DataLayer.DataAccess.NoOpNotificationSender;

import java.util.Optional;

/**
 * Orchestrates maintenance request use cases.
 */
public class MaintenanceController implements IMaintenanceController {
    private final IMaintenanceRepository maintenanceRepository;
    private final MaintenanceRequestFactoryResolver factoryResolver;
    private final MaintenanceNotifierResolver notifierResolver;

    public MaintenanceController(IMaintenanceRepository maintenanceRepository,
                                 MaintenanceRequestFactoryResolver factoryResolver) {
        this(maintenanceRepository, factoryResolver,
                new MaintenanceNotifierResolver(new GenericMaintenanceNotifier(new NoOpNotificationSender())));
    }

    public MaintenanceController(IMaintenanceRepository maintenanceRepository,
                                 MaintenanceRequestFactoryResolver factoryResolver,
                                 MaintenanceNotifierResolver notifierResolver) {
        this.maintenanceRepository = maintenanceRepository;
        this.factoryResolver = factoryResolver;
        this.notifierResolver = notifierResolver;
    }

    @Override
    public MaintenanceRequest submitRequest(String type, String unitID, String description) {
        MaintenanceRequest request = factoryResolver.resolve(type).submitRequest(unitID, description);
        maintenanceRepository.save(request);
        notifierResolver.resolve(request.getRequestType()).notifyAssignedTeam(request);
        return request;
    }

    @Override
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

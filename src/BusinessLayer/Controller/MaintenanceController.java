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
    private final IMaintenanceMediator maintenanceMediator;

    public MaintenanceController(IMaintenanceMediator maintenanceMediator) {
        this.maintenanceMediator = maintenanceMediator;
    }

    @Override
    public MaintenanceRequest submitRequest(String type, String unitID, String description) {
        return maintenanceMediator.submitRequest(type, unitID, description);
    }

    @Override
    public boolean setStatus(int reqID, String status) {
        return maintenanceMediator.setStatus(reqID, status);
    }
}

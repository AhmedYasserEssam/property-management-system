package BusinessLayer.Mediator;

import BusinessLayer.Domain.MaintenanceRequest;
import BusinessLayer.Factory.MaintenanceRequestFactoryResolver;
import BusinessLayer.Notification.MaintenanceNotifierResolver;
import BusinessLayer.Repository.IMaintenanceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IMaintenanceMediator {
    MaintenanceRequest submitRequest(String type, String unitID, String description);
    boolean setStatus(int reqID, String status);
    void registerListener(IMaintenanceEventListener listener);
}

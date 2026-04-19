package BusinessLayer.Mediator;

import BusinessLayer.Domain.MaintenanceRequest;
import BusinessLayer.Factory.MaintenanceRequestFactoryResolver;
import BusinessLayer.Notification.MaintenanceNotifierResolver;
import BusinessLayer.Repository.IMaintenanceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MaintenanceMediator implements IMaintenanceMediator {
    private final IMaintenanceRepository maintenanceRepository;
    private final MaintenanceRequestFactoryResolver factoryResolver;
    private final MaintenanceNotifierResolver notifierResolver;
    private final List<<IMIMaintenanceEventListener> listeners = new ArrayList<>();

    public MaintenanceMediator(IMaintenanceRepository maintenanceRepository,
                               MaintenanceRequestFactoryResolver factoryResolver,
                               MaintenanceNotifierResolver notifierResolver) {
        this.maintenanceRepository = maintenanceRepository;
        this.factoryResolver = factoryResolver;
        this.notifierResolver = notifierResolver;
    }

    @Override
    public void registerListener(IMaintenanceEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public MaintenanceRequest submitRequest(String type, String unitID, String description) {
        MaintenanceRequest request = factoryResolver.resolve(type).submitRequest(unitID, description);
        maintenanceRepository.save(request);
        notifierResolver.resolve(request.getRequestType()).notifyAssignedTeam(request);

        notifyListeners("REQUEST_SUBMITTED", request);
        return request;
    }

    @Override
    public boolean setStatus(int reqID, String status) {
        Optional<<MaintenanceMaintenanceRequest> existing = maintenanceRepository.findByID(reqID);
        if (existing.isEmpty()) {
            return false;
        }

        MaintenanceRequest request = existing.get();
        request.updateStatus(status);
        maintenanceRepository.update(request);

        notifyListeners("STATUS_UPDATED", request);
        return true;
    }

    private void notifyListeners(String eventType, MaintenanceRequest request) {
        for (IMaintenanceEventListener listener : listeners) {
            listener.onMaintenanceEvent(eventType, request);
        }
    }
}

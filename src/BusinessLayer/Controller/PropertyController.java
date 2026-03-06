package BusinessLayer.Controller;

import BusinessLayer.Domain.IClock;
import BusinessLayer.Domain.MaintenanceRequest;
import BusinessLayer.Domain.Property;
import BusinessLayer.Repository.IMaintenanceRepository;
import BusinessLayer.Repository.IPropertyRepository;

import java.util.Optional;

/**
 * Orchestrates property and maintenance use cases.
 */
public class PropertyController {
    private final IPropertyRepository propertyRepository;
    private final IMaintenanceRepository maintenanceRepository;
    private final IClock clock;

    public PropertyController(IPropertyRepository propertyRepository, IMaintenanceRepository maintenanceRepository, IClock clock) {
        this.propertyRepository = propertyRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.clock = clock;
    }

    public Property addProperty(String address, String type, int ownerID) {
        Property property = new Property(0, ownerID, address, type);
        propertyRepository.save(property);
        return property;
    }

    public MaintenanceRequest newRequest(int unitID, String description) {
        MaintenanceRequest request = new MaintenanceRequest(
                0,
                unitID,
                description,
                clock.getCurrentDate(),
                "OPEN"
        );
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

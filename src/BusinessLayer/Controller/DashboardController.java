package BusinessLayer.Controller;

import BusinessLayer.Repository.IPropertyRepository;

/**
 * Orchestrates dashboard queries.
 */
public class DashboardController {
    private final IPropertyRepository propertyRepository;

    public DashboardController(IPropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public int getOwnerPropertyCount(int ownerID) {
        return propertyRepository.findByOwnerID(ownerID).size();
    }

}

package BusinessLayer.Controller;

import BusinessLayer.Domain.Property;
import BusinessLayer.Repository.IPropertyRepository;

/**
 * Orchestrates property use cases.
 */
public class PropertyController {
    private final IPropertyRepository propertyRepository;

    public PropertyController(IPropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public Property addProperty(String address, String type, int ownerID) {
        Property property = new Property(0, ownerID, address, type);
        propertyRepository.save(property);
        return property;
    }
}

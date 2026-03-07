package BusinessLayer.Repository;

import BusinessLayer.Domain.Property;

import java.util.List;
import java.util.Optional;

public interface IPropertyRepository {
    void save(Property property);

    Optional<Property> findByID(int propertyID);

    List<Property> findByOwnerID(int ownerID);
}

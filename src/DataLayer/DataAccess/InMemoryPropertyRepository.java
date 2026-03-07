package DataLayer.DataAccess;

import BusinessLayer.Domain.Property;
import BusinessLayer.Repository.IPropertyRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory implementation of IPropertyRepository for testing/mock scenarios.
 * Stores properties in a HashMap — no database connection required.
 */
public class InMemoryPropertyRepository implements IPropertyRepository {

    private final Map<Integer, Property> store = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    public void save(Property property) {
        if (property.getPropertyID() == 0) {
            property.setPropertyID(idGenerator.getAndIncrement());
        }
        store.put(property.getPropertyID(), property);
        System.out.println("[InMemoryPropertyDB] Property saved -> ID=" + property.getPropertyID()
                + ", Address=" + property.getAddress());
    }

    @Override
    public Optional<Property> findByID(int propertyID) {
        System.out.println("[InMemoryPropertyDB] Property lookup -> ID=" + propertyID);
        return Optional.ofNullable(store.get(propertyID));
    }

    @Override
    public List<Property> findByOwnerID(int ownerID) {
        System.out.println("[InMemoryPropertyDB] Owner lookup -> OwnerID=" + ownerID);
        List<Property> result = new ArrayList<>();
        for (Property p : store.values()) {
            if (p.getOwnerID() == ownerID) {
                result.add(p);
            }
        }
        return result;
    }

}

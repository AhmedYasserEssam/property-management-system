package DataLayer.DataAccess;

import BusinessLayer.Repository.IPropertyRepository;
import BusinessLayer.Repository.IUnitRepository;
import BusinessLayer.Repository.PropertyStorageFactory;

/**
 * Concrete factory: produces in-memory property and unit repositories.
 * Useful for testing and development without a database.
 */
public class InMemoryStorageFactory implements PropertyStorageFactory {

    @Override
    public IPropertyRepository createPropertyRepository() {
        return new InMemoryPropertyRepository();
    }

    @Override
    public IUnitRepository createUnitRepository() {
        return new InMemoryUnitRepository();
    }

}

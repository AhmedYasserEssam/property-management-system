package DataLayer.DataAccess;

import BusinessLayer.Repository.IPropertyRepository;
import BusinessLayer.Repository.IUnitRepository;
import BusinessLayer.Repository.PropertyStorageFactory;

/**
 * Concrete factory: produces SQL Server-backed property and unit repositories.
 * Uses the relational database via JDBC for persistent storage.
 */
public class RelationalStorageFactory implements PropertyStorageFactory {

    @Override
    public IPropertyRepository createPropertyRepository() {
        return new PropertyDB();
    }

    @Override
    public IUnitRepository createUnitRepository() {
        return new UnitDB();
    }

}

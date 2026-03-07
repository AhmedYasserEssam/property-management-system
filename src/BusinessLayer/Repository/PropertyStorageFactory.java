package BusinessLayer.Repository;

/**
 * Abstract Factory for the Property/Unit storage product family.
 * Declares creation methods for related property and unit repositories
 * that must be used together consistently (e.g., both SQL-based or both in-memory).
 */
public interface PropertyStorageFactory {

    IPropertyRepository createPropertyRepository();

    IUnitRepository createUnitRepository();

}

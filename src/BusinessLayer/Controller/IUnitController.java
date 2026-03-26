package BusinessLayer.Controller;

import BusinessLayer.Domain.Unit;

/**
 * Bridge implementor interface for unit use cases.
 */
public interface IUnitController {
    Unit addUnit(String unitNumber, double rentalPrice, double area, String status);

    boolean updateUnit(int unitID, double rentalPrice, double area, String status);
}
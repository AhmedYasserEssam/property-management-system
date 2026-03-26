package BusinessLayer.Controller;

import BusinessLayer.Domain.Unit;
import BusinessLayer.Repository.IUnitRepository;

import java.util.Optional;

/**
 * Orchestrates unit use cases.
 */
public class UnitController implements IUnitController {
    private final IUnitRepository unitRepository;

    public UnitController(IUnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @Override
    public Unit addUnit(String unitNumber, double rentalPrice, double area, String status) {
        Unit unit = new Unit(0, unitNumber, rentalPrice, area, status);
        unitRepository.save(unit);
        return unit;
    }

    @Override
    public boolean updateUnit(int unitID, double rentalPrice, double area, String status) {
        Optional<Unit> existing = unitRepository.findByID(unitID);
        if (existing.isEmpty()) {
            return false;
        }

        Unit unit = existing.get();
        unit.updateDetails(rentalPrice, area, status);
        unitRepository.save(unit);
        return true;
    }

}

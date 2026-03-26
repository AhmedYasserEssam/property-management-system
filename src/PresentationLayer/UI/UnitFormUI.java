package PresentationLayer.UI;

import BusinessLayer.Controller.IUnitController;
import BusinessLayer.Domain.Unit;

/**
 * Bridge abstraction for unit forms.
 */
public class UnitFormUI {
    protected final IUnitController unitController;

    /**
     * Bridge constructor.
     */
    public UnitFormUI(IUnitController unitController) {
        this.unitController = unitController;
    }

    /**
     * Legacy method kept for backward compatibility with scaffold usage.
     */
    public void submitUpdateUnit() {
        throw new UnsupportedOperationException(
                "Use submitUpdateUnit(unitID, rentalPrice, area, status) to delegate to the bridge controller.");
    }

    public Unit submitAddUnit(String unitNumber, double rentalPrice, double area, String status) {
        return unitController.addUnit(unitNumber, rentalPrice, area, status);
    }

    public boolean submitUpdateUnit(int unitID, double rentalPrice, double area, String status) {
        return unitController.updateUnit(unitID, rentalPrice, area, status);
    }

}
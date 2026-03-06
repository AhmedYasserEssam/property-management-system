package BusinessLayer.Domain;

/**
 * Unit domain entity.
 */
public class Unit {
    private int unitID;
    private String unitNumber;
    private double rentalPrice;
    private double area;
    private String status;

    public Unit() {
    }

    public Unit(int unitID, String unitNumber, double rentalPrice, double area, String status) {
        this.unitID = unitID;
        this.unitNumber = unitNumber;
        this.rentalPrice = rentalPrice;
        this.area = area;
        this.status = status;
    }

    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public double getRentalPrice() {
        return rentalPrice;
    }

    public double getArea() {
        return area;
    }

    public String getStatus() {
        return status;
    }

    public void updateDetails(double rentalPrice, double area, String status) {
        this.rentalPrice = rentalPrice;
        this.area = area;
        this.status = status;
    }

}
package BusinessLayer.Domain;

/**
 * Property domain entity.
 */
public class Property {
    private int propertyID;
    private int ownerID;
    private String address;
    private String propertyType;

    public Property() {
    }

    public Property(int propertyID, int ownerID, String address, String propertyType) {
        this.propertyID = propertyID;
        this.ownerID = ownerID;
        this.address = address;
        this.propertyType = propertyType;
    }

    public int getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(int propertyID) {
        this.propertyID = propertyID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public String getAddress() {
        return address;
    }

    public String getPropertyType() {
        return propertyType;
    }


}
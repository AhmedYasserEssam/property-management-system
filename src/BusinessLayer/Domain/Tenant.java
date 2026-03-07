package BusinessLayer.Domain;

/**
 * Tenant domain entity.
 */
public class Tenant {
    private int tenantID;
    private String fullName;
    private String contactInfo;

    public Tenant() {
    }

    public Tenant(int tenantID, String fullName, String contactInfo) {
        this.tenantID = tenantID;
        this.fullName = fullName;
        this.contactInfo = contactInfo;
    }

    public int getTenantID() {
        return tenantID;
    }

    public void setTenantID(int tenantID) {
        this.tenantID = tenantID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void updateContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

}
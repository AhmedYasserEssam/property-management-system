package BusinessLayer.Domain;

import java.time.LocalDateTime;

/**
 * Lease aggregate root.
 */
public class Lease {
    private int leaseID;
    private int tenantID;
    private int unitID;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double rentAmount;
    private String status;

    /**
     * Default constructor for serialization/frameworks.
     */
    public Lease() {
    }

    public Lease(int leaseID, int tenantID, int unitID, LocalDateTime startDate, LocalDateTime endDate, double rentAmount) {
        this.leaseID = leaseID;
        this.tenantID = tenantID;
        this.unitID = unitID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentAmount = rentAmount;
        this.status = "ACTIVE";
    }

    public int getLeaseID() {
        return leaseID;
    }

    public void setLeaseID(int leaseID) {
        this.leaseID = leaseID;
    }

    public int getTenantID() {
        return tenantID;
    }

    public int getUnitID() {
        return unitID;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public double getRentAmount() {
        return rentAmount;
    }

    public String getStatus() {
        return status;
    }

    public void updateRent(double rentAmount) {
        this.rentAmount = rentAmount;
    }

    public void extendByDays(int durationDays) {
        this.endDate = this.endDate.plusDays(durationDays);
    }

    public void markExpiringIfWithinDays(int threshold, LocalDateTime now) {
        if (!endDate.isBefore(now) && !endDate.isAfter(now.plusDays(threshold))) {
            this.status = "EXPIRING";
        }
    }

    public void markExpired() {
        this.status = "EXPIRED";
    }

}
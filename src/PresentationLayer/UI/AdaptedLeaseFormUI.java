package PresentationLayer.UI;

import BusinessLayer.Controller.LeaseController;
import BusinessLayer.Domain.Lease;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Object Adapter: keeps Date-based UI interface while delegating to LocalDateTime domain API.
 */
public class AdaptedLeaseFormUI extends LeaseFormUI {
    private final LeaseController leaseController;
    private final String formName;
    private final String rentLabel;

    public AdaptedLeaseFormUI(String formName, String rentLabel, LeaseController leaseController) {
        this.formName = formName;
        this.rentLabel = rentLabel;
        this.leaseController = leaseController;
    }

    @Override
    public void submitLease(int tenantID, int unitID, Date start, Date end, double rent) {
        if (leaseController == null) {
            System.out.println("[" + formName + "] Lease submitted -> Tenant=" + tenantID
                    + ", Unit=" + unitID + ", " + rentLabel + "=" + rent + ", Start=" + start + ", End=" + end);
            return;
        }

        LocalDateTime adaptedStart = LocalDateTime.ofInstant(start.toInstant(), ZoneId.systemDefault());
        LocalDateTime adaptedEnd = LocalDateTime.ofInstant(end.toInstant(), ZoneId.systemDefault());
        Lease lease = leaseController.createLease(tenantID, unitID, adaptedStart, adaptedEnd, rent);

        System.out.println("[" + formName + "] Lease created -> ID=" + lease.getLeaseID()
                + ", Tenant=" + tenantID + ", Unit=" + unitID + ", " + rentLabel + "=" + rent);
    }
}
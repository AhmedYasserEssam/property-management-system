package BusinessLayer.Controller;

import BusinessLayer.Domain.Lease;
import BusinessLayer.Mediator.ILeaseMediator;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Orchestrates lease use cases.
 */
public class LeaseController {
    private final ILeaseMediator leaseMediator;

    public LeaseController(ILeaseMediator leaseMediator) {
        this.leaseMediator = leaseMediator;
    }

    public Lease createLease(int tenantID, int unitID, LocalDateTime start, LocalDateTime end, double rent) {
        return leaseMediator.createLease(tenantID, unitID, start, end, rent);
    }

    public List<<LeLease> checkExpiringLeases(int thresholdDays) {
        return leaseMediator.checkExpiringLeases(thresholdDays);
    }

    public boolean handleLeaseProposal(int leaseID, double rent, int durationDays) {
        return leaseMediator.handleLeaseProposal(leaseID, rent, durationDays);
    }
}

package BusinessLayer.Mediator;

import BusinessLayer.Domain.Lease;
import java.util.List;
import java.time.LocalDateTime;

public interface ILeaseMediator {
    Lease createLease(int tenantID, int unitID, LocalDateTime start, LocalDateTime end, double rent);
    List<<LeLease> checkExpiringLeases(int thresholdDays);
    boolean handleLeaseProposal(int leaseID, double rent, int durationDays);
    void registerListener(ILeaseEventListener listener);
}

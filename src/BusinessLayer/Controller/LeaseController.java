package BusinessLayer.Controller;

import BusinessLayer.Domain.IClock;
import BusinessLayer.Domain.Lease;
import BusinessLayer.Domain.LeaseExpiryStrategy;
import BusinessLayer.Domain.LeaseExpiryStrategyResolver;
import BusinessLayer.Mediator.ILeaseMediator;
import BusinessLayer.Mediator.LeaseMediator;
import BusinessLayer.Repository.ILeaseRepository;
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

    public LeaseController(ILeaseRepository leaseRepository,
                           IClock clock,
                           LeaseExpiryStrategyResolver leaseExpiryStrategyResolver) {
        this(new LeaseMediator(leaseRepository, clock, leaseExpiryStrategyResolver));
    }

    public LeaseController(ILeaseRepository leaseRepository, IClock clock, LeaseExpiryStrategy leaseExpiryStrategy) {
        this(new LeaseMediator(leaseRepository, clock, leaseExpiryStrategy));
    }

    public Lease createLease(int tenantID, int unitID, LocalDateTime start, LocalDateTime end, double rent) {
        return leaseMediator.createLease(tenantID, unitID, start, end, rent);
    }

    public List<Lease> checkExpiringLeases(int thresholdDays) {
        return leaseMediator.checkExpiringLeases(thresholdDays);
    }

    public boolean handleLeaseProposal(int leaseID, double rent, int durationDays) {
        return leaseMediator.handleLeaseProposal(leaseID, rent, durationDays);
    }
}

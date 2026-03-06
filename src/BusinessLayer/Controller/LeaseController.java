package BusinessLayer.Controller;

import BusinessLayer.Domain.IClock;
import BusinessLayer.Domain.Lease;
import BusinessLayer.Domain.LeaseExpiryStrategy;
import BusinessLayer.Repository.ILeaseRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Orchestrates lease use cases.
 */
public class LeaseController {
    private final ILeaseRepository leaseRepository;
    private final IClock clock;
    private final LeaseExpiryStrategy leaseExpiryStrategy;

    public LeaseController(ILeaseRepository leaseRepository, IClock clock, LeaseExpiryStrategy leaseExpiryStrategy) {
        this.leaseRepository = leaseRepository;
        this.clock = clock;
        this.leaseExpiryStrategy = leaseExpiryStrategy;
    }

    public Lease createLease(int tenantID, int unitID, LocalDateTime start, LocalDateTime end, double rent) {
        Lease lease = new Lease(0, tenantID, unitID, start, end, rent);
        leaseRepository.save(lease);
        return lease;
    }

    public List<Lease> checkExpiringLeases(int thresholdDays) {
        List<Lease> leases = leaseRepository.fetchLeasesNearEnd(thresholdDays);
        for (Lease lease : leases) {
            lease.markExpiringIfWithinDays(thresholdDays, clock.getCurrentDate());
            if (leaseExpiryStrategy.isExpired(lease, clock.getCurrentDate())) {
                lease.markExpired();
            }
            leaseRepository.save(lease);
        }
        return leases;
    }

    public boolean handleLeaseProposal(int leaseID, double rent, int durationDays) {
        Optional<Lease> existing = leaseRepository.findByID(leaseID);
        if (existing.isEmpty()) {
            return false;
        }

        Lease lease = existing.get();
        lease.updateRent(rent);
        lease.extendByDays(durationDays);
        leaseRepository.save(lease);
        return true;
    }

}

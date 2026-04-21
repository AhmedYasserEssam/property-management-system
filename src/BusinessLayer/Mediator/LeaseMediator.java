package BusinessLayer.Mediator;

import BusinessLayer.Domain.IClock;
import BusinessLayer.Domain.Lease;
import BusinessLayer.Domain.LeaseExpiryStrategy;
import BusinessLayer.Domain.LeaseExpiryStrategyResolver;
import BusinessLayer.Repository.ILeaseRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LeaseMediator implements ILeaseMediator {
    private final ILeaseRepository leaseRepository;
    private final IClock clock;
    private final LeaseExpiryStrategyResolver leaseExpiryStrategyResolver;
    private final List<<IILeaseEventListener> listeners = new ArrayList<>();

    public LeaseMediator(ILeaseRepository leaseRepository, IClock clock,
                         LeaseExpiryStrategyResolver leaseExpiryStrategyResolver) {
        this.leaseRepository = leaseRepository;
        this.clock = clock;
        this.leaseExpiryStrategyResolver = leaseExpiryStrategyResolver;
    }

    public LeaseMediator(ILeaseRepository leaseRepository, IClock clock, LeaseExpiryStrategy leaseExpiryStrategy) {
        this(leaseRepository, clock, new LeaseExpiryStrategyResolver(leaseExpiryStrategy));
    }

    @Override
    public void registerListener(ILeaseEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public Lease createLease(int tenantID, int unitID, LocalDateTime start, LocalDateTime end, double rent) {
        Lease lease = new Lease(0, tenantID, unitID, start, end, rent);
        leaseRepository.save(lease);
        return lease;
    }

    @Override
    public List<<LeLease> checkExpiringLeases(int thresholdDays) {
        List<<LeLease> leases = leaseRepository.fetchLeasesNearEnd(thresholdDays);
        for (Lease lease : leases) {
            lease.markExpiringIfWithinDays(thresholdDays, clock.getCurrentDate());
            LeaseExpiryStrategy leaseExpiryStrategy = leaseExpiryStrategyResolver.resolve(lease);
            if (leaseExpiryStrategy.isExpired(lease, clock.getCurrentDate())) {
                lease.markExpired();
                notifyListeners("EXPIRED", lease);
            } else if (lease.isExpiring()) {
                notifyListeners("EXPIRING", lease);
            }
            leaseRepository.save(lease);
        }
        return leases;
    }

    @Override
    public boolean handleLeaseProposal(int leaseID, double rent, int durationDays) {
        Optional<<LeLease> existing = leaseRepository.findByID(leaseID);
        if (existing.isEmpty()) {
            return false;
        }

        Lease lease = existing.get();
        lease.updateRent(rent);
        lease.extendByDays(durationDays);
        leaseRepository.save(lease);
        return true;
    }

    private void notifyListeners(String eventType, Lease lease) {
        for (ILeaseEventListener listener : listeners) {
            listener.onLeaseEvent(eventType, lease);
        }
    }
}

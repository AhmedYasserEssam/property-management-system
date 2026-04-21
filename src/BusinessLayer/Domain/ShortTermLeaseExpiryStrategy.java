package BusinessLayer.Domain;

import java.time.LocalDateTime;

/**
 * Short-term leases get a small grace window before becoming expired.
 */
public class ShortTermLeaseExpiryStrategy implements LeaseExpiryStrategy {
    @Override
    public boolean isExpired(Lease lease, LocalDateTime now) {
        return now.isAfter(lease.getEndDate().plusHours(1));
    }
}
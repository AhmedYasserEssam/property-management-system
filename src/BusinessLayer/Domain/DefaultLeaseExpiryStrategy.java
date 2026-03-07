package BusinessLayer.Domain;

import java.time.LocalDateTime;

public class DefaultLeaseExpiryStrategy implements LeaseExpiryStrategy {
    @Override
    public boolean isExpired(Lease lease, LocalDateTime now) {
        return lease.getEndDate().isBefore(now);
    }
}

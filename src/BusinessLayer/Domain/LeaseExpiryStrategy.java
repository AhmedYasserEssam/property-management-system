package BusinessLayer.Domain;

import java.time.LocalDateTime;

public interface LeaseExpiryStrategy {
    boolean isExpired(Lease lease, LocalDateTime now);
}

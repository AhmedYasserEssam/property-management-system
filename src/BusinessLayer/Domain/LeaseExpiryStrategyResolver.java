package BusinessLayer.Domain;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Resolves lease-expiry strategies per lease type.
 */
public class LeaseExpiryStrategyResolver {
    public static final String SHORT_TERM = "SHORT_TERM";
    public static final String LONG_TERM = "LONG_TERM";

    private final Map<String, LeaseExpiryStrategy> registry = new HashMap<>();
    private final LeaseExpiryStrategy fallbackStrategy;

    public LeaseExpiryStrategyResolver(LeaseExpiryStrategy fallbackStrategy) {
        this.fallbackStrategy = fallbackStrategy;
    }

    public void register(String leaseType, LeaseExpiryStrategy strategy) {
        registry.put(leaseType, strategy);
    }

    public LeaseExpiryStrategy resolve(String leaseType) {
        return registry.getOrDefault(leaseType, fallbackStrategy);
    }

    public LeaseExpiryStrategy resolve(Lease lease) {
        return resolve(resolveLeaseType(lease));
    }

    private String resolveLeaseType(Lease lease) {
        long leaseHours = Duration.between(lease.getStartDate(), lease.getEndDate()).toHours();
        return leaseHours <= 24L * 30L ? SHORT_TERM : LONG_TERM;
    }
}
package BusinessLayer.Domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Escalates request priority after a configurable pending duration.
 */
public class EscalationMaintenanceRequestDecorator extends MaintenanceRequestDecorator {

    private final Duration escalationAfter;
    private final String escalatedPriority;
    private final IClock clock;

    public EscalationMaintenanceRequestDecorator(MaintenanceRequest delegate, Duration escalationAfter) {
        this(delegate, escalationAfter, "CRITICAL", Clock.getInstance());
    }

    public EscalationMaintenanceRequestDecorator(MaintenanceRequest delegate,
                                                 Duration escalationAfter,
                                                 String escalatedPriority) {
        this(delegate, escalationAfter, escalatedPriority, Clock.getInstance());
    }

    public EscalationMaintenanceRequestDecorator(MaintenanceRequest delegate,
                                                 Duration escalationAfter,
                                                 String escalatedPriority,
                                                 IClock clock) {
        super(delegate);
        if (escalationAfter == null || escalationAfter.isNegative() || escalationAfter.isZero()) {
            throw new IllegalArgumentException("Escalation duration must be a positive duration");
        }
        if (escalatedPriority == null || escalatedPriority.isBlank()) {
            throw new IllegalArgumentException("Escalated priority cannot be blank");
        }
        this.escalationAfter = escalationAfter;
        this.escalatedPriority = escalatedPriority;
        this.clock = Objects.requireNonNull(clock, "clock cannot be null");
    }

    public boolean shouldEscalate(LocalDateTime atTime) {
        LocalDateTime requestDate = delegate.getRequestDate();
        if (requestDate == null || atTime == null) {
            return false;
        }
        return atTime.isAfter(requestDate.plus(escalationAfter));
    }

    @Override
    public String getPriority() {
        if (shouldEscalate(clock.getCurrentDate()) && !isCompleted()) {
            return escalatedPriority;
        }
        return delegate.getPriority();
    }
}

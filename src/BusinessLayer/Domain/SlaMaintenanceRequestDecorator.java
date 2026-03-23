package BusinessLayer.Domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Adds SLA deadline behavior to any maintenance request.
 */
public class SlaMaintenanceRequestDecorator extends MaintenanceRequestDecorator {

    private final Duration slaWindow;
    private final IClock clock;

    public SlaMaintenanceRequestDecorator(MaintenanceRequest delegate, Duration slaWindow) {
        this(delegate, slaWindow, Clock.getInstance());
    }

    public SlaMaintenanceRequestDecorator(MaintenanceRequest delegate, Duration slaWindow, IClock clock) {
        super(delegate);
        if (slaWindow == null || slaWindow.isNegative() || slaWindow.isZero()) {
            throw new IllegalArgumentException("SLA window must be a positive duration");
        }
        this.slaWindow = slaWindow;
        this.clock = Objects.requireNonNull(clock, "clock cannot be null");
    }

    public LocalDateTime getSlaDeadline() {
        LocalDateTime requestDate = delegate.getRequestDate();
        if (requestDate == null) {
            return null;
        }
        return requestDate.plus(slaWindow);
    }

    public boolean isSlaBreached(LocalDateTime atTime) {
        LocalDateTime deadline = getSlaDeadline();
        return deadline != null && atTime != null && atTime.isAfter(deadline);
    }

    @Override
    public String getStatus() {
        if (isSlaBreached(clock.getCurrentDate()) && !isCompleted()) {
            return "SLA_BREACHED";
        }
        return delegate.getStatus();
    }
}

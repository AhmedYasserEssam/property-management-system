package BusinessLayer.Observer;

import BusinessLayer.Domain.Lease;

import java.time.LocalDateTime;

public class LeaseStatusChangedEvent {
    private final Lease lease;
    private final String newStatus;
    private final LocalDateTime occurredAt;

    public LeaseStatusChangedEvent(Lease lease, String newStatus) {
        this.lease = lease;
        this.newStatus = newStatus;
        this.occurredAt = LocalDateTime.now();
    }

    public Lease getLease() {
        return lease;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}

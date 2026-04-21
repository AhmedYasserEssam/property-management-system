package BusinessLayer.Observer;

public interface LeaseStatusObserver {
    void onLeaseStatusChanged(LeaseStatusChangedEvent event);
}

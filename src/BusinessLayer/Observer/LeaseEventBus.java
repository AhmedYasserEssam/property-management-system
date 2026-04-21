package BusinessLayer.Observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LeaseEventBus {
    private static volatile LeaseEventBus instance;
    private final List<LeaseStatusObserver> observers = new CopyOnWriteArrayList<>();

    private LeaseEventBus() {
    }

    public static LeaseEventBus getInstance() {
        if (instance == null) {
            synchronized (LeaseEventBus.class) {
                if (instance == null) {
                    instance = new LeaseEventBus();
                }
            }
        }
        return instance;
    }

    public void registerObserver(LeaseStatusObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void unregisterObserver(LeaseStatusObserver observer) {
        observers.remove(observer);
    }

    public void publish(LeaseStatusChangedEvent event) {
        for (LeaseStatusObserver observer : observers) {
            observer.onLeaseStatusChanged(event);
        }
    }
}

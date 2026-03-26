package BusinessLayer.Notification;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry to resolve notifier abstraction per maintenance request type.
 */
public class MaintenanceNotifierResolver {
    private final Map<String, MaintenanceNotifier> registry = new HashMap<>();
    private final MaintenanceNotifier fallbackNotifier;

    public MaintenanceNotifierResolver(MaintenanceNotifier fallbackNotifier) {
        this.fallbackNotifier = fallbackNotifier;
    }

    public void register(String requestType, MaintenanceNotifier notifier) {
        registry.put(requestType, notifier);
    }

    public MaintenanceNotifier resolve(String requestType) {
        return registry.getOrDefault(requestType, fallbackNotifier);
    }
}
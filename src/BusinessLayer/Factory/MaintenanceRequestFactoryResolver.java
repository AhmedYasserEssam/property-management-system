package BusinessLayer.Factory;

import java.util.HashMap;
import java.util.Map;

public class MaintenanceRequestFactoryResolver {
    private final Map<String, MaintenanceRequestFactory> registry = new HashMap<>();

    public void register(String requestType, MaintenanceRequestFactory factory) {
        registry.put(requestType, factory);
    }

    public MaintenanceRequestFactory resolve(String requestType) {
        MaintenanceRequestFactory factory = registry.get(requestType);
        if (factory == null)
            throw new IllegalArgumentException("Unknown requestType: " + requestType);
        return factory;
    }
}

package BusinessLayer.Factory;

import java.util.HashMap;
import java.util.Map;
import BusinessLayer.Domain.RequestMetaData;

public class RequestMetaDataFactory {

    private final Map<String, RequestMetaData> pool = new HashMap<>();

    public RequestMetaData getOrCreate(String requestType, String priority,
            String assignedTeam, String initialStatus) {
        return pool.computeIfAbsent(
                requestType,
                key -> new RequestMetaData(priority, assignedTeam, initialStatus));
    }

}

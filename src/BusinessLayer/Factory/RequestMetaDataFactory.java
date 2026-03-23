package BusinessLayer.Factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import BusinessLayer.Domain.RequestMetaData;

public class RequestMetaDataFactory {

    private static final RequestMetaDataFactory INSTANCE = new RequestMetaDataFactory();

    private final Map<String, RequestMetaData> pool = new ConcurrentHashMap<>();

    private RequestMetaDataFactory() {
    }

    public static RequestMetaDataFactory getInstance() {
        return INSTANCE;
    }

    public RequestMetaData getOrCreate(String requestType, String priority,
            String assignedTeam, String initialStatus) {
        String metadataKey = buildKey(requestType, priority, assignedTeam, initialStatus);
        return pool.computeIfAbsent(
                metadataKey,
            key -> new RequestMetaData(requestType, priority, assignedTeam, initialStatus));
    }

    private String buildKey(String requestType, String priority, String assignedTeam, String initialStatus) {
        return String.join("|",
                normalize(requestType),
                normalize(priority),
                normalize(assignedTeam),
                normalize(initialStatus));
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().replace("|", "||");
    }
}

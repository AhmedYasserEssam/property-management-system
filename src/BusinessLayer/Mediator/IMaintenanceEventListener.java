package BusinessLayer.Mediator;

public interface IMaintenanceEventListener {
    void onMaintenanceEvent(String eventType, Object request);
}

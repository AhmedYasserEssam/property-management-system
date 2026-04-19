package BusinessLayer.Mediator;

public interface ILeaseEventListener {
    void onLeaseEvent(String eventType, Object lease);
}

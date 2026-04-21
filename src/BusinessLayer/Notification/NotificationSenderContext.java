package BusinessLayer.Notification;

import java.util.Objects;

/**
 * Strategy context for selecting the active notification sender at runtime.
 */
public class NotificationSenderContext {
    private INotificationSender currentStrategy;

    public NotificationSenderContext(INotificationSender initialStrategy) {
        this.currentStrategy = Objects.requireNonNull(initialStrategy, "initialStrategy cannot be null");
    }

    public void setStrategy(INotificationSender strategy) {
        this.currentStrategy = Objects.requireNonNull(strategy, "strategy cannot be null");
    }

    public void send(String destination, String subject, String message) {
        currentStrategy.send(destination, subject, message);
    }
}
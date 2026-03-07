package BusinessLayer.Domain;

import java.time.LocalDateTime;

/**
 * System clock abstraction implementation.
 */
public class Clock implements IClock {
    private static volatile Clock instance;

    private Clock() {}

    public static Clock getInstance() {
        if (instance == null) {
            synchronized (Clock.class) {
                if (instance == null) instance = new Clock();
            }
        }
        return instance;
    }

    @Override
    public LocalDateTime getCurrentDate() {
        return LocalDateTime.now();
    }
}
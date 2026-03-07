package BusinessLayer.Domain;

import java.time.LocalDateTime;

/**
 * Abstraction to read current time.
 */
public interface IClock {
    LocalDateTime getCurrentDate();
}

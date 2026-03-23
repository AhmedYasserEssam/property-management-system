package DataLayer.DataAccess;

/**
 * Logging abstraction for payment repository decorators.
 */
public interface IPaymentRepositoryLogger {
    void log(String message);
}

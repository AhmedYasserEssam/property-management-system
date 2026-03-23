package DataLayer.DataAccess;

/**
 * Console logger implementation for payment repository decorators.
 */
public class ConsolePaymentRepositoryLogger implements IPaymentRepositoryLogger {

    @Override
    public void log(String message) {
        System.out.println(message);
    }
}

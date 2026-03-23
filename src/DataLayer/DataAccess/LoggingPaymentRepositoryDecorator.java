package DataLayer.DataAccess;

import BusinessLayer.Domain.Payment;
import BusinessLayer.Repository.IPaymentRepository;

import java.util.Objects;
import java.util.Optional;

/**
 * Adds simple operation logging around payment repository calls.
 */
public class LoggingPaymentRepositoryDecorator extends PaymentRepositoryDecorator {

    private final IPaymentRepositoryLogger logger;

    public LoggingPaymentRepositoryDecorator(IPaymentRepository delegate) {
        this(delegate, new ConsolePaymentRepositoryLogger());
    }

    public LoggingPaymentRepositoryDecorator(IPaymentRepository delegate, IPaymentRepositoryLogger logger) {
        super(delegate);
        this.logger = Objects.requireNonNull(logger, "logger cannot be null");
    }

    @Override
    public void save(Payment payment) {
        try {
            logger.log("[PaymentRepository] save start leaseID=" + (payment != null ? payment.getLeaseID() : "null"));
            delegate.save(payment);
            logger.log("[PaymentRepository] save success paymentID=" + (payment != null ? payment.getPaymentID() : "null"));
        } catch (RuntimeException ex) {
            logger.log("[PaymentRepository] save failed: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public Optional<Payment> findByID(int paymentID) {
        try {
            logger.log("[PaymentRepository] findByID start paymentID=" + paymentID);
            Optional<Payment> result = delegate.findByID(paymentID);
            logger.log("[PaymentRepository] findByID result found=" + result.isPresent());
            return result;
        } catch (RuntimeException ex) {
            logger.log("[PaymentRepository] findByID failed: " + ex.getMessage());
            throw ex;
        }
    }
}

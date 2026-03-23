package DataLayer.DataAccess;

import BusinessLayer.Domain.Payment;
import BusinessLayer.Repository.IPaymentRepository;

import java.util.Optional;

/**
 * Guards payment repository operations with basic business input validation.
 */
public class ValidatingPaymentRepositoryDecorator extends PaymentRepositoryDecorator {

    public ValidatingPaymentRepositoryDecorator(IPaymentRepository delegate) {
        super(delegate);
    }

    @Override
    public void save(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("payment cannot be null");
        }
        if (payment.getLeaseID() <= 0) {
            throw new IllegalArgumentException("leaseID must be a positive number");
        }
        if (payment.getAmount() <= 0) {
            throw new IllegalArgumentException("payment amount must be greater than zero");
        }
        if (payment.getPaymentDate() == null) {
            throw new IllegalArgumentException("payment date cannot be null");
        }
        if (payment.getPaymentMethod() == null || payment.getPaymentMethod().isBlank()) {
            throw new IllegalArgumentException("payment method cannot be empty");
        }
        delegate.save(payment);
    }

    @Override
    public Optional<Payment> findByID(int paymentID) {
        if (paymentID <= 0) {
            throw new IllegalArgumentException("paymentID must be a positive number");
        }
        return delegate.findByID(paymentID);
    }
}

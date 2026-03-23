package DataLayer.DataAccess;

import BusinessLayer.Domain.Payment;
import BusinessLayer.Repository.IPaymentRepository;

import java.util.Objects;
import java.util.Optional;

/**
 * Base decorator for payment repositories.
 */
public abstract class PaymentRepositoryDecorator implements IPaymentRepository {
    protected final IPaymentRepository delegate;

    protected PaymentRepositoryDecorator(IPaymentRepository delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate repository cannot be null");
    }

    @Override
    public void save(Payment payment) {
        delegate.save(payment);
    }

    @Override
    public Optional<Payment> findByID(int paymentID) {
        return delegate.findByID(paymentID);
    }
}

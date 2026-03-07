package BusinessLayer.Repository;

import BusinessLayer.Domain.Payment;

import java.util.Optional;

public interface IPaymentRepository {
    void save(Payment payment);

    Optional<Payment> findByID(int paymentID);
}

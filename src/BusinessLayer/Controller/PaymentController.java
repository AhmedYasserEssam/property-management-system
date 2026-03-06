package BusinessLayer.Controller;

import BusinessLayer.Domain.IClock;
import BusinessLayer.Domain.Payment;
import BusinessLayer.Repository.IPaymentRepository;

import java.time.LocalDateTime;

/**
 * Orchestrates payment use cases.
 */
public class PaymentController {
    private final IPaymentRepository paymentRepository;
    private final IClock clock;

    public PaymentController(IPaymentRepository paymentRepository, IClock clock) {
        this.paymentRepository = paymentRepository;
        this.clock = clock;
    }

    public Payment recordPayment(int leaseID, double amount, String method) {
        Payment payment = new Payment(0, leaseID, amount, clock.getCurrentDate(), method);
        paymentRepository.save(payment);
        return payment;
    }

    public Payment recordPayment(int leaseID, double amount, String method, LocalDateTime paymentDate) {
        Payment payment = new Payment(0, leaseID, amount, paymentDate, method);
        paymentRepository.save(payment);
        return payment;
    }

}

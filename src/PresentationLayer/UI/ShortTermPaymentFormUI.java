package PresentationLayer.UI;

import BusinessLayer.Controller.PaymentController;
import BusinessLayer.Domain.Payment;

/**
 * Concrete product: payment form for short-term / vacation rentals.
 * Handles upfront or deposit-based payments with short-term rental rules.
 */
public class ShortTermPaymentFormUI extends PaymentFormUI {
    private final PaymentController paymentController;

    public ShortTermPaymentFormUI() {
        this(null);
    }

    public ShortTermPaymentFormUI(PaymentController paymentController) {
        this.paymentController = paymentController;
    }

    @Override
    public void submitPayment(int leaseID, double amount, String method) {
        if (paymentController != null) {
            Payment payment = paymentController.recordPayment(leaseID, amount, method);
            System.out.println("[ShortTermPaymentForm] Payment recorded -> ID=" + payment.getPaymentID()
                    + ", Lease=" + leaseID + ", Amount=" + amount + ", Method=" + method);
            return;
        }

        System.out.println("[ShortTermPaymentForm] Payment submitted -> Lease=" + leaseID
                + ", Amount=" + amount + ", Method=" + method);
    }

}

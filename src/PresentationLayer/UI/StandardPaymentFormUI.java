package PresentationLayer.UI;

import BusinessLayer.Controller.PaymentController;
import BusinessLayer.Domain.Payment;

/**
 * Concrete product: standard payment form for regular leases.
 * Handles monthly rent payments via standard payment methods.
 */
public class StandardPaymentFormUI extends PaymentFormUI {
    private final PaymentController paymentController;

    public StandardPaymentFormUI() {
        this(null);
    }

    public StandardPaymentFormUI(PaymentController paymentController) {
        this.paymentController = paymentController;
    }

    @Override
    public void submitPayment(int leaseID, double amount, String method) {
        if (paymentController != null) {
            Payment payment = paymentController.recordPayment(leaseID, amount, method);
            System.out.println("[StandardPaymentForm] Payment recorded -> ID=" + payment.getPaymentID()
                    + ", Lease=" + leaseID + ", Amount=" + amount + ", Method=" + method);
            return;
        }

        System.out.println("[StandardPaymentForm] Payment submitted -> Lease=" + leaseID
                + ", Amount=" + amount + ", Method=" + method);
    }

}

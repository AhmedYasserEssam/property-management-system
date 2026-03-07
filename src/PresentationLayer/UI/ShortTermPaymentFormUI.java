package PresentationLayer.UI;

/**
 * Concrete product: payment form for short-term / vacation rentals.
 * Handles upfront or deposit-based payments with short-term rental rules.
 */
public class ShortTermPaymentFormUI extends PaymentFormUI {

    @Override
    public void submitPayment(int leaseID, double amount, String method) {
        System.out.println("[ShortTermPaymentForm] Payment submitted -> Lease=" + leaseID
                + ", Amount=" + amount + ", Method=" + method);
    }

}

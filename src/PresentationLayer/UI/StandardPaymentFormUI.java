package PresentationLayer.UI;

/**
 * Concrete product: standard payment form for regular leases.
 * Handles monthly rent payments via standard payment methods.
 */
public class StandardPaymentFormUI extends PaymentFormUI {

    @Override
    public void submitPayment(int leaseID, double amount, String method) {
        System.out.println("[StandardPaymentForm] Payment submitted -> Lease=" + leaseID
                + ", Amount=" + amount + ", Method=" + method);
    }

}

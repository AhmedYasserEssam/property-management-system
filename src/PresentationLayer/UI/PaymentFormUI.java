package PresentationLayer.UI;

/**
 * Abstract product for the Rental UI Factory pattern.
 * Defines the interface for payment form UI variants.
 */
public abstract class PaymentFormUI {

    /**
     * @param leaseID 
     * @param amount 
     * @param method
     */
    public abstract void submitPayment(int leaseID, double amount, String method);

}
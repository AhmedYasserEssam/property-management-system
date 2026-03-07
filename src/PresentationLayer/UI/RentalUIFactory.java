package PresentationLayer.UI;

/**
 * Abstract Factory for the Rental UI product family.
 * Declares creation methods for related lease and payment form UI products
 * that must be used together consistently.
 */
public interface RentalUIFactory {

    LeaseFormUI createLeaseForm();

    PaymentFormUI createPaymentForm();

}

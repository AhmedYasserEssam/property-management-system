package PresentationLayer.UI;

/**
 * Concrete factory: produces the standard (long-term) rental UI family.
 * Creates lease and payment forms suited for regular tenant leases.
 */
public class StandardRentalUIFactory implements RentalUIFactory {

    @Override
    public LeaseFormUI createLeaseForm() {
        return new StandardLeaseFormUI();
    }

    @Override
    public PaymentFormUI createPaymentForm() {
        return new StandardPaymentFormUI();
    }

}

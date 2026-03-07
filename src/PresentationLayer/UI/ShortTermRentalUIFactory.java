package PresentationLayer.UI;

/**
 * Concrete factory: produces the short-term / vacation rental UI family.
 * Creates lease and payment forms suited for short-term rentals
 * with different rent rules and durations.
 */
public class ShortTermRentalUIFactory implements RentalUIFactory {

    @Override
    public LeaseFormUI createLeaseForm() {
        return new ShortTermLeaseFormUI();
    }

    @Override
    public PaymentFormUI createPaymentForm() {
        return new ShortTermPaymentFormUI();
    }

}

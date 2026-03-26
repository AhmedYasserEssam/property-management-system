package PresentationLayer.UI;

import BusinessLayer.Controller.LeaseController;

/**
 * Concrete factory: produces the short-term / vacation rental UI family.
 * Creates lease and payment forms suited for short-term rentals
 * with different rent rules and durations.
 */
public class ShortTermRentalUIFactory implements RentalUIFactory {
    private final LeaseController leaseController;

    public ShortTermRentalUIFactory() {
        this(null);
    }

    public ShortTermRentalUIFactory(LeaseController leaseController) {
        this.leaseController = leaseController;
    }

    @Override
    public LeaseFormUI createLeaseForm() {
        return new ShortTermLeaseFormUI(leaseController);
    }

    @Override
    public PaymentFormUI createPaymentForm() {
        return new ShortTermPaymentFormUI();
    }

}

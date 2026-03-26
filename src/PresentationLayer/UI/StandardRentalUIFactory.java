package PresentationLayer.UI;

import BusinessLayer.Controller.LeaseController;

/**
 * Concrete factory: produces the standard (long-term) rental UI family.
 * Creates lease and payment forms suited for regular tenant leases.
 */
public class StandardRentalUIFactory implements RentalUIFactory {
    private final LeaseController leaseController;

    public StandardRentalUIFactory() {
        this(null);
    }

    public StandardRentalUIFactory(LeaseController leaseController) {
        this.leaseController = leaseController;
    }

    @Override
    public LeaseFormUI createLeaseForm() {
        return new StandardLeaseFormUI(leaseController);
    }

    @Override
    public PaymentFormUI createPaymentForm() {
        return new StandardPaymentFormUI();
    }

}

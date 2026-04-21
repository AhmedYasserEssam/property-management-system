package PresentationLayer.UI;

import BusinessLayer.Controller.LeaseController;
import BusinessLayer.Controller.PaymentController;

/**
 * Concrete factory: produces the short-term / vacation rental UI family.
 * Creates lease and payment forms suited for short-term rentals
 * with different rent rules and durations.
 */
public class ShortTermRentalUIFactory implements RentalUIFactory {
    private final LeaseController leaseController;
    private final PaymentController paymentController;

    public ShortTermRentalUIFactory() {
        this(null, null);
    }

    public ShortTermRentalUIFactory(LeaseController leaseController) {
        this(leaseController, null);
    }

    public ShortTermRentalUIFactory(LeaseController leaseController, PaymentController paymentController) {
        this.leaseController = leaseController;
        this.paymentController = paymentController;
    }

    @Override
    public LeaseFormUI createLeaseForm() {
        return new ShortTermLeaseFormUI(leaseController);
    }

    @Override
    public PaymentFormUI createPaymentForm() {
        return new ShortTermPaymentFormUI(paymentController);
    }

}

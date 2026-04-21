package PresentationLayer.UI;

import BusinessLayer.Controller.LeaseController;
import BusinessLayer.Controller.PaymentController;

/**
 * Concrete factory: produces the standard (long-term) rental UI family.
 * Creates lease and payment forms suited for regular tenant leases.
 */
public class StandardRentalUIFactory implements RentalUIFactory {
    private final LeaseController leaseController;
    private final PaymentController paymentController;

    public StandardRentalUIFactory() {
        this(null, null);
    }

    public StandardRentalUIFactory(LeaseController leaseController) {
        this(leaseController, null);
    }

    public StandardRentalUIFactory(LeaseController leaseController, PaymentController paymentController) {
        this.leaseController = leaseController;
        this.paymentController = paymentController;
    }

    @Override
    public LeaseFormUI createLeaseForm() {
        return new StandardLeaseFormUI(leaseController);
    }

    @Override
    public PaymentFormUI createPaymentForm() {
        return new StandardPaymentFormUI(paymentController);
    }

}

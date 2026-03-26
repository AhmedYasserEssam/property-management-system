package PresentationLayer.UI;

import BusinessLayer.Controller.LeaseController;

/**
 * Concrete product: standard long-term lease form.
 * Handles regular tenant lease submissions with standard durations and rent rules.
 */
public class StandardLeaseFormUI extends AdaptedLeaseFormUI {

    public StandardLeaseFormUI() {
        this(null);
    }

    public StandardLeaseFormUI(LeaseController leaseController) {
        super("StandardLeaseForm", "Rent", leaseController);
    }
}

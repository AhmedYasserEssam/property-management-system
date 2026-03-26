package PresentationLayer.UI;

import BusinessLayer.Controller.LeaseController;

/**
 * Concrete product: short-term / vacation lease form.
 * Handles short-term lease submissions with different duration limits and daily rent rules.
 */
public class ShortTermLeaseFormUI extends AdaptedLeaseFormUI {

    public ShortTermLeaseFormUI() {
        this(null);
    }

    public ShortTermLeaseFormUI(LeaseController leaseController) {
        super("ShortTermLeaseForm", "DailyRate", leaseController);
    }
}

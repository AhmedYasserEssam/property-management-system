package PresentationLayer.UI;

/**
 * Client of the RentalUIFactory abstract factory.
 * Uses the factory to obtain lease and payment forms without
 * knowing which concrete family (standard vs short-term) is active.
 */
public class MainDashboardUI {

    private final LeaseFormUI leaseForm;
    private final PaymentFormUI paymentForm;

    public MainDashboardUI(RentalUIFactory factory) {
        this.leaseForm = factory.createLeaseForm();
        this.paymentForm = factory.createPaymentForm();
    }

    public LeaseFormUI getLeaseForm() {
        return leaseForm;
    }

    public PaymentFormUI getPaymentForm() {
        return paymentForm;
    }

    /**
     * 
     */
    public void openDashboard() {
        // TODO implement here
    }

    /**
     * @param summaryData
     */
    public void renderDashboard(Object summaryData) {
        // TODO implement here
    }

}
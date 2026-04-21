package PresentationLayer.UI;

import BusinessLayer.Controller.LeaseController;
import BusinessLayer.Domain.Lease;
import BusinessLayer.Observer.LeaseEventBus;
import BusinessLayer.Observer.LeaseStatusChangedEvent;
import BusinessLayer.Observer.LeaseStatusObserver;

import java.util.List;

/**
 * UI observer for lease lifecycle alerts.
 */
public class LeaseDashboardUI implements BusinessLayer.Mediator.ILeaseEventListener, LeaseStatusObserver {
    private final LeaseController leaseController;

    public LeaseDashboardUI() {
        this(null);
    }

    public LeaseDashboardUI(LeaseController leaseController) {
        this.leaseController = leaseController;
        LeaseEventBus.getInstance().registerObserver(this);
    }

    @Override
    public void onLeaseEvent(String eventType, Object lease) {
        if ("EXPIRING".equals(eventType) || "EXPIRED".equals(eventType)) {
            showLeaseAlert(lease);
        }
    }

    @Override
    public void onLeaseStatusChanged(LeaseStatusChangedEvent event) {
        showLeaseAlert(event.getLease());
    }

    public void showLeaseAlert(Object alerts) {
        if (alerts instanceof Lease lease) {
            System.out.println("[Lease Alert] Lease=" + lease.getLeaseID()
                    + ", Tenant=" + lease.getTenantID()
                    + ", Unit=" + lease.getUnitID()
                    + ", Status=" + lease.getStatus()
                    + ", Ends=" + lease.getEndDate());
            return;
        }

        System.out.println("[Lease Alert] " + String.valueOf(alerts));
    }

    public boolean proposeNewTerms(int leaseID, double rent, int duration) {
        if (leaseController == null) {
            throw new IllegalStateException("LeaseController is required to submit new terms.");
        }
        return leaseController.handleLeaseProposal(leaseID, rent, duration);
    }

    public List<Lease> checkExpiringLeases(int thresholdDays) {
        if (leaseController == null) {
            throw new IllegalStateException("LeaseController is required to check expiring leases.");
        }
        return leaseController.checkExpiringLeases(thresholdDays);
    }

}

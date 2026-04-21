package PresentationLayer.UI;

import BusinessLayer.Observer.LeaseEventBus;
import BusinessLayer.Observer.LeaseStatusChangedEvent;
import BusinessLayer.Observer.LeaseStatusObserver;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class LeaseDashboardUI implements BusinessLayer.Mediator.ILeaseEventListener, LeaseStatusObserver {

    /**
     * Default constructor
     */
    public LeaseDashboardUI() {
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

    /**
     * @param alerts
     */
    public void showLeaseAlert(Object alerts) {
        // TODO implement here
    }

    /**
     * @param rent
     * @param duration
     */
    public void proposeNewTerms(double rent, int duration) {
        // TODO implement here
    }

}
package PresentationLayer.UI;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class LeaseDashboardUI implements BusinessLayer.Mediator.ILeaseEventListener {

    /**
     * Default constructor
     */
    public LeaseDashboardUI() {
    }

    @Override
    public void onLeaseEvent(String eventType, Object lease) {
        if ("EXPIRING".equals(eventType) || "EXPIRED".equals(eventType)) {
            showLeaseAlert(lease);
        }
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
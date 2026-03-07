package PresentationLayer.UI;

import java.util.Date;

/**
 * Abstract product for the Rental UI Factory pattern.
 * Defines the interface for lease form UI variants.
 */
public abstract class LeaseFormUI {

    /**
     * @param tenantID 
     * @param unitID 
     * @param start 
     * @param end 
     * @param rent
     */
    public abstract void submitLease(int tenantID, int unitID, Date start, Date end, double rent);

}
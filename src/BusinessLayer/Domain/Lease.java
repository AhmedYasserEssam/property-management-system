package BusinessLayer.Domain;

import java.util.Date;

/**
 * 
 */
public class Lease {

    /**
     * Default constructor
     */
    public Lease() {
    }

    /**
     * 
     */
    private int leaseID;

    /**
     * 
     */
    private Date startDate;

    /**
     * 
     */
    private Date endDate;

    /**
     * 
     */
    private double rentAmount;

    /**
     * 
     */
    private String status;

    /**
     * @param threshold
     */
    public void findAndMarkExpiring(int threshold) {
        // TODO implement here
    }

}
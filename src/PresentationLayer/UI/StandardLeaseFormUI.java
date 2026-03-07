package PresentationLayer.UI;

import java.util.Date;

/**
 * Concrete product: standard long-term lease form.
 * Handles regular tenant lease submissions with standard durations and rent rules.
 */
public class StandardLeaseFormUI extends LeaseFormUI {

    @Override
    public void submitLease(int tenantID, int unitID, Date start, Date end, double rent) {
        System.out.println("[StandardLeaseForm] Lease submitted -> Tenant=" + tenantID
                + ", Unit=" + unitID + ", Rent=" + rent + ", Start=" + start + ", End=" + end);
    }

}

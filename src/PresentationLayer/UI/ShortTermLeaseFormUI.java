package PresentationLayer.UI;

import java.util.Date;

/**
 * Concrete product: short-term / vacation lease form.
 * Handles short-term lease submissions with different duration limits and daily rent rules.
 */
public class ShortTermLeaseFormUI extends LeaseFormUI {

    @Override
    public void submitLease(int tenantID, int unitID, Date start, Date end, double rent) {
        System.out.println("[ShortTermLeaseForm] Lease submitted -> Tenant=" + tenantID
                + ", Unit=" + unitID + ", DailyRate=" + rent + ", Start=" + start + ", End=" + end);
    }

}

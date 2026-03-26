package PresentationLayer.UI;

import BusinessLayer.Controller.ITenantController;
import BusinessLayer.Domain.Tenant;

/**
 * Bridge abstraction for tenant forms.
 * Different UI variants can reuse the same controller implementor.
 */
public class TenantFormUI {
    protected final ITenantController tenantController;

    /**
     * Bridge constructor.
     */
    public TenantFormUI(ITenantController tenantController) {
        this.tenantController = tenantController;
    }

    /**
     * Legacy method kept for backward compatibility with scaffold usage.
     */
    public void submitAddTenant() {
        throw new UnsupportedOperationException(
                "Use submitAddTenant(name, contact) to delegate to the bridge controller.");
    }

    public Tenant submitAddTenant(String name, String contact) {
        return tenantController.addTenant(name, contact);
    }

}
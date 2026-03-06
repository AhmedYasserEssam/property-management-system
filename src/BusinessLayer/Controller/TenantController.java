package BusinessLayer.Controller;

import BusinessLayer.Domain.Tenant;
import BusinessLayer.Repository.ITenantRepository;

/**
 * Orchestrates tenant use cases.
 */
public class TenantController {
    private final ITenantRepository tenantRepository;

    public TenantController(ITenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public Tenant addTenant(String name, String contact) {
        Tenant tenant = new Tenant(0, name, contact);
        tenantRepository.save(tenant);
        return tenant;
    }

}

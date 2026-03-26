package BusinessLayer.Controller;

import BusinessLayer.Domain.Tenant;

/**
 * Bridge implementor interface for tenant use cases.
 */
public interface ITenantController {
    Tenant addTenant(String name, String contact);
}
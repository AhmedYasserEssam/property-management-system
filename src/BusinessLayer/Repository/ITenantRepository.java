package BusinessLayer.Repository;

import BusinessLayer.Domain.Tenant;

import java.util.Optional;

public interface ITenantRepository {
    void save(Tenant tenant);

    Optional<Tenant> findByID(int tenantID);
}

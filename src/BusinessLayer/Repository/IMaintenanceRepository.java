package BusinessLayer.Repository;

import BusinessLayer.Domain.MaintenanceRequest;

import java.util.Optional;

public interface IMaintenanceRepository {
    void save(MaintenanceRequest request);

    void update(MaintenanceRequest request);

    Optional<MaintenanceRequest> findByID(int requestID);
}

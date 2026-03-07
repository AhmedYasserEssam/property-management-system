package BusinessLayer.Repository;

import BusinessLayer.Domain.Lease;

import java.util.List;
import java.util.Optional;

public interface ILeaseRepository {
    void save(Lease lease);

    Optional<Lease> findByID(int leaseID);

    List<Lease> fetchLeasesNearEnd(int thresholdDays);
}

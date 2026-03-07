package BusinessLayer.Repository;

import BusinessLayer.Domain.Unit;

import java.util.Optional;

public interface IUnitRepository {
    Optional<Unit> findByID(int id);

    void save(Unit unit);
}

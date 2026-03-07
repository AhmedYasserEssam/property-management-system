package DataLayer.DataAccess;

import BusinessLayer.Domain.Unit;
import BusinessLayer.Repository.IUnitRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory implementation of IUnitRepository for testing/mock scenarios.
 * Stores units in a HashMap — no database connection required.
 */
public class InMemoryUnitRepository implements IUnitRepository {

    private final Map<Integer, Unit> store = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    public void save(Unit unit) {
        if (unit.getUnitID() == 0) {
            unit.setUnitID(idGenerator.getAndIncrement());
        }
        store.put(unit.getUnitID(), unit);
        System.out.println("[InMemoryUnitDB] Unit saved -> ID=" + unit.getUnitID()
                + ", Number=" + unit.getUnitNumber());
    }

    @Override
    public Optional<Unit> findByID(int id) {
        System.out.println("[InMemoryUnitDB] Unit lookup -> ID=" + id);
        return Optional.ofNullable(store.get(id));
    }

}

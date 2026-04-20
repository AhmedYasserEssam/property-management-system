package DataLayer.DataAccess;

/**
 * Template-method base for repositories that persist entities with integer IDs.
 */
public abstract class BaseRepository<T> {

    public final void save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity cannot be null");
        }

        beforeSave(entity);

        if (getEntityID(entity) == 0) {
            insert(entity);
        } else {
            update(entity);
        }
    }

    protected void beforeSave(T entity) {
        // Hook for subclasses that need a side effect (e.g., logging) before persistence.
    }

    protected abstract int getEntityID(T entity);

    protected abstract void insert(T entity);

    protected abstract void update(T entity);
}

package BusinessLayer.Repository;

import BusinessLayer.Domain.Lease;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Iterator abstraction over leases that are nearing expiration.
 */
public class ExpiringLeaseIterator implements Iterator<Lease> {
    private final Iterator<Lease> delegate;

    public ExpiringLeaseIterator(List<Lease> leases) {
        this.delegate = leases.iterator();
    }

    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    public Lease next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more expiring leases");
        }
        return delegate.next();
    }
}

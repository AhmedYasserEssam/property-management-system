package DataLayer.DataAccess;

import BusinessLayer.Domain.MaintenanceRequest;
import BusinessLayer.Factory.MaintenanceRequestFactoryResolver;
import BusinessLayer.Factory.RequestMetaDataFactory;
import BusinessLayer.Factory.TenantMaintenanceFactory;
import BusinessLayer.Factory.UrgentMaintenanceFactory;
import BusinessLayer.Repository.IMaintenanceRepository;

import java.util.Optional;

/**
 * Object Adapter that hides MaintenanceDB construction details behind IMaintenanceRepository.
 */
public class MaintenanceRepositoryAdapter implements IMaintenanceRepository {
    private final MaintenanceRequestFactoryResolver factoryResolver;
    private final IMaintenanceRepository adaptee;

    public MaintenanceRepositoryAdapter() {
        this(SqlServerConnectionManager.getInstance());
    }

    public MaintenanceRepositoryAdapter(IDbConnectionProvider connectionProvider) {
        this.factoryResolver = createDefaultFactoryResolver();
        this.adaptee = new MaintenanceDB(connectionProvider, factoryResolver);
    }

    public MaintenanceRequestFactoryResolver getFactoryResolver() {
        return factoryResolver;
    }

    @Override
    public void save(MaintenanceRequest request) {
        adaptee.save(request);
    }

    @Override
    public void update(MaintenanceRequest request) {
        adaptee.update(request);
    }

    @Override
    public Optional<MaintenanceRequest> findByID(int requestID) {
        return adaptee.findByID(requestID);
    }

    private MaintenanceRequestFactoryResolver createDefaultFactoryResolver() {
        RequestMetaDataFactory metaFactory = RequestMetaDataFactory.getInstance();
        MaintenanceRequestFactoryResolver resolver = new MaintenanceRequestFactoryResolver();
        resolver.register(
                "TENANT_REPORTED",
                new TenantMaintenanceFactory(
                        metaFactory.getOrCreate("TENANT_REPORTED", "MEDIUM", "General Maintenance", "PENDING_REVIEW")));
        resolver.register(
                "URGENT",
                new UrgentMaintenanceFactory(
                        metaFactory.getOrCreate("URGENT", "CRITICAL", "Emergency Crew", "IN_PROGRESS")));
        return resolver;
    }
}
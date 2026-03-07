package Application;

import BusinessLayer.Controller.DashboardController;
import BusinessLayer.Controller.ExpenseController;
import BusinessLayer.Controller.LeaseController;
import BusinessLayer.Controller.MaintenanceController;
import BusinessLayer.Controller.PaymentController;
import BusinessLayer.Controller.PropertyController;
import BusinessLayer.Controller.TenantController;
import BusinessLayer.Controller.UnitController;
import BusinessLayer.Domain.Clock;
import BusinessLayer.Domain.DefaultLeaseExpiryStrategy;
import BusinessLayer.Domain.IClock;
import BusinessLayer.Factory.MaintenanceRequestFactoryResolver;
import BusinessLayer.Factory.TenantMaintenanceFactory;
import BusinessLayer.Factory.UrgentMaintenanceFactory;
import BusinessLayer.Repository.PropertyStorageFactory;
import DataLayer.DataAccess.ExpenseDB;
import DataLayer.DataAccess.LeaseDB;
import DataLayer.DataAccess.MaintenanceDB;
import DataLayer.DataAccess.PaymentDB;
import DataLayer.DataAccess.RelationalStorageFactory;
import DataLayer.DataAccess.TenantDB;

/**
 * Composition root that wires concrete implementations to controller abstractions.
 * This is the only class in the application that knows about both the Business Layer
 * and the Data Layer concrete types.
 */
public final class ApplicationFactory {

    private final IClock clock = Clock.getInstance();
    private final PropertyStorageFactory storageFactory;

    public ApplicationFactory() {
        this(new RelationalStorageFactory());
    }

    public ApplicationFactory(PropertyStorageFactory storageFactory) {
        this.storageFactory = storageFactory;
    }

    public DashboardController createDashboardController() {
        return new DashboardController(storageFactory.createPropertyRepository());
    }

    public ExpenseController createExpenseController() {
        return new ExpenseController(new ExpenseDB(), clock);
    }

    public LeaseController createLeaseController() {
        return new LeaseController(new LeaseDB(clock), clock, new DefaultLeaseExpiryStrategy());
    }

    public PaymentController createPaymentController() {
        return new PaymentController(new PaymentDB(), clock);
    }

    public MaintenanceController createMaintenanceController() {
        MaintenanceRequestFactoryResolver resolver = new MaintenanceRequestFactoryResolver();
        resolver.register("TENANT_REPORTED", new TenantMaintenanceFactory());
        resolver.register("URGENT", new UrgentMaintenanceFactory());
        return new MaintenanceController(new MaintenanceDB(), resolver);
    }

    public PropertyController createPropertyController() {
        return new PropertyController(storageFactory.createPropertyRepository());
    }

    public TenantController createTenantController() {
        return new TenantController(new TenantDB());
    }

    public UnitController createUnitController() {
        return new UnitController(storageFactory.createUnitRepository());
    }
}

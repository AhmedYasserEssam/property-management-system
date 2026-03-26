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
import BusinessLayer.Notification.GenericMaintenanceNotifier;
import BusinessLayer.Notification.MaintenanceNotifierResolver;
import BusinessLayer.Notification.TenantMaintenanceNotifier;
import BusinessLayer.Notification.UrgentMaintenanceNotifier;
import BusinessLayer.Repository.IPaymentRepository;
import BusinessLayer.Repository.PropertyStorageFactory;
import DataLayer.DataAccess.ConsoleEmailNotificationSender;
import DataLayer.DataAccess.ExpenseDB;
import DataLayer.DataAccess.LeaseDB;
import DataLayer.DataAccess.LoggingPaymentRepositoryDecorator;
import DataLayer.DataAccess.MaintenanceRepositoryAdapter;
import DataLayer.DataAccess.PaymentDB;
import DataLayer.DataAccess.RelationalStorageFactory;
import DataLayer.DataAccess.ConsoleSmsNotificationSender;
import DataLayer.DataAccess.TenantDB;
import DataLayer.DataAccess.ValidatingPaymentRepositoryDecorator;

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
        IPaymentRepository paymentRepository = new PaymentDB();
        paymentRepository = new ValidatingPaymentRepositoryDecorator(paymentRepository);
        paymentRepository = new LoggingPaymentRepositoryDecorator(paymentRepository);
        return new PaymentController(paymentRepository, clock);
    }

    public MaintenanceController createMaintenanceController() {
        MaintenanceRepositoryAdapter maintenanceRepository = new MaintenanceRepositoryAdapter();

        MaintenanceNotifierResolver notifierResolver = new MaintenanceNotifierResolver(
            new GenericMaintenanceNotifier(new ConsoleEmailNotificationSender()));
        notifierResolver.register("TENANT_REPORTED", new TenantMaintenanceNotifier(new ConsoleEmailNotificationSender()));
        notifierResolver.register("URGENT", new UrgentMaintenanceNotifier(new ConsoleSmsNotificationSender()));

        return new MaintenanceController(
            maintenanceRepository,
            maintenanceRepository.getFactoryResolver(),
            notifierResolver);
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

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
import BusinessLayer.Domain.LeaseExpiryStrategyResolver;
import BusinessLayer.Domain.ShortTermLeaseExpiryStrategy;
import BusinessLayer.Notification.GenericMaintenanceNotifier;
import BusinessLayer.Notification.NotificationSenderContext;
import BusinessLayer.Notification.MaintenanceNotifierResolver;
import BusinessLayer.Notification.TenantMaintenanceNotifier;
import BusinessLayer.Notification.UrgentMaintenanceNotifier;
import BusinessLayer.Mediator.IMaintenanceMediator;
import BusinessLayer.Mediator.ILeaseMediator;
import BusinessLayer.Mediator.MaintenanceMediator;
import BusinessLayer.Mediator.LeaseMediator;
import BusinessLayer.Repository.ILeaseRepository;
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
import DataLayer.DataAccess.NoOpNotificationSender;
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
        ILeaseRepository leaseRepo = new LeaseDB(clock);
        LeaseExpiryStrategyResolver strategyResolver = new LeaseExpiryStrategyResolver(new DefaultLeaseExpiryStrategy());
        strategyResolver.register(LeaseExpiryStrategyResolver.SHORT_TERM, new ShortTermLeaseExpiryStrategy());
        ILeaseMediator leaseMediator = new LeaseMediator(leaseRepo, clock, strategyResolver);
        return new LeaseController(leaseMediator);
    }

    public PaymentController createPaymentController() {
        IPaymentRepository paymentRepository = new PaymentDB();
        paymentRepository = new ValidatingPaymentRepositoryDecorator(paymentRepository);
        paymentRepository = new LoggingPaymentRepositoryDecorator(paymentRepository);
        return new PaymentController(paymentRepository, clock);
    }

    public MaintenanceController createMaintenanceController() {
        MaintenanceRepositoryAdapter maintenanceRepository = new MaintenanceRepositoryAdapter();
        NotificationSenderContext notificationSenderContext =
            new NotificationSenderContext(new NoOpNotificationSender());
        ConsoleEmailNotificationSender emailSender = new ConsoleEmailNotificationSender();
        ConsoleSmsNotificationSender smsSender = new ConsoleSmsNotificationSender();

        MaintenanceNotifierResolver notifierResolver = new MaintenanceNotifierResolver(
            new GenericMaintenanceNotifier(notificationSenderContext, emailSender));
        notifierResolver.register("TENANT_REPORTED", new TenantMaintenanceNotifier(notificationSenderContext, emailSender));
        notifierResolver.register("URGENT", new UrgentMaintenanceNotifier(notificationSenderContext, smsSender));

        IMaintenanceMediator maintenanceMediator = new MaintenanceMediator(
            maintenanceRepository,
            maintenanceRepository.getFactoryResolver(),
            notifierResolver);

        return new MaintenanceController(maintenanceMediator);
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

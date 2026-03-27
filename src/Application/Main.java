package Application;

import BusinessLayer.Controller.IMaintenanceController;
import BusinessLayer.Controller.IPropertyController;
import BusinessLayer.Controller.ITenantController;
import BusinessLayer.Controller.IUnitController;
import BusinessLayer.Controller.LeaseController;
import BusinessLayer.Controller.MaintenanceController;
import BusinessLayer.Controller.PropertyController;
import BusinessLayer.Controller.TenantController;
import BusinessLayer.Controller.UnitController;
import BusinessLayer.Domain.MaintenanceRequest;
import BusinessLayer.Domain.EscalationMaintenanceRequestDecorator;
import BusinessLayer.Domain.Lease;
import BusinessLayer.Domain.DefaultLeaseExpiryStrategy;
import BusinessLayer.Domain.Payment;
import BusinessLayer.Domain.Property;
import BusinessLayer.Domain.RequestMetaData;
import BusinessLayer.Domain.SlaMaintenanceRequestDecorator;
import BusinessLayer.Domain.Tenant;
import BusinessLayer.Domain.Unit;
import BusinessLayer.Domain.Clock;
import BusinessLayer.Domain.IClock;
import BusinessLayer.Factory.MaintenanceRequestFactory;
import BusinessLayer.Factory.MaintenanceRequestFactoryResolver;
import BusinessLayer.Factory.RequestMetaDataFactory;
import BusinessLayer.Factory.TenantMaintenanceFactory;
import BusinessLayer.Factory.UrgentMaintenanceFactory;
import BusinessLayer.Notification.GenericMaintenanceNotifier;
import BusinessLayer.Notification.INotificationSender;
import BusinessLayer.Notification.MaintenanceNotifierResolver;
import BusinessLayer.Notification.TenantMaintenanceNotifier;
import BusinessLayer.Notification.UrgentMaintenanceNotifier;
import BusinessLayer.Repository.ILeaseRepository;
import BusinessLayer.Repository.IMaintenanceRepository;
import BusinessLayer.Repository.IPaymentRepository;
import BusinessLayer.Repository.IPropertyRepository;
import BusinessLayer.Repository.ITenantRepository;
import BusinessLayer.Repository.IUnitRepository;
import BusinessLayer.Repository.PropertyStorageFactory;
import DataLayer.DataAccess.IDbConnectionProvider;
import DataLayer.DataAccess.LoggingPaymentRepositoryDecorator;
import DataLayer.DataAccess.MaintenanceRepositoryAdapter;
import DataLayer.DataAccess.RelationalStorageFactory;
import DataLayer.DataAccess.ValidatingPaymentRepositoryDecorator;
import PresentationLayer.UI.RequestFormUI;
import PresentationLayer.UI.StandardLeaseFormUI;
import PresentationLayer.UI.PropertyFormUI;
import PresentationLayer.UI.TenantFormUI;
import PresentationLayer.UI.UnitFormUI;
import PresentationLayer.UI.MainDashboardUI;
import PresentationLayer.UI.RentalUIFactory;
import PresentationLayer.UI.ShortTermRentalUIFactory;
import PresentationLayer.UI.StandardRentalUIFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            System.out.print("Choose an option: ");
            String input = scanner.nextLine().trim();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.\n");
                continue;
            }

            if (choice == 0) {
                System.out.println("Goodbye!");
                break;
            }

            try {
                switch (choice) {
                    case 1: singletonDemo();        break;
                    case 2: abstractFactoryDemo();   break;
                    case 3: factoryMethodDemo();     break;
                    case 4: flyweightDemo();         break;
                    case 5: maintenanceDecoratorFlyweightDemo(); break;
                    case 6: paymentDecoratorDemo(); break;
                    case 7: bridgePatternDemo(); break;
                    case 8: adapterPatternDemo(); break;
                    default:
                        System.out.println("Unknown option: " + choice);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("=== Real Estate System - Pattern Demos ===");
        System.out.println("1. Singleton Pattern Demo");
        System.out.println("2. Abstract Factory Pattern Demo");
        System.out.println("3. Factory Method Pattern Demo");
        System.out.println("4. Flyweight Pattern Demo");
        System.out.println("5. Maintenance Decorator + Flyweight Test");
        System.out.println("6. Payment Decorator Test");
        System.out.println("7. Bridge Pattern Test Cases");
        System.out.println("8. Adapter Pattern Test Cases");
        System.out.println("0. Exit");
        System.out.println("------------------------------------------");
    }

    // ---- 1. Singleton Pattern Demo ----

    private static void singletonDemo() {
        System.out.println();
        System.out.println("=== Singleton Pattern Demo ===");
        System.out.println();

        System.out.println("--- Clock Singleton ---");
        IClock clock1 = Clock.getInstance();
        IClock clock2 = Clock.getInstance();
        System.out.println("clock1 hashCode = " + System.identityHashCode(clock1));
        System.out.println("clock2 hashCode = " + System.identityHashCode(clock2));
        System.out.println("Same instance?   " + (clock1 == clock2));
        System.out.println("Current date via clock1: " + clock1.getCurrentDate());
        System.out.println("Current date via clock2: " + clock2.getCurrentDate());
        System.out.println();

        System.out.println("--- SqlServerConnectionManager Singleton ---");
        try {
            DataLayer.DataAccess.SqlServerConnectionManager cm1 =
                    DataLayer.DataAccess.SqlServerConnectionManager.getInstance();
            DataLayer.DataAccess.SqlServerConnectionManager cm2 =
                    DataLayer.DataAccess.SqlServerConnectionManager.getInstance();
            System.out.println("cm1 hashCode = " + System.identityHashCode(cm1));
            System.out.println("cm2 hashCode = " + System.identityHashCode(cm2));
            System.out.println("Same instance? " + (cm1 == cm2));
        } catch (Exception e) {
            System.out.println("SqlServerConnectionManager requires DB env vars.");
            System.out.println("Skipped (set RES_DB_URL, RES_DB_USER, RES_DB_PASSWORD to test).");
        }

        System.out.println();
        System.out.println("=== Singleton Pattern verified! ===");
    }

    // ---- 2. Abstract Factory Pattern Demo ----

    private static void abstractFactoryDemo() {
        System.out.println();
        System.out.println("=== Abstract Factory Pattern Demo ===");

        // --- Use Case 1: Rental UI Factory ---
        System.out.println();
        System.out.println("--- Use Case 1: Rental UI Factory ---");
        System.out.println();

        System.out.println("Creating Standard Rental UI family...");
        RentalUIFactory standardUIFactory = new StandardRentalUIFactory();
        MainDashboardUI standardDashboard = new MainDashboardUI(standardUIFactory);
        standardDashboard.getLeaseForm().submitLease(1, 101, new Date(), new Date(), 1200.00);
        standardDashboard.getPaymentForm().submitPayment(1, 1200.00, "BANK_TRANSFER");
        System.out.println();

        System.out.println("Creating Short-Term Rental UI family...");
        RentalUIFactory shortTermUIFactory = new ShortTermRentalUIFactory();
        MainDashboardUI shortTermDashboard = new MainDashboardUI(shortTermUIFactory);
        shortTermDashboard.getLeaseForm().submitLease(2, 202, new Date(), new Date(), 85.00);
        shortTermDashboard.getPaymentForm().submitPayment(2, 425.00, "CARD");

        // --- Use Case 2: Property Storage Factory ---
        System.out.println();
        System.out.println("--- Use Case 2: Property Storage Factory ---");
        System.out.println();

        System.out.println("Using Relational (SQL Server) storage backend...");
        PropertyStorageFactory relationalStorage = new RelationalStorageFactory();
        System.out.println("Factory type -> " + relationalStorage.getClass().getSimpleName());
        System.out.println();

        IPropertyRepository propRepo = relationalStorage.createPropertyRepository();
        Property prop = new Property(0, 1, "123 Main Street", "Residential");
        propRepo.save(prop);
        System.out.println("Property created -> ID=" + prop.getPropertyID()
                + ", Address=" + prop.getAddress() + ", Type=" + prop.getPropertyType());
        propRepo.findByID(prop.getPropertyID());
        propRepo.findByOwnerID(1);
        System.out.println();

        IUnitRepository unitRepo = relationalStorage.createUnitRepository();
        Unit unit = new Unit(0, "A1", 950.00, 65.0, "AVAILABLE");
        unitRepo.save(unit);
        System.out.println("Unit created -> ID=" + unit.getUnitID()
                + ", Number=" + unit.getUnitNumber()
                + ", Price=" + unit.getRentalPrice()
                + ", Area=" + unit.getArea()
                + ", Status=" + unit.getStatus());
        unitRepo.findByID(unit.getUnitID());
        System.out.println();

        System.out.println("PropertyRepository type -> " + propRepo.getClass().getSimpleName());
        System.out.println("UnitRepository type -> " + unitRepo.getClass().getSimpleName());

        System.out.println();
        System.out.println("=== Abstract Factory Pattern verified! ===");
    }

    // ---- 3. Factory Method Pattern Demo ----

    private static void factoryMethodDemo() {
        System.out.println();
        System.out.println("=== Factory Method Pattern Demo ===");
        System.out.println();

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

        System.out.println("--- TenantMaintenanceFactory ---");
        MaintenanceRequestFactory tenantFactory = resolver.resolve("TENANT_REPORTED");
        MaintenanceRequest tenantReq = tenantFactory.submitRequest("101", "Leaky faucet in kitchen");
        System.out.println("Type:          " + tenantReq.getRequestType());
        System.out.println("Class:         " + tenantReq.getClass().getSimpleName());
        System.out.println("Priority:      " + tenantReq.getPriority());
        System.out.println("Assigned Team: " + tenantReq.getAssignedTeam());
        System.out.println("Status:        " + tenantReq.getStatus());
        System.out.println("Unit:          " + tenantReq.getUnitID());
        System.out.println("Issue:         " + tenantReq.getIssueDescription());
        System.out.println();

        System.out.println("--- UrgentMaintenanceFactory ---");
        MaintenanceRequestFactory urgentFactory = resolver.resolve("URGENT");
        MaintenanceRequest urgentReq = urgentFactory.submitRequest("202", "Gas leak detected");
        System.out.println("Type:          " + urgentReq.getRequestType());
        System.out.println("Class:         " + urgentReq.getClass().getSimpleName());
        System.out.println("Priority:      " + urgentReq.getPriority());
        System.out.println("Assigned Team: " + urgentReq.getAssignedTeam());
        System.out.println("Status:        " + urgentReq.getStatus());
        System.out.println("Unit:          " + urgentReq.getUnitID());
        System.out.println("Issue:         " + urgentReq.getIssueDescription());

        System.out.println();
        System.out.println("=== Factory Method Pattern verified! ===");
    }

    // ---- 4. Flyweight Pattern Demo ----

    private static void flyweightDemo() {
        System.out.println();
        System.out.println("=== Flyweight Pattern Demo ===");
        System.out.println();

        RequestMetaDataFactory metaDataFactory = RequestMetaDataFactory.getInstance();

        RequestMetaData tenantMetaA = metaDataFactory.getOrCreate(
                "TENANT_REPORTED", "MEDIUM", "General Maintenance", "PENDING_REVIEW");
        RequestMetaData tenantMetaB = metaDataFactory.getOrCreate(
                "TENANT_REPORTED", "MEDIUM", "General Maintenance", "PENDING_REVIEW");
        RequestMetaData urgentMeta = metaDataFactory.getOrCreate(
                "URGENT", "CRITICAL", "Emergency Crew", "IN_PROGRESS");

        System.out.println("tenantMetaA identity: " + System.identityHashCode(tenantMetaA));
        System.out.println("tenantMetaB identity: " + System.identityHashCode(tenantMetaB));
        System.out.println("urgentMeta identity:  " + System.identityHashCode(urgentMeta));
        System.out.println();

        boolean sameIntrinsicStateShared = tenantMetaA == tenantMetaB;
        boolean differentIntrinsicStateSeparated = tenantMetaA != urgentMeta;

        System.out.println("Same intrinsic data reuses object?        " + sameIntrinsicStateShared);
        System.out.println("Different intrinsic data creates new one? " + differentIntrinsicStateSeparated);
        System.out.println("Sample metadata string: " + tenantMetaA);
        System.out.println();

        if (sameIntrinsicStateShared && differentIntrinsicStateSeparated) {
            System.out.println("=== Flyweight Pattern verified! ===");
        } else {
            System.out.println("=== Flyweight Pattern check failed ===");
        }
    }

    // ---- 5. Maintenance Decorator + Flyweight Test ----

    private static void maintenanceDecoratorFlyweightDemo() {
        System.out.println();
        System.out.println("=== Maintenance Decorator + Flyweight Test ===");
        System.out.println();

        RequestMetaDataFactory metaFactory = RequestMetaDataFactory.getInstance();
        RequestMetaData sharedMetaA = metaFactory.getOrCreate(
                "TENANT_REPORTED", "MEDIUM", "General Maintenance", "PENDING_REVIEW");
        RequestMetaData sharedMetaB = metaFactory.getOrCreate(
                "TENANT_REPORTED", "MEDIUM", "General Maintenance", "PENDING_REVIEW");

        MaintenanceRequest baseRequest = new MaintenanceRequest(
                "REQ-100",
                "101",
                "Broken sink",
                LocalDateTime.now().minusHours(8),
                "PENDING_REVIEW",
                sharedMetaA);

        MaintenanceRequest slaDecorated = new SlaMaintenanceRequestDecorator(baseRequest, Duration.ofHours(2));
        MaintenanceRequest escalatedDecorated = new EscalationMaintenanceRequestDecorator(
                slaDecorated,
                Duration.ofHours(4),
                "CRITICAL");

        boolean flyweightReused = sharedMetaA == sharedMetaB;
        boolean decoratorKeepsFlyweight = escalatedDecorated.getMetadata() == sharedMetaA;
        boolean typeForwarded = "TENANT_REPORTED".equals(escalatedDecorated.getRequestType());
        boolean escalationApplied = "CRITICAL".equals(escalatedDecorated.getPriority());
        boolean slaApplied = "SLA_BREACHED".equals(escalatedDecorated.getStatus());

        System.out.println("Flyweight reused for same metadata?      " + flyweightReused);
        System.out.println("Decorator keeps shared flyweight object? " + decoratorKeepsFlyweight);
        System.out.println("Request type preserved through decorator? " + typeForwarded);
        System.out.println("Escalation decorator applied?             " + escalationApplied);
        System.out.println("SLA decorator applied?                    " + slaApplied);

        if (flyweightReused && decoratorKeepsFlyweight && typeForwarded && escalationApplied && slaApplied) {
            System.out.println("=== Maintenance Decorator + Flyweight verified! ===");
        } else {
            System.out.println("=== Maintenance Decorator + Flyweight check failed ===");
        }
    }

    // ---- 6. Payment Decorator Test ----

    private static void paymentDecoratorDemo() {
        System.out.println();
        System.out.println("=== Payment Decorator Test ===");
        System.out.println();

        IPaymentRepository inMemoryRepo = new InMemoryPaymentRepository();
        IPaymentRepository decoratedRepo = new LoggingPaymentRepositoryDecorator(
                new ValidatingPaymentRepositoryDecorator(inMemoryRepo));

        Payment validPayment = new Payment(0, 1, 500.0, LocalDateTime.now(), "CARD");
        decoratedRepo.save(validPayment);
        Optional<Payment> found = decoratedRepo.findByID(validPayment.getPaymentID());

        boolean validSavedAndFound = found.isPresent() && found.get().getAmount() == 500.0;

        boolean invalidRejected;
        try {
            decoratedRepo.save(new Payment(0, 0, -5.0, LocalDateTime.now(), ""));
            invalidRejected = false;
        } catch (IllegalArgumentException ex) {
            invalidRejected = true;
        }

        System.out.println("Valid payment saved and found? " + validSavedAndFound);
        System.out.println("Invalid payment rejected?      " + invalidRejected);

        if (validSavedAndFound && invalidRejected) {
            System.out.println("=== Payment Decorator verified! ===");
        } else {
            System.out.println("=== Payment Decorator check failed ===");
        }
    }

    // ---- 7. Bridge Pattern Test Cases ----

    private static void bridgePatternDemo() {
        System.out.println();
        System.out.println("=== Bridge Pattern Test Cases ===");
        System.out.println();

        bridgeCase1UiFormToController();
        System.out.println();
        bridgeCase2MaintenanceNotifierToSender();
    }

    private static void bridgeCase1UiFormToController() {
        System.out.println("--- Bridge Case 1: UI Form Abstraction <-> Controller Implementor ---");

        InMemoryTenantRepository tenantRepository = new InMemoryTenantRepository();
        InMemoryUnitRepository unitRepository = new InMemoryUnitRepository();
        InMemoryPropertyRepository propertyRepository = new InMemoryPropertyRepository();

        ITenantController realTenantController = new TenantController(tenantRepository);
        IUnitController realUnitController = new UnitController(unitRepository);
        IPropertyController realPropertyController = new PropertyController(propertyRepository);

        TenantFormUI tenantForm = new TenantFormUI(realTenantController);
        UnitFormUI unitForm = new UnitFormUI(realUnitController);
        PropertyFormUI propertyForm = new PropertyFormUI(realPropertyController);

        Tenant tenant = tenantForm.submitAddTenant("Mona Nabil", "mona@email.com");
        Unit unit = unitForm.submitAddUnit("B2", 1300.0, 88.0, "AVAILABLE");
        boolean updated = unitForm.submitUpdateUnit(unit.getUnitID(), 1400.0, 88.0, "OCCUPIED");
        Property property = propertyForm.submitAddProperty("45 Garden Rd", "Residential", 7);

        ITenantController stubTenantController = (name, contact) -> new Tenant(999, "Stub " + name, contact);
        TenantFormUI testTenantForm = new TenantFormUI(stubTenantController);
        Tenant stubTenant = testTenantForm.submitAddTenant("Bridge Swap", "none");

        boolean realImplementorWorks = tenant.getTenantID() > 0
                && unit.getUnitID() > 0
                && updated
                && property.getPropertyID() > 0;
        boolean implementorSwappedIndependently = stubTenant.getTenantID() == 999;

        System.out.println("Real controller implementor works?   " + realImplementorWorks);
        System.out.println("Controller implementor can be swapped? " + implementorSwappedIndependently);

        if (realImplementorWorks && implementorSwappedIndependently) {
            System.out.println("Bridge Case 1 verified.");
        } else {
            System.out.println("Bridge Case 1 check failed.");
        }
    }

    private static void bridgeCase2MaintenanceNotifierToSender() {
        System.out.println("--- Bridge Case 2: Maintenance Notifier Abstraction <-> Sender Implementor ---");

        RequestMetaDataFactory metaFactory = RequestMetaDataFactory.getInstance();
        MaintenanceRequestFactoryResolver resolver = new MaintenanceRequestFactoryResolver();
        resolver.register("TENANT_REPORTED", new TenantMaintenanceFactory(
                metaFactory.getOrCreate("TENANT_REPORTED", "MEDIUM", "General Maintenance", "PENDING_REVIEW")));
        resolver.register("URGENT", new UrgentMaintenanceFactory(
                metaFactory.getOrCreate("URGENT", "CRITICAL", "Emergency Crew", "IN_PROGRESS")));

        RecordingNotificationSender emailSender = new RecordingNotificationSender("EMAIL");
        RecordingNotificationSender smsSender = new RecordingNotificationSender("SMS");

        MaintenanceNotifierResolver notifierResolver = new MaintenanceNotifierResolver(
                new GenericMaintenanceNotifier(emailSender));
        notifierResolver.register("TENANT_REPORTED", new TenantMaintenanceNotifier(emailSender));
        notifierResolver.register("URGENT", new UrgentMaintenanceNotifier(smsSender));

        InMemoryMaintenanceRepository maintenanceRepository = new InMemoryMaintenanceRepository();
        IMaintenanceController maintenanceController = new MaintenanceController(
                maintenanceRepository,
                resolver,
                notifierResolver);

        RequestFormUI requestForm = new RequestFormUI(maintenanceController);
        requestForm.reportIssue("TENANT_REPORTED", 301, "Water leak");
        requestForm.reportIssue("URGENT", 302, "Electrical short circuit");

        boolean tenantNotificationUsedEmail = emailSender.count == 1 && emailSender.lastSubject.startsWith("Tenant Reported");
        boolean urgentNotificationUsedSms = smsSender.count == 1 && smsSender.lastSubject.startsWith("URGENT");
        boolean requestsSaved = maintenanceRepository.size() == 2;

        System.out.println("Tenant-reported request used email sender? " + tenantNotificationUsedEmail);
        System.out.println("Urgent request used SMS sender?             " + urgentNotificationUsedSms);
        System.out.println("Requests persisted through controller?      " + requestsSaved);

        if (tenantNotificationUsedEmail && urgentNotificationUsedSms && requestsSaved) {
            System.out.println("Bridge Case 2 verified.");
        } else {
            System.out.println("Bridge Case 2 check failed.");
        }
    }

    // ---- 8. Adapter Pattern Test Cases ----

    private static void adapterPatternDemo() {
        System.out.println();
        System.out.println("=== Adapter Pattern Test Cases ===");
        System.out.println();

        adapterCase1MaintenanceRepositoryAdapter();
        System.out.println();
        adapterCase2DateToLocalDateTimeLeaseFormAdapter();
    }

    private static void adapterCase1MaintenanceRepositoryAdapter() {
        System.out.println("--- Adapter Case 1: IMaintenanceRepository Adapter around MaintenanceDB ---");

        IDbConnectionProvider failingConnectionProvider = new FailingConnectionProvider();
        MaintenanceRepositoryAdapter adapter = new MaintenanceRepositoryAdapter(failingConnectionProvider);

        boolean cleanTargetInterface = adapter instanceof IMaintenanceRepository;
        boolean resolverHiddenInsideAdapter = adapter.getFactoryResolver().resolve("TENANT_REPORTED") != null
                && adapter.getFactoryResolver().resolve("URGENT") != null;

        RequestMetaData metadata = RequestMetaDataFactory.getInstance().getOrCreate(
                "TENANT_REPORTED", "MEDIUM", "General Maintenance", "PENDING_REVIEW");
        MaintenanceRequest request = new MaintenanceRequest("401", "Adapter smoke test", metadata);

        boolean delegatedToAdaptee;
        try {
            adapter.save(request);
            delegatedToAdaptee = false;
        } catch (RuntimeException ex) {
            delegatedToAdaptee = ex.getMessage() != null && ex.getMessage().contains("Failed to insert maintenance request");
        }

        System.out.println("Adapter presents IMaintenanceRepository API? " + cleanTargetInterface);
        System.out.println("Factory resolver hidden in adapter wiring?    " + resolverHiddenInsideAdapter);
        System.out.println("Call delegated to wrapped MaintenanceDB?      " + delegatedToAdaptee);

        if (cleanTargetInterface && resolverHiddenInsideAdapter && delegatedToAdaptee) {
            System.out.println("Adapter Case 1 verified.");
        } else {
            System.out.println("Adapter Case 1 check failed.");
        }
    }

    private static void adapterCase2DateToLocalDateTimeLeaseFormAdapter() {
        System.out.println("--- Adapter Case 2: Date-based LeaseForm adapted to LocalDateTime API ---");

        InMemoryLeaseRepository leaseRepository = new InMemoryLeaseRepository();
        LeaseController leaseController = new LeaseController(
                leaseRepository,
                Clock.getInstance(),
                new DefaultLeaseExpiryStrategy());

        StandardLeaseFormUI form = new StandardLeaseFormUI(leaseController);

        LocalDateTime expectedStart = LocalDateTime.of(2026, 3, 1, 10, 30);
        LocalDateTime expectedEnd = LocalDateTime.of(2026, 3, 31, 10, 30);
        Date uiStart = Date.from(expectedStart.atZone(ZoneId.systemDefault()).toInstant());
        Date uiEnd = Date.from(expectedEnd.atZone(ZoneId.systemDefault()).toInstant());

        form.submitLease(77, 12, uiStart, uiEnd, 2500.0);

        Lease savedLease = leaseRepository.getLastSaved();
        boolean saved = savedLease != null;
        boolean startAdapted = saved && expectedStart.equals(savedLease.getStartDate());
        boolean endAdapted = saved && expectedEnd.equals(savedLease.getEndDate());

        System.out.println("Lease saved through controller?           " + saved);
        System.out.println("Date -> LocalDateTime start adapted?     " + startAdapted);
        System.out.println("Date -> LocalDateTime end adapted?       " + endAdapted);

        if (saved && startAdapted && endAdapted) {
            System.out.println("Adapter Case 2 verified.");
        } else {
            System.out.println("Adapter Case 2 check failed.");
        }
    }

    private static final class RecordingNotificationSender implements INotificationSender {
        private final String channel;
        private int count;
        private String lastDestination;
        private String lastSubject;
        private String lastMessage;

        private RecordingNotificationSender(String channel) {
            this.channel = channel;
        }

        @Override
        public void send(String destination, String subject, String message) {
            count++;
            lastDestination = destination;
            lastSubject = subject;
            lastMessage = message;
            System.out.println("[" + channel + "] To=" + destination + " | Subject=" + subject + " | " + message);
        }
    }

    private static final class FailingConnectionProvider implements IDbConnectionProvider {
        @Override
        public Connection getConnection() throws SQLException {
            throw new SQLException("Intentional test failure to avoid real DB dependency");
        }
    }

    private static final class InMemoryTenantRepository implements ITenantRepository {
        private final Map<Integer, Tenant> tenants = new HashMap<>();
        private int nextId = 1;

        @Override
        public void save(Tenant tenant) {
            if (tenant.getTenantID() == 0) {
                tenant.setTenantID(nextId++);
            }
            tenants.put(tenant.getTenantID(), tenant);
        }

        @Override
        public Optional<Tenant> findByID(int tenantID) {
            return Optional.ofNullable(tenants.get(tenantID));
        }
    }

    private static final class InMemoryPropertyRepository implements IPropertyRepository {
        private final Map<Integer, Property> properties = new HashMap<>();
        private int nextId = 1;

        @Override
        public void save(Property property) {
            if (property.getPropertyID() == 0) {
                property.setPropertyID(nextId++);
            }
            properties.put(property.getPropertyID(), property);
        }

        @Override
        public Optional<Property> findByID(int propertyID) {
            return Optional.ofNullable(properties.get(propertyID));
        }

        @Override
        public List<Property> findByOwnerID(int ownerID) {
            List<Property> result = new ArrayList<>();
            for (Property property : properties.values()) {
                if (property.getOwnerID() == ownerID) {
                    result.add(property);
                }
            }
            return result;
        }
    }

    private static final class InMemoryUnitRepository implements IUnitRepository {
        private final Map<Integer, Unit> units = new HashMap<>();
        private int nextId = 1;

        @Override
        public Optional<Unit> findByID(int id) {
            return Optional.ofNullable(units.get(id));
        }

        @Override
        public void save(Unit unit) {
            if (unit.getUnitID() == 0) {
                unit.setUnitID(nextId++);
            }
            units.put(unit.getUnitID(), unit);
        }
    }

    private static final class InMemoryMaintenanceRepository implements IMaintenanceRepository {
        private final Map<Integer, MaintenanceRequest> requests = new HashMap<>();
        private int nextId = 1;

        @Override
        public void save(MaintenanceRequest request) {
            if (request.getRequestID() == null || request.getRequestID().isBlank()) {
                request.setRequestID(String.valueOf(nextId++));
            }
            requests.put(Integer.parseInt(request.getRequestID()), request);
        }

        @Override
        public void update(MaintenanceRequest request) {
            save(request);
        }

        @Override
        public Optional<MaintenanceRequest> findByID(int requestID) {
            return Optional.ofNullable(requests.get(requestID));
        }

        public int size() {
            return requests.size();
        }
    }

    private static final class InMemoryLeaseRepository implements ILeaseRepository {
        private final Map<Integer, Lease> leases = new HashMap<>();
        private int nextId = 1;
        private Lease lastSaved;

        @Override
        public void save(Lease lease) {
            if (lease.getLeaseID() == 0) {
                lease.setLeaseID(nextId++);
            }
            leases.put(lease.getLeaseID(), lease);
            lastSaved = lease;
        }

        @Override
        public Optional<Lease> findByID(int leaseID) {
            return Optional.ofNullable(leases.get(leaseID));
        }

        @Override
        public List<Lease> fetchLeasesNearEnd(int thresholdDays) {
            return new ArrayList<>();
        }

        public Lease getLastSaved() {
            return lastSaved;
        }
    }

    private static final class InMemoryPaymentRepository implements IPaymentRepository {
        private final Map<Integer, Payment> payments = new HashMap<>();
        private int nextId = 1;

        @Override
        public void save(Payment payment) {
            if (payment.getPaymentID() == 0) {
                payment.setPaymentID(nextId++);
            }
            payments.put(payment.getPaymentID(), payment);
        }

        @Override
        public Optional<Payment> findByID(int paymentID) {
            return Optional.ofNullable(payments.get(paymentID));
        }
    }
}

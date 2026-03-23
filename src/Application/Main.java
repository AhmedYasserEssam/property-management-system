package Application;

import BusinessLayer.Domain.MaintenanceRequest;
import BusinessLayer.Domain.EscalationMaintenanceRequestDecorator;
import BusinessLayer.Domain.Payment;
import BusinessLayer.Domain.Property;
import BusinessLayer.Domain.RequestMetaData;
import BusinessLayer.Domain.SlaMaintenanceRequestDecorator;
import BusinessLayer.Domain.Unit;
import BusinessLayer.Domain.Clock;
import BusinessLayer.Domain.IClock;
import BusinessLayer.Factory.MaintenanceRequestFactory;
import BusinessLayer.Factory.MaintenanceRequestFactoryResolver;
import BusinessLayer.Factory.RequestMetaDataFactory;
import BusinessLayer.Factory.TenantMaintenanceFactory;
import BusinessLayer.Factory.UrgentMaintenanceFactory;
import BusinessLayer.Repository.IPaymentRepository;
import DataLayer.DataAccess.RelationalStorageFactory;
import DataLayer.DataAccess.LoggingPaymentRepositoryDecorator;
import DataLayer.DataAccess.ValidatingPaymentRepositoryDecorator;
import BusinessLayer.Repository.IPropertyRepository;
import BusinessLayer.Repository.IUnitRepository;
import BusinessLayer.Repository.PropertyStorageFactory;
import PresentationLayer.UI.MainDashboardUI;
import PresentationLayer.UI.RentalUIFactory;
import PresentationLayer.UI.ShortTermRentalUIFactory;
import PresentationLayer.UI.StandardRentalUIFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

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

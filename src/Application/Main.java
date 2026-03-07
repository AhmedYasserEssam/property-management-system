package Application;

import BusinessLayer.Domain.MaintenanceRequest;
import BusinessLayer.Domain.Property;
import BusinessLayer.Domain.Unit;
import BusinessLayer.Domain.Clock;
import BusinessLayer.Domain.IClock;
import BusinessLayer.Factory.MaintenanceRequestFactory;
import BusinessLayer.Factory.MaintenanceRequestFactoryResolver;
import BusinessLayer.Factory.TenantMaintenanceFactory;
import BusinessLayer.Factory.UrgentMaintenanceFactory;
import DataLayer.DataAccess.RelationalStorageFactory;
import BusinessLayer.Repository.IPropertyRepository;
import BusinessLayer.Repository.IUnitRepository;
import BusinessLayer.Repository.PropertyStorageFactory;
import PresentationLayer.UI.MainDashboardUI;
import PresentationLayer.UI.RentalUIFactory;
import PresentationLayer.UI.ShortTermRentalUIFactory;
import PresentationLayer.UI.StandardRentalUIFactory;

import java.util.Date;
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

        MaintenanceRequestFactoryResolver resolver = new MaintenanceRequestFactoryResolver();
        resolver.register("TENANT_REPORTED", new TenantMaintenanceFactory());
        resolver.register("URGENT", new UrgentMaintenanceFactory());

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
}

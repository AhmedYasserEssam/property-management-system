package Application;

import BusinessLayer.Controller.DashboardController;
import BusinessLayer.Controller.ExpenseController;
import BusinessLayer.Controller.LeaseController;
import BusinessLayer.Controller.PaymentController;
import BusinessLayer.Controller.PropertyController;
import BusinessLayer.Controller.TenantController;
import BusinessLayer.Controller.UnitController;
import BusinessLayer.Domain.Expense;
import BusinessLayer.Domain.Lease;
import BusinessLayer.Domain.MaintenanceRequest;
import BusinessLayer.Domain.Payment;
import BusinessLayer.Domain.Property;
import BusinessLayer.Domain.Tenant;
import BusinessLayer.Domain.Unit;
import DataLayer.DataAccess.UnitDB;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static ApplicationFactory factory;
    private static TenantController tenantCtrl;
    private static PropertyController propertyCtrl;
    private static UnitController unitCtrl;
    private static LeaseController leaseCtrl;
    private static PaymentController paymentCtrl;
    private static ExpenseController expenseCtrl;
    private static DashboardController dashboardCtrl;

    public static void main(String[] args) {
        factory = new ApplicationFactory();
        tenantCtrl = factory.createTenantController();
        propertyCtrl = factory.createPropertyController();
        unitCtrl = factory.createUnitController();
        leaseCtrl = factory.createLeaseController();
        paymentCtrl = factory.createPaymentController();
        expenseCtrl = factory.createExpenseController();
        dashboardCtrl = factory.createDashboardController();

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
                    case 1:  addTenant(scanner);              break;
                    case 2:  addProperty(scanner);            break;
                    case 3:  addUnit(scanner);                break;
                    case 4:  createLease(scanner);            break;
                    case 5:  recordPayment(scanner);          break;
                    case 6:  recordExpense(scanner);          break;
                    case 7:  submitMaintenanceRequest(scanner); break;
                    case 8:  updateMaintenanceStatus(scanner);  break;
                    case 9:  updateUnit(scanner);             break;
                    case 10: checkExpiringLeases(scanner);    break;
                    case 11: handleLeaseProposal(scanner);    break;
                    case 12: viewOwnerPropertyCount(scanner); break;
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
        System.out.println("=== Real Estate System ===");
        System.out.println(" 1. Add Tenant");
        System.out.println(" 2. Add Property");
        System.out.println(" 3. Add Unit");
        System.out.println(" 4. Create Lease");
        System.out.println(" 5. Record Payment");
        System.out.println(" 6. Record Expense");
        System.out.println(" 7. Submit Maintenance Request");
        System.out.println(" 8. Update Maintenance Status");
        System.out.println(" 9. Update Unit");
        System.out.println("10. Check Expiring Leases");
        System.out.println("11. Handle Lease Proposal");
        System.out.println("12. View Owner Property Count");
        System.out.println(" 0. Exit");
        System.out.println("--------------------------");
    }

    // ---- 1. Add Tenant ----

    private static void addTenant(Scanner sc) {
        System.out.print("Full name: ");
        String name = sc.nextLine().trim();
        System.out.print("Contact info: ");
        String contact = sc.nextLine().trim();

        Tenant tenant = tenantCtrl.addTenant(name, contact);
        System.out.println("Tenant created -> ID=" + tenant.getTenantID()
                + ", Name=" + tenant.getFullName()
                + ", Contact=" + tenant.getContactInfo());
    }

    // ---- 2. Add Property ----

    private static void addProperty(Scanner sc) {
        System.out.print("Address: ");
        String address = sc.nextLine().trim();
        System.out.print("Type (Residential/Commercial): ");
        String type = sc.nextLine().trim();
        System.out.print("Owner ID: ");
        int ownerID = Integer.parseInt(sc.nextLine().trim());

        Property property = propertyCtrl.addProperty(address, type, ownerID);
        System.out.println("Property created -> ID=" + property.getPropertyID()
                + ", Address=" + property.getAddress()
                + ", Type=" + property.getPropertyType());
    }

    // ---- 3. Add Unit ----

    private static void addUnit(Scanner sc) {
        System.out.print("Unit number: ");
        String number = sc.nextLine().trim();
        System.out.print("Rental price: ");
        double price = Double.parseDouble(sc.nextLine().trim());
        System.out.print("Area (sq m): ");
        double area = Double.parseDouble(sc.nextLine().trim());
        System.out.print("Status (AVAILABLE/OCCUPIED): ");
        String status = sc.nextLine().trim();

        Unit unit = new Unit(0, number, price, area, status);
        UnitDB repo = new UnitDB();
        repo.save(unit);
        System.out.println("Unit created -> ID=" + unit.getUnitID()
                + ", Number=" + unit.getUnitNumber()
                + ", Price=" + unit.getRentalPrice()
                + ", Area=" + unit.getArea()
                + ", Status=" + unit.getStatus());
    }

    // ---- 4. Create Lease ----

    private static void createLease(Scanner sc) {
        System.out.print("Tenant ID: ");
        int tenantID = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Unit ID: ");
        int unitID = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Start date (yyyy-MM-dd): ");
        LocalDateTime start = LocalDate.parse(sc.nextLine().trim()).atStartOfDay();
        System.out.print("End date (yyyy-MM-dd): ");
        LocalDateTime end = LocalDate.parse(sc.nextLine().trim()).atStartOfDay();
        System.out.print("Rent amount: ");
        double rent = Double.parseDouble(sc.nextLine().trim());

        Lease lease = leaseCtrl.createLease(tenantID, unitID, start, end, rent);
        System.out.println("Lease created -> ID=" + lease.getLeaseID()
                + ", Tenant=" + lease.getTenantID()
                + ", Unit=" + lease.getUnitID()
                + ", Rent=" + lease.getRentAmount()
                + ", Status=" + lease.getStatus());
    }

    // ---- 5. Record Payment ----

    private static void recordPayment(Scanner sc) {
        System.out.print("Lease ID: ");
        int leaseID = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Amount: ");
        double amount = Double.parseDouble(sc.nextLine().trim());
        System.out.print("Method (CASH/BANK_TRANSFER/CARD): ");
        String method = sc.nextLine().trim();

        Payment payment = paymentCtrl.recordPayment(leaseID, amount, method);
        System.out.println("Payment recorded -> ID=" + payment.getPaymentID()
                + ", Lease=" + payment.getLeaseID()
                + ", Amount=" + payment.getAmount()
                + ", Method=" + payment.getPaymentMethod()
                + ", Date=" + payment.getPaymentDate().toLocalDate());
    }

    // ---- 6. Record Expense ----

    private static void recordExpense(Scanner sc) {
        System.out.print("Property ID: ");
        int propertyID = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Amount: ");
        double amount = Double.parseDouble(sc.nextLine().trim());
        System.out.print("Category: ");
        String category = sc.nextLine().trim();

        Expense expense = expenseCtrl.recordExpense(propertyID, amount, category);
        System.out.println("Expense recorded -> ID=" + expense.getExpenseID()
                + ", Property=" + expense.getPropertyID()
                + ", Amount=" + expense.getAmount()
                + ", Category=" + expense.getCategory()
                + ", Date=" + expense.getDate().toLocalDate());
    }

    // ---- 7. Submit Maintenance Request ----

    private static void submitMaintenanceRequest(Scanner sc) {
        System.out.print("Unit ID: ");
        int unitID = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Issue description: ");
        String description = sc.nextLine().trim();

        MaintenanceRequest req = propertyCtrl.newRequest(unitID, description);
        System.out.println("Request created -> ID=" + req.getRequestID()
                + ", Unit=" + req.getUnitID()
                + ", Issue=" + req.getIssueDescription()
                + ", Status=" + req.getStatus());
    }

    // ---- 8. Update Maintenance Status ----

    private static void updateMaintenanceStatus(Scanner sc) {
        System.out.print("Request ID: ");
        int reqID = Integer.parseInt(sc.nextLine().trim());
        System.out.print("New status (OPEN/IN_PROGRESS/RESOLVED/CLOSED): ");
        String status = sc.nextLine().trim();

        boolean ok = propertyCtrl.setStatus(reqID, status);
        if (ok) {
            System.out.println("Request " + reqID + " updated to " + status);
        } else {
            System.out.println("Request " + reqID + " not found.");
        }
    }

    // ---- 9. Update Unit ----

    private static void updateUnit(Scanner sc) {
        System.out.print("Unit ID: ");
        int unitID = Integer.parseInt(sc.nextLine().trim());
        System.out.print("New rental price: ");
        double price = Double.parseDouble(sc.nextLine().trim());
        System.out.print("New area (sq m): ");
        double area = Double.parseDouble(sc.nextLine().trim());
        System.out.print("New status (AVAILABLE/OCCUPIED): ");
        String status = sc.nextLine().trim();

        boolean ok = unitCtrl.updateUnit(unitID, price, area, status);
        if (ok) {
            System.out.println("Unit " + unitID + " updated.");
        } else {
            System.out.println("Unit " + unitID + " not found.");
        }
    }

    // ---- 10. Check Expiring Leases ----

    private static void checkExpiringLeases(Scanner sc) {
        System.out.print("Threshold (days): ");
        int days = Integer.parseInt(sc.nextLine().trim());

        List<Lease> leases = leaseCtrl.checkExpiringLeases(days);
        if (leases.isEmpty()) {
            System.out.println("No leases expiring within " + days + " days.");
        } else {
            System.out.println("Leases expiring within " + days + " days:");
            for (Lease l : leases) {
                System.out.println("  ID=" + l.getLeaseID()
                        + ", Tenant=" + l.getTenantID()
                        + ", Ends=" + l.getEndDate().toLocalDate()
                        + ", Status=" + l.getStatus());
            }
        }
    }

    // ---- 11. Handle Lease Proposal ----

    private static void handleLeaseProposal(Scanner sc) {
        System.out.print("Lease ID: ");
        int leaseID = Integer.parseInt(sc.nextLine().trim());
        System.out.print("New rent amount: ");
        double rent = Double.parseDouble(sc.nextLine().trim());
        System.out.print("Extend by (days): ");
        int days = Integer.parseInt(sc.nextLine().trim());

        boolean ok = leaseCtrl.handleLeaseProposal(leaseID, rent, days);
        if (ok) {
            System.out.println("Lease " + leaseID + " updated: rent=" + rent + ", extended by " + days + " days.");
        } else {
            System.out.println("Lease " + leaseID + " not found.");
        }
    }

    // ---- 12. View Owner Property Count ----

    private static void viewOwnerPropertyCount(Scanner sc) {
        System.out.print("Owner ID: ");
        int ownerID = Integer.parseInt(sc.nextLine().trim());

        int count = dashboardCtrl.getOwnerPropertyCount(ownerID);
        System.out.println("Owner " + ownerID + " has " + count + " property(ies).");
    }
}

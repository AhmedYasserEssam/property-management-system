package PresentationLayer.UI;

import BusinessLayer.Controller.DashboardController;
import BusinessLayer.Domain.Expense;
import BusinessLayer.Domain.Lease;
import BusinessLayer.Domain.MaintenanceRequest;
import BusinessLayer.Domain.Property;
import BusinessLayer.Domain.Tenant;
import BusinessLayer.Domain.Unit;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Client of the RentalUIFactory abstract factory.
 * Uses the factory to obtain lease and payment forms without
 * knowing which concrete family (standard vs short-term) is active.
 */
public class MainDashboardUI {

    private final LeaseFormUI leaseForm;
    private final PaymentFormUI paymentForm;
    private final PropertyFormUI propertyForm;
    private final TenantFormUI tenantForm;
    private final UnitFormUI unitForm;
    private final RequestFormUI requestForm;
    private final StatusUI statusUI;
    private final ExpenseFormUI expenseForm;
    private final LeaseDashboardUI leaseDashboardUI;
    private final DashboardController dashboardController;
    private final Scanner scanner;
    private final String dashboardName;

    public MainDashboardUI(RentalUIFactory factory) {
        this("Rental Dashboard", factory, null, null, null, null, null, null, null, null, new Scanner(System.in));
    }

    public MainDashboardUI(String dashboardName,
                           RentalUIFactory factory,
                           PropertyFormUI propertyForm,
                           TenantFormUI tenantForm,
                           UnitFormUI unitForm,
                           RequestFormUI requestForm,
                           StatusUI statusUI,
                           ExpenseFormUI expenseForm,
                           LeaseDashboardUI leaseDashboardUI,
                           DashboardController dashboardController,
                           Scanner scanner) {
        this.dashboardName = dashboardName;
        this.leaseForm = factory.createLeaseForm();
        this.paymentForm = factory.createPaymentForm();
        this.propertyForm = propertyForm;
        this.tenantForm = tenantForm;
        this.unitForm = unitForm;
        this.requestForm = requestForm;
        this.statusUI = statusUI;
        this.expenseForm = expenseForm;
        this.leaseDashboardUI = leaseDashboardUI;
        this.dashboardController = dashboardController;
        this.scanner = scanner;
    }

    public LeaseFormUI getLeaseForm() {
        return leaseForm;
    }

    public PaymentFormUI getPaymentForm() {
        return paymentForm;
    }

    /**
     * Starts interactive console dashboard UI.
     */
    public void openDashboard() {
        if (!isFullyWired()) {
            throw new IllegalStateException("MainDashboardUI is not fully wired with controller-backed forms.");
        }

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Select an option: ");
            try {
                switch (choice) {
                    case 1 -> addTenant();
                    case 2 -> addUnit();
                    case 3 -> updateUnit();
                    case 4 -> addProperty();
                    case 5 -> createLease();
                    case 6 -> checkExpiringLeases();
                    case 7 -> recordPayment();
                    case 8 -> createMaintenanceRequest();
                    case 9 -> updateMaintenanceStatus();
                    case 10 -> recordExpense();
                    case 11 -> checkOwnerPropertyCount();
                    case 12 -> proposeLeaseTerms();
                    case 0 -> running = false;
                    default -> System.out.println("Unknown option.");
                }
            } catch (RuntimeException ex) {
                System.out.println("Operation failed: " + ex.getMessage());
            }
            System.out.println();
        }

        System.out.println("Exiting " + dashboardName + ".");
    }

    /**
     * @param summaryData
     */
    public void renderDashboard(Object summaryData) {
        System.out.println("=== " + dashboardName + " ===");
        if (summaryData != null) {
            System.out.println(summaryData);
        }
    }

    private void printMenu() {
        renderDashboard("1) Add Tenant\n"
                + "2) Add Unit\n"
                + "3) Update Unit\n"
                + "4) Add Property\n"
                + "5) Create Lease\n"
                + "6) Check Expiring Leases\n"
                + "7) Record Payment\n"
                + "8) Submit Maintenance Request\n"
                + "9) Update Maintenance Status\n"
                + "10) Record Expense\n"
                + "11) Owner Property Count\n"
                + "12) Propose New Lease Terms\n"
                + "0) Exit");
    }

    private void addTenant() {
        String name = readLine("Tenant full name: ");
        String contact = readLine("Tenant contact: ");
        Tenant tenant = tenantForm.submitAddTenant(name, contact);
        System.out.println("Tenant created. ID=" + tenant.getTenantID());
    }

    private void addUnit() {
        String unitNumber = readLine("Unit number: ");
        double rentalPrice = readDouble("Rental price: ");
        double area = readDouble("Area: ");
        String status = readLine("Status (AVAILABLE/OCCUPIED): ");
        Unit unit = unitForm.submitAddUnit(unitNumber, rentalPrice, area, status);
        System.out.println("Unit created. ID=" + unit.getUnitID());
    }

    private void updateUnit() {
        int unitID = readPositiveInt("Unit ID: ");
        double rentalPrice = readPositiveDouble("Rental price: ");
        double area = readPositiveDouble("Area: ");
        String status = readLine("Status (AVAILABLE/OCCUPIED/MAINTENANCE): ");
        boolean updated = unitForm.submitUpdateUnit(unitID, rentalPrice, area, status);
        System.out.println(updated ? "Unit updated." : "Unit not found.");
    }

    private void addProperty() {
        int ownerID = readInt("Owner ID: ");
        String address = readLine("Address: ");
        String type = readLine("Property type: ");
        Property property = propertyForm.submitAddProperty(address, type, ownerID);
        System.out.println("Property created. ID=" + property.getPropertyID());
    }

    private void createLease() {
        int tenantID = readInt("Tenant ID: ");
        int unitID = readInt("Unit ID: ");
        LocalDate startDate = readDate("Start date (yyyy-mm-dd): ");
        LocalDate endDate = readDate("End date (yyyy-mm-dd): ");
        double rent = readDouble("Rent amount: ");

        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        leaseForm.submitLease(tenantID, unitID, start, end, rent);
        leaseDashboardUI.showLeaseAlert("Lease submission completed.");
    }

    private void checkExpiringLeases() {
        int thresholdDays = readNonNegativeInt("Threshold days: ");
        List<Lease> leases = leaseDashboardUI.checkExpiringLeases(thresholdDays);
        if (leases.isEmpty()) {
            System.out.println("No leases expire within " + thresholdDays + " days.");
            return;
        }

        for (Lease lease : leases) {
            System.out.println("Lease=" + lease.getLeaseID()
                    + ", Tenant=" + lease.getTenantID()
                    + ", Unit=" + lease.getUnitID()
                    + ", Status=" + lease.getStatus()
                    + ", Ends=" + lease.getEndDate());
        }
    }

    private void recordPayment() {
        int leaseID = readInt("Lease ID: ");
        double amount = readDouble("Payment amount: ");
        String method = readLine("Payment method (CARD/CASH/BANK_TRANSFER): ");
        paymentForm.submitPayment(leaseID, amount, method);
    }

    private void createMaintenanceRequest() {
        String type = readLine("Request type (TENANT_REPORTED/URGENT): ");
        int unitID = readInt("Unit ID: ");
        String description = readLine("Issue description: ");

        MaintenanceRequest request = requestForm.reportIssue(type, unitID, description);
        System.out.println("Maintenance request created. ID=" + request.getRequestID()
                + ", Type=" + request.getRequestType() + ", Status=" + request.getStatus());
    }

    private void updateMaintenanceStatus() {
        int requestID = readInt("Request ID: ");
        String status = readLine("New status: ");
        boolean updated = statusUI.updateStatus(requestID, status);
        System.out.println(updated ? "Status updated." : "Request not found.");
    }

    private void recordExpense() {
        int propertyID = readInt("Property ID: ");
        double amount = readDouble("Expense amount: ");
        String category = readLine("Expense category: ");
        Expense expense = expenseForm.submitRecordExpense(propertyID, amount, category);
        System.out.println("Expense recorded. ID=" + expense.getExpenseID());
    }

    private void checkOwnerPropertyCount() {
        int ownerID = readInt("Owner ID: ");
        int count = dashboardController.getOwnerPropertyCount(ownerID);
        System.out.println("Owner " + ownerID + " has " + count + " properties.");
    }

    private void proposeLeaseTerms() {
        int leaseID = readInt("Lease ID: ");
        double newRent = readDouble("New rent amount: ");
        int durationDays = readInt("Extension days: ");
        boolean applied = leaseDashboardUI.proposeNewTerms(leaseID, newRent, durationDays);
        System.out.println(applied ? "Lease proposal applied." : "Lease not found.");
    }

    private int readInt(String prompt) {
        while (true) {
            String value = readLine(prompt);
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            String value = readLine(prompt);
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Please enter a value greater than zero.");
        }
    }

    private int readNonNegativeInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value >= 0) {
                return value;
            }
            System.out.println("Please enter zero or a positive value.");
        }
    }

    private double readPositiveDouble(String prompt) {
        while (true) {
            double value = readDouble(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Please enter a value greater than zero.");
        }
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            String value = readLine(prompt);
            try {
                return LocalDate.parse(value);
            } catch (RuntimeException ex) {
                System.out.println("Please enter date in yyyy-mm-dd format.");
            }
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private boolean isFullyWired() {
        return propertyForm != null
                && tenantForm != null
                && unitForm != null
                && requestForm != null
                && statusUI != null
                && expenseForm != null
                && leaseDashboardUI != null
                && dashboardController != null;
    }

}

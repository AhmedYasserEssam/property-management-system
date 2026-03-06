package Application;

import BusinessLayer.Controller.DashboardController;
import BusinessLayer.Controller.ExpenseController;
import BusinessLayer.Controller.LeaseController;
import BusinessLayer.Controller.PaymentController;
import BusinessLayer.Controller.PropertyController;
import BusinessLayer.Controller.TenantController;
import BusinessLayer.Controller.UnitController;
import BusinessLayer.Domain.Clock;
import BusinessLayer.Domain.DefaultLeaseExpiryStrategy;
import BusinessLayer.Domain.IClock;
import DataLayer.DataAccess.ExpenseDB;
import DataLayer.DataAccess.LeaseDB;
import DataLayer.DataAccess.MaintenanceDB;
import DataLayer.DataAccess.PaymentDB;
import DataLayer.DataAccess.PropertyDB;
import DataLayer.DataAccess.TenantDB;
import DataLayer.DataAccess.UnitDB;

/**
 * Composition root that wires concrete implementations to controller abstractions.
 * This is the only class in the application that knows about both the Business Layer
 * and the Data Layer concrete types.
 */
public final class ApplicationFactory {

    private final IClock clock = Clock.getInstance();

    public DashboardController createDashboardController() {
        return new DashboardController(new PropertyDB());
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

    public PropertyController createPropertyController() {
        return new PropertyController(new PropertyDB(), new MaintenanceDB(), clock);
    }

    public TenantController createTenantController() {
        return new TenantController(new TenantDB());
    }

    public UnitController createUnitController() {
        return new UnitController(new UnitDB());
    }
}

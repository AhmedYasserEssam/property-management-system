package PresentationLayer.UI;

import BusinessLayer.Controller.DashboardController;
import BusinessLayer.Domain.Expense;
import BusinessLayer.Domain.Lease;
import BusinessLayer.Domain.MaintenanceRequest;
import BusinessLayer.Domain.Property;
import BusinessLayer.Domain.Tenant;
import BusinessLayer.Domain.Unit;
import DataLayer.DataAccess.AuthService;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class DesktopDashboardUI {
    private static final Color APP_BG = new Color(246, 248, 251);
    private static final Color PANEL_BG = new Color(255, 255, 255);
    private static final Color SURFACE_MUTED = new Color(239, 243, 248);
    private static final Color BORDER = new Color(208, 216, 228);
    private static final Color PRIMARY = new Color(24, 73, 121);
    private static final Color PRIMARY_HOVER = new Color(18, 61, 103);
    private static final Color PRIMARY_DARK = new Color(15, 51, 84);
    private static final Color BUTTON_GREEN = new Color(34, 139, 79);
    private static final Color BUTTON_GREEN_HOVER = new Color(25, 112, 62);
    private static final Color BUTTON_GREEN_DARK = new Color(18, 89, 49);
    private static final Color TEXT_PRIMARY = new Color(24, 37, 53);
    private static final Color TEXT_SECONDARY = new Color(84, 101, 122);
    private static final Color SUCCESS = new Color(30, 102, 62);
    private static final Color DANGER = new Color(169, 45, 45);

    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font FONT_H2 = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 12);

    private static final int SPACE_1 = 8;
    private static final int SPACE_2 = 12;
    private static final int SPACE_3 = 16;
    private static final int FIELD_HEIGHT = 34;
    private static final int BUTTON_HEIGHT = 30;

    private static final DateTimeFormatter LOG_TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

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
    private final AuthService authService;

    private AuthService.AuthenticatedUser activeUser;

    private final JTextArea outputArea = new JTextArea();
    private final JLabel statusLabel = new JLabel("Ready");

    private final JTextField tenantNameField = new JTextField();
    private final JTextField tenantContactField = new JTextField();

    private final JTextField unitNumberField = new JTextField();
    private final JTextField unitPriceField = new JTextField();
    private final JTextField unitAreaField = new JTextField();
    private final JComboBox<String> unitStatusField = new JComboBox<>(new String[]{"AVAILABLE", "OCCUPIED"});
    private final JTextField unitUpdateIdField = new JTextField();
    private final JTextField unitUpdatePriceField = new JTextField();
    private final JTextField unitUpdateAreaField = new JTextField();
    private final JComboBox<String> unitUpdateStatusField = new JComboBox<>(new String[]{"AVAILABLE", "OCCUPIED", "MAINTENANCE"});

    private final JTextField propertyOwnerIdField = new JTextField("1");
    private final JTextField propertyAddressField = new JTextField();
    private final JTextField propertyTypeField = new JTextField("Residential");
    private final JTextField ownerCountField = new JTextField("1");

    private final JTextField leaseTenantIdField = new JTextField();
    private final JTextField leaseUnitIdField = new JTextField();
    private final JTextField leaseStartField = new JTextField(LocalDate.now().toString());
    private final JTextField leaseEndField = new JTextField(LocalDate.now().plusYears(1).toString());
    private final JTextField leaseRentField = new JTextField();
    private final JTextField proposalLeaseIdField = new JTextField();
    private final JTextField proposalRentField = new JTextField();
    private final JTextField proposalDurationField = new JTextField("30");
    private final JTextField expiryThresholdField = new JTextField("30");

    private final JComboBox<String> maintenanceTypeField = new JComboBox<>(new String[]{"TENANT_REPORTED", "URGENT"});
    private final JTextField maintenanceUnitIdField = new JTextField();
    private final JTextField maintenanceDescriptionField = new JTextField();
    private final JTextField maintenanceRequestIdField = new JTextField();
    private final JComboBox<String> maintenanceStatusField = new JComboBox<>(new String[]{
            "PENDING_REVIEW", "IN_PROGRESS", "COMPLETED", "CANCELLED"
    });

    private final JTextField paymentLeaseIdField = new JTextField();
    private final JTextField paymentAmountField = new JTextField();
    private final JComboBox<String> paymentMethodField = new JComboBox<>(new String[]{"BANK_TRANSFER", "CARD", "CASH"});
    private final JTextField expensePropertyIdField = new JTextField();
    private final JTextField expenseAmountField = new JTextField();
    private final JTextField expenseCategoryField = new JTextField("Maintenance");

    public DesktopDashboardUI(RentalUIFactory factory,
                              PropertyFormUI propertyForm,
                              TenantFormUI tenantForm,
                              UnitFormUI unitForm,
                              RequestFormUI requestForm,
                              StatusUI statusUI,
                              ExpenseFormUI expenseForm,
                              LeaseDashboardUI leaseDashboardUI,
                              DashboardController dashboardController,
                              AuthService authService) {
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
        this.authService = authService;
    }

    public void open() {
        SwingUtilities.invokeLater(this::buildAndShowFrame);
    }

    private void buildAndShowFrame() {
        applySystemLookAndFeel();

        Optional<AuthService.AuthenticatedUser> login = showLoginDialog();
        if (login.isEmpty()) {
            return;
        }
        activeUser = login.get();
        initializeInputStyles();

        JFrame frame = new JFrame(isOwner() ? "Owner Operations Dashboard" : "Tenant Self-Service Portal");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(APP_BG);

        frame.add(buildHeader(), BorderLayout.NORTH);

        JTabbedPane workflowTabs = new JTabbedPane();
        workflowTabs.setFont(FONT_BODY);
        workflowTabs.setBackground(PANEL_BG);
        workflowTabs.setForeground(TEXT_PRIMARY);

        if (isOwner()) {
            workflowTabs.addTab("Portfolio", wrapTab(buildPortfolioTab()));
            workflowTabs.addTab("Leasing", wrapTab(buildLeasingTab()));
            workflowTabs.addTab("Maintenance", wrapTab(buildMaintenanceTab()));
            workflowTabs.addTab("Finance", wrapTab(buildFinanceTab()));
        } else {
            workflowTabs.addTab("Tenant Actions", wrapTab(buildTenantPortalTab()));
        }

        JPanel activityPanel = buildActivityPanel();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, workflowTabs, activityPanel);
        splitPane.setResizeWeight(0.74);
        splitPane.setBorder(BorderFactory.createEmptyBorder(SPACE_2, SPACE_2, SPACE_2, SPACE_2));
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);
        configureResponsiveSplit(frame, splitPane);

        frame.add(splitPane, BorderLayout.CENTER);
        frame.add(buildFooter(), BorderLayout.SOUTH);

        append("Session started for " + activeUser.getRole() + " #" + activeUser.getUserId() + ".");
        frame.setMinimumSize(new Dimension(920, 640));
        frame.setSize(1240, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PANEL_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                BorderFactory.createEmptyBorder(SPACE_3, SPACE_3 + 4, SPACE_3, SPACE_3 + 4)));

        JLabel title = new JLabel(isOwner() ? "Owner Operations Console" : "Tenant Self-Service Console");
        title.setForeground(TEXT_PRIMARY);
        title.setFont(FONT_TITLE);

        JLabel subtitle = new JLabel("Signed in as " + activeUser.getDisplayName() + " ("
                + activeUser.getRole() + " #" + activeUser.getUserId() + ")");
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setFont(FONT_BODY);

        JPanel textWrap = new JPanel();
        textWrap.setOpaque(false);
        textWrap.setLayout(new BoxLayout(textWrap, BoxLayout.Y_AXIS));
        textWrap.add(title);
        textWrap.add(Box.createVerticalStrut(3));
        textWrap.add(subtitle);

        header.add(textWrap, BorderLayout.WEST);
        return header;
    }

    private JPanel buildPortfolioTab() {
        JPanel tab = createTabContainer();
        tab.add(buildTenantSection());
        tab.add(Box.createVerticalStrut(10));
        tab.add(buildUnitSection());
        tab.add(Box.createVerticalStrut(10));
        tab.add(buildUnitUpdateSection());
        tab.add(Box.createVerticalStrut(10));
        tab.add(buildPropertySection());
        tab.add(Box.createVerticalStrut(10));
        tab.add(buildOwnerMetricsSection());
        tab.add(Box.createVerticalGlue());
        return tab;
    }

    private JPanel buildLeasingTab() {
        JPanel tab = createTabContainer();
        tab.add(buildCreateLeaseSection());
        tab.add(Box.createVerticalStrut(10));
        tab.add(buildLeaseExpirySection());
        tab.add(Box.createVerticalStrut(10));
        tab.add(buildLeaseProposalSection());
        tab.add(Box.createVerticalGlue());
        return tab;
    }

    private JPanel buildMaintenanceTab() {
        JPanel tab = createTabContainer();
        tab.add(buildMaintenanceCreateSection());
        tab.add(Box.createVerticalStrut(10));
        tab.add(buildMaintenanceStatusSection());
        tab.add(Box.createVerticalGlue());
        return tab;
    }

    private JPanel buildFinanceTab() {
        JPanel tab = createTabContainer();
        tab.add(buildPaymentSection());
        tab.add(Box.createVerticalStrut(10));
        tab.add(buildExpenseSection());
        tab.add(Box.createVerticalGlue());
        return tab;
    }

    private JPanel buildTenantPortalTab() {
        JPanel tab = createTabContainer();
        tab.add(buildPaymentSection());
        tab.add(Box.createVerticalStrut(10));
        tab.add(buildMaintenanceCreateSection());
        tab.add(Box.createVerticalStrut(10));
        tab.add(buildLeaseProposalSection());
        tab.add(Box.createVerticalGlue());
        return tab;
    }

    private JPanel buildActivityPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, SPACE_1));
        panel.setBackground(PANEL_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(SPACE_2, SPACE_2, SPACE_2, SPACE_2)));

        JLabel heading = new JLabel("Activity Feed");
        heading.setFont(FONT_H2);
        heading.setForeground(PRIMARY_DARK);

        JLabel helper = new JLabel("Recent actions and validation feedback.");
        helper.setFont(FONT_LABEL);
        helper.setForeground(TEXT_SECONDARY);

        JPanel headingWrap = new JPanel();
        headingWrap.setOpaque(false);
        headingWrap.setLayout(new BoxLayout(headingWrap, BoxLayout.Y_AXIS));
        headingWrap.add(heading);
        headingWrap.add(Box.createVerticalStrut(3));
        headingWrap.add(helper);

        outputArea.setEditable(false);
        outputArea.setFont(FONT_MONO);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setBackground(SURFACE_MUTED);
        outputArea.setForeground(TEXT_PRIMARY);
        outputArea.setBorder(BorderFactory.createEmptyBorder(SPACE_1, SPACE_1, SPACE_1, SPACE_1));

        JScrollPane scroller = new JScrollPane(outputArea);
        scroller.setBorder(BorderFactory.createLineBorder(BORDER));

        JButton clearBtn = createSecondaryButton("Clear Feed", () -> outputArea.setText(""));

        panel.add(headingWrap, BorderLayout.NORTH);
        panel.add(scroller, BorderLayout.CENTER);
        panel.add(clearBtn, BorderLayout.SOUTH);
        panel.setPreferredSize(new Dimension(340, 0));
        return panel;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createEmptyBorder(SPACE_1 - 2, SPACE_3, SPACE_1, SPACE_3));
        footer.setBackground(PANEL_BG);
        statusLabel.setFont(FONT_LABEL);
        statusLabel.setForeground(TEXT_SECONDARY);
        footer.add(statusLabel, BorderLayout.WEST);
        return footer;
    }

    private JPanel buildTenantSection() {
        JPanel section = createSection("Tenant Onboarding");
        GridBagConstraints gbc = sectionConstraints();
        addLabeledField(section, gbc, 0, "Full Name", tenantNameField);
        addLabeledField(section, gbc, 1, "Contact", tenantContactField);
        addSectionButton(section, gbc, 2, createButton("Create Tenant", this::addTenant));
        return section;
    }

    private JPanel buildUnitSection() {
        JPanel section = createSection("Unit Setup");
        GridBagConstraints gbc = sectionConstraints();
        addLabeledField(section, gbc, 0, "Unit Number", unitNumberField);
        addLabeledField(section, gbc, 1, "Rental Price", unitPriceField);
        addLabeledField(section, gbc, 2, "Area", unitAreaField);
        addLabeledField(section, gbc, 3, "Status", unitStatusField);
        addSectionButton(section, gbc, 4, createButton("Create Unit", this::addUnit));
        return section;
    }

    private JPanel buildUnitUpdateSection() {
        JPanel section = createSection("Unit Update");
        GridBagConstraints gbc = sectionConstraints();
        addLabeledField(section, gbc, 0, "Unit ID", unitUpdateIdField);
        addLabeledField(section, gbc, 1, "Rental Price", unitUpdatePriceField);
        addLabeledField(section, gbc, 2, "Area", unitUpdateAreaField);
        addLabeledField(section, gbc, 3, "Status", unitUpdateStatusField);
        addSectionButton(section, gbc, 4, createButton("Update Unit", this::updateUnit));
        return section;
    }

    private JPanel buildPropertySection() {
        JPanel section = createSection("Property Registration");
        GridBagConstraints gbc = sectionConstraints();
        addLabeledField(section, gbc, 0, "Owner ID", propertyOwnerIdField);
        addLabeledField(section, gbc, 1, "Address", propertyAddressField);
        addLabeledField(section, gbc, 2, "Property Type", propertyTypeField);
        addSectionButton(section, gbc, 3, createButton("Create Property", this::addProperty));
        return section;
    }

    private JPanel buildOwnerMetricsSection() {
        JPanel section = createSection("Owner Metrics");
        GridBagConstraints gbc = sectionConstraints();
        addLabeledField(section, gbc, 0, "Owner ID", ownerCountField);
        addSectionButton(section, gbc, 1, createButton("Check Portfolio Count", this::ownerPropertyCount));
        return section;
    }

    private JPanel buildCreateLeaseSection() {
        JPanel section = createSection("Lease Creation");
        GridBagConstraints gbc = sectionConstraints();
        addLabeledField(section, gbc, 0, "Tenant ID", leaseTenantIdField);
        addLabeledField(section, gbc, 1, "Unit ID", leaseUnitIdField);
        addLabeledField(section, gbc, 2, "Start Date", leaseStartField);
        addLabeledField(section, gbc, 3, "End Date", leaseEndField);
        addLabeledField(section, gbc, 4, "Rent", leaseRentField);
        addSectionButton(section, gbc, 5, createButton("Create Lease", this::createLease));
        return section;
    }

    private JPanel buildLeaseExpirySection() {
        JPanel section = createSection("Lease Health Check");
        GridBagConstraints gbc = sectionConstraints();
        addLabeledField(section, gbc, 0, "Threshold Days", expiryThresholdField);
        addSectionButton(section, gbc, 1, createButton("Check Expiring Leases", this::checkExpiringLeases));
        return section;
    }

    private JPanel buildLeaseProposalSection() {
        JPanel section = createSection("Lease Renewal Proposal");
        GridBagConstraints gbc = sectionConstraints();
        addLabeledField(section, gbc, 0, "Lease ID", proposalLeaseIdField);
        addLabeledField(section, gbc, 1, "New Rent", proposalRentField);
        addLabeledField(section, gbc, 2, "Extension Days", proposalDurationField);
        addSectionButton(section, gbc, 3, createButton("Apply Proposal", this::proposeLeaseTerms));
        return section;
    }

    private JPanel buildMaintenanceCreateSection() {
        JPanel section = createSection("Maintenance Request");
        GridBagConstraints gbc = sectionConstraints();
        addLabeledField(section, gbc, 0, "Type", maintenanceTypeField);
        addLabeledField(section, gbc, 1, "Unit ID", maintenanceUnitIdField);
        addLabeledField(section, gbc, 2, "Description", maintenanceDescriptionField);
        addSectionButton(section, gbc, 3, createButton("Submit Request", this::submitMaintenance));
        return section;
    }

    private JPanel buildMaintenanceStatusSection() {
        JPanel section = createSection("Maintenance Status");
        GridBagConstraints gbc = sectionConstraints();
        addLabeledField(section, gbc, 0, "Request ID", maintenanceRequestIdField);
        addLabeledField(section, gbc, 1, "New Status", maintenanceStatusField);
        addSectionButton(section, gbc, 2, createButton("Update Status", this::updateMaintenanceStatus));
        return section;
    }

    private JPanel buildPaymentSection() {
        JPanel section = createSection("Payment Posting");
        GridBagConstraints gbc = sectionConstraints();
        addLabeledField(section, gbc, 0, "Lease ID", paymentLeaseIdField);
        addLabeledField(section, gbc, 1, "Amount", paymentAmountField);
        addLabeledField(section, gbc, 2, "Method", paymentMethodField);
        addSectionButton(section, gbc, 3, createButton("Record Payment", this::recordPayment));
        return section;
    }

    private JPanel buildExpenseSection() {
        JPanel section = createSection("Expense Logging");
        GridBagConstraints gbc = sectionConstraints();
        addLabeledField(section, gbc, 0, "Property ID", expensePropertyIdField);
        addLabeledField(section, gbc, 1, "Amount", expenseAmountField);
        addLabeledField(section, gbc, 2, "Category", expenseCategoryField);
        addSectionButton(section, gbc, 3, createButton("Record Expense", this::recordExpense));
        return section;
    }

    private JPanel createTabContainer() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(APP_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(SPACE_2, SPACE_2, SPACE_2, SPACE_2));
        return panel;
    }

    private JScrollPane wrapTab(JPanel tab) {
        JScrollPane scroller = new JScrollPane(tab);
        scroller.setBorder(BorderFactory.createEmptyBorder());
        scroller.getVerticalScrollBar().setUnitIncrement(16);
        scroller.setBackground(APP_BG);
        scroller.getViewport().setBackground(APP_BG);
        return scroller;
    }

    private JPanel createSection(String title) {
        JPanel section = new JPanel(new GridBagLayout());
        section.setBackground(PANEL_BG);
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createTitledBorder(
                        BorderFactory.createEmptyBorder(SPACE_1, SPACE_1, SPACE_1, SPACE_1),
                        title)));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        return section;
    }

    private GridBagConstraints sectionConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private void addLabeledField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel label = new JLabel(labelText + ":");
        label.setLabelFor(field);
        label.setFont(FONT_LABEL);
        label.setForeground(TEXT_SECONDARY);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private void addLabeledField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComboBox<String> box) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel label = new JLabel(labelText + ":");
        label.setLabelFor(box);
        label.setFont(FONT_LABEL);
        label.setForeground(TEXT_SECONDARY);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(box, gbc);
    }

    private void addSectionButton(JPanel panel, GridBagConstraints gbc, int row, JButton button) {
        int oldFill = gbc.fill;
        int oldAnchor = gbc.anchor;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(button, gbc);
        gbc.gridwidth = 1;
        gbc.fill = oldFill;
        gbc.anchor = oldAnchor;
    }

    private JButton createButton(String title, Runnable action) {
        return createButton(title, action, true);
    }

    private JButton createSecondaryButton(String title, Runnable action) {
        return createButton(title, action, false);
    }

    private JButton createButton(String title, Runnable action, boolean primary) {
        JButton button = new JButton(title);
        button.setFont(FONT_BUTTON);
        button.setBackground(primary ? BUTTON_GREEN : SURFACE_MUTED);
        button.setForeground(primary ? Color.BLACK : PRIMARY_DARK);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primary ? BUTTON_GREEN : BORDER),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        Dimension preferredSize = button.getPreferredSize();
        button.setPreferredSize(new Dimension(preferredSize.width, BUTTON_HEIGHT));
        installButtonStates(button, primary);
        button.addActionListener(e -> {
            try {
                action.run();
            } catch (RuntimeException ex) {
                fail(ex.getMessage());
            }
        });
        return button;
    }

    private void addTenant() {
        requireOwner("Only owners can add tenants.");
        Tenant tenant = tenantForm.submitAddTenant(
                requireText(tenantNameField, "Full Name"),
                requireText(tenantContactField, "Contact"));
        append("Tenant created. ID=" + tenant.getTenantID() + ", Name=" + tenant.getFullName());
        setStatus("Tenant created");
    }

    private void addUnit() {
        requireOwner("Only owners can add units.");
        Unit unit = unitForm.submitAddUnit(
                requireText(unitNumberField, "Unit Number"),
                parsePositiveDouble(unitPriceField.getText(), "Rental Price"),
                parsePositiveDouble(unitAreaField.getText(), "Area"),
                String.valueOf(unitStatusField.getSelectedItem()));
        append("Unit created. ID=" + unit.getUnitID() + ", Number=" + unit.getUnitNumber());
        setStatus("Unit created");
    }

    private void updateUnit() {
        requireOwner("Only owners can update units.");
        int unitId = parsePositiveInt(unitUpdateIdField.getText(), "Unit ID");
        boolean updated = unitForm.submitUpdateUnit(
                unitId,
                parsePositiveDouble(unitUpdatePriceField.getText(), "Rental Price"),
                parsePositiveDouble(unitUpdateAreaField.getText(), "Area"),
                String.valueOf(unitUpdateStatusField.getSelectedItem()));
        append(updated ? "Unit " + unitId + " updated." : "Unit " + unitId + " was not found.");
        setStatus(updated ? "Unit updated" : "Unit not found");
    }

    private void addProperty() {
        requireOwner("Only owners can register properties.");
        Property property = propertyForm.submitAddProperty(
                requireText(propertyAddressField, "Address"),
                requireText(propertyTypeField, "Property Type"),
                parsePositiveInt(propertyOwnerIdField.getText(), "Owner ID"));
        append("Property created. ID=" + property.getPropertyID() + ", Address=" + property.getAddress());
        setStatus("Property created");
    }

    private void createLease() {
        requireOwner("Only owners can create leases.");
        LocalDate startDate = LocalDate.parse(requireText(leaseStartField, "Start Date"));
        LocalDate endDate = LocalDate.parse(requireText(leaseEndField, "End Date"));
        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("End Date must be after Start Date.");
        }
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        leaseForm.submitLease(
                parsePositiveInt(leaseTenantIdField.getText(), "Tenant ID"),
                parsePositiveInt(leaseUnitIdField.getText(), "Unit ID"),
                start,
                end,
                parsePositiveDouble(leaseRentField.getText(), "Rent"));
        leaseDashboardUI.showLeaseAlert("Lease submission completed.");
        append("Lease created for Tenant=" + leaseTenantIdField.getText().trim()
                + ", Unit=" + leaseUnitIdField.getText().trim());
        setStatus("Lease created");
    }

    private void checkExpiringLeases() {
        requireOwner("Only owners can check expiring leases.");
        int thresholdDays = parseNonNegativeInt(expiryThresholdField.getText(), "Threshold Days");
        List<Lease> leases = leaseDashboardUI.checkExpiringLeases(thresholdDays);
        if (leases.isEmpty()) {
            append("No leases expire within " + thresholdDays + " days.");
        } else {
            append("Found " + leases.size() + " lease(s) expiring within " + thresholdDays + " days:");
            for (Lease lease : leases) {
                append("Lease " + lease.getLeaseID() + " | Tenant=" + lease.getTenantID()
                        + " | Unit=" + lease.getUnitID() + " | Status=" + lease.getStatus()
                        + " | Ends=" + lease.getEndDate().toLocalDate());
            }
        }
        setStatus("Lease health check complete");
    }

    private void recordPayment() {
        requireOwnerOrTenant("Payments are available to authenticated users only.");
        paymentForm.submitPayment(
                parsePositiveInt(paymentLeaseIdField.getText(), "Lease ID"),
                parsePositiveDouble(paymentAmountField.getText(), "Amount"),
                String.valueOf(paymentMethodField.getSelectedItem()));
        append("Payment recorded for Lease=" + paymentLeaseIdField.getText().trim());
        setStatus("Payment recorded");
    }

    private void submitMaintenance() {
        requireOwnerOrTenant("Maintenance requests are available to authenticated users only.");
        MaintenanceRequest request = requestForm.reportIssue(
                String.valueOf(maintenanceTypeField.getSelectedItem()),
                parsePositiveInt(maintenanceUnitIdField.getText(), "Unit ID"),
                requireText(maintenanceDescriptionField, "Description"));
        append("Maintenance request created. ID=" + request.getRequestID() + ", Status=" + request.getStatus());
        setStatus("Maintenance request submitted");
    }

    private void updateMaintenanceStatus() {
        requireOwner("Only owners can update maintenance statuses.");
        boolean updated = statusUI.updateStatus(
                parsePositiveInt(maintenanceRequestIdField.getText(), "Request ID"),
                String.valueOf(maintenanceStatusField.getSelectedItem()));
        append(updated ? "Maintenance status updated." : "Maintenance request not found.");
        setStatus(updated ? "Maintenance status updated" : "Maintenance request not found");
    }

    private void recordExpense() {
        requireOwner("Only owners can record expenses.");
        Expense expense = expenseForm.submitRecordExpense(
                parsePositiveInt(expensePropertyIdField.getText(), "Property ID"),
                parsePositiveDouble(expenseAmountField.getText(), "Amount"),
                requireText(expenseCategoryField, "Category"));
        append("Expense recorded. ID=" + expense.getExpenseID() + ", Category=" + expense.getCategory());
        setStatus("Expense recorded");
    }

    private void ownerPropertyCount() {
        requireOwner("Only owners can view owner portfolio metrics.");
        int ownerId = parsePositiveInt(ownerCountField.getText(), "Owner ID");
        int count = dashboardController.getOwnerPropertyCount(ownerId);
        append("Owner " + ownerId + " has " + count + " properties.");
        setStatus("Owner metrics refreshed");
    }

    private void proposeLeaseTerms() {
        requireOwnerOrTenant("Lease proposals are available to authenticated users only.");
        boolean applied = leaseDashboardUI.proposeNewTerms(
                parsePositiveInt(proposalLeaseIdField.getText(), "Lease ID"),
                parsePositiveDouble(proposalRentField.getText(), "New Rent"),
                parsePositiveInt(proposalDurationField.getText(), "Extension Days"));
        append(applied ? "Lease proposal applied." : "Lease not found.");
        setStatus(applied ? "Lease proposal applied" : "Lease not found");
    }

    private Optional<AuthService.AuthenticatedUser> showLoginDialog() {
        JComboBox<AuthService.Role> roleField = new JComboBox<>(AuthService.Role.values());
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        configureCombo(roleField);
        configureTextInput(usernameField);
        configureTextInput(passwordField);

        while (true) {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(PANEL_BG);
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(SPACE_1, SPACE_1, SPACE_1, SPACE_1)));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(6, 6, 6, 6);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            addLoginField(panel, gbc, 0, "Role", roleField);
            addLoginField(panel, gbc, 1, "Username", usernameField);
            addLoginField(panel, gbc, 2, "Password", passwordField);

            String[] actions = {"Sign In", "Create Tenant", "Create Owner", "Cancel"};
            int result = JOptionPane.showOptionDialog(
                    null,
                    panel,
                    "Sign In",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    actions,
                    actions[0]);

            if (result == 0) {
                String username;
                try {
                    username = requireText(usernameField, "Username");
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                String password = new String(passwordField.getPassword());
                AuthService.Role role = (AuthService.Role) roleField.getSelectedItem();
                Optional<AuthService.AuthenticatedUser> authenticated = authService.authenticate(role, username, password);
                if (authenticated.isPresent()) {
                    return authenticated;
                }

                JOptionPane.showMessageDialog(
                        null,
                        "Invalid credentials. For seeded data use Owner(Default Owner/owner123), Tenant(Default Tenant/tenant123).",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (result == 1) {
                registerAccount(AuthService.Role.TENANT, roleField, usernameField, passwordField);
            } else if (result == 2) {
                registerAccount(AuthService.Role.OWNER, roleField, usernameField, passwordField);
            } else {
                return Optional.empty();
            }
        }
    }

    private void registerAccount(AuthService.Role role,
                                 JComboBox<AuthService.Role> roleField,
                                 JTextField usernameField,
                                 JPasswordField passwordField) {
        JTextField nameField = new JTextField();
        JPasswordField newPasswordField = new JPasswordField();
        configureTextInput(nameField);
        configureTextInput(newPasswordField);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(PANEL_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(SPACE_1, SPACE_1, SPACE_1, SPACE_1)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String roleNameLabel = role == AuthService.Role.OWNER ? "Owner Name" : "Tenant Full Name";
        addLoginField(panel, gbc, 0, roleNameLabel, nameField);
        addLoginField(panel, gbc, 1, "Password", newPasswordField);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Create " + (role == AuthService.Role.OWNER ? "Owner" : "Tenant"),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String displayName = requireText(nameField, roleNameLabel);
            String password = new String(newPasswordField.getPassword()).trim();
            if (password.isBlank()) {
                throw new IllegalArgumentException("Password cannot be blank.");
            }

            AuthService.AuthenticatedUser created = role == AuthService.Role.OWNER
                    ? authService.registerOwner(displayName, password)
                    : authService.registerTenant(displayName, password);

            roleField.setSelectedItem(created.getRole());
            usernameField.setText(created.getDisplayName());
            passwordField.setText(password);

            JOptionPane.showMessageDialog(
                    null,
                    (role == AuthService.Role.OWNER ? "Owner" : "Tenant") + " created successfully.\n"
                            + "Username: " + created.getDisplayName() + "\n"
                            + "User ID: " + created.getUserId() + "\n"
                            + "Credentials were prefilled. Click Sign In to continue.",
                    "Registration Complete",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addLoginField(JPanel panel, GridBagConstraints gbc, int row, String labelText, Component input) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel label = new JLabel(labelText + ":");
        label.setFont(FONT_LABEL);
        label.setForeground(TEXT_SECONDARY);
        if (input instanceof JTextField field) {
            label.setLabelFor(field);
        }
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(input, gbc);
    }

    private boolean isOwner() {
        return activeUser != null && activeUser.getRole() == AuthService.Role.OWNER;
    }

    private boolean isTenant() {
        return activeUser != null && activeUser.getRole() == AuthService.Role.TENANT;
    }

    private void requireOwner(String message) {
        if (!isOwner()) {
            throw new IllegalStateException(message);
        }
    }

    private void requireOwnerOrTenant(String message) {
        if (!isOwner() && !isTenant()) {
            throw new IllegalStateException(message);
        }
    }

    private int parseInt(String value, String fieldName) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " must be a valid integer.", ex);
        }
    }

    private int parsePositiveInt(String value, String fieldName) {
        int parsed = parseInt(value, fieldName);
        if (parsed <= 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than zero.");
        }
        return parsed;
    }

    private int parseNonNegativeInt(String value, String fieldName) {
        int parsed = parseInt(value, fieldName);
        if (parsed < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be negative.");
        }
        return parsed;
    }

    private double parseDouble(String value, String fieldName) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " must be a valid number.", ex);
        }
    }

    private double parsePositiveDouble(String value, String fieldName) {
        double parsed = parseDouble(value, fieldName);
        if (parsed <= 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than zero.");
        }
        return parsed;
    }

    private String requireText(JTextField field, String fieldName) {
        String value = field.getText().trim();
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
        }
        return value;
    }

    private void append(String message) {
        outputArea.append("[" + LocalDateTime.now().format(LOG_TIME) + "] " + message + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    private void fail(String message) {
        append("Operation failed: " + message);
        setStatus("Error: " + message, true);
        JOptionPane.showMessageDialog(null, message, "Operation Failed", JOptionPane.ERROR_MESSAGE);
    }

    private void setStatus(String status) {
        setStatus(status, false);
    }

    private void setStatus(String status, boolean error) {
        statusLabel.setText(status);
        statusLabel.setForeground(error ? DANGER : SUCCESS);
    }

    private void initializeInputStyles() {
        List<JTextField> fields = Arrays.asList(
                tenantNameField,
                tenantContactField,
                unitNumberField,
                unitPriceField,
                unitAreaField,
                unitUpdateIdField,
                unitUpdatePriceField,
                unitUpdateAreaField,
                propertyOwnerIdField,
                propertyAddressField,
                propertyTypeField,
                ownerCountField,
                leaseTenantIdField,
                leaseUnitIdField,
                leaseStartField,
                leaseEndField,
                leaseRentField,
                proposalLeaseIdField,
                proposalRentField,
                proposalDurationField,
                expiryThresholdField,
                maintenanceUnitIdField,
                maintenanceDescriptionField,
                maintenanceRequestIdField,
                paymentLeaseIdField,
                paymentAmountField,
                expensePropertyIdField,
                expenseAmountField,
                expenseCategoryField);
        for (JTextField field : fields) {
            configureTextInput(field);
        }

        List<JComboBox<String>> combos = Arrays.asList(
                unitStatusField,
                unitUpdateStatusField,
                maintenanceTypeField,
                maintenanceStatusField,
                paymentMethodField);
        for (JComboBox<String> combo : combos) {
            configureCombo(combo);
        }
    }

    private void configureTextInput(JTextField field) {
        field.setFont(FONT_BODY);
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(Color.WHITE);
        field.setCaretColor(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        Dimension preferred = field.getPreferredSize();
        field.setPreferredSize(new Dimension(preferred.width, FIELD_HEIGHT));
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY, 2),
                        BorderFactory.createEmptyBorder(5, 7, 5, 7)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        BorderFactory.createEmptyBorder(6, 8, 6, 8)));
            }
        });
    }

    private void configureCombo(JComboBox<?> box) {
        box.setFont(FONT_BODY);
        box.setForeground(TEXT_PRIMARY);
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createLineBorder(BORDER));
        Dimension preferred = box.getPreferredSize();
        box.setPreferredSize(new Dimension(preferred.width, FIELD_HEIGHT));
    }

    private void installButtonStates(JButton button, boolean primary) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!button.isEnabled()) {
                    return;
                }
                button.setBackground(primary ? BUTTON_GREEN_HOVER : new Color(230, 236, 244));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!button.isEnabled()) {
                    return;
                }
                button.setBackground(primary ? BUTTON_GREEN : SURFACE_MUTED);
            }
        });

        button.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(primary ? BUTTON_GREEN_DARK : PRIMARY_DARK, 2),
                        BorderFactory.createEmptyBorder(4, 9, 4, 9)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(primary ? BUTTON_GREEN : BORDER),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)));
            }
        });
    }

    private void configureResponsiveSplit(JFrame frame, JSplitPane splitPane) {
        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int width = frame.getWidth();
                if (width < 980) {
                    splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                    splitPane.setResizeWeight(0.78);
                    splitPane.setDividerLocation(0.78);
                } else {
                    splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                    splitPane.setResizeWeight(0.74);
                    splitPane.setDividerLocation(0.74);
                }
            }
        });
    }

    private void applySystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Keep default look and feel if system LAF is unavailable.
        }
    }
}

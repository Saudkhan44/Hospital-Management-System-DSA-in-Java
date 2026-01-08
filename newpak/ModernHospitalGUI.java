package newpak;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ModernHospitalGUI extends JFrame {

    private HospitalSystemLogic system;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JComboBox<String> opdDeptCombo = new JComboBox<>(); // Initialize here
    // Modern & High-Contrast Color Palette (Main Colors)
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);       // Bright blue
    private final Color PRIMARY_DARK = new Color(31, 97, 141);         // Darker blue for sidebar
    private final Color ACCENT_COLOR = new Color(231, 76, 60);         // Red for alerts
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);       // Green for success
    private final Color WARNING_COLOR = new Color(241, 196, 15);       // Yellow for warnings
    private final Color PURPLE_COLOR = new Color(155, 89, 182);        // Purple
    private final Color INFO_COLOR = new Color(52, 152, 219);          // Info blue

    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);   // Light grey background
    private final Color CARD_COLOR = new Color(255, 255, 255);         // White for panels/cards

    private final Color TEXT_PRIMARY = new Color(33, 33, 33);          // Dark grey for text
    private final Color TEXT_SECONDARY = new Color(117, 117, 117);     // Medium grey for secondary text
    private final Color BORDER_COLOR = new Color(224, 224, 224);       // Light grey for borders

    // Components
    private DefaultTableModel patientTableModel, emergencyTableModel, opdTableModel;
    private JTable patientTable, emergencyTable, opdTable;
    private JTextArea medicalHistoryArea, reportArea;
    private JList<String> departmentList;
    private DefaultListModel<String> departmentListModel;

    public ModernHospitalGUI() {
        system = HospitalSystemLogic.getInstance();

        // Frame setup
        setTitle("Modern Hospital Management System");
        setSize(1250, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initializeUI();
        setVisible(true);
    }

    private void initializeUI() {
        // Main container with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create all panels
        mainPanel.add(createDashboardPanel(), "DASHBOARD");
        mainPanel.add(createPatientManagementPanel(), "PATIENTS");
        mainPanel.add(createEmergencyPanel(), "EMERGENCY");
        mainPanel.add(createOPDPanel(), "OPD");
        mainPanel.add(createMedicalHistoryPanel(), "HISTORY");
        mainPanel.add(createDepartmentsPanel(), "DEPARTMENTS"); // No parameter needed now
        mainPanel.add(createReportsPanel(), "REPORTS");

        // Create modern sidebar
        JPanel sidebar = createSidebarPanel();

        // Set layout
        setLayout(new BorderLayout());
        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        // Start with dashboard
        cardLayout.show(mainPanel, "DASHBOARD");
    }

    private void refreshOPDDepartmentCombo() {
        opdDeptCombo.removeAllItems();
        List<String> departments = system.getAllDepartments();
        for (String dept : departments) {
            opdDeptCombo.addItem(dept);
        }
    }

    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(PRIMARY_DARK);
        sidebar.setPreferredSize(new Dimension(180, getHeight()));

        // Logo/Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        headerPanel.setBackground(PRIMARY_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));

        JLabel logo = new JLabel("HOSPITAL");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        headerPanel.add(logo);

        sidebar.add(headerPanel);

        // Menu Panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(PRIMARY_DARK);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        // Menu items
        String[][] menuItems = {
                {"", "Dashboard", "DASHBOARD"},
                {"", "Patients", "PATIENTS"},
                {"", "Emergency", "EMERGENCY"},
                {"", "OPD", "OPD"},
                {"", "History", "HISTORY"},
                {"", "Departments", "DEPARTMENTS"},
                {"", "Reports", "REPORTS"}
        };

        for (String[] item : menuItems) {
            JPanel menuItem = createMenuItem(item[1], item[2]);
            menuPanel.add(menuItem);
            menuPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        }

        menuPanel.add(Box.createVerticalGlue());

        // Exit button
        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        exitPanel.setBackground(PRIMARY_DARK);
        JButton exitBtn = createMenuButton("Exit", ACCENT_COLOR, new Dimension(140, 35));
        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to exit?", "Exit System",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        exitPanel.add(exitBtn);

        sidebar.add(menuPanel);
        sidebar.add(exitPanel);

        return sidebar;
    }

    private JPanel createMenuItem(String text, String panelName) {
        JPanel itemPanel = new JPanel(new BorderLayout(8, 0));
        itemPanel.setBackground(PRIMARY_DARK);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        itemPanel.setPreferredSize(new Dimension(170, 35));

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textLabel.setForeground(Color.WHITE);

        itemPanel.add(textLabel, BorderLayout.CENTER);

        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, panelName);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                itemPanel.setBackground(PRIMARY_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                itemPanel.setBackground(PRIMARY_DARK);
            }
        });

        return itemPanel;
    }

    private JButton createMenuButton(String text, Color color, Dimension size) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setForeground(Color.BLACK);
        btn.setBackground(color);
        btn.setPreferredSize(size);
        btn.setMaximumSize(size);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });

        return btn;
    }
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ---------------- Header ----------------
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);

        JLabel header = new JLabel("Dashboard Overview");
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setForeground(TEXT_PRIMARY);

        JLabel dateLabel = new JLabel(new SimpleDateFormat("MMM dd, yyyy").format(new Date()));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(TEXT_SECONDARY);

        headerPanel.add(header, BorderLayout.WEST);
        headerPanel.add(dateLabel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // ---------------- Stats Cards ----------------
        JPanel statsPanel = new JPanel(new GridBagLayout());
        statsPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        statsPanel.add(createModernStatCard("Total Patients", system.getTotalPatients(), PRIMARY_COLOR, "\uD83D\uDC64"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        statsPanel.add(createModernStatCard("Emergency", system.getEmergencyCount(), ACCENT_COLOR, "\u26A0"), gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        statsPanel.add(createModernStatCard("OPD Patients", system.getOPDCount(), SUCCESS_COLOR, "\uD83C\uDFE5"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        statsPanel.add(createModernStatCard("Departments", system.getDepartmentCount(), PURPLE_COLOR, "\uD83D\uDCCB"), gbc);

        panel.add(statsPanel, BorderLayout.CENTER);

        // ---------------- Quick Actions ----------------
        JPanel actionsPanel = new JPanel(new GridBagLayout());
        actionsPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints actionGbc = new GridBagConstraints();
        actionGbc.insets = new Insets(0, 10, 0, 10);
        actionGbc.fill = GridBagConstraints.HORIZONTAL;
        actionGbc.weightx = 1.0;

        actionGbc.gridx = 0; actionGbc.gridy = 0;
        actionsPanel.add(createQuickActionButton("Add Patient", PRIMARY_COLOR, e -> showAddPatientDialog()), actionGbc);
        actionGbc.gridx = 1;
        actionsPanel.add(createQuickActionButton("Emergency", ACCENT_COLOR, e -> showAddEmergencyDialog()), actionGbc);
        actionGbc.gridx = 2;
        actionsPanel.add(createQuickActionButton("OPD Registration", SUCCESS_COLOR, e -> showAddOPDDialog()), actionGbc);

        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createModernStatCard(String title, int value, Color color, String icon) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200, 80), 0, true),
                BorderFactory.createEmptyBorder(35, 20, 20, 20)
        ));
        card.setPreferredSize(new Dimension(280, 160));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ---------------- Emoji at the top ----------------
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 42));
        iconLabel.setForeground(color);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // ---------------- Title in the middle ----------------
        JLabel titleLabel = new JLabel(title);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(TEXT_SECONDARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        // ---------------- Value at the bottom ----------------
        JLabel valueLabel = new JLabel(String.valueOf(value));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(TEXT_PRIMARY);

        // Add components to card
        card.add(iconLabel);
        card.add(titleLabel);
        card.add(Box.createVerticalGlue());
        card.add(valueLabel);

        return card;
    }



    private JButton createQuickActionButton(String text, Color color, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.BLACK);
        btn.setBackground(color);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 1, true), // Rounded
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 40));
        btn.addActionListener(action);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });

        return btn;
    }

    private JPanel createPatientManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header with action buttons
        JPanel headerPanel = new JPanel(new BorderLayout());

        JLabel header = new JLabel("Patient Management");
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(TEXT_PRIMARY);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton addBtn = createActionButton("Add Patient", SUCCESS_COLOR, e -> showAddPatientDialog(), new Dimension(110, 32));
        JButton refreshBtn = createActionButton("Refresh", INFO_COLOR, e -> refreshPatientTable(), new Dimension(90, 32));
        JButton searchBtn = createActionButton("Search", WARNING_COLOR, e -> showSearchPatientDialog(), new Dimension(90, 32));

        buttonPanel.add(searchBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(addBtn);

        headerPanel.add(header, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Patient Table with action buttons
        String[] columns = {"ID", "Name", "Age", "Gender", "Disease", "Contact", "Actions"};
        patientTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return column == 6 ? JPanel.class : String.class;
            }
        };

        patientTable = new JTable(patientTableModel);
        patientTable.setRowHeight(35);
        patientTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        patientTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        patientTable.getTableHeader().setBackground(PRIMARY_COLOR);
        patientTable.getTableHeader().setForeground(Color.WHITE);

        // Set column widths
        patientTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        patientTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        patientTable.getColumnModel().getColumn(2).setPreferredWidth(40);
        patientTable.getColumnModel().getColumn(3).setPreferredWidth(60);
        patientTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        patientTable.getColumnModel().getColumn(5).setPreferredWidth(90);
        patientTable.getColumnModel().getColumn(6).setPreferredWidth(120);

        // Custom renderer for action buttons
        patientTable.getColumnModel().getColumn(6).setCellRenderer(new TableCellButtonRenderer());
        patientTable.getColumnModel().getColumn(6).setCellEditor(new TableCellButtonEditor());

        JScrollPane scrollPane = new JScrollPane(patientTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scrollPane.getViewport().setBackground(CARD_COLOR);

        panel.add(scrollPane, BorderLayout.CENTER);

        // Load initial data
        refreshPatientTable();

        return panel;
    }

    // Custom cell renderer for action buttons
    class TableCellButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private JButton editBtn;
        private JButton deleteBtn;

        public TableCellButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 3));
            setBackground(CARD_COLOR);

            editBtn = new JButton("Edit");
            editBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            editBtn.setBackground(new Color(52, 152, 219));
            editBtn.setForeground(Color.BLACK);
            editBtn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(41, 128, 185), 1),
                    BorderFactory.createEmptyBorder(3, 8, 3, 8)
            ));
            editBtn.setFocusPainted(false);
            editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            deleteBtn = new JButton("Delete");
            deleteBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            deleteBtn.setBackground(new Color(231, 76, 60));
            deleteBtn.setForeground(Color.BLACK);
            deleteBtn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(192, 57, 43), 1),
                    BorderFactory.createEmptyBorder(3, 8, 3, 8)
            ));
            deleteBtn.setFocusPainted(false);
            deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            add(editBtn);
            add(deleteBtn);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Custom cell editor for action buttons
    class TableCellButtonEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private JPanel panel;
        private JButton editBtn;
        private JButton deleteBtn;
        private int currentRow;

        public TableCellButtonEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 3));
            panel.setBackground(CARD_COLOR);

            editBtn = new JButton("Edit");
            editBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            editBtn.setBackground(new Color(52, 152, 219));
            editBtn.setForeground(Color.BLACK);
            editBtn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(41, 128, 185), 1),
                    BorderFactory.createEmptyBorder(3, 8, 3, 8)
            ));
            editBtn.setFocusPainted(false);
            editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            editBtn.addActionListener(e -> {
                int row = patientTable.convertRowIndexToModel(currentRow);
                Patient patient = system.getAllPatients().get(row);
                showEditPatientDialog(patient);
                fireEditingStopped();
            });

            deleteBtn = new JButton("Delete");
            deleteBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            deleteBtn.setBackground(new Color(231, 76, 60));
            deleteBtn.setForeground(Color.BLACK);
            deleteBtn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(192, 57, 43), 1),
                    BorderFactory.createEmptyBorder(3, 8, 3, 8)
            ));
            deleteBtn.setFocusPainted(false);
            deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            deleteBtn.addActionListener(e -> {
                int row = patientTable.convertRowIndexToModel(currentRow);
                Patient patient = system.getAllPatients().get(row);
                int confirm = JOptionPane.showConfirmDialog(ModernHospitalGUI.this,
                        "Delete patient " + patient.getPatientName() + "?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    system.deletePatient(patient.getPatientId());
                    refreshPatientTable();
                }
                fireEditingStopped();
            });

            panel.add(editBtn);
            panel.add(deleteBtn);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            currentRow = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }

    private JButton createActionButton(String text, Color color, ActionListener action, Dimension size) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setForeground(Color.BLACK);
        btn.setBackground(color);
        btn.setPreferredSize(size);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });

        return btn;
    }

    private void refreshPatientTable() {
        patientTableModel.setRowCount(0);
        List<Patient> patients = system.getAllPatients();

        for (Patient patient : patients) {
            patientTableModel.addRow(new Object[]{
                    patient.getPatientId(),
                    patient.getPatientName(),
                    patient.getPatientAge(),
                    patient.getGender(),
                    patient.getDisease(),
                    patient.getContact(),
                    null
            });
        }
    }

    private void showAddPatientDialog() {
        JDialog dialog = createDialog("Add New Patient", 400, 380);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        formPanel.setBackground(CARD_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create text fields with compact size
        JTextField idField = new JTextField();
        idField.setPreferredSize(new Dimension(180, 28));
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(180, 28));
        JTextField ageField = new JTextField();
        ageField.setPreferredSize(new Dimension(180, 28));
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderCombo.setPreferredSize(new Dimension(180, 28));
        JTextField diseaseField = new JTextField();
        diseaseField.setPreferredSize(new Dimension(180, 28));
        JTextField contactField = new JTextField();
        contactField.setPreferredSize(new Dimension(180, 28));

        // Add fields to form
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createFieldLabel("Patient ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createFieldLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createFieldLabel("Age:"), gbc);
        gbc.gridx = 1;
        formPanel.add(ageField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createFieldLabel("Gender:"), gbc);
        gbc.gridx = 1;
        formPanel.add(genderCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(createFieldLabel("Disease:"), gbc);
        gbc.gridx = 1;
        formPanel.add(diseaseField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(createFieldLabel("Contact:"), gbc);
        gbc.gridx = 1;
        formPanel.add(contactField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setBackground(CARD_COLOR);

        JButton saveBtn = createActionButton("Save", SUCCESS_COLOR, e -> {
            try {
                boolean success = system.addPatient(
                        Integer.parseInt(idField.getText()),
                        nameField.getText(),
                        Integer.parseInt(ageField.getText()),
                        (String) genderCombo.getSelectedItem(),
                        diseaseField.getText(),
                        contactField.getText()
                );

                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Patient added successfully!");
                    refreshPatientTable();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Patient ID already exists!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers!");
            }
        }, new Dimension(90, 28));

        JButton cancelBtn = createActionButton("Cancel", TEXT_SECONDARY, e -> dialog.dispose(), new Dimension(90, 28));

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    private void showEditPatientDialog(Patient patient) {
        JDialog dialog = createDialog("Edit Patient", 400, 380);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        formPanel.setBackground(CARD_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField idField = new JTextField(String.valueOf(patient.getPatientId()));
        idField.setPreferredSize(new Dimension(180, 28));
        idField.setEditable(false);
        idField.setBackground(new Color(240, 240, 240));

        JTextField nameField = new JTextField(patient.getPatientName());
        nameField.setPreferredSize(new Dimension(180, 28));

        JTextField ageField = new JTextField(String.valueOf(patient.getPatientAge()));
        ageField.setPreferredSize(new Dimension(180, 28));

        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderCombo.setSelectedItem(patient.getGender());
        genderCombo.setPreferredSize(new Dimension(180, 28));

        JTextField diseaseField = new JTextField(patient.getDisease());
        diseaseField.setPreferredSize(new Dimension(180, 28));

        JTextField contactField = new JTextField(patient.getContact());
        contactField.setPreferredSize(new Dimension(180, 28));

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createFieldLabel("Patient ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createFieldLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createFieldLabel("Age:"), gbc);
        gbc.gridx = 1;
        formPanel.add(ageField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createFieldLabel("Gender:"), gbc);
        gbc.gridx = 1;
        formPanel.add(genderCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(createFieldLabel("Disease:"), gbc);
        gbc.gridx = 1;
        formPanel.add(diseaseField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(createFieldLabel("Contact:"), gbc);
        gbc.gridx = 1;
        formPanel.add(contactField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setBackground(CARD_COLOR);

        JButton saveBtn = createActionButton("Save", SUCCESS_COLOR, e -> {
            try {
                boolean success = system.updatePatient(
                        patient.getPatientId(),
                        nameField.getText(),
                        Integer.parseInt(ageField.getText()),
                        (String) genderCombo.getSelectedItem(),
                        diseaseField.getText(),
                        contactField.getText()
                );

                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Patient updated successfully!");
                    refreshPatientTable();
                    dialog.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers!");
            }
        }, new Dimension(100, 28));

        JButton cancelBtn = createActionButton("Cancel", TEXT_SECONDARY, e -> dialog.dispose(), new Dimension(90, 28));

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showSearchPatientDialog() {
        String idStr = JOptionPane.showInputDialog(this, "Enter Patient ID to search:");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idStr.trim());
                Patient patient = system.searchPatient(id);
                if (patient != null) {
                    JOptionPane.showMessageDialog(this,
                            "<html><b>Patient Found:</b><br><br>" +
                                    "<b>ID:</b> " + patient.getPatientId() + "<br>" +
                                    "<b>Name:</b> " + patient.getPatientName() + "<br>" +
                                    "<b>Age:</b> " + patient.getPatientAge() + "<br>" +
                                    "<b>Gender:</b> " + patient.getGender() + "<br>" +
                                    "<b>Disease:</b> " + patient.getDisease() + "<br>" +
                                    "<b>Contact:</b> " + patient.getContact() + "</html>",
                            "Patient Information", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Patient not found!");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Patient ID!");
            }
        }
    }

    private JDialog createDialog(String title, int width, int height) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        return dialog;
    }

    private JPanel createEmergencyPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header
        JLabel header = new JLabel("Emergency Department");
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(ACCENT_COLOR);
        panel.add(header, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(320);
        splitPane.setBackground(BACKGROUND_COLOR);

        // Left: Add Emergency Form
        splitPane.setLeftComponent(createEmergencyFormPanel());

        // Right: Emergency Queue
        splitPane.setRightComponent(createEmergencyQueuePanel());

        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createEmergencyFormPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel("Add Emergency Case");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(TEXT_PRIMARY);
        panel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField idField = new JTextField();
        idField.setPreferredSize(new Dimension(160, 28));
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(160, 28));
        JComboBox<String> priorityCombo = new JComboBox<>(
                new String[]{"HIGH (Critical)", "MEDIUM (Urgent)", "LOW (Stable)"}
        );
        priorityCombo.setPreferredSize(new Dimension(160, 28));
        JTextField conditionField = new JTextField();
        conditionField.setPreferredSize(new Dimension(160, 28));

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createFieldLabel("Patient ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createFieldLabel("Patient Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createFieldLabel("Priority:"), gbc);
        gbc.gridx = 1;
        formPanel.add(priorityCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createFieldLabel("Condition:"), gbc);
        gbc.gridx = 1;
        formPanel.add(conditionField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        buttonPanel.setBackground(CARD_COLOR);

        JButton addBtn = createActionButton("Add Emergency", ACCENT_COLOR, e -> {
            try {
                // ✅ CORRECT PRIORITY MAPPING
                int priority;
                switch (priorityCombo.getSelectedIndex()) {
                    case 0 -> priority = 3; // HIGH
                    case 1 -> priority = 2; // MEDIUM
                    case 2 -> priority = 1; // LOW
                    default -> priority = 1;
                }

                boolean success = system.addEmergencyPatient(
                        Integer.parseInt(idField.getText()),
                        nameField.getText(),
                        priority,
                        conditionField.getText()
                );

                if (success) {
                    JOptionPane.showMessageDialog(this, "Emergency case added!");
                    refreshEmergencyTable();
                    idField.setText("");
                    nameField.setText("");
                    conditionField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Emergency queue is full!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please check input!");
            }
        }, new Dimension(130, 32));

        buttonPanel.add(addBtn);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }


    private JPanel createEmergencyQueuePanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel("Emergency Queue");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(TEXT_PRIMARY);

        JButton treatBtn = createActionButton("Treat Next", SUCCESS_COLOR, e -> {
            EmergencyPatient patient = system.treatNextEmergencyPatient();
            if (patient != null) {
                JOptionPane.showMessageDialog(this,
                        String.format("Treated Patient:<br><b>Name:</b> %s<br><b>Priority:</b> %d<br><b>Condition:</b> %s",
                                patient.getPatientName(), patient.getPriority(), patient.getCondition()),
                        "Treatment Complete", JOptionPane.INFORMATION_MESSAGE);
                refreshEmergencyTable();
            } else {
                JOptionPane.showMessageDialog(this, "No emergency patients!");
            }
        }, new Dimension(110, 32));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_COLOR);
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(treatBtn, BorderLayout.EAST);

        // Emergency Table
        String[] columns = {"Priority", "ID", "Name", "Condition", "Arrival Time"};
        emergencyTableModel = new DefaultTableModel(columns, 0);

        emergencyTable = new JTable(emergencyTableModel);
        emergencyTable.setRowHeight(28);
        emergencyTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        refreshEmergencyTable();

        JScrollPane scrollPane = new JScrollPane(emergencyTable);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void refreshEmergencyTable() {
        emergencyTableModel.setRowCount(0);
        List<EmergencyPatient> patients = system.getAllEmergencyPatients();

        for (EmergencyPatient p : patients) {
            String priority = "";
            switch(p.getPriority()) {
                case 3: priority = "HIGH"; break;
                case 2: priority = "MEDIUM"; break;
                case 1: priority = "LOW"; break;
            }

            emergencyTableModel.addRow(new Object[]{
                    priority,
                    p.getPatientId(),
                    p.getPatientName(),
                    p.getCondition(),
                    new Date(p.getArrivalTime())
            });
        }
    }

    private JPanel createOPDPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel header = new JLabel("Outpatient Department");
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(SUCCESS_COLOR);
        panel.add(header, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);

        splitPane.setLeftComponent(createOPDFormPanel());
        splitPane.setRightComponent(createOPDQueuePanel());

        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createOPDFormPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel("OPD Registration");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(TEXT_PRIMARY);
        panel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField idField = new JTextField();
        idField.setPreferredSize(new Dimension(160, 28));
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(160, 28));
        // Use the class-level opdDeptCombo
        refreshOPDDepartmentCombo(); // Initialize with current departments
        opdDeptCombo.setPreferredSize(new Dimension(160, 28));

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createFieldLabel("Patient ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createFieldLabel("Patient Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createFieldLabel("Department:"), gbc);
        gbc.gridx = 1;
        formPanel.add(opdDeptCombo, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        buttonPanel.setBackground(CARD_COLOR);

        JButton registerBtn = createActionButton("Register", SUCCESS_COLOR, e -> {
            try {
                boolean success = system.addOPDPatient(
                        Integer.parseInt(idField.getText()),
                        nameField.getText(),
                        (String) opdDeptCombo.getSelectedItem()
                );

                if (success) {
                    JOptionPane.showMessageDialog(this, "OPD patient registered!");
                    refreshOPDTable();
                    idField.setText("");
                    nameField.setText("");
                    // Refresh department list to show updated patient count
                    refreshDepartmentList();
                } else {
                    JOptionPane.showMessageDialog(this, "Registration failed!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please check input!");
            }
        }, new Dimension(110, 32));

        buttonPanel.add(registerBtn);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createOPDQueuePanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel("OPD Waiting Queue");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(TEXT_PRIMARY);

        JButton treatBtn = createActionButton("Call Next", SUCCESS_COLOR, e -> {
            OPDPatient patient = system.treatNextOPDPatient();
            if (patient != null) {
                JOptionPane.showMessageDialog(this,
                        String.format("Now treating:<br><b>Token:</b> %d<br><b>Name:</b> %s<br><b>Department:</b> %s",
                                patient.getTokenNumber(), patient.getPatientName(), patient.getDepartment()),
                        "Patient Called", JOptionPane.INFORMATION_MESSAGE);
                refreshOPDTable();
            } else {
                JOptionPane.showMessageDialog(this, "No OPD patients waiting!");
            }
        }, new Dimension(110, 32));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_COLOR);
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(treatBtn, BorderLayout.EAST);

        // OPD Table
        String[] columns = {"Token", "ID", "Name", "Department"};
        opdTableModel = new DefaultTableModel(columns, 0);

        opdTable = new JTable(opdTableModel);
        opdTable.setRowHeight(28);
        opdTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        refreshOPDTable();

        JScrollPane scrollPane = new JScrollPane(opdTable);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void refreshOPDTable() {
        opdTableModel.setRowCount(0);
        List<OPDPatient> patients = system.getAllOPDPatients();

        for (OPDPatient p : patients) {
            opdTableModel.addRow(new Object[]{
                    p.getTokenNumber(),
                    p.getPatientId(),
                    p.getPatientName(),
                    p.getDepartment()
            });
        }
    }

    private void showAddEmergencyDialog() {
        cardLayout.show(mainPanel, "EMERGENCY");
    }

    private void showAddOPDDialog() {
        cardLayout.show(mainPanel, "OPD");
    }

    private JPanel createMedicalHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel header = new JLabel("Medical History Records");
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(PURPLE_COLOR);
        panel.add(header, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(8, 8));
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Add Record Panel
        JPanel addPanel = new JPanel(new BorderLayout(6, 6));
        addPanel.setBackground(CARD_COLOR);
        addPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        addPanel.setPreferredSize(new Dimension(0, 70));

        JLabel addLabel = new JLabel("Add New Medical Record:");
        addLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addLabel.setForeground(TEXT_PRIMARY);

        JTextField recordField = new JTextField();
        recordField.setPreferredSize(new Dimension(300, 28));
        JButton addBtn = createActionButton("Add", PURPLE_COLOR, e -> {
            String record = recordField.getText().trim();
            if (!record.isEmpty()) {
                if (system.addMedicalRecord(record)) {
                    JOptionPane.showMessageDialog(this, "Medical record added!");
                    recordField.setText("");
                    refreshMedicalHistory();
                } else {
                    JOptionPane.showMessageDialog(this, "Medical history storage is full!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Record cannot be empty!");
            }
        }, new Dimension(70, 28));

        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.setBackground(CARD_COLOR);
        inputPanel.add(recordField, BorderLayout.CENTER);
        inputPanel.add(addBtn, BorderLayout.EAST);

        addPanel.add(addLabel, BorderLayout.NORTH);
        addPanel.add(inputPanel, BorderLayout.CENTER);

        // History Display
        JPanel historyPanel = new JPanel(new BorderLayout(8, 8));
        historyPanel.setBackground(CARD_COLOR);
        historyPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel historyLabel = new JLabel("Medical History:");
        historyLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        historyLabel.setForeground(TEXT_PRIMARY);

        medicalHistoryArea = new JTextArea();
        medicalHistoryArea.setEditable(false);
        medicalHistoryArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        medicalHistoryArea.setBackground(new Color(250, 250, 250));

        refreshMedicalHistory();

        JScrollPane scrollPane = new JScrollPane(medicalHistoryArea);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(CARD_COLOR);

        JButton refreshBtn = createActionButton("Refresh", PRIMARY_COLOR, e -> refreshMedicalHistory(), new Dimension(90, 28));
        JButton clearBtn = createActionButton("Clear", ACCENT_COLOR, e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Clear all medical history?", "Confirm Clear",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                system.clearMedicalHistory();
                refreshMedicalHistory();
            }
        }, new Dimension(90, 28));

        buttonPanel.add(refreshBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(clearBtn);

        historyPanel.add(historyLabel, BorderLayout.NORTH);
        historyPanel.add(scrollPane, BorderLayout.CENTER);
        historyPanel.add(buttonPanel, BorderLayout.SOUTH);

        contentPanel.add(addPanel, BorderLayout.NORTH);
        contentPanel.add(historyPanel, BorderLayout.CENTER);

        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private void refreshMedicalHistory() {
        medicalHistoryArea.setText("");
        List<String> records = system.getAllMedicalRecords();

        if (records.isEmpty()) {
            medicalHistoryArea.setText("No medical records available.");
        } else {
            for (int i = records.size() - 1; i >= 0; i--) {
                medicalHistoryArea.append(records.get(i) + "\n\n");
            }
        }
    }


    private JPanel createDepartmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ---------------- Header ----------------
        JLabel header = new JLabel("Hospital Departments");
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(WARNING_COLOR);
        panel.add(header, BorderLayout.NORTH);

        // ---------------- Split Pane ----------------
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(250);

        // ---------------- Left Panel: Operations ----------------
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(CARD_COLOR);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        // ---------------- Add Department ----------------
        JLabel addLabel = new JLabel("Add New Department:");
        addLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addLabel.setForeground(TEXT_PRIMARY);
        addLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField addField = new JTextField();
        addField.setMaximumSize(new Dimension(120, 22));
        addField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        addField.setAlignmentX(Component.LEFT_ALIGNMENT);
        addField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JButton addBtn = createActionButton("Add", WARNING_COLOR, e -> {
            String deptName = addField.getText().trim();
            if (!deptName.isEmpty()) {
                if (system.addDepartment(deptName)) {
                    JOptionPane.showMessageDialog(this, "Department added!");
                    addField.setText("");
                    refreshDepartmentList();
                    refreshOPDDepartmentCombo(); // Update OPD combo too
                } else {
                    JOptionPane.showMessageDialog(this, "Department already exists!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Department name cannot be empty!");
            }
        }, new Dimension(120, 28));
        addBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(addLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(addField);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        leftPanel.add(addBtn);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // ---------------- Remove Department ----------------
        JLabel removeLabel = new JLabel("Remove Department:");
        removeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        removeLabel.setForeground(TEXT_PRIMARY);
        removeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField removeField = new JTextField();
        removeField.setMaximumSize(new Dimension(120, 22));
        removeField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        removeField.setAlignmentX(Component.LEFT_ALIGNMENT);
        removeField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JButton removeBtn = createActionButton("Remove", ACCENT_COLOR, e -> {
            String deptName = removeField.getText().trim();
            if (!deptName.isEmpty()) {
                if (system.removeDepartment(deptName)) {
                    JOptionPane.showMessageDialog(this, "Department removed!");
                    removeField.setText("");
                    refreshDepartmentList();
                    refreshOPDDepartmentCombo(); // Update OPD combo too
                } else {
                    JOptionPane.showMessageDialog(this, "Department not found!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Enter department name to remove!");
            }
        }, new Dimension(120, 28));
        removeBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(removeLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(removeField);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        leftPanel.add(removeBtn);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // ---------------- Update Department ----------------
        JLabel updateLabel = new JLabel("Update Department Name:");
        updateLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        updateLabel.setForeground(TEXT_PRIMARY);
        updateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField oldNameField = new JTextField();
        oldNameField.setMaximumSize(new Dimension(120, 22));
        oldNameField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        oldNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        oldNameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JTextField newNameField = new JTextField();
        newNameField.setMaximumSize(new Dimension(120, 22));
        newNameField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        newNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        newNameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JButton updateBtn = createActionButton("Update", PRIMARY_COLOR, e -> {
            String oldName = oldNameField.getText().trim();
            String newName = newNameField.getText().trim();
            if (!oldName.isEmpty() && !newName.isEmpty()) {
                if (system.updateDepartmentName(oldName, newName)) {
                    JOptionPane.showMessageDialog(this, "Department name updated!");
                    oldNameField.setText("");
                    newNameField.setText("");
                    refreshDepartmentList();
                    refreshOPDDepartmentCombo(); // Update OPD combo too
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed! Check names.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Fill both old and new department names!");
            }
        }, new Dimension(120, 28));
        updateBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(updateLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(oldNameField);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(newNameField);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        leftPanel.add(updateBtn);

        // ---------------- Right Panel: Department List ----------------
        JPanel rightPanel = new JPanel(new BorderLayout(8, 8));
        rightPanel.setBackground(CARD_COLOR);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel listLabel = new JLabel("Department List:");
        listLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        listLabel.setForeground(TEXT_PRIMARY);

        departmentListModel = new DefaultListModel<>();
        departmentList = new JList<>(departmentListModel);
        departmentList.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        departmentList.setBackground(new Color(250, 250, 250));
        departmentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        refreshDepartmentList();

        JScrollPane scrollPane = new JScrollPane(departmentList);

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setBackground(CARD_COLOR);
        JLabel statsLabel = new JLabel("Total Departments: " + system.getDepartmentCount());
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statsLabel.setForeground(TEXT_SECONDARY);
        statsPanel.add(statsLabel);

        rightPanel.add(listLabel, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.add(statsPanel, BorderLayout.SOUTH);

        // ---------------- Combine Left & Right ----------------
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    // ---------------- Refresh Department List ----------------
    private void refreshDepartmentList() {
        departmentListModel.clear();
        List<String> departments = system.getDepartmentsWithStats();
        for (String dept : departments) {
            departmentListModel.addElement(dept);
        }
    }


    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel header = new JLabel("System Reports & Analytics");
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(PRIMARY_COLOR);
        panel.add(header, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(8, 8));
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Report display
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        reportArea.setBackground(new Color(250, 250, 250));
        reportArea.setText(system.exportSystemReport());

        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton refreshBtn = createActionButton("Refresh", PRIMARY_COLOR, e -> {
            reportArea.setText(system.exportSystemReport());
        }, new Dimension(90, 32));

        JButton exportBtn = createActionButton("Export", SUCCESS_COLOR, e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Report");
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(this, "Report export feature would save to: " +
                        fileChooser.getSelectedFile().getPath());
            }
        }, new Dimension(90, 32));

        JButton printBtn = createActionButton("Print", WARNING_COLOR, e -> {
            try {
                reportArea.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing failed!");
            }
        }, new Dimension(90, 32));

        buttonPanel.add(refreshBtn);
        buttonPanel.add(exportBtn);
        buttonPanel.add(printBtn);

        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ModernHospitalGUI();
            }
        });
    }
}
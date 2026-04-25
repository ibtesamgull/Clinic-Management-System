package clinicmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;

public class AdminDashboard extends JFrame {

    private JPanel mainPanel;
    private String username, role;

    public AdminDashboard(String username, String role) {
        this.role = role;
        this.username = username;

        setTitle("Admin Dashboard - Clinic Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // SIDE PANEL
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setPreferredSize(new Dimension(220, 0));
        sidePanel.setBackground(new Color(44, 62, 80));

        // LOGO
        ImageIcon icon = new ImageIcon(getClass().getResource("/icons/" + "logo.png"));
        Image image = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(image));
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        sidePanel.add(logoLabel);

        // USERNAME DISPLAY
        JLabel userLabel = new JLabel("Welcome " + role + " ! " + username.toUpperCase());
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        userLabel.setForeground(Color.WHITE);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userLabel.setMaximumSize(new Dimension(200, 30));
        userLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

        sidePanel.add(userLabel);
        sidePanel.revalidate();
        sidePanel.repaint();

        //Dashboard Main Button
        JButton dashboardBtn = createSidebarButton("Dashboard");

        // Doctors Main Button
        JButton doctorsBtn = createSidebarButton("Doctors");
        JPanel doctorsSubPanel = createSubPanel();
        JButton doctorListBtn = createSubButton("+ Doctor List");
        JButton addDoctorBtn = createSubButton("+ Add Doctor");
        doctorsSubPanel.add(doctorListBtn);
        doctorsSubPanel.add(addDoctorBtn);
        doctorsSubPanel.setVisible(false);
        doctorsBtn.addActionListener(e -> toggleSubPanel(doctorsSubPanel));

        // Patients Main Button
        JButton patientsBtn = createSidebarButton("Patients");
        JPanel patientsSubPanel = createSubPanel();
        JButton addPatientBtn = createSubButton("+ Add New Patient");
        JButton patientListBtn = createSubButton("+ Patient List");
        JButton viewRecordsBtn = createSubButton("+ View Medical Records");

        patientsSubPanel.add(addPatientBtn);
        patientsSubPanel.add(patientListBtn);
        patientsSubPanel.add(viewRecordsBtn);
        patientsSubPanel.setVisible(false);
        patientsBtn.addActionListener(e -> toggleSubPanel(patientsSubPanel));

        // Appointments Main Button
        JButton appointmentBtn = createSidebarButton("Appointment");
        JPanel appointmentsSubPanel = createSubPanel();
        JButton appointmentListBtn = createSubButton("+ Appointment List");
        JButton addAppointmentBtn = createSubButton("+ Add Appointment");
        appointmentsSubPanel.add(appointmentListBtn);
        appointmentsSubPanel.add(addAppointmentBtn);
        appointmentsSubPanel.setVisible(false);
        appointmentBtn.addActionListener(e -> toggleSubPanel(appointmentsSubPanel));

        // Prescription Main Button
        JButton prescriptionsBtn = createSidebarButton("Prescriptions");
        JPanel prescriptionsSubPanel = createSubPanel();
        JButton prescriptionsListBtn = createSubButton("+ Prescription List");
        JButton addprescriptionBtn = createSubButton("+ Add Prescription");
        prescriptionsSubPanel.add(prescriptionsListBtn);
        prescriptionsSubPanel.add(addprescriptionBtn);
        prescriptionsSubPanel.setVisible(false);
        prescriptionsBtn.addActionListener(e -> toggleSubPanel(prescriptionsSubPanel));

        // Billing Main Button
        JButton billingBtn = createSidebarButton("Billing");
        JPanel billingSubPanel = createSubPanel();
        JButton generateBillBtn = createSubButton("+ Generate Bill");
        JButton billsListBtn = createSubButton("+ Bills List");
        billingSubPanel.add(generateBillBtn);
        billingSubPanel.add(billsListBtn);
        billingSubPanel.setVisible(false);
        billingBtn.addActionListener(e -> toggleSubPanel(billingSubPanel));

        //Employee Main Button
        JButton employeesBtn = createSidebarButton("Employees");
        JPanel employeesSubPanel = createSubPanel();
        JButton employeesListBtn = createSubButton("+ Employees List");
        JButton addEmployeesBtn = createSubButton("+ Add Employee");
        employeesSubPanel.add(employeesListBtn);
        employeesSubPanel.add(addEmployeesBtn);
        employeesSubPanel.setVisible(false);
        employeesBtn.addActionListener(e -> toggleSubPanel(employeesSubPanel));

        JButton settingsBtn = createSidebarButton("Settings");
        JPanel settingsSubPanel = createSubPanel();
        JButton profileBtn = createSubButton("+ Profile");
        JButton manageUsersBtn = createSubButton("+ Manage Users");
        settingsSubPanel.add(profileBtn);
        settingsSubPanel.add(manageUsersBtn);
        settingsSubPanel.setVisible(false);
        settingsBtn.addActionListener(e -> toggleSubPanel(settingsSubPanel));

        JButton logoutBtn = createSidebarButton("Logout");
        logoutBtn.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(this, "Do You Want To Logout?", "Logout?", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginPage();
            }

        });

        // Add to sidebar
        sidePanel.add(Box.createVerticalStrut(15));
        sidePanel.add(dashboardBtn);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(doctorsBtn);
        sidePanel.add(doctorsSubPanel);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(patientsBtn);
        sidePanel.add(patientsSubPanel);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(appointmentBtn);
        sidePanel.add(appointmentsSubPanel);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(prescriptionsBtn);
        sidePanel.add(prescriptionsSubPanel);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(billingBtn);
        sidePanel.add(billingSubPanel);

        // Add to sidebar
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(employeesBtn);
        sidePanel.add(employeesSubPanel);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(settingsBtn);
        sidePanel.add(settingsSubPanel);
        sidePanel.add(Box.createVerticalStrut(10));

        sidePanel.add(logoutBtn);

        add(sidePanel, BorderLayout.WEST);

        // Sub-button actions
        dashboardBtn.addActionListener(e -> switchPanel("Dashboard"));
        doctorListBtn.addActionListener(e -> switchPanel("Doctor List Panel"));
        addDoctorBtn.addActionListener(e -> switchPanel("Add Doctor Form"));
        patientListBtn.addActionListener(e -> switchPanel("Patient List Panel"));
        addPatientBtn.addActionListener(e -> switchPanel("Add New Patient Form"));
        appointmentListBtn.addActionListener(e -> switchPanel("Appointment List Panel"));
        addAppointmentBtn.addActionListener(e -> switchPanel("Add New Appointment"));
        addEmployeesBtn.addActionListener(e -> switchPanel("Add Employee"));
        employeesListBtn.addActionListener(e -> switchPanel("Employee List"));
        profileBtn.addActionListener(e -> switchPanel("Profile"));
        generateBillBtn.addActionListener(e -> switchPanel("Generate Bill"));
        billsListBtn.addActionListener(e -> switchPanel("Bills List"));
        viewRecordsBtn.addActionListener(e -> switchPanel("View Medical Records"));
        manageUsersBtn.addActionListener(e -> switchPanel("Manage Users"));
        addprescriptionBtn.addActionListener(e -> switchPanel("Add Prescription"));
        prescriptionsListBtn.addActionListener(e -> switchPanel("Prescriptions List"));

        add(sidePanel, BorderLayout.WEST);

        // MAIN PANEL
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        showDashboard();
        add(mainPanel, BorderLayout.CENTER);

        // Based on role, control visibility:
        if (!role.equals("admin")) {
            employeesBtn.setVisible(false);
            employeesSubPanel.setVisible(false);
            manageUsersBtn.setVisible(false);
        }

        if (role.equals("doctor")) {
            appointmentListBtn.setText("+ My Appointments");
            addPatientBtn.setVisible(false);
            addAppointmentBtn.setVisible(false);
            employeesBtn.setVisible(false);
            addDoctorBtn.setVisible(false);
            prescriptionsBtn.setVisible(true);
            billingBtn.setVisible(false);
            billingSubPanel.setVisible(false);
        }

        if (role.equals("receptionist")) {
            prescriptionsBtn.setVisible(false);
            manageUsersBtn.setVisible(false);
            addDoctorBtn.setVisible(false);
            viewRecordsBtn.setVisible(false);

        }

        setVisible(true);
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(52, 73, 94));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        return btn;
    }

    private JButton createSubButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(64, 84, 104));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Short.MAX_VALUE, 35));
        btn.setPreferredSize(new Dimension(200, 35));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 40, 5, 10));
        return btn;
    }

    private JPanel createSubPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(52, 73, 94));
        return panel;
    }

    private void toggleSubPanel(JPanel panel) {
        panel.setVisible(!panel.isVisible());
        revalidate();
        repaint();
    }

    private void switchPanel(String name) {
        mainPanel.removeAll();
        switch (name) {
            case "Dashboard":
                showDashboard();
                break;
            case "Add Doctor Form":
                mainPanel.removeAll();
                mainPanel.add(new AddDoctorPanel(), BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
                break;
            case "Doctor List Panel":
                mainPanel.removeAll();
                mainPanel.add(new DoctorListPanel(), BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
                break;
            case "Add New Patient Form":
                mainPanel.removeAll();
                mainPanel.add(new AddNewPatientPanel(username), BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
                break;
            case "Patient List Panel":
                mainPanel.removeAll();
                mainPanel.add(new PatientListPanel(), BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
                break;

            case "Add New Appointment":
                mainPanel.removeAll();
                mainPanel.add(new AddAppointmentPanel(), BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
                break;
            case "Appointment List Panel":
                mainPanel.removeAll();
                if (role.equals("doctor")) {
                    mainPanel.add(new MyAppointments(username, role), BorderLayout.CENTER);
                } else {
                    mainPanel.add(new AppointmentListPanel(), BorderLayout.CENTER);
                }

                mainPanel.revalidate();
                mainPanel.repaint();
                break;
            case "Add Employee":
                mainPanel.removeAll();
                mainPanel.add(new AddEmployeePanel(), BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
                break;
            case "Employee List":
                mainPanel.removeAll();
                mainPanel.add(new EmployeeListPanel(), BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
                break;
            case "Add Prescription":
                mainPanel.removeAll();
                mainPanel.add(new PrescriptionPanel(), BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
                break;
            case "Prescriptions List":
                mainPanel.removeAll();
                mainPanel.add(new ShowPrescriptionPanel(), BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
                break;
            case "Profile":
                mainPanel.removeAll();
                mainPanel.add(new ProfilePanel(username), BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
                break;
            case "Generate Bill":
                mainPanel.removeAll();
                mainPanel.add(new GenerateBillPanel(), BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
                break;

            case "Bills List":
                mainPanel.removeAll();
                mainPanel.add(new BillsListPanel(), BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
                break;

            case "View Medical Records":
                mainPanel.removeAll();
                mainPanel.add(new MedicalRecordsListPanel(), BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
                break;

            case "Manage Users":
                mainPanel.removeAll();
                mainPanel.add(new ManageUsersPanel(), BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
                break;

            case "Logout":
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?");
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose();
                    new LoginPage();
                    return;
                }
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showDashboard() {
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BorderLayout());

        // TITLE BAR
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Clinic Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JPanel cardsPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Fetch counts from DB
        int totalPatients = getCount("SELECT COUNT(*) FROM patient_tbl");
        int todayAppointments = getCount("SELECT COUNT(*) FROM appointment_tbl WHERE DATE(appointment_date) = CURDATE()");
        int totalAppointments = getCount("SELECT COUNT(*) FROM appointment_tbl");
        int totalDoctors = getCount("SELECT COUNT(*) FROM doctors_tbl");

        // Create cards
        cardsPanel.add(createCard(String.valueOf(totalPatients), "Total Patients"));
        cardsPanel.add(createCard(String.valueOf(todayAppointments), "Today Appointments"));
        cardsPanel.add(createCard(String.valueOf(totalAppointments), "Total Appointments"));
        cardsPanel.add(createCard(String.valueOf(totalDoctors), "Total Doctors"));


        mainContainer.add(headerPanel, BorderLayout.NORTH);
        mainContainer.add(cardsPanel, BorderLayout.CENTER);
        mainPanel.removeAll();
        mainPanel.add(mainContainer, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel createCard(String count, String label) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(100, 120));

        JLabel countLabel = new JLabel(count, SwingConstants.CENTER);
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel textLabel = new JLabel(label, SwingConstants.CENTER);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        card.add(textLabel, BorderLayout.NORTH);
        card.add(countLabel, BorderLayout.CENTER);

        return card;
    }

    private int getCount(String query) {
        int count = 0;
        try {
            Conn conn = new Conn();
            ResultSet rs = conn.stm.executeQuery(query);
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static void main(String[] args) {
        new AdminDashboard("Ebad", "admin").setVisible(true);
    }

}

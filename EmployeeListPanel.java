package clinicmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class AddEmployeePanel extends JPanel {

    private JTextField firstNameField, lastNameField, dobField, emailField, phoneField, addressField;
    private JTextField hireDateField, salaryField, gradeField;
    private JComboBox<String> genderBox, roleBox, statusBox;
    private JButton submitButton;

    public AddEmployeePanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Add New Employee");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        add(title, gbc);
        gbc.gridwidth = 1;

        int row = 1;

        // Row 1: First Name | Last Name
        addLabel("First Name:", gbc, row, 0);
        firstNameField = addTextField(gbc, row, 1);
        addLabel("Last Name:", gbc, row, 2);
        lastNameField = addTextField(gbc, row++, 3);

        // Row 2: Gender | Date of Birth
        addLabel("Gender:", gbc, row, 0);
        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        addComponent(genderBox, gbc, row, 1);
        addLabel("DOB (YYYY-MM-DD):", gbc, row, 2);
        dobField = addTextField(gbc, row++, 3);

        // Row 3: Email | Phone
        addLabel("Email:", gbc, row, 0);
        emailField = addTextField(gbc, row, 1);
        addLabel("Phone Number:", gbc, row, 2);
        phoneField = addTextField(gbc, row++, 3);

        // Row 4: Address | Role
        addLabel("Address:", gbc, row, 0);
        addressField = addTextField(gbc, row, 1);
        addLabel("Role:", gbc, row, 2);
        roleBox = new JComboBox<>(new String[]{"Doctor", "Nurse", "Receptionist", "Admin", "Technician", "Pharmacist", "Lab Assistant"});
        addComponent(roleBox, gbc, row++, 3);

        // Row 5: Hire Date | Salary
        addLabel("Hire Date (YYYY-MM-DD):", gbc, row, 0);
        hireDateField = addTextField(gbc, row, 1);
        addLabel("Salary:", gbc, row, 2);
        salaryField = addTextField(gbc, row++, 3);

        // Row 6: Grade | Status
        addLabel("Grade:", gbc, row, 0);
        gradeField = addTextField(gbc, row, 1);
        addLabel("Status:", gbc, row, 2);
        statusBox = new JComboBox<>(new String[]{"Active", "Inactive", "On Leave"});
        addComponent(statusBox, gbc, row++, 3);

        // Submit Button (centered)
        submitButton = new JButton("Add Employee");
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        add(submitButton, gbc);

        // Action Listener
        submitButton.addActionListener((ActionEvent e) -> {
    // Validate required fields
    if (firstNameField.getText().trim().isEmpty() ||
        lastNameField.getText().trim().isEmpty() ||
        dobField.getText().trim().isEmpty() ||
        emailField.getText().trim().isEmpty() ||
        phoneField.getText().trim().isEmpty() ||
        addressField.getText().trim().isEmpty() ||
        hireDateField.getText().trim().isEmpty() ||
        salaryField.getText().trim().isEmpty() ||
        gradeField.getText().trim().isEmpty()) {
        
        JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Email format
    if (!emailField.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
        JOptionPane.showMessageDialog(this, "Invalid email format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Phone number check
    if (!phoneField.getText().matches("\\d{7,15}")) {
        JOptionPane.showMessageDialog(this, "Phone number must be numeric and between 7 to 15 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Date format check
    String dateRegex = "\\d{4}-\\d{2}-\\d{2}";
    if (!dobField.getText().matches(dateRegex)) {
        JOptionPane.showMessageDialog(this, "Date of Birth must be in YYYY-MM-DD format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (!hireDateField.getText().matches(dateRegex)) {
        JOptionPane.showMessageDialog(this, "Hire Date must be in YYYY-MM-DD format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Salary must be numeric
    try {
        Double.parseDouble(salaryField.getText());
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Salary must be a numeric value.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // All validations passed – insert into DB
    try {
        Conn c = new Conn();

        String sql = "INSERT INTO employees " +
                "(first_name, last_name, gender, date_of_birth, email, phone_number, address, role, hire_date, salary, grade, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = c.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, firstNameField.getText());
        stmt.setString(2, lastNameField.getText());
        stmt.setString(3, genderBox.getSelectedItem().toString());
        stmt.setString(4, dobField.getText());
        stmt.setString(5, emailField.getText());
        stmt.setString(6, phoneField.getText());
        stmt.setString(7, addressField.getText());
        stmt.setString(8, roleBox.getSelectedItem().toString().toLowerCase());
        stmt.setString(9, hireDateField.getText());
        stmt.setDouble(10, Double.parseDouble(salaryField.getText()));
        stmt.setString(11, gradeField.getText());
        stmt.setString(12, statusBox.getSelectedItem().toString());

        int rows = stmt.executeUpdate();

        if (rows > 0) {
            JOptionPane.showMessageDialog(null, "Employee added successfully.");

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int empId = generatedKeys.getInt(1);
                String role = roleBox.getSelectedItem().toString().toLowerCase();

                // Only create user if role is system-usable
                if (role.equals("admin") || role.equals("doctor") || role.equals("receptionist")) {
                    // Check if user already exists
                    String check = "SELECT * FROM users_tbl WHERE username = ?";
                    PreparedStatement checkStmt = c.conn.prepareStatement(check);
                    checkStmt.setString(1, emailField.getText());
                    ResultSet rs = checkStmt.executeQuery();

                    if (!rs.next()) {
                        String insertUser = "INSERT INTO users_tbl (username, password_hash, role, first_login,employee_id) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement pst = c.conn.prepareStatement(insertUser);
                        pst.setString(1, emailField.getText());
                        pst.setString(2, "123456"); // Default password or empId
                        pst.setString(3, role);
                        pst.setBoolean(4, true); // Force password change
                        pst.setInt(5, empId); // Force password change

                        pst.executeUpdate();
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error adding employee.");
        }

        c.conn.close();
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
    }
    clearFields();
});


    }

    private void addLabel(String text, GridBagConstraints gbc, int row, int col) {
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel(text), gbc);
    }

    private JTextField addTextField(GridBagConstraints gbc, int row, int col) {
        JTextField tf = new JTextField(15);
        addComponent(tf, gbc, row, col);
        return tf;
    }

    private void addComponent(JComponent comp, GridBagConstraints gbc, int row, int col) {
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        add(comp, gbc);
    }
    private void clearFields() {
    firstNameField.setText("");
    lastNameField.setText("");
    dobField.setText("");
    emailField.setText("");
    phoneField.setText("");
    addressField.setText("");
    hireDateField.setText("");
    salaryField.setText("");
    gradeField.setText("");
    genderBox.setSelectedIndex(0);
    roleBox.setSelectedIndex(0);
    statusBox.setSelectedIndex(0);
}


   
}


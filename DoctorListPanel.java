package clinicmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class AddDoctorPanel extends JPanel {

    JTextField nameField = new JTextField();
    JTextField emailField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JTextField designationField = new JTextField();
    JTextField degreesField = new JTextField();
    JTextField departmentField = new JTextField();
    JTextField specialistField = new JTextField();
    JTextField experienceField = new JTextField();
    JTextField servicePlaceField = new JTextField();
    JTextField birthDateField = new JTextField("yyyy-mm-dd");
    JTextField phoneField = new JTextField();
    JTextField feesField = new JTextField();
    JTextArea addressField;
    JTextField salaryField;
    JComboBox<String> bloodGroupField;
    JButton submitBtn = new JButton("Add Doctor");
    JButton clearBtn = new JButton("Clear");
    ButtonGroup genderGroup;

    public AddDoctorPanel() {
        setLayout(null);

        JLabel titleLabel = new JLabel("Create Doctor");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBounds(250, 10, 200, 30);
        add(titleLabel);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);

        int x1 = 30, x2 = 360, y = 50, gap = 35;

        addLabel("Doctor Name", x1, y, labelFont); nameField = addField(x1 + 120, y, fieldFont); y += gap;
        addLabel("Email Address", x1, y, labelFont); emailField = addField(x1 + 120, y, fieldFont); y += gap;
        addLabel("Password", x1, y, labelFont); passwordField.setBounds(x1 + 120, y, 150, 25); add(passwordField); y += gap;
        addLabel("Designation", x1, y, labelFont); designationField = addField(x1 + 120, y, fieldFont); y += gap;
        addLabel("Degrees", x1, y, labelFont); degreesField = addField(x1 + 120, y, fieldFont); y += gap;
        addLabel("Department", x1, y, labelFont); departmentField = addField(x1 + 120, y, fieldFont); y += gap;
        addLabel("Specialist", x1, y, labelFont); specialistField = addField(x1 + 120, y, fieldFont); y += gap;
        addLabel("Salary", x1, y, labelFont); salaryField = addField(x1 + 120, y, fieldFont);

        y = 50;
        addLabel("Experience", x2, y, labelFont); experienceField = addField(x2 + 120, y, fieldFont); y += gap;
        addLabel("Service Place", x2, y, labelFont); servicePlaceField = addField(x2 + 120, y, fieldFont); y += gap;
        addLabel("Birth Date", x2, y, labelFont); birthDateField = addField(x2 + 120, y, fieldFont); y += gap;
        addLabel("Phone", x2, y, labelFont); phoneField = addField(x2 + 120, y, fieldFont); y += gap;
        addLabel("Fees", x2, y, labelFont); feesField = addField(x2 + 120, y, fieldFont); y += gap;

        addLabel("Gender", x2, y, labelFont);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        genderPanel.setBounds(x2 + 120, y, 150, 25);
        JRadioButton maleRadio = new JRadioButton("Male");
        JRadioButton femaleRadio = new JRadioButton("Female");
        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        add(genderPanel);
        y += gap;

        addLabel("Blood Group", x2, y, labelFont);
        bloodGroupField = new JComboBox<>(new String[]{"Select", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        bloodGroupField.setBounds(x2 + 120, y, 150, 25);
        add(bloodGroupField); y += gap;

        addLabel("Address", x2, y, labelFont);
        addressField = new JTextArea(3, 20);
        addressField.setLineWrap(true);
        addressField.setWrapStyleWord(true);
        addressField.setBounds(x2 + 120, y, 150, 50);
        add(addressField);

        submitBtn.setBounds(250, 400, 100, 30);
        clearBtn.setBounds(370, 400, 100, 30);
        add(submitBtn); add(clearBtn);

                birthDateField.setText("YYYY-MM-DD");

        clearBtn.addActionListener(e -> clearFields());

        submitBtn.addActionListener(e -> {
            String gender = maleRadio.isSelected() ? "Male" : (femaleRadio.isSelected() ? "Female" : "");
            String bloodGroup = (String) bloodGroupField.getSelectedItem();

            if (nameField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                passwordField.getPassword().length == 0 ||
                designationField.getText().trim().isEmpty() ||
                degreesField.getText().trim().isEmpty() ||
                departmentField.getText().trim().isEmpty() ||
                experienceField.getText().trim().isEmpty() ||
                servicePlaceField.getText().trim().isEmpty() ||
                birthDateField.getText().trim().isEmpty() ||
                phoneField.getText().trim().isEmpty() ||
                feesField.getText().trim().isEmpty() ||
                gender.isEmpty() || salaryField.getText().trim().isEmpty() ||
                bloodGroup.equals("Select") ||
                addressField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields.");
                return;
            }
             // Validate name fields
if (!isValidName(nameField.getText())) {
    JOptionPane.showMessageDialog(this, "Invalid  Name. Only letters and spaces allowed.");
    return;
}

// fees validation
        double fees = Double.parseDouble(feesField.getText().trim());
        double salary = Double.parseDouble(salaryField.getText().trim());
        if(fees <= 0 || salary <= 0){
             JOptionPane.showMessageDialog(this, "Fees and Salary should be positive Numbers");
            return;
        }
        

        // Email validation
        String email = emailField.getText().trim();
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
            return;
        }

        // Phone number validation
        String phone = phoneField.getText().trim();
        if (!phone.matches("\\d{10,15}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be 10-15 digits.");
            return;
        }

        // Date validation (basic YYYY-MM-DD format)
        String birthDate = birthDateField.getText().trim();
        if (!birthDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Birth date must be in format YYYY-MM-DD.");
            return;
        }

            try {
                Conn conn = new Conn();
                String doctorId = generateDoctorID();

                String sql = "INSERT INTO doctors_tbl (name, email, designation, degrees, department, specialist, experience, service_place, birth_date, phone_number, gender, blood_group, address, doctor_id, fees) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.conn.prepareStatement(sql);
                stmt.setString(1, "Dr. "+nameField.getText().trim());
                stmt.setString(2, emailField.getText().trim());
                stmt.setString(3, designationField.getText().trim());
                stmt.setString(4, degreesField.getText().trim());
                stmt.setString(5, departmentField.getText().trim());
                stmt.setString(6, specialistField.getText().trim());
                stmt.setString(7, experienceField.getText().trim());
                stmt.setString(8, servicePlaceField.getText().trim());
                stmt.setString(9, birthDateField.getText().trim());
                stmt.setString(10, phoneField.getText().trim());
                stmt.setString(11, gender);
                stmt.setString(12, bloodGroup);
                stmt.setString(13, addressField.getText().trim());
                stmt.setString(14, doctorId);
                stmt.setDouble(15, Double.parseDouble(feesField.getText().trim()));
                stmt.executeUpdate();
                
                
                String insertEmp = "INSERT INTO employees (first_name, email, role, date_of_birth, phone_number, address, status, last_name, hire_date, salary, grade) VALUES (?, ?, ?, ?, ?, ?, ?, ? , ?,?,?)";
                PreparedStatement empStmt = conn.conn.prepareStatement(insertEmp);
                empStmt.setString(1, nameField.getText().split(" ")[0]);
                empStmt.setString(2, emailField.getText().trim());
                empStmt.setString(3, "Doctor");
                empStmt.setString(4, birthDateField.getText().trim());
                empStmt.setString(5, phoneField.getText().trim());
                empStmt.setString(6, addressField.getText().trim());
                empStmt.setString(7, "Active");
                empStmt.setString(8, nameField.getText().split(" ")[1]);
                 empStmt.setString(9, LocalDate.now().toString());
                empStmt.setDouble(10, salary);
                empStmt.setString(11, "16");


                empStmt.executeUpdate();

                createUser();

                JOptionPane.showMessageDialog(this, "Doctor added successfully!");
                clearFields();

                stmt.close();
                empStmt.close();
                conn.conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
            }
        });
    }

    private void createUser(){
        int employee_id=0;
        try{
            Conn conn = new Conn();
            String fetchQuery = "SELECT employee_id from employees "
                    + "WHERE role = ? and email = ?";
            PreparedStatement pstm = conn.conn.prepareStatement(fetchQuery);
            pstm.setString(1, "doctor");
            pstm.setString(2, emailField.getText().trim());
            ResultSet eidRS = pstm.executeQuery();
            if(eidRS.next()){
                employee_id = eidRS.getInt("employee_id");
            }
            pstm.close();
            
            String insertUser = "INSERT INTO users_tbl (employee_id,username, password_hash, role) VALUES (?,?, ?, ?)";
                PreparedStatement userStmt = conn.conn.prepareStatement(insertUser);
                userStmt.setInt(1, employee_id);
                userStmt.setString(2, emailField.getText().trim());
                userStmt.setString(3, new String(passwordField.getPassword()));
                userStmt.setString(4, "doctor");
                userStmt.executeUpdate();
                
                userStmt.close();
                conn.conn.close();

        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    private void addLabel(String text, int x, int y, Font font) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 120, 25);
        label.setFont(font);
        add(label);
    }

    private JTextField addField(int x, int y, Font font) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 150, 25);
        tf.setFont(font);
        add(tf);
        return tf;
    }

    public void clearFields() {
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        designationField.setText("");
        degreesField.setText("");
        departmentField.setText("");
        specialistField.setText("");
        experienceField.setText("");
        servicePlaceField.setText("");
        birthDateField.setText("");
        phoneField.setText("");
        feesField.setText("");
        genderGroup.clearSelection();
        bloodGroupField.setSelectedIndex(0);
        addressField.setText("");
        salaryField.setText("");
    }

    public String generateDoctorID() {
        String newID = "D001";
        try {
            Conn conn = new Conn();
            String sql = "SELECT doctor_id FROM doctors_tbl ORDER BY doctor_id DESC LIMIT 1";
            ResultSet rs = conn.stm.executeQuery(sql);

            if (rs.next()) {
                String lastId = rs.getString("doctor_id");
                int num = Integer.parseInt(lastId.substring(1));
                num++;
                newID = String.format("D%03d", num);
            }

            rs.close();
            conn.conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newID;
    }
      private boolean isValidName(String name) {
    return name != null && name.matches("^[A-Za-z ]{1,50}$");
}
}

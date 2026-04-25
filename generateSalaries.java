/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicmanagementsystem;


import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddNewPatientPanel extends JPanel {

    public JTextField patientIdField, familyNameField, givenNameField, patientEmailField, patientPhoneField,
        mobileNumberField, postCodeField, addressField, birthDateField,
        emgFamilyNameField, emgGivenNameField, emgPhoneField, emgMobileField,
        createByField, createDateField;

public JComboBox<String> sexCombo, bloodGroupCombo;

    private String currentUser;
    public JButton addBtn, clearBtn;

    public AddNewPatientPanel(String currentUser) {
    this.currentUser = currentUser;
        setLayout(null);
        setBackground(new Color(245, 245, 245));
        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);
        Color fgColor = new Color(50, 50, 50);

        JLabel title = new JLabel("Add New Patient");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(fgColor);
        title.setBounds(230, 15, 300, 30);
        add(title);

        int x1 = 30, x2 = 330, y = 60, gap = 35;

        // Left Column
        addLabel("Patient ID", x1, y, labelFont, fgColor);
        patientIdField = addField(x1 + 100, y, fieldFont, fgColor);
patientIdField.setEditable(false);
patientIdField.setText(generatePatientId());

        y += gap;
        addLabel("Family Name", x1, y, labelFont, fgColor); familyNameField = addField(x1 + 100, y, fieldFont, fgColor);
        y += gap;
        addLabel("Given Name", x1, y, labelFont, fgColor); givenNameField = addField(x1 + 100, y, fieldFont, fgColor);
        y += gap;
        addLabel("Email", x1, y, labelFont, fgColor); patientEmailField = addField(x1 + 100, y, fieldFont, fgColor);
        y += gap;
        addLabel("Phone", x1, y, labelFont, fgColor); patientPhoneField = addField(x1 + 100, y, fieldFont, fgColor);
        y += gap;
        addLabel("Mobile", x1, y, labelFont, fgColor); mobileNumberField = addField(x1 + 100, y, fieldFont, fgColor);
        y += gap;
        addLabel("Post Code", x1, y, labelFont, fgColor); postCodeField = addField(x1 + 100, y, fieldFont, fgColor);
        y += gap;
        addLabel("Address", x1, y, labelFont, fgColor); addressField = addField(x1 + 100, y, fieldFont, fgColor);

        // Right Column
        y = 60;
        addLabel("Sex", x2, y, labelFont, fgColor);
        sexCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
sexCombo.setBounds(x2 + 125, y, 150, 25);
sexCombo.setFont(fieldFont);
add(sexCombo);

        y += gap;
        addLabel("Birth Date", x2, y, labelFont, fgColor); birthDateField = addField(x2 + 125, y, fieldFont, fgColor);
        y += gap;
        addLabel("Blood Group", x2, y, labelFont, fgColor); 
        bloodGroupCombo = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"});
bloodGroupCombo.setBounds(x2 + 125, y, 150, 25);
bloodGroupCombo.setFont(fieldFont);
add(bloodGroupCombo);

        y += gap;
        addLabel("Emg. Family Name", x2, y, labelFont, fgColor); emgFamilyNameField = addField(x2 + 125, y, fieldFont, fgColor);
        y += gap;
        addLabel("Emg. Given Name", x2, y, labelFont, fgColor); emgGivenNameField = addField(x2 + 125, y, fieldFont, fgColor);
        y += gap;
        addLabel("Emg. Phone", x2, y, labelFont, fgColor); emgPhoneField = addField(x2 + 125, y, fieldFont, fgColor);
        y += gap;
        addLabel("Emg. Mobile", x2, y, labelFont, fgColor); emgMobileField = addField(x2 + 125, y, fieldFont, fgColor);
        y += gap;
        addLabel("Create By", x2, y, labelFont, fgColor); createByField = addField(x2 + 125, y, fieldFont, fgColor);
        y += gap;
        createByField.setText(currentUser);
    createByField.setEditable(false);
        addLabel("Create Date", x2, y, labelFont, fgColor); createDateField = addField(x2 + 125, y, fieldFont, fgColor);
        createDateField.setText(java.time.LocalDate.now().toString());
        createDateField.setEditable(false); // Prevent manual editing

        // Buttons
        addBtn = new JButton("Add");
        addBtn.setBounds(230, 420, 80, 30);
        add(addBtn);
        addBtn.addActionListener(e -> savePatientToDatabase());


        clearBtn = new JButton("Clear");
        clearBtn.setBounds(330, 420, 80, 30);
        add(clearBtn);
        clearBtn.addActionListener(e -> clearForm());

    }
    
    private void savePatientToDatabase() {
    try {
        // Basic Validation
        if (patientIdField.getText().trim().isEmpty() ||
            familyNameField.getText().trim().isEmpty() ||
            givenNameField.getText().trim().isEmpty() ||
            patientEmailField.getText().trim().isEmpty() ||
            patientPhoneField.getText().trim().isEmpty() ||
            birthDateField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please fill all required fields.");
            return;
        }
        // Validate name fields
if (!isValidName(familyNameField.getText())) {
    JOptionPane.showMessageDialog(this, "Invalid Family Name. Only letters and spaces allowed.");
    return;
}
if (!isValidName(givenNameField.getText())) {
    JOptionPane.showMessageDialog(this, "Invalid Given Name. Only letters and spaces allowed.");
    return;
}
if (!isValidName(emgFamilyNameField.getText())) {
    JOptionPane.showMessageDialog(this, "Invalid Emergency Family Name. Only letters and spaces allowed.");
    return;
}
if (!isValidName(emgGivenNameField.getText())) {
    JOptionPane.showMessageDialog(this, "Invalid Emergency Given Name. Only letters and spaces allowed.");
    return;
}

        // Email validation
        String email = patientEmailField.getText().trim();
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
            return;
        }

        // Phone number validation
        String phone = patientPhoneField.getText().trim();
        String mobile = mobileNumberField.getText().trim();
        if (!phone.matches("\\d{10,15}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be 10-15 digits.");
            return;
        }
        if (!mobile.isEmpty() && !mobile.matches("\\d{10,15}")) {
            JOptionPane.showMessageDialog(this, "Mobile number must be 10-15 digits.");
            return;
        }

        // Date validation (basic YYYY-MM-DD format)
        String birthDate = birthDateField.getText().trim();
        if (!birthDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Birth date must be in format YYYY-MM-DD.");
            return;
        }

        String createDate = createDateField.getText().trim();
        if (!createDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Create date must be in format YYYY-MM-DD.");
            return;
        }

        // Database Insert
        String sql = "INSERT INTO patient_tbl (patient_id, family_name, given_name, patient_email, patient_phone, " +
                "mobile_number, post_code, address, sex, birth_date, blood_group, " +
                "emg_family_name, emg_given_name, emg_phone, emg_mobile, create_by, create_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Conn conn = new Conn(); // Your DB connection class
        PreparedStatement pst = conn.conn.prepareStatement(sql);

        pst.setString(1, patientIdField.getText().trim());
        pst.setString(2, familyNameField.getText().trim());
        pst.setString(3, givenNameField.getText().trim());
        pst.setString(4, email);
        pst.setString(5, phone);
        pst.setString(6, mobile);
        pst.setString(7, postCodeField.getText().trim());
        pst.setString(8, addressField.getText().trim());
        pst.setString(9, sexCombo.getSelectedItem().toString().trim());
        pst.setString(10, birthDate);
        pst.setString(11, bloodGroupCombo.getSelectedItem().toString().trim());
        pst.setString(12, emgFamilyNameField.getText().trim());
        pst.setString(13, emgGivenNameField.getText().trim());
        pst.setString(14, emgPhoneField.getText().trim());
        pst.setString(15, emgMobileField.getText().trim());
       pst.setString(16, currentUser); // or "admin" if hardcoded
pst.setString(17, java.time.LocalDate.now().toString());


        int result = pst.executeUpdate();

        if (result > 0) {
            JOptionPane.showMessageDialog(this, "Patient saved successfully!");
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save patient.");
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}

private void clearForm() {
    patientIdField.setText("");
    familyNameField.setText("");
    givenNameField.setText("");
    patientEmailField.setText("");
    patientPhoneField.setText("");
    mobileNumberField.setText("");
    postCodeField.setText("");
    addressField.setText("");
    sexCombo.setSelectedIndex(0);
    birthDateField.setText("");
    bloodGroupCombo.setSelectedIndex(0);
    emgFamilyNameField.setText("");
    emgGivenNameField.setText("");
    emgPhoneField.setText("");
    emgMobileField.setText("");
    createByField.setText("");
    createDateField.setText(java.time.LocalDate.now().toString()); // reset to today
}

    
    private void addLabel(String text, int x, int y, Font font, Color fg) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 150, 25);
        label.setFont(font);
        label.setForeground(fg);
        add(label);
    }

    private JTextField addField(int x, int y, Font font, Color fg) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 150, 25);
        tf.setFont(font);
        tf.setForeground(fg);
        add(tf);
        return tf;
    }
    
    private String generatePatientId() {
    try {
        Conn conn = new Conn();
        String q = "SELECT patient_id FROM patient_tbl ORDER BY patient_id DESC LIMIT 1";
        ResultSet rs = conn.stm.executeQuery(q);

        String lastId = "P000";
        if (rs.next()) {
            lastId = rs.getString("patient_id"); // e.g., "P025"
        }
        int num = Integer.parseInt(lastId.substring(1)) + 1;
        return String.format("P%03d", num);
    } catch (Exception e) {
        e.printStackTrace();
        return "P001"; // fallback
    }
}
    private boolean isValidName(String name) {
    return name != null && name.matches("^[A-Za-z ]{1,50}$");
}


}

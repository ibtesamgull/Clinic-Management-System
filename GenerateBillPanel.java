package clinicmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddMedicalRecordPanel extends JPanel {
    private JComboBox<String> patientCombo, doctorCombo;
    private JTextField diagnosisField, treatmentField;
    private JTextArea notesArea;
    private JSpinner dateSpinner;

    public AddMedicalRecordPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setBorder(BorderFactory.createTitledBorder("Add Medical Record"));
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Patient
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Patient:"), gbc);
        gbc.gridx = 1;
        patientCombo = new JComboBox<>();
        populatePatients();
        add(patientCombo, gbc);

        // Doctor
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Doctor:"), gbc);
        gbc.gridx = 1;
        doctorCombo = new JComboBox<>();
        populateDoctors();
        add(doctorCombo, gbc);

        // Diagnosis
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Diagnosis:"), gbc);
        gbc.gridx = 1;
        diagnosisField = new JTextField();
        add(diagnosisField, gbc);

        // Treatment
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Treatment:"), gbc);
        gbc.gridx = 1;
        treatmentField = new JTextField();
        add(treatmentField, gbc);

        // Date
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        add(dateSpinner, gbc);

        // Notes
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Notes:"), gbc);
        gbc.gridx = 1;
        notesArea = new JTextArea(4, 20);
        add(new JScrollPane(notesArea), gbc);

        // Submit
        gbc.gridx = 1; gbc.gridy++;
        JButton submitBtn = new JButton("Add Record");
        submitBtn.addActionListener(e -> insertRecord());
        add(submitBtn, gbc);
    }

    private void populatePatients() {
        try (Connection conn = new Conn().conn;
             ResultSet rs = conn.createStatement().executeQuery("SELECT patient_id, CONCAT(given_name,' ',family_name) AS name FROM patient_tbl")) {
            while (rs.next()) {
                patientCombo.addItem(rs.getString("patient_id") + " - " + rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateDoctors() {
        try (Connection conn = new Conn().conn;
             ResultSet rs = conn.createStatement().executeQuery("SELECT doctor_id, name FROM doctors_tbl")) {
            while (rs.next()) {
                doctorCombo.addItem(rs.getString("doctor_id") + " - " + rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertRecord() {
        try {
            String patientId = patientCombo.getSelectedItem().toString().split(" - ")[0];
            String doctorId = doctorCombo.getSelectedItem().toString().split(" - ")[0];
            String diagnosis = diagnosisField.getText();
            String treatment = treatmentField.getText();
            String notes = notesArea.getText();
            java.sql.Date sqlDate = new java.sql.Date(((java.util.Date) dateSpinner.getValue()).getTime());

            String query = "INSERT INTO medical_records (patient_id, doctor_id, diagnosis, treatment, date, notes) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = new Conn().conn.prepareStatement(query);
            stmt.setString(1, patientId);
            stmt.setString(2, doctorId);
            stmt.setString(3, diagnosis);
            stmt.setString(4, treatment);
            stmt.setDate(5, sqlDate);
            stmt.setString(6, notes);

            int res = stmt.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(this, "Record added successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving record.");
        }
    }
}

package clinicmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class AddPrescriptionPanel extends JPanel {
    private JComboBox<String> appointmentCombo;
    private JTextField dateField;
    private JTextArea medicationArea, dosageArea, instructionArea;
    private JButton saveBtn, clearBtn;

    public AddPrescriptionPanel() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Prescription"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);
        int row = 0;

        addLabel(formPanel, "Appointment:", labelFont, gbc, 0, row);
        appointmentCombo = new JComboBox<>();
        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(appointmentCombo, gbc);

        addLabel(formPanel, "Date:", labelFont, gbc, 0, row);
        dateField = new JTextField(LocalDate.now().toString());
        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(dateField, gbc);

        addLabel(formPanel, "Medication:", labelFont, gbc, 0, row);
        medicationArea = new JTextArea(3, 20);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(medicationArea), gbc);
        row++;

        addLabel(formPanel, "Dosage:", labelFont, gbc, 0, row);
        dosageArea = new JTextArea(3, 20);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(dosageArea), gbc);
        row++;

        addLabel(formPanel, "Instructions:", labelFont, gbc, 0, row);
        instructionArea = new JTextArea(3, 20);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(instructionArea), gbc);
        row++;

        saveBtn = new JButton("Save");
        clearBtn = new JButton("Clear");
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(saveBtn, gbc);
        gbc.gridx = 1;
        formPanel.add(clearBtn, gbc);

        add(formPanel, BorderLayout.CENTER);

        saveBtn.addActionListener(e -> insertPrescription());
        clearBtn.addActionListener(e -> clearForm());

        loadAppointments();
    }

    private void addLabel(JPanel panel, String text, Font font, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        JLabel label = new JLabel(text);
        label.setFont(font);
        panel.add(label, gbc);
    }

    private void loadAppointments() {
        try {
            Conn conn = new Conn();
            String query = "SELECT a.appointment_id, CONCAT(p.given_name,' ',p.family_name) AS patient_name, d.name AS doctor_name " +
                           "FROM appointment_tbl a " +
                           "JOIN patient_tbl p ON a.patient_id = p.patient_id " +
                           "JOIN doctors_tbl d ON a.doctor_id = d.doctor_id";
            ResultSet rs = conn.stm.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString("appointment_id");
                String label = id + " | Dr. " + rs.getString("doctor_name") + " | " + rs.getString("patient_name");
                appointmentCombo.addItem(label);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load appointments.");
        }
    }

    private void insertPrescription() {
        try {
            String selected = appointmentCombo.getSelectedItem().toString();
            String appointmentId = selected.split(" ")[0];

            Conn conn = new Conn();
            // Get doctor and patient ID
            String fetch = "SELECT patient_id, doctor_id FROM appointment_tbl WHERE appointment_id = ?";
            PreparedStatement pstFetch = conn.conn.prepareStatement(fetch);
            pstFetch.setString(1, appointmentId);
            ResultSet rs = pstFetch.executeQuery();

            String patientId = null, doctorId = null;
            if (rs.next()) {
                patientId = rs.getString("patient_id");
                doctorId = rs.getString("doctor_id");
            }

            String sql = "INSERT INTO prescriptions_tbl (patient_id, doctor_id, appointment_id, date, medication, dosage, instructions) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.conn.prepareStatement(sql);
            pst.setString(1, patientId);
            pst.setString(2, doctorId);
            pst.setString(3, appointmentId);
            pst.setDate(4, java.sql.Date.valueOf(dateField.getText()));
            pst.setString(5, medicationArea.getText());
            pst.setString(6, dosageArea.getText());
            pst.setString(7, instructionArea.getText());

            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Prescription saved successfully!");
                clearForm();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void clearForm() {
        medicationArea.setText("");
        dosageArea.setText("");
        instructionArea.setText("");
        dateField.setText(LocalDate.now().toString());
        if (appointmentCombo.getItemCount() > 0)
            appointmentCombo.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Add Prescription");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new AddPrescriptionPanel());
        frame.setVisible(true);
    }
}

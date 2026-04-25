package clinicmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class PrescriptionPanel extends JPanel {
    JComboBox<String> appointmentCombo;
    JTextArea diagnosisArea, notesArea;
    JTextField medicineField;
    JButton addPrescriptionBtn, addMedicineBtn, clearBtn;
    ArrayList<String> selectedMedicines = new ArrayList<>();

    public PrescriptionPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Prescription Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(title, gbc);

        gbc.gridwidth = 1;
        addLabel("Appointment ID:", 0, 1, gbc);
        appointmentCombo = addComboBox(1, 1, gbc);

        addLabel("Diagnosis:", 0, 2, gbc);
        diagnosisArea = new JTextArea(2, 15);
        JScrollPane diagnosisScroll = new JScrollPane(diagnosisArea);
        gbc.gridx = 1; gbc.gridy = 2;
        add(diagnosisScroll, gbc);

        addLabel("Notes:", 0, 3, gbc);
        notesArea = new JTextArea(2, 15);
        JScrollPane notesScroll = new JScrollPane(notesArea);
        gbc.gridx = 1; gbc.gridy = 3;
        add(notesScroll, gbc);

        addLabel("Medicines:", 0, 4, gbc);
        medicineField = new JTextField();
        medicineField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 4;
        add(medicineField, gbc);

        addMedicineBtn = new JButton("Add Medicines");
        gbc.gridx = 1; gbc.gridy = 5;
        add(addMedicineBtn, gbc);

        addPrescriptionBtn = new JButton("Create Prescription");
        gbc.gridx = 0; gbc.gridy = 6;
        add(addPrescriptionBtn, gbc);

        clearBtn = new JButton("Clear");
        gbc.gridx = 1;
        add(clearBtn, gbc);

        clearBtn.addActionListener(e -> clearForm());
        addPrescriptionBtn.addActionListener(e -> createPrescription());
        addMedicineBtn.addActionListener(e -> selectMedicines());

        loadAppointments();
    }

    private void selectMedicines() {
        try {
            Conn conn = new Conn();
            ResultSet rs = conn.stm.executeQuery("SELECT medicine_id, name,dosage FROM medicine_tbl");
            DefaultListModel<String> model = new DefaultListModel<>();
            ArrayList<String> idList = new ArrayList<>();
            ArrayList<String> nameList = new ArrayList<>();
           ArrayList<String> dosageList = new ArrayList<>();

            
            while (rs.next()) {
                idList.add(rs.getString("medicine_id"));
                nameList.add(rs.getString("name"));
                dosageList.add(rs.getString("dosage"));
                model.addElement(rs.getString("medicine_id")+" "+rs.getString("name")+" - "+rs.getString("dosage"));
            }

            JList<String> list = new JList<>(model);
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            JScrollPane scroll = new JScrollPane(list);

            int result = JOptionPane.showConfirmDialog(this, scroll, "Select Medicines", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                selectedMedicines.clear();
                StringBuilder sb = new StringBuilder();
                for (int i : list.getSelectedIndices()) {
                    String medName = nameList.get(i);
                    if (dosageList.get(i) != null && !dosageList.get(i).trim().isEmpty()) {
                        selectedMedicines.add(idList.get(i));
                        sb.append(medName).append(" - ").append(dosageList.get(i)).append(", ");
                    }
                }
                if (sb.length() > 0) sb.setLength(sb.length() - 2);
                medicineField.setText(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading medicines: " + e.getMessage());
        }
    }

    private void loadAppointments() {
        try {
            Conn conn = new Conn();
            ResultSet rs = conn.stm.executeQuery("SELECT appointment_id FROM appointment_tbl");
            while (rs.next()) {
                appointmentCombo.addItem(rs.getString("appointment_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createPrescription() {
        try {
            String appointmentId = (String) appointmentCombo.getSelectedItem();
            if (appointmentId == null) {
                JOptionPane.showMessageDialog(this, "Select an appointment.");
                return;
            }

            Conn conn = new Conn();
            PreparedStatement fetchStmt = conn.conn.prepareStatement("SELECT patient_id, doctor_id FROM appointment_tbl WHERE appointment_id = ?");
            fetchStmt.setString(1, appointmentId);
            ResultSet rs = fetchStmt.executeQuery();
            if (!rs.next()) return;
            String patientId = rs.getString("patient_id");
            String doctorId = rs.getString("doctor_id");

            String prescId = generatePrescriptionId();
            PreparedStatement insertPresc = conn.conn.prepareStatement("INSERT INTO prescriptions_tbl (prescription_id, patient_id, doctor_id,appointment_id, date, diagnosis, notes) VALUES (?, ?, ?,?, CURDATE(), ?, ?)");
            insertPresc.setString(1, prescId);
            insertPresc.setString(2, patientId);
            insertPresc.setString(3, doctorId);
            insertPresc.setString(4, appointmentId);
            insertPresc.setString(5, diagnosisArea.getText());
            insertPresc.setString(6, notesArea.getText());
            insertPresc.executeUpdate();

            for (String medEntry : medicineField.getText().split(", ")) {
                String[] parts = medEntry.split(" - ");
                if (parts.length == 2) {
                    String name = parts[0];
                    String dosage = parts[1];

                    PreparedStatement medIdStmt = conn.conn.prepareStatement("SELECT medicine_id FROM medicine_tbl WHERE name = ?");
                    medIdStmt.setString(1, name);
                    ResultSet medRs = medIdStmt.executeQuery();
                    if (medRs.next()) {
                        String medId = medRs.getString("medicine_id");
                        PreparedStatement insertMed = conn.conn.prepareStatement("INSERT INTO prescription_medicines (prescription_id, medicine_id, dosage) VALUES (?, ?, ?)");
                        insertMed.setString(1, prescId);
                        insertMed.setString(2, medId);
                        insertMed.setString(3, dosage);
                        insertMed.executeUpdate();
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "Prescription added successfully.");
            clearForm();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void clearForm() {
        diagnosisArea.setText("");
        notesArea.setText("");
        medicineField.setText("");
        selectedMedicines.clear();
    }

    private String generatePrescriptionId() {
        try {
            Conn conn = new Conn();
            ResultSet rs = conn.stm.executeQuery("SELECT prescription_id FROM prescriptions_tbl ORDER BY prescription_id DESC LIMIT 1");
            String last = "PR000";
            if (rs.next()) last = rs.getString(1);
            int num = Integer.parseInt(last.substring(2)) + 1;
            return String.format("PR%03d", num);
        } catch (Exception e) {
            e.printStackTrace();
            return "PR001";
        }
    }

    private void addLabel(String text, int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x;
        gbc.gridy = y;
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(label, gbc);
    }

    private JComboBox<String> addComboBox(int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x;
        gbc.gridy = y;
        JComboBox<String> cb = new JComboBox<>();
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(cb, gbc);
        return cb;
    }
}

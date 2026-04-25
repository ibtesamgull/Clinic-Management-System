package clinicmanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MedicalRecordsListPanel extends JPanel {
    JTable table;
    DefaultTableModel model;
    JTextField searchField;

    public MedicalRecordsListPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Medical Records"));

        // 🔍 Search bar at the top
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(25);
        JButton searchBtn = new JButton("Search");
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        add(searchPanel, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new String[]{"Record ID", "Patient", "Doctor", "Diagnosis", "Treatment", "Date", "Notes"}, 0);
        table = new JTable(model);
        loadData(null);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ⬇️ Buttons at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addBtn = new JButton("Add Record");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton refreshBtn = new JButton("Refresh");
        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // 🔁 Action listeners
        addBtn.addActionListener(e -> openAddRecordDialog());
        deleteBtn.addActionListener(e -> deleteSelectedRecord());
        refreshBtn.addActionListener(e -> {
            searchField.setText("");
            model.setRowCount(0);
            loadData(null);
        });
        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();
            model.setRowCount(0);
            loadData(query);
        });
    }

    private void loadData(String searchQuery) {
        String sql = "SELECT mr.record_id, CONCAT(p.given_name, ' ', p.family_name) AS patient, " +
                "d.name AS doctor, mr.diagnosis, mr.treatment, mr.date, mr.notes " +
                "FROM medical_records mr " +
                "JOIN patient_tbl p ON mr.patient_id = p.patient_id " +
                "JOIN doctors_tbl d ON mr.doctor_id = d.doctor_id";

        if (searchQuery != null && !searchQuery.isEmpty()) {
            sql += " WHERE CONCAT(p.given_name, ' ', p.family_name) LIKE ? " +
                   "OR d.name LIKE ? OR mr.diagnosis LIKE ?";
        }

        try (Connection conn = new Conn().conn;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (searchQuery != null && !searchQuery.isEmpty()) {
                String q = "%" + searchQuery.toLowerCase() + "%";
                ps.setString(1, q);
                ps.setString(2, q);
                ps.setString(3, q);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("record_id"),
                            rs.getString("patient"),
                            rs.getString("doctor"),
                            rs.getString("diagnosis"),
                            rs.getString("treatment"),
                            rs.getDate("date"),
                            rs.getString("notes")
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteSelectedRecord() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.");
            return;
        }

        int recordId = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete record ID: " + recordId + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = new Conn().conn;
             PreparedStatement ps = conn.prepareStatement("DELETE FROM medical_records WHERE record_id = ?")) {
            ps.setInt(1, recordId);
            ps.executeUpdate();
            model.removeRow(row);
            JOptionPane.showMessageDialog(this, "Record deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete record.");
        }
    }

    private void openAddRecordDialog() {
        JFrame frame = new JFrame("Add Medical Record");
        frame.setContentPane(new AddMedicalRecordPanel());
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

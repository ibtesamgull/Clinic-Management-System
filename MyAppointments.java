package clinicmanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class BillsListPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton markAsPaidBtn, searchBtn,viewBtn;
    private JTextField searchField;

    public BillsListPanel() {
        setLayout(new BorderLayout());

        // Top panel with search
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchBills());
        topPanel.add(new JLabel("Search (Bill ID / Patient ID):"));
        topPanel.add(searchField);
        topPanel.add(searchBtn);

        // Table Setup
        String[] columns = {"Appointment ID", "Patient ID", "Amount", "Paid", "Billing Date"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        loadBills(); // Initial load

        // Bottom Panel with action buttons
        markAsPaidBtn = new JButton("Mark as Paid");
        markAsPaidBtn.addActionListener(e -> markSelectedBillAsPaid());
        viewBtn = new JButton("View Bill");
        viewBtn.addActionListener(e -> {
         String appointment_id = model.getValueAt(table.getSelectedRow(),0).toString();
          new ViewBillFrame(appointment_id).setVisible(true);
        });
        

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(markAsPaidBtn);
        bottomPanel.add(viewBtn);
        

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        
    }

    private void loadBills() {
        model.setRowCount(0);
        try {
            Conn conn = new Conn();
            String sql = "SELECT appointment_id, patient_id, amount, paid, billing_date FROM bills";
            ResultSet rs = conn.stm.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("appointment_id"),
                    rs.getString("patient_id"),
                    rs.getDouble("amount"),
                    rs.getString("paid"),
                    rs.getDate("billing_date")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchBills() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Bill ID or Patient ID to search.");
            loadBills(); // Show all if nothing entered
            return;
        }

        model.setRowCount(0);
        try {
            Conn conn = new Conn();
            String sql = "SELECT appointment_id, patient_id, amount, paid, billing_date FROM bills " +
                         "WHERE LOWER(patient_id) LIKE ? OR CAST(bill_id AS CHAR) LIKE ?";
            PreparedStatement pst = conn.conn.prepareStatement(sql);
            String wildcard = "%" + keyword + "%";
            pst.setString(1, wildcard);
            pst.setString(2, wildcard);

            ResultSet rs = pst.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                model.addRow(new Object[]{
                    rs.getString("appointment_id"),
                    rs.getString("patient_id"),
                    rs.getDouble("amount"),
                    rs.getString("paid"),
                    rs.getDate("billing_date")
                });
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "No bills found for: " + keyword);
                loadBills();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void markSelectedBillAsPaid() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bill to mark as paid.");
            return;
        }

        String appointment_id =  model.getValueAt(row, 0).toString();
        String paidStatus = (String) model.getValueAt(row, 3);

        if (paidStatus.equalsIgnoreCase("Yes")) {
            JOptionPane.showMessageDialog(this, "This bill is already marked as paid.");
            return;
        }

        try {
            Conn conn = new Conn();
            String sql = "UPDATE bills SET paid = 'Yes' WHERE appointment_id = ?";
            PreparedStatement pst = conn.conn.prepareStatement(sql);
            pst.setString(1, appointment_id);
            int updated = pst.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Bill marked as paid successfully.");
                loadBills(); // Refresh table
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



   
}

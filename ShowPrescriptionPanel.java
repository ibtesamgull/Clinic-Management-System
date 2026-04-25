package clinicmanagementsystem;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EmployeeListPanel extends JPanel {
    JTable table;
    DefaultTableModel model;
    JButton deleteButton, updateButton;

    public EmployeeListPanel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Employee List");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Role"}, 0);
        table = new JTable(model);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        deleteButton = new JButton("Delete Employee");
        updateButton = new JButton("Update Employee");
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        deleteButton.addActionListener(e -> deleteEmployee());
        updateButton.addActionListener(e -> updateEmployee());

        loadEmployees();
    }

    private void loadEmployees() {
        try {
            Conn conn = new Conn();
            ResultSet rs = conn.stm.executeQuery("SELECT employee_id, CONCAT(first_name, ' ', last_name) as name, email, role FROM employees");
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("employee_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("role")
                });
            }
            rs.close();
            conn.conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteEmployee() {
        int selected = table.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Select an employee to delete.");
            return;
        }

        int employeeId = (int) model.getValueAt(selected, 0);
        String email = (String) model.getValueAt(selected, 2);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this employee?");
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Conn c = new Conn();

            // Delete employee
            String deleteEmp = "DELETE FROM employees WHERE employee_id = ?";
            PreparedStatement pst1 = c.conn.prepareStatement(deleteEmp);
            pst1.setInt(1, employeeId);
            pst1.executeUpdate();

            // Delete corresponding user, if exists
            String deleteUser = "DELETE FROM users_tbl WHERE username = ?";
            PreparedStatement pst2 = c.conn.prepareStatement(deleteUser);
            pst2.setString(1, email);
            pst2.executeUpdate();

            JOptionPane.showMessageDialog(this, "Employee and user (if any) deleted.");
            loadEmployees();

            c.conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting employee/user: " + e.getMessage());
        }
    }

    private void updateEmployee() {
        int selected = table.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Select an employee to update.");
            return;
        }

        int employeeId = (int) model.getValueAt(selected, 0);
        String currentName = (String) model.getValueAt(selected, 1);
        String currentEmail = (String) model.getValueAt(selected, 2);
        String currentRole = (String) model.getValueAt(selected, 3);

        String newEmail = JOptionPane.showInputDialog(this, "Enter new email:", currentEmail);
        String[] roles = {"Doctor", "Nurse", "Receptionist", "Admin", "Technician", "Pharmacist", "Lab Assistant"};
        String newRole = (String) JOptionPane.showInputDialog(this, "Select new role:", "Update Role",
                JOptionPane.PLAIN_MESSAGE, null, roles, currentRole);

        if (newEmail != null && newRole != null) {
            try {
                Conn conn = new Conn();
                String sql = "UPDATE employees SET email = ?, role = ? WHERE employee_id = ?";
                PreparedStatement pst = conn.conn.prepareStatement(sql);
                pst.setString(1, newEmail);
                pst.setString(2, newRole);
                pst.setInt(3, employeeId);
                pst.executeUpdate();
                loadEmployees();
                JOptionPane.showMessageDialog(this, "Employee updated.");
                conn.conn.close();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating employee.");
            }
        }
    }

   
}

package clinicmanagementsystem;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageUsersPanel extends JPanel {
    JTable table;
    DefaultTableModel model;
   JButton addUserBtn, deleteUserBtn, updateUserBtn;


    public ManageUsersPanel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Manage Users");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"User ID","Employee ID", "Username", "Role"}, 0);
        table = new JTable(model);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addUserBtn = new JButton("Add User");
        deleteUserBtn = new JButton("Delete User");
        updateUserBtn = new JButton("Update User");
buttonPanel.add(addUserBtn);
buttonPanel.add(updateUserBtn);
buttonPanel.add(deleteUserBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        addUserBtn.addActionListener(e -> addUser());
        deleteUserBtn.addActionListener(e -> deleteUser());
        updateUserBtn.addActionListener(e -> updateUser());


        loadUsers();
    }
    private void updateUser() {
    int selected = table.getSelectedRow();
    if (selected == -1) {
        JOptionPane.showMessageDialog(this, "Select a user to update.");
        return;
    }

    int userId = (int) model.getValueAt(selected, 0);
    String currentUsername = (String) model.getValueAt(selected, 2);
    String currentRole = (String) model.getValueAt(selected, 3);

    String username = JOptionPane.showInputDialog(this, "Enter new username:", currentUsername);
    String[] roles = {"admin", "doctor", "receptionist"};
    String role = (String) JOptionPane.showInputDialog(this, "Select new role:", "Update Role",
            JOptionPane.PLAIN_MESSAGE, null, roles, currentRole);

    if (username != null && role != null) {
        try {
            Conn conn = new Conn();
            String sql = "UPDATE users_tbl SET username = ?, role = ? WHERE user_id = ?";
            PreparedStatement pst = conn.conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, role);
            pst.setInt(3, userId);
            pst.executeUpdate();
            loadUsers();
            JOptionPane.showMessageDialog(this, "User updated.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating user.");
        }
    }
}

    private void loadUsers() {
        try {
            Conn conn = new Conn();
            ResultSet rs = conn.stm.executeQuery("SELECT * FROM users_tbl");
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("user_id"),
                    rs.getInt("employee_id"),
                    rs.getString("username"),
                    rs.getString("role")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addUser() {
    String username = JOptionPane.showInputDialog(this, "Enter username (must match employee email):");
    String password = JOptionPane.showInputDialog(this, "Enter password:");
    String[] roles = {"admin", "doctor", "receptionist"};
    String role = (String) JOptionPane.showInputDialog(this, "Select role:", "Role",
            JOptionPane.PLAIN_MESSAGE, null, roles, roles[0]);

    if (username != null && password != null && role != null) {
        try {
            Conn conn = new Conn();

            // Check if employee with this email and role exists
            String checkEmployee = "SELECT * FROM employees WHERE email = ? AND LOWER(role) = ?";
            PreparedStatement empStmt = conn.conn.prepareStatement(checkEmployee);
            empStmt.setString(1, username);
            empStmt.setString(2, role.toLowerCase());
            ResultSet rs = empStmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "No matching employee found with this email and role.");
                return;
            }

            // Check if user already exists
            String checkUser = "SELECT * FROM users_tbl WHERE username = ?";
            PreparedStatement userStmt = conn.conn.prepareStatement(checkUser);
            userStmt.setString(1, username);
            ResultSet rs2 = userStmt.executeQuery();

            if (rs2.next()) {
                JOptionPane.showMessageDialog(this, "User already exists.");
                return;
            }

            // Add user
            String insert = "INSERT INTO users_tbl (username, password_hash, role) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.conn.prepareStatement(insert);
            pst.setString(1, username);
            pst.setString(2, password); // Consider hashing this in real apps
            pst.setString(3, role.toLowerCase());
            pst.executeUpdate();
            loadUsers();
            JOptionPane.showMessageDialog(this, "User added.");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding user.");
        }
    }
}


    private void deleteUser() {
        int selected = table.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Select a user to delete.");
            return;
        }

        int userId = (int) model.getValueAt(selected, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?");
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Conn conn = new Conn();
            String delete = "DELETE FROM users_tbl WHERE user_id = ?";
            PreparedStatement pst = conn.conn.prepareStatement(delete);
            pst.setInt(1, userId);
            pst.executeUpdate();
            loadUsers();
            JOptionPane.showMessageDialog(this, "User deleted.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.add(new ManageUsersPanel ());
        frame.setBounds(200, 200, 400, 500);
        frame.setVisible(true);
        
    }
}

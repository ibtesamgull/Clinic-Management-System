package clinicmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ChangePasswordPanel extends JPanel {
    private JPasswordField currentPasswordField, newPasswordField, confirmPasswordField;
    private JButton changeButton;
    private int userId;

    public ChangePasswordPanel(int userId) {
        this.userId = userId;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Change Password");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;

        addLabel("Current Password:", 0, 1, gbc);
        currentPasswordField = addPasswordField(1, 1, gbc);

        addLabel("New Password:", 0, 2, gbc);
        newPasswordField = addPasswordField(1, 2, gbc);

        addLabel("Confirm New Password:", 0, 3, gbc);
        confirmPasswordField = addPasswordField(1, 3, gbc);

        changeButton = new JButton("Change Password");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(changeButton, gbc);

        changeButton.addActionListener(e -> changePassword());
    }

    private void addLabel(String text, int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x;
        gbc.gridy = y;
        add(new JLabel(text), gbc);
    }

    private JPasswordField addPasswordField(int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x;
        gbc.gridy = y;
        JPasswordField pf = new JPasswordField(15);
        add(pf, gbc);
        return pf;
    }

    private void changePassword() {
        String current = new String(currentPasswordField.getPassword());
        String newPass = new String(newPasswordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());

        if (!newPass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "New passwords do not match.");
            return;
        }

        try {
            Conn conn = new Conn();
            String checkQuery = "SELECT * FROM users WHERE user_id = ? AND password = ?";
            PreparedStatement checkStmt = conn.conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, userId);
            checkStmt.setString(2, current);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                String update = "UPDATE users SET password = ? WHERE user_id = ?";
                PreparedStatement updateStmt = conn.conn.prepareStatement(update);
                updateStmt.setString(1, newPass);
                updateStmt.setInt(2, userId);
                updateStmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Password updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Current password is incorrect.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.add(new ChangePasswordPanel(22));
        frame.setBounds(200, 200, 400, 500);
        frame.setVisible(true);
        
    }
}

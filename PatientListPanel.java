package clinicmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ChangePasswordFrame extends JFrame {
    private JPasswordField newPasswordField, confirmPasswordField;
    private JButton changeBtn, cancelBtn;
    private int userId;

    public ChangePasswordFrame(int userId) {
        this.userId = userId;
        setTitle("Change Password");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel newPasswordLabel = new JLabel("New Password:");
        gbc.gridx = 0; gbc.gridy = 0;
        add(newPasswordLabel, gbc);

        newPasswordField = new JPasswordField();
        gbc.gridx = 1; gbc.gridy = 0;
        add(newPasswordField, gbc);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        gbc.gridx = 0; gbc.gridy = 1;
        add(confirmPasswordLabel, gbc);

        confirmPasswordField = new JPasswordField();
        gbc.gridx = 1; gbc.gridy = 1;
        add(confirmPasswordField, gbc);

        changeBtn = new JButton("Change");
        gbc.gridx = 0; gbc.gridy = 2;
        add(changeBtn, gbc);

        cancelBtn = new JButton("Cancel");
        gbc.gridx = 1; gbc.gridy = 2;
        add(cancelBtn, gbc);

        changeBtn.addActionListener(e -> handleChangePassword());
        cancelBtn.addActionListener(e -> dispose());
    }

    private void handleChangePassword() {
        String newPass = new String(newPasswordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());

        if (newPass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields cannot be empty.");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }

        try {
            Conn conn = new Conn();
            String sql = "UPDATE users_tbl SET password_hash = ?, first_login = FALSE WHERE user_id = ?";
            PreparedStatement pst = conn.conn.prepareStatement(sql);
            pst.setString(1, newPass); // In production, hash the password!
            pst.setInt(2, userId);
            int res = pst.executeUpdate();

            if (res > 0) {
                JOptionPane.showMessageDialog(this, "Password updated successfully. Please login again.");
                dispose();
                SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));

                
            } else {
                JOptionPane.showMessageDialog(this, "Update failed.");
            }

            pst.close();
            conn.conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

}

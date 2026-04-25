package clinicmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginPage extends JFrame {

    private JTextField userField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JButton loginBtn, cancelBtn;

    public LoginPage() {
        setTitle("Clinic Management System - Login");
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Clinic Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 30, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(userLabel, gbc);

        userField = new JTextField();
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        mainPanel.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(passLabel, gbc);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel roleLabel = new JLabel("Login as:");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(roleLabel, gbc);

        roleCombo = new JComboBox<>(new String[]{"Admin", "Doctor", "Receptionist"});
        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        mainPanel.add(roleCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        loginBtn = new JButton("Login");
        styleButton(loginBtn, new Color(45, 140, 240));
        loginBtn.addActionListener(e -> loginAction());
        mainPanel.add(loginBtn, gbc);

        cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn, new Color(200, 50, 50));
        cancelBtn.addActionListener(e -> System.exit(0));
        gbc.gridx = 1;
        mainPanel.add(cancelBtn, gbc);

        add(mainPanel);
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
    }

    private void loginAction() {
        String username = userField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = roleCombo.getSelectedItem().toString().toLowerCase();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.");
            return;
        }

        try {
            Conn conn = new Conn();
            String sql = "SELECT * FROM users_tbl WHERE username = ? AND password_hash = ? AND role = ?";
            PreparedStatement pst = conn.conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, role);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                boolean isFirstLogin = rs.getBoolean("first_login");
                if (isFirstLogin) {
                    dispose();
                    new ChangePasswordFrame(rs.getInt("user_id")).setVisible(true);
                } else {
                    dispose();
                    new AdminDashboard(username, role).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials or role.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during login: " + e.getMessage());
        }
    }

}

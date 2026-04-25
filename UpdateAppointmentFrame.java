package clinicmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class GenerateBillPanel extends JPanel {
    private JComboBox<String> appointmentCombo;
    private JTextField amountField, taxField, methodField;
    private JButton generateBtn;
    double totalAmount;
    double tax;

    
    public GenerateBillPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Generate Bill");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;

        // Appointment selection
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Appointment ID:"), gbc);
        appointmentCombo = new JComboBox<>();
        loadAppointments();
        gbc.gridx = 1;
        add(appointmentCombo, gbc);

        // Amount
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Amount:"), gbc);
        amountField = new JTextField();
        gbc.gridx = 1;
        add(amountField, gbc);

        // Tax
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Total Tax:"), gbc);
        taxField = new JTextField();
        gbc.gridx = 1;
        add(taxField, gbc);

        // Payment Method
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Payment Method:"), gbc);
        methodField = new JTextField();
        gbc.gridx = 1;
        add(methodField, gbc);

        // Generate button
        generateBtn = new JButton("Generate Bill");
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        add(generateBtn, gbc);
        amountField.setEditable(false);
        taxField.setEditable(false);
        loadAmount();
        generateBtn.addActionListener(e -> generateBill());
        
        appointmentCombo.addItemListener(e -> loadAmount());
    }

    private void loadAppointments() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.stm.executeQuery("SELECT appointment_id FROM appointment_tbl");
            while (rs.next()) {
                appointmentCombo.addItem(rs.getString("appointment_id"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       
    }
    private void loadAmount(){
        String appointmentId = (String) appointmentCombo.getSelectedItem();
        try {
        Conn c = new Conn();
        //fetch doctor fee
        double doctorFees=0.0;
        String feeQuery = "SELECT d.fees from doctors_tbl d "
                + "JOIN appointment_tbl a ON a.doctor_id = d.doctor_id "
                + "WHERE a.appointment_id = '"+appointmentId+"'";
        ResultSet rs = c.stm.executeQuery(feeQuery);
        if(rs.next()){
           doctorFees = rs.getDouble("fees");
        }
     
        //fetch medicine price
        double price = 0.0;
        String medicineQuery = "SELECT price from medicine_tbl m "
                + "JOIN prescription_medicines pm ON m.medicine_id = pm.medicine_id "
                + "JOIN prescriptions_tbl p ON pm.prescription_id = p.prescription_id "
                + "JOIN appointment_tbl a ON p.appointment_id = a.appointment_id "
                + "WHERE a.appointment_id = '"+appointmentId+"'";
                rs = c.stm.executeQuery(medicineQuery);
                while(rs.next()){
                  price += rs.getDouble("price");
                }
        //total price + fees
         totalAmount = doctorFees + price;
        amountField.setText(totalAmount+"");
         tax = totalAmount * 0.02;
        taxField.setText(tax+"");
        }catch(Exception e){
         JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    private void generateBill() {
    String appointmentId = (String) appointmentCombo.getSelectedItem();
    String method = methodField.getText().trim();

    // Basic Validation
    if (appointmentId == null || appointmentId.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please select an appointment.");
        return;
    }

    if (  method.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields.");
        return;
    }
    try{
                Conn c = new Conn();

    
        String query = "SELECT patient_id FROM appointment_tbl WHERE appointment_id = ?";
        PreparedStatement fetch = c.conn.prepareStatement(query);
        fetch.setString(1, appointmentId);
        ResultSet rs = fetch.executeQuery();
        
        

        if (rs.next()) {
            String patientId = rs.getString("patient_id");

            String sql = "INSERT INTO bills (patient_id, appointment_id, amount, total_tax, payment_method, billing_date, paid) " +
                         "VALUES (?, ?, ?, ?, ?, NOW(), false)";
            PreparedStatement ps = c.conn.prepareStatement(sql);
            ps.setString(1, patientId);
            ps.setString(2, appointmentId);
            ps.setDouble(3, totalAmount);
            ps.setDouble(4, tax);
            ps.setString(5, method);

            int res = ps.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(this, "Bill generated successfully!");
                amountField.setText("");
                taxField.setText("");
                methodField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Bill generation failed.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "No patient found for selected appointment.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
    }
}
}



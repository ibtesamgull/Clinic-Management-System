package clinicmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewAppointmentFrame extends JFrame {
    JLabel appointmentIdLabel, patientNameLabel, doctorNameLabel, phoneLabel,
           dateLabel, timeLabel, problemLabel, sequenceLabel,status;

    public ViewAppointmentFrame(String appointmentID) {
        setTitle("View Appointment - " + appointmentID);
        setSize(650, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 14);

        int row = 0;

        addLabel("Appointment ID:", 0, row, labelFont, gbc);
        appointmentIdLabel = addValue("-", 1, row++, valueFont, gbc);

        addLabel("Patient Name:", 0, row, labelFont, gbc);
        patientNameLabel = addValue("-", 1, row, valueFont, gbc);

        addLabel("Doctor Name:", 2, row, labelFont, gbc);
        doctorNameLabel = addValue("-", 3, row++, valueFont, gbc);

        addLabel("Phone Number:", 0, row, labelFont, gbc);
        phoneLabel = addValue("-", 1, row, valueFont, gbc);

        addLabel("Appointment Date:", 2, row, labelFont, gbc);
        dateLabel = addValue("-", 3, row++, valueFont, gbc);

        addLabel("Time:", 0, row, labelFont, gbc);
        timeLabel = addValue("-", 1, row, valueFont, gbc);

        addLabel("Sequence:", 2, row, labelFont, gbc);
        sequenceLabel = addValue("-", 3, row++, valueFont, gbc);

        addLabel("Problem:", 0, row, labelFont, gbc);
        problemLabel = addValue("-", 1, row++, valueFont, gbc, 3);
        
        addLabel("Status:", 2, row, labelFont, gbc);

        status = addValue("-", 3, row++, valueFont, gbc);


        fetchAppointmentData(appointmentID);
    }

    private void addLabel(String text, int x, int y, Font font, GridBagConstraints gbc) {
        gbc.gridx = x;
        gbc.gridy = y;
        JLabel label = new JLabel(text);
        label.setFont(font);
        add(label, gbc);
    }

    private JLabel addValue(String text, int x, int y, Font font, GridBagConstraints gbc) {
        return addValue(text, x, y, font, gbc, 1);
    }

    private JLabel addValue(String text, int x, int y, Font font, GridBagConstraints gbc, int colspan) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = colspan;

        JLabel valueLabel = new JLabel(text);
        valueLabel.setFont(font);
        valueLabel.setForeground(new Color(51, 51, 51));
        valueLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        add(valueLabel, gbc);
        gbc.gridwidth = 1; // reset

        return valueLabel;
    }

    private void fetchAppointmentData(String appointmentID) {
        try {
            Conn conn = new Conn();
            String query = "SELECT a.*, " +
                           "p.given_name AS patient_first_name,"
                        + " p.family_name AS patient_last_name," +
                           "d.name AS doctor_name ,"
                    + "status " +
                           "FROM appointment_tbl a " +
                           "JOIN patient_tbl p ON a.patient_id = p.patient_id " +
                           "JOIN doctors_tbl d ON a.doctor_id = d.doctor_id " +
                           "WHERE a.appointment_id = ?";
            PreparedStatement pst = conn.conn.prepareStatement(query);
            pst.setString(1, appointmentID);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                appointmentIdLabel.setText(rs.getString("appointment_id"));
                patientNameLabel.setText(rs.getString("patient_first_name")+" "+rs.getString("patient_last_name"))  ;
                doctorNameLabel.setText(rs.getString("doctor_name"));
                phoneLabel.setText(rs.getString("phone_number"));
                dateLabel.setText(rs.getString("appointment_date"));
                timeLabel.setText(rs.getString("appointment_time"));
                problemLabel.setText(rs.getString("problem"));
                sequenceLabel.setText(String.valueOf(rs.getInt("sequence")));
                status.setText(rs.getString("status"));
            } else {
                JOptionPane.showMessageDialog(this, "No appointment found with ID: " + appointmentID);
                dispose();
            }

            rs.close();
            pst.close();
            conn.conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
        }
    }

    // Test main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewAppointmentFrame("AP001").setVisible(true));
    }
}

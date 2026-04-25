/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicmanagementsystem;
import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddAppointmentPanel extends JPanel {

    public JTextField appointmentIdField, phoneField, dateField, problemField, getDateField;
    public JComboBox<String> patientCombo, doctorCombo, timeCombo;
    public JButton addBtn, backBtn, clearBtn;
    
   

    public AddAppointmentPanel() {
       setLayout(null);
        setBackground(new Color(240, 240, 240)); // Light background

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 20);
        Color darkGray = new Color(51, 51, 51);

        JLabel title = new JLabel("Appointment Management");
        title.setFont(titleFont);
        title.setForeground(darkGray);
        title.setBounds(180, 15, 350, 30);
        add(title);
        // Labels + Fields
        addLabel("Appointment ID", 40, 60, labelFont, darkGray);
        appointmentIdField = addTextField(160, 60, darkGray);
        appointmentIdField.setEditable(false);
        appointmentIdField.setText(generateAppointmentID());

        addLabel("Patient ID", 340, 60, labelFont, darkGray);
        patientCombo = addComboBox(440, 60);

        addLabel("Phone No.", 40, 100, labelFont, darkGray);
        phoneField = addTextField(160, 100, darkGray);

        addLabel("Doctor ID", 340, 100, labelFont, darkGray);
        doctorCombo = addComboBox(440, 100);

        addLabel("Date", 40, 140, labelFont, darkGray);
        dateField = addTextField(160, 140, darkGray);

        addLabel("Problem", 340, 140, labelFont, darkGray);
        problemField = addTextField(440, 140, darkGray);

        addLabel("Time", 40, 180, labelFont, darkGray);
        timeCombo = addComboBox(160, 180);
        
        timeCombo.addItemListener(e-> {
        loadPatientCombo();
                loadDoctorCombo();
        });
        
        addLabel("Get Date", 340, 180, labelFont, darkGray);
        addLabel(java.time.LocalDateTime.now().toString(),440, 180,labelFont, darkGray);   
        
        
        
        
    String[] amPm = {"AM", "PM"};
    for (int hour = 9; hour <= 17; hour++) {
    for (int min = 0; min < 60; min += 30) {
        int displayHour = (hour == 12 || hour == 0) ? 12 : hour % 12;
        String timeStr = String.format("%02d:%02d %s", displayHour, min, amPm[hour / 12]);
        timeCombo.addItem(timeStr);
    }
}
                loadPatientCombo();
                loadDoctorCombo();
             
        // Buttons
        addBtn = addButton("Add", 190, 250);
        backBtn = addButton("Back", 280, 250);
        clearBtn = addButton("Clear", 370, 250);
        
        addBtn.addActionListener(e->{
            addAppointment();
        });
        clearBtn.addActionListener(e->clearForm());
    }
    
    private void clearForm() {
    phoneField.setText("");
    dateField.setText("");
    problemField.setText("");
 // or LocalDate.now().toString() if preferred

    if (patientCombo.getItemCount() > 0) patientCombo.setSelectedIndex(0);
    if (doctorCombo.getItemCount() > 0) doctorCombo.setSelectedIndex(0);
    if (timeCombo.getItemCount() > 0) timeCombo.setSelectedIndex(0);
}

    private void addLabel(String text, int x, int y, Font font, Color fg) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(fg);
        label.setBounds(x, y, 170, 25);
        add(label);
    }

    private JTextField addTextField(int x, int y, Color fg) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 150, 28);
        tf.setBackground(Color.WHITE);
        tf.setForeground(fg);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        add(tf);
        return tf;
    }

    private JComboBox<String> addComboBox(int x, int y) {
        JComboBox<String> cb = new JComboBox<>();
        cb.setBounds(x, y, 150, 28);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        add(cb);
        return cb;
    }

    private JButton addButton(String text, int x, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 80, 30);
        btn.setBackground(new Color(200, 200, 200));
        btn.setForeground(new Color(60, 60, 60));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        add(btn);
        return btn;
    }
    
    private void loadPatientCombo(){
        try {
    String time = timeCombo.getSelectedItem().toString();
    dateField.setText("2025-07-03");
    String date = dateField.getText();

    // Format time
    SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a");
    SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
    String formattedTime = outputFormat.format(inputFormat.parse(time));

    Conn conn = new Conn();
    String q = "SELECT DISTINCT p.patient_id FROM patient_tbl p " +
               "LEFT JOIN appointment_tbl app ON p.patient_id = app.patient_id " +
               "AND app.appointment_date = ? AND TIME_FORMAT(app.appointment_time, '%H:%i') = ? " +
               "WHERE app.appointment_id IS NULL";

    PreparedStatement pst = conn.conn.prepareStatement(q);
    pst.setString(1, date);
    pst.setString(2, formattedTime);

    ResultSet rs = pst.executeQuery();
    patientCombo.removeAllItems();

    while (rs.next()) {
        String patientId = rs.getString("patient_id");
        patientCombo.addItem(patientId);
    }

    patientCombo.revalidate();
    patientCombo.repaint();
} catch (Exception e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(null, "Error loading patients: " + e.getMessage());
}
    }
    
     private void loadDoctorCombo(){
        try {
    String time = timeCombo.getSelectedItem().toString();
        dateField.setText("2025-07-03");
    String date = dateField.getText();

    // Format time
    SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a");
    SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
    String formattedTime = outputFormat.format(inputFormat.parse(time));

    Conn conn = new Conn();
    String q = "SELECT DISTINCT d.doctor_id,d.name FROM doctors_tbl d " +
               "LEFT JOIN appointment_tbl app ON d.doctor_id = app.doctor_id " +
               "AND app.appointment_date = ? AND TIME_FORMAT(app.appointment_time, '%H:%i') = ? " +
               "WHERE app.appointment_id IS NULL";

    PreparedStatement pst = conn.conn.prepareStatement(q);
    pst.setString(1, date);
    pst.setString(2, formattedTime);

    ResultSet rs = pst.executeQuery();
    doctorCombo.removeAllItems();

    while (rs.next()) {
        String doctorId = rs.getString("doctor_id")+" - "+ rs.getString("name");
        doctorCombo.addItem(doctorId);
    }

    doctorCombo.revalidate();
    doctorCombo.repaint();
} catch (Exception e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(null, "Error loading doctors: " + e.getMessage());
}
    }
     
     private void addAppointment() {
    try {
        String patient = (String) patientCombo.getSelectedItem();
        String doctor = (String) doctorCombo.getSelectedItem();
        String phone = phoneField.getText().trim();
        String date = dateField.getText().trim();
        String problem = problemField.getText().trim();
        String timeStr = (String) timeCombo.getSelectedItem();

        if (patient == null || doctor == null || phone.isEmpty() || date.isEmpty() || problem.isEmpty() || timeStr == null) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.");
            return;
        }

        if (!phone.matches("\\d{10,15}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be 10-15 digits.");
            return;
        }

        // Optional: validate date format (yyyy-MM-dd)
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Date must be in format yyyy-MM-dd.");
            return;
        }

        Conn c = new Conn();
        appointmentIdField.setText(generateAppointmentID());

        String seqQuery = "SELECT MAX(sequence) FROM appointment_tbl WHERE appointment_date = ?";
        PreparedStatement ps = c.conn.prepareStatement(seqQuery);
        ps.setDate(1, java.sql.Date.valueOf(date));
        ResultSet rs2 = ps.executeQuery();

        int nextSequence = 1;
        if (rs2.next() && rs2.getInt(1) != 0) {
            nextSequence = rs2.getInt(1) + 1;
        }

        String insert = "INSERT INTO appointment_tbl " +
                "(appointment_id, patient_id, phone_number, doctor_id, sequence ,problem, get_date_time, appointment_date, appointment_time) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pst = c.conn.prepareStatement(insert);
        pst.setString(1, generateAppointmentID());
        pst.setString(2, patient);
        pst.setString(3, phone);
        pst.setString(4, doctor.split(" - ")[0]); // doctor_id only
        pst.setInt(5, nextSequence);
        pst.setString(6, problem);
        pst.setTimestamp(7, new java.sql.Timestamp(System.currentTimeMillis()));
        pst.setDate(8, java.sql.Date.valueOf(date));

        // Convert time to 24hr format
        SimpleDateFormat inFormat = new SimpleDateFormat("hh:mm a");
        java.util.Date parsedTime = inFormat.parse(timeStr);
        pst.setTime(9, new java.sql.Time(parsedTime.getTime()));

        int res = pst.executeUpdate();
        if (res > 0) {
            JOptionPane.showMessageDialog(null, "Appointment added successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "Insert failed.");
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}

     
     private String generateAppointmentID(){
         try{
         Conn c = new Conn();
             // Get the latest appointment_id
        String q = "SELECT appointment_id FROM appointment_tbl ORDER BY appointment_id DESC LIMIT 1";
        ResultSet rs = c.stm.executeQuery(q);

        String lastId = "AP000";
        if (rs.next()) {
    lastId = rs.getString(1); // e.g., "AP025"
        }
    int num = Integer.parseInt(lastId.substring(2)) + 1;
    String newId = String.format("AP%03d", num);
    return newId;
         }
         catch(Exception e){
             e.printStackTrace();
         }
         return null;
     }

    // For testing
    public static void main(String[] args) {
        JFrame frame = new JFrame("Appointment Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 380); // final size
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new AddAppointmentPanel());
        frame.setVisible(true);
    }


}


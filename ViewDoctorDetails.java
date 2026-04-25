/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicmanagementsystem;

import java.awt.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.*;


/**
 *
 * @author Uni
 */
public class MyAppointments extends JPanel {
    JTable table;
    DefaultTableModel model;
    JComboBox<String> showEntries;
    JTextField searchField;
    int currentPage = 1;
    int entriesPerPage = 10;
    int totalPages = 1;
    JLabel pageLabel; 
    JButton completeBtn, cancelBtn;
    String username,role;


    public MyAppointments(String username,String role) {
        this.username = username;
        this.role = role;
        setLayout(new BorderLayout());
        setBackground(Color.white);

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JLabel title = new JLabel("Appointment List", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel showLabel = new JLabel("Show: ");
        String[] entries = {"10", "25", "50", "100"};
        showEntries = new JComboBox<>(entries);

        leftPanel.add(Box.createHorizontalStrut(20));
        leftPanel.add(showLabel);
        leftPanel.add(showEntries);

        JLabel searchLabel = new JLabel("Search: ");
        searchField = new JTextField(15);
        rightPanel.add(searchLabel);
        rightPanel.add(searchField);
        
         searchField.addActionListener(e -> searchAppointments());
        showEntries.addActionListener(e -> loadAppointments());

        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Patient", "Doctor", "Phone", "Date", "Time", "Problem", "Action"};
        model = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };

        table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);

        table.getColumn("Action").setCellRenderer(new ActionRenderer());
        table.getColumn("Action").setCellEditor(new ActionEditor());

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        
        //pagination
       // Pagination controls
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton prevButton = new JButton("Previous");
         pageLabel = new JLabel(currentPage+"");
        JButton nextButton = new JButton("Next");
        
        prevButton.addActionListener(e -> {
    if (currentPage > 1) {
        currentPage--;
        loadAppointments();
    }
});

nextButton.addActionListener(e -> {
    if (currentPage < totalPages) {
        currentPage++;
        loadAppointments();
    }
});

showEntries.addItemListener(aListener -> {
    entriesPerPage = Integer.parseInt(showEntries.getSelectedItem().toString());
    currentPage = 1;
    loadAppointments();
});
        
        paginationPanel.add(prevButton);
        paginationPanel.add(pageLabel);
        paginationPanel.add(nextButton);
        
        // Wrap both pagination and action buttons in one panel
JPanel bottomPanel = new JPanel(new BorderLayout());

// Status action buttons (bottom-left)
JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
completeBtn = new JButton("Mark as Completed");
cancelBtn = new JButton("Mark as Cancelled");

styleStatusButton(completeBtn, new Color(39, 174, 96));
styleStatusButton(cancelBtn, new Color(231, 76, 60));

statusPanel.add(completeBtn);
statusPanel.add(cancelBtn);

// Add listeners
completeBtn.addActionListener(e -> handleStatusUpdate("Completed"));
cancelBtn.addActionListener(e -> handleStatusUpdate("Cancelled"));

// Add both panels to bottomPanel
bottomPanel.add(statusPanel, BorderLayout.WEST);
bottomPanel.add(paginationPanel, BorderLayout.EAST);

// Finally add to main panel
add(bottomPanel, BorderLayout.SOUTH);

        showEntries.addItemListener(aListener -> {
            model.setRowCount(0);
            loadAppointments();
                    } );
        
        loadAppointments();
  
    }
    private void styleStatusButton(JButton button, Color color) {
    button.setBackground(color);
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
}
private void handleStatusUpdate(String status) {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select an appointment first.");
        return;
    }

    String appointmentId = table.getValueAt(selectedRow, 0).toString();
    int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to mark appointment " + appointmentId + " as " + status + "?",
            "Confirm Status Update", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        updateStatus(appointmentId, status);
    }
}
private void updateStatus(String appointmentId, String status) {
    try {
        Conn conn = new Conn();
        String q = "UPDATE appointment_tbl SET status = ? WHERE appointment_id = ?";
        PreparedStatement pst = conn.conn.prepareStatement(q);
        pst.setString(1, status);
        pst.setString(2, appointmentId);
        int rows = pst.executeUpdate();
        pst.close();
        conn.conn.close();
        if (rows > 0) {
            JOptionPane.showMessageDialog(this, "Status updated to " + status + " for ID: " + appointmentId);
            loadAppointments();
        } else {
            JOptionPane.showMessageDialog(this, "Update failed.");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Status update error: " + e.getMessage());
    }
}

    class ActionRenderer extends DefaultTableCellRenderer {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton updateBtn, deleteBtn, viewBtn;

        public ActionRenderer() {
            try {
                viewBtn = createIconButton("icons/view.png");
                updateBtn = createIconButton("icons/update.png");
                deleteBtn = createIconButton("icons/delete.png");

                panel.add(viewBtn);
                panel.add(updateBtn);
                panel.add(deleteBtn);
            } catch (Exception e) {
                System.err.println("Error loading icons.");
            }
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return panel;
        }
    }

    class ActionEditor extends AbstractCellEditor implements TableCellEditor {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton updateBtn, viewBtn;

        public ActionEditor() {
            viewBtn = createIconButton("icons/view.png");
            updateBtn = createIconButton("icons/update.png");

                    
            panel.add(viewBtn);
            panel.add(updateBtn);
            
            viewBtn.addActionListener(e -> {
                int row = table.getEditingRow(); // 🟢 This gives the clicked row
               String appID = table.getValueAt(row, 0).toString();
               new ViewAppointmentFrame(appID).setVisible(true);
               stopCellEditing();

            });
            updateBtn.addActionListener(e -> {
                int row = table.getEditingRow();
                String appointmentID = table.getValueAt(row, 0).toString();
                // TODO: open UpdateAppointmentFrame(appointmentID)
                new UpdateAppointmentFrame(appointmentID);
                stopCellEditing();
            });

        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    private JButton createIconButton(String path) {
        URL url = ClassLoader.getSystemResource(path);
        ImageIcon icon = new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH));
        JButton button = new JButton(icon);
        button.setPreferredSize(new Dimension(30, 30));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }

    private void loadAppointments() {
        model.setRowCount(0);
    int offset = (currentPage - 1) * entriesPerPage;
        String doctor_id= null;
        try {
            Conn conn = new Conn();
            String query = "SELECT d.doctor_id from doctors_tbl d "
                    + "JOIN employees e ON d.email = e.email "
                    + "JOIN users_tbl u ON e.employee_id = u.employee_id "
                    + "WHERE u.username = '"+username+"' and u.role = '"+role+"'";
            ResultSet didRS = conn.stm.executeQuery(query);
            if(didRS.next()){
               doctor_id=  didRS.getString("doctor_id");
            }
            
             // Get total record count
        ResultSet countRS = conn.stm.executeQuery("SELECT COUNT(*) FROM appointment_tbl");
        if (countRS.next()) {
            int totalRecords = countRS.getInt(1);
            totalPages = (int) Math.ceil(totalRecords / (double) entriesPerPage);
        }
        
             // Load paginated records
        String sql = "SELECT appointment_id, patient_id, doctor_id, phone_number, appointment_date, appointment_time, problem FROM appointment_tbl "
                + "WHERE doctor_id = ? "+
                     "LIMIT ? OFFSET ?";
        PreparedStatement pst = conn.conn.prepareStatement(sql);
         pst.setString(1, doctor_id);
        pst.setInt(2, entriesPerPage);
        pst.setInt(3, offset);
        ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("appointment_id"),
                        rs.getString("patient_id"),
                        rs.getString("doctor_id"),
                        rs.getString("phone_number"),
                        rs.getString("appointment_date"),
                        rs.getString("appointment_time"),
                        rs.getString("problem"),
                        "Action"
                });
            }
                    pageLabel.setText("Page " + currentPage + " of " + totalPages);

            rs.close();
            conn.conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
        }
    }

    private void searchAppointments() {
                    boolean found = false;

        String input = searchField.getText().trim().toLowerCase();
        if (input.isEmpty()) {
            loadAppointments();
            return;
        }

        model.setRowCount(0);
     int offset = (currentPage - 1) * entriesPerPage;

        try {
            Conn conn = new Conn();
             // Get total record count
        ResultSet countRS = conn.stm.executeQuery("SELECT COUNT(*) FROM appointment_tbl");
        if (countRS.next()) {
            int totalRecords = countRS.getInt(1);
            totalPages = (int) Math.ceil(totalRecords / (double) entriesPerPage);
        }
            String sql = "SELECT * FROM appointment_tbl WHERE " +
                    "LOWER(appointment_id) LIKE ? OR " +
                    "LOWER(patient_id) LIKE ? OR " +
                    "LOWER(doctor_id) LIKE ? OR " +
                    "LOWER(problem) LIKE ? "
                    + "LIMIT ? OFFSET ?";
            PreparedStatement pst = conn.conn.prepareStatement(sql);
            pst.setInt(5, entriesPerPage);
               pst.setInt(6, offset);
            String keyword = "%" + input + "%";
            for (int i = 1; i <= 4; i++) {
                pst.setString(i, keyword);
            }
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                found = true;
                model.addRow(new Object[]{
                        rs.getString("appointment_id"),
                        rs.getString("patient_id"),
                        rs.getString("doctor_id"),
                        rs.getString("phone_number"),
                        rs.getString("appointment_date"),
                        rs.getString("appointment_time"),
                        rs.getString("problem"),
                        "Action"
                });
            }
        pageLabel.setText("Page " + currentPage + " of " + totalPages);

            rs.close();
            conn.conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage());
        }
        if(!found){
            JOptionPane.showMessageDialog(this, "No records found!");
        }
    }

}

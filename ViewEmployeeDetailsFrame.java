
package clinicmanagementsystem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

public class PatientListPanel extends JPanel {
    JComboBox<String> showEntries;
    DefaultTableModel model;
    JTable patientTable;
    JTextField searchField;
    int currentPage = 1;
    int entriesPerPage = 10;
    int totalPages = 1;
    JLabel pageLabel; 
    public PatientListPanel() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JLabel titleLabel = new JLabel("Patient List", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        // titleLabel moved to top panel

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
        searchField.addActionListener(e->{
            searchPatientData();
        });
        
        topPanel.add(titleLabel,BorderLayout.NORTH);
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Name", "Email", "Phone", "Blood Group","Action"};
         model = new DefaultTableModel(columnNames, 0){
              @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // only Action column is editable (for buttons)
         }
         };
         patientTable = new JTable(model);
        patientTable.setRowHeight(40);
        
        // Action buttons column renderer/editor
        patientTable.getColumn("Action").setCellRenderer(new PatientListPanel.ActionRenderer());
        patientTable.getColumn("Action").setCellEditor(new PatientListPanel.ActionEditor());
        
        patientTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        patientTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        patientTable.getTableHeader().setBackground(new Color(52, 73, 94));
        patientTable.getTableHeader().setForeground(Color.WHITE);
        patientTable.setGridColor(new Color(220, 220, 220));
        JScrollPane scrollPane = new JScrollPane(patientTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Pagination controls
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton prevButton = new JButton("Previous");
         pageLabel = new JLabel(currentPage+"");
        JButton nextButton = new JButton("Next");
        
         prevButton.addActionListener(e -> {
    if (currentPage > 1) {
        currentPage--;
        refreshTable();
    }
});

nextButton.addActionListener(e -> {
    if (currentPage < totalPages) {
        currentPage++;
        refreshTable();
    }
});

showEntries.addItemListener(aListener -> {
    entriesPerPage = Integer.parseInt(showEntries.getSelectedItem().toString());
    currentPage = 1;
    refreshTable();
});
        
        paginationPanel.add(prevButton);
        paginationPanel.add(pageLabel);
        paginationPanel.add(nextButton);

        add(paginationPanel, BorderLayout.SOUTH);
        showEntries.addItemListener(aListener -> {
            model.setRowCount(0);
            refreshTable();
                    } );
        
        refreshTable();
       
    }
    // Renderer for Action column
    class ActionRenderer extends DefaultTableCellRenderer {
        JPanel panel;
        JButton editBtn, viewBtn, deleteBtn;

        public ActionRenderer() {
            panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            
       URL updateIcon = ClassLoader.getSystemResource("icons/update.png");
       URL viewIcon = ClassLoader.getSystemResource("icons/view.png");
       URL deleteIcon = ClassLoader.getSystemResource("icons/delete.png");


        if (updateIcon == null || viewIcon== null || deleteIcon == null) {
    System.err.println("Icon not found!");
        } else {
    ImageIcon i = new ImageIcon(viewIcon);
    Image i1 = i.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    i = new ImageIcon(i1);
    viewBtn = new JButton(i);
    viewBtn.setPreferredSize(new Dimension(30, 30));
    panel.add(viewBtn);
    
     i = new ImageIcon(updateIcon);
     i1 = i.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    i = new ImageIcon(i1);
    editBtn = new JButton(i);
    editBtn.setPreferredSize(new Dimension(30, 30));    
    panel.add(editBtn);
    
     i = new ImageIcon(deleteIcon);
     i1 = i.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    i = new ImageIcon(i1);
    deleteBtn = new JButton(i);
    deleteBtn.setPreferredSize(new Dimension(30, 30));
    panel.add(deleteBtn);
    
    
        }
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return panel;
        }
    }

    // Editor for Action column
    class ActionEditor extends AbstractCellEditor implements TableCellEditor {
        JPanel panel;
        JButton editBtn, viewBtn, deleteBtn;

        public ActionEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
             URL updateIcon = ClassLoader.getSystemResource("icons/update.png");
       URL viewIcon = ClassLoader.getSystemResource("icons/view.png");
       URL deleteIcon = ClassLoader.getSystemResource("icons/delete.png");


        if (updateIcon == null || viewIcon== null || deleteIcon == null) {
    System.err.println("Icon not found!");
        } else {
            ImageIcon i = new ImageIcon(viewIcon);
    Image i1 = i.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    i = new ImageIcon(i1);
    viewBtn = new JButton(i);
    viewBtn.setPreferredSize(new Dimension(30, 30));
    panel.add(viewBtn);
    
     i = new ImageIcon(updateIcon);
     i1 = i.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    i = new ImageIcon(i1);
    editBtn = new JButton(i);
    editBtn.setPreferredSize(new Dimension(30, 30));    
    panel.add(editBtn);
    
     i = new ImageIcon(deleteIcon);
     i1 = i.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    i = new ImageIcon(i1);
    deleteBtn = new JButton(i);
    deleteBtn.setPreferredSize(new Dimension(30, 30));
    panel.add(deleteBtn);
        }
            
            // Button listeners (example)
            editBtn.addActionListener(e -> {
                int row = patientTable.getEditingRow(); // 🟢 This gives the clicked row
               String patient_id = patientTable.getValueAt(row, 0).toString();
               new UpdatePatientForm(patient_id).setVisible(true);
               stopCellEditing();

            });
            viewBtn.addActionListener(e -> {
               int row = patientTable.getEditingRow(); // 🟢 This gives the clicked row
               String patient_id = patientTable.getValueAt(row, 0).toString();
               new ViewPatientForm(patient_id).setVisible(true);
                stopCellEditing();
            });
            deleteBtn.addActionListener(e -> {
            int row = patientTable.getEditingRow(); // 🟢 This gives the clicked row
            String patientID = patientTable.getValueAt(row, 0).toString();
            int result = JOptionPane.showConfirmDialog(panel,
                    "Are U Sure U want to Delete Doctor ID : "+patientID+"?");
               if(result == JOptionPane.YES_OPTION){
                   deletePatient(patientID);
               }
               
            stopCellEditing();
            model.setRowCount(0);
            refreshTable();
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
    
    private void deletePatient(String id){
        try{
            Conn conn = new Conn();
            String query = "Delete from patient_tbl where patient_id = '"+id+"';";
            conn.stm.executeUpdate(query);
            JOptionPane.showMessageDialog( this,"Patient ID : "+id+" Deleted Successfully!");
            
        }
        catch(Exception e){
          JOptionPane.showMessageDialog( this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }
        currentPage = 1;
refreshTable();

    }
    private void refreshTable(){
         model.setRowCount(0);
    int offset = (currentPage - 1) * entriesPerPage;

        
         try {
             Conn conn = new Conn();
              // Get total record count
        ResultSet countRS = conn.stm.executeQuery("SELECT COUNT(*) FROM patient_tbl");
        if (countRS.next()) {
            int totalRecords = countRS.getInt(1);
            totalPages = (int) Math.ceil(totalRecords / (double) entriesPerPage);
        }
         // Load paginated records
        String sql = "SELECT patient_id, family_name,given_name, patient_email, mobile_number, blood_group FROM patient_tbl " +
                     "LIMIT ? OFFSET ?";
        PreparedStatement pst = conn.conn.prepareStatement(sql);
        pst.setInt(1, entriesPerPage);
        pst.setInt(2, offset);
             try (ResultSet rs = pst.executeQuery()) {
                 while (rs.next()) {
                     String id = rs.getString("patient_id");
                     String name = rs.getString("given_name") + " " + rs.getString("family_name");
                     String email = rs.getString("patient_email");
                     String phone = rs.getString("mobile_number");
                     String bloodGroup = rs.getString("blood_group");
                     model.addRow(new Object[]{id, name, email, phone, bloodGroup});
                 }
                 
                 pageLabel.setText("Page " + currentPage + " of " + totalPages);
             }
            conn.stm.close();
            conn.conn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void searchPatientData() {
    String searchInput = searchField.getText().trim().toLowerCase();
    if (searchInput.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a value to search.");
        return;
    }

    ResultSet rs = null;

    try {
        Conn conn = new Conn();

        String sql = "SELECT * FROM patient_tbl " +
                     "WHERE LOWER(given_name) LIKE ? " +
                     "   OR LOWER(family_name) LIKE ? " +
                     "   OR LOWER(patient_email) LIKE ? " +
                     "   OR LOWER(patient_phone) LIKE ? " +
                     "   OR LOWER(patient_id) LIKE ? " +
                     "   OR patient_id = ?";

        PreparedStatement pst = conn.conn.prepareStatement(sql);

        String wildcardSearch = "%" + searchInput + "%";

        pst.setString(1, wildcardSearch); // LIKE given_name
        pst.setString(2, wildcardSearch); // LIKE family_name
        pst.setString(3, wildcardSearch); // LIKE email
        pst.setString(4, wildcardSearch); // LIKE phone
        pst.setString(5, wildcardSearch); // LIKE phone


        try {
            pst.setInt(5, Integer.parseInt(searchInput));
        } catch (NumberFormatException e) {
            pst.setInt(5, -1); // unlikely ID
        }

        rs = pst.executeQuery();

        model.setRowCount(0); // Clear previous table rows

        boolean found = false;
        while (rs.next()) {
            found = true;
            String id = rs.getString("patient_id");
            String givenName = rs.getString("given_name");
            String familyName = rs.getString("family_name");
            String email = rs.getString("patient_email");
            String phone = rs.getString("patient_phone");
            model.addRow(new Object[]{id, givenName, familyName, email, phone});
        }

        if (!found) {
            refreshTable(); // Optional: show all if not found
            JOptionPane.showMessageDialog(this, "No records found for: " + searchInput);
        }

        rs.close();
        pst.close();
        conn.conn.close();

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database error:\n" + e.getMessage());
    }
}


   
}


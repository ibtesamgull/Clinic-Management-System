
package clinicmanagementsystem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

public class DoctorListPanel extends JPanel {
    JComboBox<String> showEntries;
    DefaultTableModel model;
    JTable doctorTable;
    JTextField searchField;
    int currentPage = 1;
    int entriesPerPage = 10;
    int totalPages = 1;
    JLabel pageLabel;  

    public DoctorListPanel() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JLabel titleLabel = new JLabel("Doctor List", SwingConstants.CENTER);
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
            searchData();
        });
        
        topPanel.add(titleLabel,BorderLayout.NORTH);
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Name", "Email", "Phone", "Specialist","Action"};
         model = new DefaultTableModel(columnNames, 0){
              @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // only Action column is editable (for buttons)
         }
         };
         doctorTable = new JTable(model);
        doctorTable.setRowHeight(40);
        
        // Action buttons column renderer/editor
        doctorTable.getColumn("Action").setCellRenderer(new DoctorListPanel.ActionRenderer());
        doctorTable.getColumn("Action").setCellEditor(new DoctorListPanel.ActionEditor());
        
        doctorTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        doctorTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        doctorTable.getTableHeader().setBackground(new Color(52, 73, 94));
        doctorTable.getTableHeader().setForeground(Color.WHITE);
        doctorTable.setGridColor(new Color(220, 220, 220));
        JScrollPane scrollPane = new JScrollPane(doctorTable);
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
                int row = doctorTable.getEditingRow(); // 🟢 This gives the clicked row
               String doctorID = doctorTable.getValueAt(row, 0).toString();
               new UpdateDoctorDetailsForm(doctorID).setVisible(true);
               stopCellEditing();

            });
            viewBtn.addActionListener(e -> {
               int row = doctorTable.getEditingRow(); // 🟢 This gives the clicked row
               String doctorID = doctorTable.getValueAt(row, 0).toString();
               new ViewDoctorDetails(doctorID).setVisible(true);
                stopCellEditing();
            });
            deleteBtn.addActionListener(e -> {
            int row = doctorTable.getEditingRow(); // 🟢 This gives the clicked row
            String doctorID =doctorTable.getValueAt(row, 0).toString();
            int result = JOptionPane.showConfirmDialog(panel,
                    "Are U Sure U want to Delete Doctor ID : "+doctorID+"?");
               if(result == JOptionPane.YES_OPTION){
                   deleteDoctor(doctorID);
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
   private void refreshTable() {
    model.setRowCount(0);
    int offset = (currentPage - 1) * entriesPerPage;

    try {
        Conn conn = new Conn();
        // Get total record count
        ResultSet countRS = conn.stm.executeQuery("SELECT COUNT(*) FROM doctors_tbl");
        if (countRS.next()) {
            int totalRecords = countRS.getInt(1);
            totalPages = (int) Math.ceil(totalRecords / (double) entriesPerPage);
        }

        // Load paginated records
        String sql = "SELECT doctor_id, name, email, phone_number, specialist FROM doctors_tbl " +
                     "LIMIT ? OFFSET ?";
        PreparedStatement pst = conn.conn.prepareStatement(sql);
        pst.setInt(1, entriesPerPage);
        pst.setInt(2, offset);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            String id = rs.getString("doctor_id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String phone = rs.getString("phone_number");
            String specialist = rs.getString("specialist");
            model.addRow(new Object[]{id, name, email, phone, specialist});
        }

        pageLabel.setText("Page " + currentPage + " of " + totalPages);

        rs.close();
        pst.close();
        conn.conn.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void searchData() {
    String searchInput = searchField.getText().trim().toLowerCase();
    if (searchInput.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Please enter a value to search.");
    return;
}

    ResultSet rs = null;

    try {
        Conn conn = new Conn(); // Assuming conn.conn is your Connection and conn.stm is your Statement

        // Smart search across name, specialist, and doctor_id
        String sql = "SELECT * FROM doctors_tbl " +
                     "WHERE LOWER(name) LIKE ? " +
                     "   OR LOWER(specialist) LIKE ? " +
                     "   OR LOWER(name) = ? " +
                     "   OR LOWER(specialist) = ? " +
                     "   OR doctor_id = ?";

        PreparedStatement pst = conn.conn.prepareStatement(sql);

        String wildcardSearch = "%" + searchInput + "%";

        pst.setString(1, wildcardSearch); // LIKE name
        pst.setString(2, wildcardSearch); // LIKE specialist
        pst.setString(3, searchInput);    // exact name
        pst.setString(4, searchInput);    // exact specialist

        // Try parsing doctor ID (assumes int); fallback to -1
        try {
            pst.setInt(5, Integer.parseInt(searchInput));
        } catch (NumberFormatException e) {
            pst.setInt(5, -1); // unlikely ID to prevent match
        }

        rs = pst.executeQuery();

        // Clear table before populating
        model.setRowCount(0);

        boolean found = false;
        while (rs.next()) {
            found = true;
            String id = rs.getString("doctor_id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String phone = rs.getString("phone_number");
            String specialist = rs.getString("specialist");
            model.addRow(new Object[]{id, name, email, phone, specialist});
        }

        if (!found) {
refreshTable();
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

    
    private void deleteDoctor(String id){
        String email=null;
        try{
            
            Conn conn = new Conn();
            String fetchEmail = "SELECT email from doctors_tbl where doctor_id = '"+id+"'";
            ResultSet emailRS = conn.stm.executeQuery(fetchEmail);
            if(emailRS.next()){
                email = emailRS.getString("email");
            }
            String query = "Delete from doctors_tbl where doctor_id = '"+id+"';";
            String query_employee = "Delete from employees where email = '"+email+"'";
            conn.stm.executeUpdate(query);
            conn.stm.execute(query_employee);
            JOptionPane.showMessageDialog( this,"Doctor ID : "+id+" Deleted Successfully!");
            
  
        }
        catch(Exception e){
          JOptionPane.showMessageDialog( this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }
        currentPage = 1;
    refreshTable();

        
    }
}


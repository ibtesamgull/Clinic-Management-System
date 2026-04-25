
package clinicmanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Conn {
    Connection conn;
    Statement stm;
    Conn(){
         try {
             conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/clinicmanagementsystem", "root", "sami300978");
             stm = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

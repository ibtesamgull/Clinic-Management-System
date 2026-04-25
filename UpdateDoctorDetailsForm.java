
package clinicmanagementsystem;

/**
 *
 * @author SAMI
 */
public class generateSalaries {
    public static void main(String[] args){
    try{
    Conn conn = new Conn();
        
        for(int i = 1;i<=86;i++){
    conn.stm.executeUpdate("INSERT INTO doctors_tbl (fees) "
        + "VALUES ("+2500.0+") WHERE doctor_id = '"+i+"'");
}
        
}catch(Exception e){
    e.printStackTrace();
}
    }
}

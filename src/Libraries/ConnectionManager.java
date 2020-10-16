package Libraries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    public static Connection GetConnection(){
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://wgudb.ucertify.com:3306/WJ07tam", "U07tam", "53689127651");
            return connection;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

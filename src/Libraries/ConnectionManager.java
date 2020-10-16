package Libraries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    public static Connection GetConnection(){
        try{
            String url = "jdbc:mysql://wgudb.ucertify.com:3306/WJ07tam";
            Properties info = new Properties();
            info.put("user", "U07tam");
            info.put("password", "53689127651");
            Connection conn = DriverManager.getConnection(url, info);
            return conn;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

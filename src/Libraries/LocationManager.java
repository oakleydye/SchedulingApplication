package Libraries;

import com.mysql.cj.jdbc.admin.TimezoneDump;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author oakleydye
 *
 * Helper class to do various tasks relating to a users location
 */
public class LocationManager {
    /**
     * Gets a users location based on their ip address
     * @return String, the state or other division name
     */
    public static String GetLocation(){
        try{
            String ip = GetIP();
            if (ip.equals("")){
                return "Location not found";
            }
            URL url = new URL("https://ipapi.co/" + ip + "/region");
            URLConnection conn = url.openConnection();
            String location;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))){
                location = reader.readLine();
            }
            return location;
        } catch (Exception ex){
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * Gets the offset from utc time based on the users location
     * @return String in the format of +/-08, the offset from utc
     */
    public static String GetOffset(){
        try{
            String ip = GetIP();
            if (ip.equals("")){
                return "Location not found";
            }
            URL url = new URL("https://ipapi.co/" + ip + "/utc_offset");
            URLConnection conn = url.openConnection();
            String offset;
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))){
                offset = reader.readLine();
            }
            return offset.replace("00", "");
        } catch (Exception ex){
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * This method returns the offset from UTC. The difference between the
     * above method and this is that the above is more accurate.
     * GetOffset() returns the offset based on the user's actual location,
     * this method pulls based on whatever timezone the user has put in their machine.
     * @return int version of the offset
     */
    public static long GetOffsetFromComputerSetting(){
        TimeZone zone = TimeZone.getDefault();
        return zone.getOffset(Calendar.getInstance().getTimeInMillis());
    }

    /**
     * Method to get the user ip address from an amazon api
     * @return string ip address
     */
    private static String GetIP(){
        try{
            URL url = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String ip = in.readLine();
            in.close();
            return ip;
        } catch (Exception ex){
            ex.printStackTrace();
            return "";
        }
    }
}

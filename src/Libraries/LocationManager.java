package Libraries;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class LocationManager {
    public static String GetLocation(){
        try{
            String ip = GetIP();
            URL url = new URL("https://ipapi.co/" + ip + "/region");
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "java-ipapi-client");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String location = reader.readLine();
            reader.close();
            return location;
        } catch (Exception ex){
            ex.printStackTrace();
            return "";
        }
    }

    private static String GetIP(){
        try{
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            String ip = in.readLine();
            in.close();
            return ip;
        } catch (Exception ex){
            ex.printStackTrace();
            return "";
        }
    }
}

package Libraries;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author oakleydye
 *
 * Helper class, interfaces with a google apps script to use google translate for translation of the app
 * This allows the app to be translated into any language, not just one.
 *
 * Google script is attached as well in Libraries.googleScript.js
 */
public class TranslationManager {
    public static String translate(String langFrom, String langTo, String text){
        try{
            String urlStr = "https://script.google.com/macros/s/AKfycbwPG4-909vaapT06umuD_MaU1QDvC2QT3n7qjYvyBB349Gq7IU/exec" +
                    "?q=" + URLEncoder.encode(text, StandardCharsets.UTF_8) +
                    "&target=" + langTo +
                    "&source=" + langFrom;
            URL url = new URL(urlStr);
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null){
                stringBuilder.append(inputLine);
            }
            in.close();
            return stringBuilder.toString().replace("&#39;", "\'");
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}

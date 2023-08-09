import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;

public class WeatherAPI {
    private static final String API_KEY = "YOUR_API_KEY_HERE";
    private static final String API_BASE_URL = "http://api.weatherapi.com/v1/current.json";

    public static JSONObject getWeatherData(String location) {
        try {
            URL url = new URL(API_BASE_URL + "?key=" + API_KEY + "&q=" + location);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return new JSONObject(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

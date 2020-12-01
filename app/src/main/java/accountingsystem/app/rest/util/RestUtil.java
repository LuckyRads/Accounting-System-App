package accountingsystem.app.rest.util;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestUtil {

    static final String SERVER_URL = "http://192.168.1.253:8080/accounting-system";

    public static String executeGet(String requestUrl, String urlParam) {
        try {
            URL url = new URL(SERVER_URL + requestUrl + urlParam);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "AccountingSystemApp");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Connection", "keep-alive");

            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuffer response = new StringBuffer();

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                return response.toString();
            } else {
                // TODO: alert user about server failure
                return "Server has returned a bad status.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            // TOOD: alert user about failure
            return "Failed to execute request.";
        }
    }

}

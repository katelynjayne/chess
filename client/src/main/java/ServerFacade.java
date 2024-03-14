import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
   private final String urlString;
   public ServerFacade(int port) {
      urlString = "http://localhost:" + port;
   }

   public AuthData register(String username, String password, String email) throws Exception {
      try {
         URL url = new URI(urlString + "/user").toURL();
         HttpURLConnection http = (HttpURLConnection) url.openConnection();
         http.setRequestMethod("POST");
         http.setDoOutput(true);
         OutputStream requestBody = http.getOutputStream();
         UserData request = new UserData(username, password, email);
         String json = new Gson().toJson(request);
         requestBody.write(json.getBytes());
         http.connect();
         if (http.getResponseCode() == 200) {
            if (http.getContentLength() < 0) {
               try (InputStream respBody = http.getInputStream()) {
                  InputStreamReader reader = new InputStreamReader(respBody);
                     return new Gson().fromJson(reader, AuthData.class);
               }
            }
         }
      }
      catch (Exception e){
         throw new Exception(e.getMessage());
      }
      throw new Exception("something janky happened");
   }
}

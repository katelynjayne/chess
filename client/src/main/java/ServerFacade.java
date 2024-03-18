import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import responseAndRequest.CreateGameRequest;
import responseAndRequest.CreateGameResponse;
import responseAndRequest.ExceptionResponse;
import responseAndRequest.ListGamesResponse;

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
      UserData request = new UserData(username, password, email);
      return sendRequest("/user", "POST", request, AuthData.class, null);
   }


   public AuthData login(String username, String password) throws Exception {
      UserData request = new UserData(username, password, null);
      return sendRequest("/session", "POST", request, AuthData.class, null);
   }

   public CreateGameResponse createGame(String authToken, String gameName) throws Exception {
      CreateGameRequest request = new CreateGameRequest(gameName);
      return sendRequest("/game", "POST", request, CreateGameResponse.class, authToken);
   }

   public ListGamesResponse listGames(String authToken) throws Exception {
      return sendRequest("/game", "GET", null, ListGamesResponse.class, authToken);
   }

   private <T> T sendRequest(String path, String method, Object requestObj, Class<T> responseClass, String auth) throws Exception{
      try {
         URL url = new URI(urlString + path).toURL();
         HttpURLConnection http = (HttpURLConnection) url.openConnection();
         http.setRequestMethod(method);
         http.setDoOutput(true);
         if (auth != null) {
            http.setRequestProperty("Authorization", auth);
         }
         if (requestObj != null) {
            OutputStream requestBody = http.getOutputStream();
            String json = new Gson().toJson(requestObj);
            requestBody.write(json.getBytes());
         }
         http.connect();
         if (http.getResponseCode() == 200) {
            if (http.getContentLength() < 0) {
               try (InputStream respBody = http.getInputStream()) {
                  InputStreamReader reader = new InputStreamReader(respBody);
                  return new Gson().fromJson(reader, responseClass);
               }
            }
            else {
               return null;
            }
         }
         else {
            InputStream error = http.getErrorStream();
            InputStreamReader reader = new InputStreamReader(error);
            ExceptionResponse response = new Gson().fromJson(reader, ExceptionResponse.class);
            throw new Exception(response.getMessage());
         }
      }
      catch (Exception e){
         throw new Exception(e.getMessage());
      }
   }
}
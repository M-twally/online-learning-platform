package logic.com;

import Db.com.UserData;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;

public class UserClient {

    private static final String USER_INFO_URL = "http://localhost:5000/user/getByMail";
    private static final String USER_INFO_URLD = "http://localhost:8000/user/getByMail";

    public static UserData fetchUserData(String userEmail) {
        try {
            Client client = ClientBuilder.newClient();

//            WebTarget target = client.target(USER_INFO_URL)
            WebTarget target = client.target(USER_INFO_URL)
                    .queryParam("email", userEmail);

            Response response = target.request(MediaType.APPLICATION_JSON)
                    .get();

            String responseContent = response.readEntity(String.class);
           System.out.println("Response Content: " + responseContent);

            JsonObject responseObject = Json.createReader(new StringReader(responseContent)).readObject();
            JsonObject userData = responseObject.getJsonObject("user");

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                UserData userDataObject = new UserData();
                userDataObject.setId(userData.getString("_id"));
                userDataObject.setName(userData.getString("name"));
                userDataObject.setEmail(userData.getString("email"));
                userDataObject.setRole(userData.getString("role"));

                System.out.println("Fetched User Data: " + userDataObject);
                return userDataObject;
            } else {
                throw new RuntimeException("Failed to fetch user data. Status: " + response.getStatus());
//                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching user data", e);
        }
    }



}



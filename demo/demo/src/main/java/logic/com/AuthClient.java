package logic.com;


import Db.com.UserData;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;


import javax.ws.rs.core.HttpHeaders;

public class AuthClient {

    private static final String AUTH_URL = "http://localhost:5000/user/auth";
    private static final String AUTH_URLD = "http://localhost:8000/user/auth";

    public static UserData verifyToken(String token) {
        try {
            Client client = ClientBuilder.newClient();

//            WebTarget target = client.target(AUTH_URL);
            WebTarget target = client.target(AUTH_URL);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            invocationBuilder.header(HttpHeaders.AUTHORIZATION, token); // Set the token directly

            // Debug statement to view the headers
            System.out.println("Authorization Header: " + token);

            Response response = invocationBuilder.get();

            String responseContent = response.readEntity(String.class);
            System.out.println("Response Content: " + responseContent);

            JsonObject responseObject = Json.createReader(new StringReader(responseContent)).readObject();
            JsonObject userData = responseObject.getJsonObject("user");

            if (response.getStatus() == Response.Status.OK.getStatusCode() && userData != null) {
                UserData userDataObject = new UserData();
                userDataObject.setId(userData.getString("_id"));
                userDataObject.setName(userData.getString("name"));
                userDataObject.setEmail(userData.getString("email"));
                userDataObject.setRole(userData.getString("role"));

                System.out.println("Fetched User Data: " + userDataObject);
                return userDataObject;
            } else {
                throw new RuntimeException("Failed to verify token or user data not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while verifying token", e);
        }
    }
}

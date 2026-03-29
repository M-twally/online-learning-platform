package logic.com;//import Db.com.Enrollment;
//
//import javax.json.Json;
//import javax.json.JsonArray;
//import javax.json.JsonObject;
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import java.io.StringReader;
//import java.util.ArrayList;
//import java.util.List;
//
//public class TemporaryEnrollmentClient {
//    private static final String TEMPORARY_ENROLLMENT_URL = "http://localhost:5003/enroll/sendTemporary-enrollments";
//
//    // Define a method to process the temporary enrollments received from the body
//    private static List<Enrollment> processTemporaryEnrollments(JsonArray temporaryEnrollmentsJson) {
//        List<Enrollment> temporaryEnrollments = new ArrayList<>();
//        for (int i = 0; i < temporaryEnrollmentsJson.size(); i++) {
//            JsonObject jsonObject = temporaryEnrollmentsJson.getJsonObject(i);
//
//            // Log the data before creating the Enrollment object
//            System.out.println("Received Enrollment Data: " + jsonObject);
//
//            Enrollment temporaryEnrollment = createEnrollmentFromJson(jsonObject);
//            if (temporaryEnrollment != null) {
//                temporaryEnrollments.add(temporaryEnrollment);
//            }
//        }
//        System.out.println("Fetched Temporary Enrollments: " + temporaryEnrollments);
//        return temporaryEnrollments;
//    }
//
//    // Method to create Enrollment object from JsonObject
//    private static Enrollment createEnrollmentFromJson(JsonObject jsonObject) {
//        try {
//            Enrollment temporaryEnrollment = new Enrollment(
//                    jsonObject.getString("courseName"),
//                    jsonObject.getString("category"),
//                    jsonObject.getString("instructorId"),
//                    jsonObject.getString("instructorName"),
//                    jsonObject.getString("studentName"),
//                    jsonObject.getString("studentEmail"),
//                    jsonObject.getString("courseId")
//            );
//            return temporaryEnrollment;
//        } catch (Exception e) {
//            System.out.println("Error creating Enrollment object: " + e.getMessage());
//            return null;
//        }
//    }
//
//    // Method to fetch temporary enrollments by sending a request
//    public static List<Enrollment> fetchTemporaryEnrollments() {
//        try {
//            Client client = ClientBuilder.newClient();
//
//            WebTarget target = client.target(TEMPORARY_ENROLLMENT_URL);
//
//            // Assuming you are sending an empty JSON object as the request body
//            JsonObject requestBody = Json.createObjectBuilder().build();
//
//            Response response = target.request(MediaType.APPLICATION_JSON)
//                    .post(Entity.json(requestBody));
//
//            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
//                String responseContent = response.readEntity(String.class);
//                JsonObject jsonResponse = Json.createReader(new StringReader(responseContent)).readObject();
//
//                if (jsonResponse.containsKey("data")) {
//                    JsonArray jsonArray = jsonResponse.getJsonArray("data");
//                    return processTemporaryEnrollments(jsonArray); // Process and return the fetched enrollments
//                } else {
//                    System.out.println("Unexpected response format: " + jsonResponse);
//                    return null;
//                }
//            } else {
//                // Handle error response
//                System.out.println("Failed to fetch temporary enrollments. Status code: " + response.getStatus());
//                return null;
//            }
//        } catch (Exception e) {
//            // Handle exception
//            System.out.println("Error fetching temporary enrollments: " + e.getMessage());
//            return null;
//        }
//    }
//}


import Db.com.Enrollment;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class TemporaryEnrollmentClient {
    private static final String TEMPORARY_ENROLLMENT_URL = "http://localhost:5003/enroll/sendTemporary-enrollments";

    // Method to fetch temporary enrollments by sending a request
    public static List<Enrollment> fetchTemporaryEnrollments() {
        try {
            Client client = ClientBuilder.newClient();

            WebTarget target = client.target(TEMPORARY_ENROLLMENT_URL);

            // Assuming you are sending an empty JSON object as the request body
            JsonObject requestBody = Json.createObjectBuilder().build();

            Response response = target.request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(requestBody));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                String responseContent = response.readEntity(String.class);
                JsonObject jsonResponse = Json.createReader(new StringReader(responseContent)).readObject();

                if (jsonResponse.containsKey("temporaryEnrollments")) {
                    JsonArray temporaryEnrollmentsJson = jsonResponse.getJsonArray("temporaryEnrollments");
                    return processTemporaryEnrollments(temporaryEnrollmentsJson); // Process and return the fetched enrollments
                } else {
                    System.out.println("No temporary enrollments found in the response: " + jsonResponse);
                    return null;
                }
            } else {
                // Handle error response
                System.out.println("Failed to fetch temporary enrollments. Status code: " + response.getStatus());
                return null;
            }
        } catch (Exception e) {
            // Handle exception
            System.out.println("Error fetching temporary enrollments: " + e.getMessage());
            return null;
        }
    }

    // Define a method to process the temporary enrollments received from the body
    private static List<Enrollment> processTemporaryEnrollments(JsonArray temporaryEnrollmentsJson) {
        List<Enrollment> temporaryEnrollments = new ArrayList<>();
        for (int i = 0; i < temporaryEnrollmentsJson.size(); i++) {
            JsonObject jsonObject = temporaryEnrollmentsJson.getJsonObject(i);
            Enrollment temporaryEnrollment = new Enrollment(
                    jsonObject.getString("courseName"),
                    jsonObject.getString("category"),
                    jsonObject.getString("instructorId"),
                    jsonObject.getString("instructorName"),
                    jsonObject.getString("studentName"),
                    jsonObject.getString("studentEmail"),
                    jsonObject.getString("courseId")
            );
            temporaryEnrollments.add(temporaryEnrollment);
        }
        System.out.println("List of Temporary Enrollments: " + temporaryEnrollments);
        return temporaryEnrollments;
    }
}

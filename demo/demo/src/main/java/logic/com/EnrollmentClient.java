package logic.com;

import Db.com.Enrollment;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;

public class EnrollmentClient {

    private static final String ENROLLMENT_INFO_URL = "http://localhost:8000/enrollment/getByInstructorAndCourse";

    public static Enrollment fetchEnrollmentData(String token, String instructorName, String courseName) {
        try {
            Client client = ClientBuilder.newClient();

            WebTarget target = client.target(ENROLLMENT_INFO_URL)
                    .queryParam("instructorName", instructorName)
                    .queryParam("courseName", courseName);

            Response response = target.request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .get();

            String responseContent = response.readEntity(String.class);
            System.out.println("Response Content: " + responseContent);

            JsonObject responseObject = Json.createReader(new StringReader(responseContent)).readObject();
            JsonObject enrollmentData = responseObject.getJsonObject("enrollment");

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                String coursename = enrollmentData.getString("courseName");
                String category = enrollmentData.getString("category");
                String instructorId = enrollmentData.getString("instructorId");
                String instructorname = enrollmentData.getString("instructorName");
                String studentName = enrollmentData.getString("studentName");
                String studentEmail = enrollmentData.getString("studentEmail");
                String courseId = enrollmentData.getString("courseId");

                Enrollment enrollmentDataObject = new Enrollment(coursename, category, instructorId, instructorname, studentName, studentEmail, courseId);

                System.out.println("Fetched Enrollment Data: " + enrollmentDataObject);
                return enrollmentDataObject;
            } else {
                // Handle error response
                return null;
            }
        } catch (Exception e) {
            // Handle exception
            return null;
        }
    }
}

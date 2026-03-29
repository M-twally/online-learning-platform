package logic.com;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class NumberClient {

    private static final String COURSE_UPDATE_URL = "http://localhost:5001/course/incrementNumberOfStudents";
    private static final String COURSE_UPDATE_URL2 = "http://localhost:5001/course/decrementNumberOfStudents";
    private static final String COURSE_UPDATE_URLD = "http://localhost:8001/course/incrementNumberOfStudents";
    private static final String COURSE_UPDATE_URL2D = "http://localhost:8001/course/decrementNumberOfStudents";

    public static boolean incrementCourse(String courseId) {
        try {
            Client client = ClientBuilder.newClient();
//            Response response = client.target(COURSE_UPDATE_URL)
            Response response = client.target(COURSE_UPDATE_URL)
                    .queryParam("courseId", courseId)
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.json(null)); // Empty body

            return response.getStatus() == Response.Status.OK.getStatusCode();
        } catch (Exception e) {
            System.out.println("Error occurred while updating course: " + e.getMessage());
            return false;
        }
    }

    public static boolean decrementCourse(String courseId) {
        try {
            Client client = ClientBuilder.newClient();
//            Response response = client.target(COURSE_UPDATE_URL2)
            Response response = client.target(COURSE_UPDATE_URL2)
                    .queryParam("courseId", courseId)
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.json(null)); // Empty body

            return response.getStatus() == Response.Status.OK.getStatusCode();
        } catch (Exception e) {
            System.out.println("Error occurred while updating course: " + e.getMessage());
            return false;
        }
    }
}

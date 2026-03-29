package logic.com;

import Db.com.CourseData;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;

public class CourseClient {

    private static final String COURSE_INFO_URL = "http://localhost:5001/course/getCourseByUserAndName";
    private static final String COURSE_INFO_URLD = "http://localhost:8001/course/getCourseByUserAndName";


    public static CourseData fetchCourseData(String userName, String courseName) {
    try {
        Client client = ClientBuilder.newClient();

//        WebTarget target = client.target(COURSE_INFO_URL)
        WebTarget target = client.target(COURSE_INFO_URL)
                .queryParam("instructorName", userName)
                .queryParam("courseName", courseName);

        Response response = target.request(MediaType.APPLICATION_JSON)
                .get();

        String responseContent = response.readEntity(String.class);
        System.out.println("Response Content: " + responseContent);

        JsonObject responseObject = Json.createReader(new StringReader(responseContent)).readObject();
        JsonObject responseData = responseObject.getJsonObject("responseData");

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            CourseData courseData = new CourseData();
            courseData.setUserId(responseData.getString("userId"));
            courseData.setUserName(responseData.getString("userName"));
            courseData.setUserEmail(responseData.getString("userEmail"));
            courseData.setCategory(responseData.getString("category"));
            courseData.setCourseName(responseData.getString("courseName"));
            courseData.setCourseId(responseData.getString("courseId"));

            System.out.println("Fetched Course Data: " + courseData);
            return courseData;
        } else {
            throw new RuntimeException("Failed to fetch course data. Status: " + response.getStatus());
//            return null;
        }
    } catch (Exception e) {
        throw new RuntimeException("Error occurred while fetching course data", e);
    }
}




}



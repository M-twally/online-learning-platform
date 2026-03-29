package logic.com;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import DB_Methods.DB_Methods;
import Db.com.*;
import org.bson.Document;


import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static DB_Methods.DB_Methods.*;

@Stateless
@Path("/students")
public class Student {

    /// student will enroll in the course ///
   @POST
   @Path("/enroll")
   @Consumes(MediaType.APPLICATION_JSON)
   public Response Enroll(@HeaderParam("Authorization")  String token,JsonObject jsonObject){
    try {
        System.out.println(token);
        System.out.println("Entered Enrollment");
        String instructorName = jsonObject.getString("instructorName");
        System.out.println(instructorName);
        String courseName = jsonObject.getString("courseName").toLowerCase();
        System.out.println("Course Name: " + courseName);
        if (!jsonObject.containsKey("instructorName")  || !jsonObject.containsKey("courseName")) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You must enter instructorName, email, and courseName").build();
        }
        if (token==null){
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build();
        }
        UserData userData=AuthClient.verifyToken(token);
        System.out.println(userData);
        System.out.println("Fetched User Data: " + userData);
        String Uname=userData.getName();
        String Uemail=userData.getEmail();
        String Urole=userData.getRole();
        String Uid=userData.getId();
        System.out.println("user Id: " + Uid);
        System.out.println("user Name is : "+Uname);
        System.out.println("user Email is : "+Uemail);
        System.out.println("user Role is : "+Urole);

        if (!Urole.equalsIgnoreCase("student")){
            return Response.status(Response.Status.BAD_REQUEST).entity("only student can enroll in course").build();
        }
        System.out.println("///////////////////////////////////////////////////");
        CourseData courseData = CourseClient.fetchCourseData(instructorName, courseName);
        System.out.println(courseData);
        System.out.println("Fetched Course Data: " + courseData);
        String Cname=courseData.getCourseName();
        String Ccategory=courseData.getCategory();
        String Cinstructor=courseData.getUserId();
        String Cinstructorname=courseData.getUserName();
        String Cuseremail=courseData.getUserEmail();
        String CcourseId=courseData.getCourseId();
        System.out.println("Course Name is : "+Cname);
        System.out.println("Course Category is : "+Ccategory);
        System.out.println("Course instructor is : "+Cinstructorname);
        System.out.println("Course instructor email  is : "+Cuseremail);
        System.out.println("Course id is : "+CcourseId);
        boolean isenrollementExists=DB_Methods.isUserEnrolled(Uid,Cname);
        System.out.println(isenrollementExists);
        if (isenrollementExists){
            return Response.status(Response.Status.CONFLICT).entity("Enrollment already exists").build();
        }
        Enrollment enrollment = new Enrollment(courseName, Ccategory, Cinstructor, Cinstructorname, Uname, Uemail,CcourseId);
        boolean enrollmentSaved = DB_Methods.saveEnrollment(enrollment,Uid);
        System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
        System.out.println(enrollmentSaved);
        if (enrollmentSaved) {
//                boolean result = NumberClient.incrementCourse(CcourseId);
//            return Response.ok("Enrollment saved successfully").build();
            return Response.status(Response.Status.OK).entity("Enrollment saved ").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to save enrollment").build();
        }
    } catch (Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error occurred during test user data retrieval: " + e.getMessage()).build();
    }
}


    /// student will delete his enrollment ///
    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response Delete(@HeaderParam("Authorization") String token,JsonObject jsonObject) {
        try {
            System.out.println("Entered Deleting");
            String courseName = jsonObject.getString("courseName").toLowerCase();
            System.out.println("Course Name: " + courseName);
            String instructorName = jsonObject.getString("instructorName");
            System.out.println(instructorName);
            if (!jsonObject.containsKey("courseName")|| !jsonObject.containsKey("instructorName")) {
                return Response.status(Response.Status.BAD_REQUEST).entity("You must enter courseName or instructorName").build();
            }
            UserData userData=AuthClient.verifyToken(token);
            System.out.println(userData);
            System.out.println("Fetched User Data: " + userData);
            String Uname=userData.getName();
            String Uemail=userData.getEmail();
            String Urole=userData.getRole();
            String Uid=userData.getId();
            System.out.println("user Id: " + Uid);
            System.out.println("user Name is : "+Uname);
            System.out.println("user Email is : "+Uemail);
            System.out.println("user Role is : "+Urole);


            if (!Urole.equalsIgnoreCase("student")){
                return Response.status(Response.Status.BAD_REQUEST).entity("only student can enroll in course").build();
            }
            System.out.println("///////////////////////////////////////////////////");
            CourseData courseData = CourseClient.fetchCourseData(instructorName, courseName);
            System.out.println(courseData);
            System.out.println("Fetched Course Data: " + courseData);
            String Cname=courseData.getCourseName();
            String Ccategory=courseData.getCategory();
            String Cinstructor=courseData.getUserId();
            String Cinstructorname=courseData.getUserName();
            String Cuseremail=courseData.getUserEmail();
            String CcourseId=courseData.getCourseId();
            System.out.println("Course Name is : "+Cname);
            System.out.println("Course Category is : "+Ccategory);
            System.out.println("Course instructor is : "+Cinstructorname);
            System.out.println("Course instructor email  is : "+Cuseremail);
            System.out.println("Course id is : "+CcourseId);

            boolean isenrollementExists=DB_Methods.isUserEnrolled(Uid,courseName);
            System.out.println(isenrollementExists);
            if (!isenrollementExists){
                return Response.status(Response.Status.CONFLICT).entity("you are not enrolled in this course").build();
            }
            boolean isdeleted=DB_Methods.deleteEnrollment(Uid,courseName);
            System.out.println(isdeleted);
            if (isdeleted){
               boolean result = NumberClient.decrementCourse(CcourseId);
                return Response.status(Response.Status.OK).entity("Enrollment deleted successfully").build();
            }
             else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to delete enrollment").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error occurred during deleting  : " + e.getMessage()).build();
        }
    }

    /// The instructor will accept or reject the enrollment request ///
    @PUT
    @Path("/update/enrollment")
    @Consumes(MediaType.APPLICATION_JSON)

    public Response updateEnrollment(@HeaderParam("Authorization") String token ,JsonObject jsonObject) {
        try {
            String courseName =jsonObject.getString("courseName");
            String StudentEmail=jsonObject.getString("StudentEmail");
            if (token==null){
                return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build();
            }
            UserData userData=AuthClient.verifyToken(token);
            System.out.println(userData);
            System.out.println("Fetched User Data: " + userData);
            String instructorName=userData.getName();
            String instructorEmail=userData.getEmail();
            String instructorRole=userData.getRole();
            String InstructorIdU=userData.getId();
            System.out.println("user Id: " + InstructorIdU);
            System.out.println("user Name is : "+instructorName);
            System.out.println("user Email is : "+instructorEmail);
            System.out.println("user Role is : "+instructorRole);

            if (!instructorRole.equalsIgnoreCase("instructor")){
                return Response.status(Response.Status.BAD_REQUEST).entity("only instructors can accept or reject").build();
            }

            // Find the enrollment by name
            String enrollmentId = findEnrollmentIdByName(courseName,StudentEmail);
            List<Enrollment> enrollment=findEnrollmentsByName(courseName,StudentEmail);
            System.out.println(enrollment);
            Map<String, Object> enrollmentFields = new HashMap<>();
            Enrollment firstEnrollment = enrollment.get(0); // Assuming at least one enrollment is retrieved

            enrollmentFields.put("id", firstEnrollment.getId());
            enrollmentFields.put("courseName", firstEnrollment.getCourseName());
            enrollmentFields.put("category", firstEnrollment.getCategory());
            enrollmentFields.put("instructorId", firstEnrollment.getInstructorId());
            enrollmentFields.put("instructorName", firstEnrollment.getInstructorName());
            enrollmentFields.put("status", firstEnrollment.getStatus());
            enrollmentFields.put("studentName", firstEnrollment.getStudentName());
            enrollmentFields.put("studentEmail", firstEnrollment.getStudentEmail());
            enrollmentFields.put("courseId", firstEnrollment.getCourseId());

            String category=firstEnrollment.getCategory();
            String instructorId=firstEnrollment.getInstructorId();
            String instructorName2=firstEnrollment.getInstructorName();
            String status=firstEnrollment.getStatus();
            String studentName=firstEnrollment.getStudentName();
            String studentEmail=firstEnrollment.getStudentEmail();
            String courseId=firstEnrollment.getCourseId();

            System.out.println("Enrollment Fields:");
            System.out.println("ID: " + enrollmentId);
            System.out.println("Course Name: " + courseName);
            System.out.println("Category: " + category);
            System.out.println("Instructor ID: " + instructorId);
            System.out.println("Instructor Name: " + instructorName2);
            System.out.println("Status: " + status);
            System.out.println("Student Name: " + studentName);
            System.out.println("Student Email: " + studentEmail);
            System.out.println("Course ID: " + courseId);


            if (enrollmentId == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Enrollment not found with name: " + courseName).build();
            }
            if (!instructorId.equals(InstructorIdU)){
                return Response.status(Response.Status.CONFLICT).entity("this is not your course").build();
            }
            String updatedStatus = findEnrollmentStatus(enrollmentId); // Implement this method to fetch the status from the database
            System.out.println(updatedStatus);
            if (!updatedStatus.equalsIgnoreCase("pending")) {
                return Response.status(Response.Status.CONFLICT).entity("You already accepted or rejected this student enrollment").build();
            }
            String newValue = "Accepted";
            boolean enrollmentUpdated = DB_Methods.updateEnrollmentStatus(enrollmentId, studentEmail, newValue);
            System.out.println(enrollmentUpdated);
            if (!enrollmentUpdated) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Error occurred while updating enrollment status").build();
            }
            // isa tsht8l
            boolean result = NumberClient.incrementCourse(courseId);
            return Response.ok("Enrollment updated successfully").build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error occurred while updating the enrollment: " + e.getMessage()).build();
        }
    }


    /// user will get all his enrollment ///
    @GET
    @Path("/enrollment/history")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response MyEnrollment(@HeaderParam("Authorization") String token) {
        try {
            if (token == null) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build();
            }
            UserData userData = AuthClient.verifyToken(token);
            System.out.println(userData);
            System.out.println("Fetched User Data: " + userData);
            String studentName = userData.getName();
            String studentEmail = userData.getEmail();
            String studentRole = userData.getRole();
            String studentId = userData.getId();
            System.out.println("user Id: " + studentId);
            System.out.println("user Name is : " + studentName);
            System.out.println("user Email is : " + studentEmail);
            System.out.println("user Role is : " + studentRole);

            if (!studentRole.equalsIgnoreCase("student")) {
                return Response.status(Response.Status.BAD_REQUEST).entity("only user ").build();
            }
            List<Document> enrollments = DB_Methods.getAllEnrollmentsByUserId(studentId);


            // Check if the list of enrollments is empty
            if (enrollments.isEmpty()) {
                return Response.ok("You are not enrolled in any course yet").build();
            }

//              return Response.ok(enrollments).build();
            return Response.ok("Your enrollments history\n " + enrollments).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error occurred while updating the enrollment: " + e.getMessage()).build();
        }
    }

    /// instructor will get all emails the m2 service use it ///
    @GET
    @Path("/enrollment/emails")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response EmailsEnrollment(@QueryParam("courseId") String courseId) {
        try {

            System.out.println(courseId);
            List<String> emails = findStudentEmailsByCourseId(courseId);
            System.out.println(emails);
            GenericEntity<List<String>> entity = new GenericEntity<List<String>>(emails) {};

            // Check if the list of enrollments is empty
            if (emails.isEmpty()) {
                return Response.ok("No students enrolled in this course").build();
            }

//            return Response.ok(emails).build();
            return Response.ok(entity, MediaType.APPLICATION_JSON).build();


        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error occurred while updating the enrollment: " + e.getMessage()).build();
        }
    }


    @GET
    @Path("/enrollment/WithName")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response EmailsEnrollmentByCourseAndInstructor(JsonObject jsonObject) {
        try {
            String courseName =jsonObject.getString("courseName");
            String instructorName =jsonObject.getString("instructorName");
            // Validate inputs
            if (courseName == null || instructorName == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Both courseName and instructorName are required").build();
            }

            List<Enrollment> enrollments = findEnrollmentsByCourseAndInstructor(courseName, instructorName);

            GenericEntity<List<Enrollment>> entity = new GenericEntity<List<Enrollment>>(enrollments) {};

            // Check if the list of emails is empty
            if (enrollments.isEmpty()) {
                return Response.ok("No students enrolled in this course with the given instructor").build();
            }
            return Response.ok(entity, MediaType.APPLICATION_JSON).build();
            // Return the list of emails
//            return Response.ok(enrollments).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error occurred while fetching student emails: " + e.getMessage()).build();
        }
    }



    @POST
    @Path("/enroll2")
    public Response enrollTemporary() {
        try {
            System.out.println("Entered Temporary Enrollment");
            System.out.println("Where this shit weeeeeeeeeeeeeeeent");

            // Fetch temporary enrollments
            List<Enrollment> temporaryEnrollments = TemporaryEnrollmentClient.fetchTemporaryEnrollments();
            System.out.println("List of Temporary Enrollments: " + temporaryEnrollments);

            for (Enrollment enrollment : temporaryEnrollments) {
                // Check if enrollment already exists in the database
                boolean isEnrollmentExists = DB_Methods.isUserEnrolledByEmail(enrollment.getStudentEmail(), enrollment.getCourseName());
                if (isEnrollmentExists) {
                    System.out.println("Enrollment already exists for course: " + enrollment.getCourseName() + " and user email: " + enrollment.getStudentEmail());
                    continue; // Skip this enrollment
                }

                // Save the enrollment
                boolean enrollmentSaved = DB_Methods.saveEnrollmentWithEmail(enrollment, enrollment.getStudentEmail());
                System.out.println(enrollmentSaved);
                if (enrollmentSaved) {
                    System.out.println("Enrollment saved successfully for course: " + enrollment.getCourseName() + " and user email: " + enrollment.getStudentName());
                    return Response.status(Response.Status.CREATED).entity(enrollment).build();
                } else {
                    System.out.println("Failed to save enrollment for course: " + enrollment.getCourseName() + " and user email: " + enrollment.getStudentEmail());
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(enrollment).build();
                }
            }

            return Response.ok("Temporary enrollments processed successfully").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error occurred during temporary enrollment: " + e.getMessage()).build();
        }
    }





}















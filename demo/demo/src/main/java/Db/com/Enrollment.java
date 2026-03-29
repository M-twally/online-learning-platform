package Db.com;

import org.bson.types.ObjectId;

public class Enrollment {
    private ObjectId id;
    private String courseName;
    private String category;
    private String instructorId;
    private String instructorName;
    private String status;
    private String courseId;
    private String studentName;
    private String studentEmail;

    // Constructor with default status
    public Enrollment(String courseName, String category, String instructorId, String instructorName, String studentName, String studentEmail,String courseId) {
        this.courseName = courseName;
        this.category = category;
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.status = "pending"; // Default status
        this.studentName=studentName;
        this.studentEmail=studentEmail;
        this.courseId = courseId;
    }

    // Getters and setters for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getters and setters for other fields (courseName, category, etc.)
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    public String getStudentEmail() {
        return studentEmail;
    }
    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    // Override toString() method for debugging purposes
    @Override
    public String toString() {
        return "Enrollment{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", category='" + category + '\'' +
                ", instructorId='" + instructorId + '\'' +
                ", instructorName='" + instructorName + '\'' +
                ", status='" + status + '\'' +
                ", studentName='" + studentName + '\'' +
                ", studentEmail='" + studentEmail + '\'' +
                ", courseId='" + courseId + '\'' +
                '}';
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}

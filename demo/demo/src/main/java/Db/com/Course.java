//package Db.com;
//import java.util.List;
//
//public class Course {
//    private String name;
//    private int duration;
//    private String category;
//    private double rating;
//    private int capacity;
//    private int enrolledStudents;
//    private List<String> reviews;
//    private String instructorName;
//    private String instructorEmail; // Added instructor email field
//    private int years_of_experience; // Changed field name to follow Java naming conventions
//
//    // Constructors, getters, and setters
//
//    public Course() {
//    }
//
//    public Course(String name, int duration, String category, double rating, int capacity, int enrolledStudents, List<String> reviews, String instructorName, String instructorEmail, int years_of_experience) {
//        this.name = name;
//        this.duration = duration;
//        this.category = category;
//        this.rating = rating;
//        this.capacity = capacity;
//        this.enrolledStudents = enrolledStudents;
//        this.reviews = reviews;
//        this.instructorName = instructorName;
//        this.instructorEmail = instructorEmail; // Initialize instructor email
//        this.years_of_experience = years_of_experience; // Initialize years of experience
//    }
//
//    // Getters and setters
//
//    public String getInstructorEmail() {
//        return instructorEmail;
//    }
//
//    public void setInstructorEmail(String instructorEmail) {
//        this.instructorEmail = instructorEmail;
//    }
//
//    public int getYearsOfExperience() {
//        return years_of_experience;
//    }
//
//    public void setYearsOfExperience(int years_of_experience) {
//        this.years_of_experience = years_of_experience;
//    }
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getDuration() {
//        return duration;
//    }
//
//    public void setDuration(int duration) {
//        this.duration = duration;
//    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
//    public double getRating() {
//        return rating;
//    }
//
//    public void setRating(double rating) {
//        this.rating = rating;
//    }
//
//    public int getCapacity() {
//        return capacity;
//    }
//
//    public void setCapacity(int capacity) {
//        this.capacity = capacity;
//    }
//
//    public int getEnrolledStudents() {
//        return enrolledStudents;
//    }
//
//    public void setEnrolledStudents(int enrolledStudents) {
//        this.enrolledStudents = enrolledStudents;
//    }
//
//    public List<String> getReviews() {
//        return reviews;
//    }
//
//    public void setReviews(List<String> reviews) {
//        this.reviews = reviews;
//    }
//
//    public String getInstructorName() {
//        return instructorName;
//    }
//
//    public void setInstructorName(String instructorName) {
//        this.instructorName = instructorName;
//    }
//
//    // Other getters and setters...
//
//    @Override
//    public String toString() {
//        return "Course{" +
//                "name='" + name + '\'' +
//                ", duration=" + duration +
//                ", category='" + category + '\'' +
//                ", rating=" + rating +
//                ", capacity=" + capacity +
//                ", enrolledStudents=" + enrolledStudents +
//                ", reviews=" + reviews +
//                ", instructorName='" + instructorName + '\'' +
//                ", instructorEmail='" + instructorEmail + '\'' +
//                ", years_of_experience=" + years_of_experience +
//                '}';
//    }
//}
package Db.com;

import java.util.List;

public class Course {
    private String name;
    private int duration;
    private String category;
    private double rating;
    private int capacity;
    private int enrolledStudents;
    private List<String> reviews;
    private String instructorName;
    private String instructorEmail;
    private int years_of_experience;
    private String published; // New field

    // Constructors, getters, and setters

    public Course() {
        this.published = "no"; // Set default value
    }

    public Course(String name, int duration, String category, double rating, int capacity, int enrolledStudents, List<String> reviews, String instructorName, String instructorEmail, int years_of_experience) {
        this.name = name;
        this.duration = duration;
        this.category = category;
        this.rating = rating;
        this.capacity = capacity;
        this.enrolledStudents = enrolledStudents;
        this.reviews = reviews;
        this.instructorName = instructorName;
        this.instructorEmail = instructorEmail;
        this.years_of_experience = years_of_experience;
        this.published = "no"; // Set default value
    }

    // Getters and setters

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(int enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public int getYearsOfExperience() {
        return years_of_experience;
    }

    public void setYearsOfExperience(int years_of_experience) {
        this.years_of_experience = years_of_experience;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", category='" + category + '\'' +
                ", rating=" + rating +
                ", capacity=" + capacity +
                ", enrolledStudents=" + enrolledStudents +
                ", reviews=" + reviews +
                ", instructorName='" + instructorName + '\'' +
                ", instructorEmail='" + instructorEmail + '\'' +
                ", years_of_experience=" + years_of_experience +
                ", published='" + published + '\'' +
                '}';
    }
}

//package DB_Methods;
//
//import DB_Connection.DB;
//import Db.com.Enrollment;
//import com.mongodb.client.FindIterable;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.model.Filters;
//import com.mongodb.client.result.DeleteResult;
//import com.mongodb.client.result.UpdateResult;
//import org.bson.Document;
//import org.bson.conversions.Bson;
//import org.bson.types.ObjectId;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class DB_Methods {
//
//
//    public static boolean saveEnrollment(Enrollment enrollment, String userId) {
//        try {
//            // Initialize database connection
//            DB.initializeDatabaseConnection();
//
//            // Get the enrollments collection
//            MongoCollection<Document> enrollmentsCollection = DB.getEnrollmentsCollection();
//
//            // Convert Enrollment object to Document
//            Document enrollmentDocument = new Document()
//                    .append("courseName", enrollment.getCourseName())
//                    .append("category", enrollment.getCategory())
//                    .append("instructorId", enrollment.getInstructorId()) // Assuming instructorId is ObjectId
//                    .append("instructorName", enrollment.getInstructorName())
//                    .append("status", enrollment.getStatus())
//                    .append("courseId", enrollment.getCourseId())
//                    .append("studentName", enrollment.getStudentName())
//                    .append("studentEmail",enrollment.getStudentEmail())
//                    .append("studentId", userId); // Assuming userId is a String
//
//            // Insert the document into the enrollments collection
//            enrollmentsCollection.insertOne(enrollmentDocument);
//
//            // Close database connection
//            DB.closeDatabaseConnection();
//
//            return true;
//        } catch (Exception e) {
//            System.err.println("Error saving enrollment: " + e.getMessage());
//            return false;
//        }
//    }
//
//
//    public static boolean saveEnrollmentWithEmail(Enrollment enrollment, String studentEmail) {
//        try {
//            // Initialize database connection
//            DB.initializeDatabaseConnection();
//
//            // Get the enrollments collection
//            MongoCollection<Document> enrollmentsCollection = DB.getEnrollmentsCollection();
//
//            // Convert Enrollment object to Document
//            Document enrollmentDocument = new Document()
//                    .append("courseName", enrollment.getCourseName())
//                    .append("category", enrollment.getCategory())
//                    .append("instructorId", enrollment.getInstructorId()) // Assuming instructorId is ObjectId
//                    .append("instructorName", enrollment.getInstructorName())
//                    .append("status", enrollment.getStatus())
//                    .append("courseId", enrollment.getCourseId())
//                    .append("studentName", enrollment.getStudentName())
//                    .append("studentEmail", studentEmail);
//
//            // Insert the document into the enrollments collection
//            enrollmentsCollection.insertOne(enrollmentDocument);
//
//            // Close database connection
//            DB.closeDatabaseConnection();
//
//            return true;
//        } catch (Exception e) {
//            System.err.println("Error saving enrollment with email: " + e.getMessage());
//            return false;
//        }
//    }
//
//
//    public static boolean isUserEnrolled(String userId, String courseName) {
//        try {
//            // Initialize database connection
//            DB.initializeDatabaseConnection();
//
//            // Get the enrollments collection
//            MongoCollection<Document> enrollmentsCollection = DB.getEnrollmentsCollection();
//
//            // Construct the query
//            Document query = new Document("studentId", userId)
//                    .append("courseName", courseName);
//
//            // Execute the query and check if any document matches
//            boolean userEnrolled = enrollmentsCollection.find(query).first() != null;
//
//            // Close database connection
//            DB.closeDatabaseConnection();
//
//            return userEnrolled;
//        } catch (Exception e) {
//            System.err.println("Error checking enrollment status: " + e.getMessage());
//            return false;
//        }
//    }
//
//
//    public static boolean isUserEnrolledByEmail(String studentEmail, String courseName) {
//        try {
//            // Initialize database connection
//            DB.initializeDatabaseConnection();
//
//            // Get the enrollments collection
//            MongoCollection<Document> enrollmentsCollection = DB.getEnrollmentsCollection();
//
//            // Construct the query
//            Document query = new Document("studentEmail", studentEmail)
//                    .append("courseName", courseName);
//
//            // Execute the query and check if any document matches
//            boolean userEnrolled = enrollmentsCollection.find(query).first() != null;
//
//            // Close database connection
//            DB.closeDatabaseConnection();
//
//            return userEnrolled;
//        } catch (Exception e) {
//            System.err.println("Error checking enrollment status by email: " + e.getMessage());
//            return false;
//        }
//    }
//
//
//    public static boolean deleteEnrollment(String userId, String courseName) {
//        try {
//            // Initialize database connection
//            DB.initializeDatabaseConnection();
//
//            // Get the enrollments collection
//            MongoCollection<Document> enrollmentsCollection = DB.getEnrollmentsCollection();
//
//            // Construct the query
//            Document query = new Document("studentId", userId)
//                    .append("courseName", courseName);
//
//            // Delete the document matching the query
//            DeleteResult deleteResult = enrollmentsCollection.deleteOne(query);
//
//            // Close database connection
//            DB.closeDatabaseConnection();
//
//            // Return true if at least one document was deleted
//            return deleteResult.getDeletedCount() > 0;
//        } catch (Exception e) {
//            System.err.println("Error deleting enrollment: " + e.getMessage());
//            return false;
//        }
//    }
//
//
//    public static List<Document> getAllEnrollmentsByUserId(String userId) {
//        List<Document> enrollments = new ArrayList<>();
//        try {
//            // Initialize database connection
//            DB.initializeDatabaseConnection();
//
//            // Get the enrollments collection
//            MongoCollection<Document> enrollmentsCollection = DB.getEnrollmentsCollection();
//
//            // Query for enrollments by user ID
//            Bson filter = Filters.eq("studentId", userId); // Use the userId directly as a string
//            FindIterable<Document> result = enrollmentsCollection.find(filter);
//
//            // Iterate over the results and add them to the list
//            for (Document doc : result) {
//                enrollments.add(doc);
//            }
//
//            // Close database connection
//            DB.closeDatabaseConnection();
//        } catch (Exception e) {
//            System.err.println("Error fetching enrollments: " + e.getMessage());
//        }
//
//        // Check if no enrollments found
//        if (enrollments.isEmpty()) {
//            // Return a specific message
//            enrollments.add(new Document("message", "Sorry, you are not enrolled in any course yet."));
//        }
//
//        return enrollments;
//    }
//
//
//    public static boolean updateEnrollmentStatus(String enrollmentId, String userEmail, String newStatus) {
//    try {
//        // Initialize database connection
//        DB.initializeDatabaseConnection();
//
//        // Get the enrollments collection
//        MongoCollection<Document> enrollmentCollection = DB.getEnrollmentsCollection();
//
//        // Construct the query to find the enrollment by its ID and student email
//        Document query = new Document("_id", new ObjectId(enrollmentId))
//                .append("studentEmail", userEmail);
//
//        // Construct the update operation to set the new status
//        Document update = new Document("$set", new Document("status", newStatus));
//
//        // Perform the update operation
//        UpdateResult updateResult = enrollmentCollection.updateOne(query, update);
//
//        // Close database connection
//        DB.closeDatabaseConnection();
//
//        // Return true if at least one document was updated
//        return updateResult.getModifiedCount() > 0;
//    } catch (Exception e) {
//        System.err.println("Error updating enrollment status: " + e.getMessage());
//        return false;
//    }
//}
//
//
//    public  static String findEnrollmentIdByName(String courseName, String studentEmail) {
//        try {
//            DB.initializeDatabaseConnection();
//            MongoCollection<Document> collection = DB.getEnrollmentsCollection(); // Assuming you have a collection for enrollments
//            Document query = new Document("courseName", courseName)
//                    .append("studentEmail", studentEmail);
//            Document enrollment = collection.find(query).projection(
//                    new Document("_id", 1)
//            ).first();
//            if (enrollment != null) {
//                return enrollment.getObjectId("_id").toString();
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            System.err.println(e.toString());
//            return null;
//        }
//    }
//
//
//    public static List<Enrollment> findEnrollmentsByName(String courseName, String studentEmail) {
//        try {
//            DB.initializeDatabaseConnection();
//            MongoCollection<Document> collection = DB.getEnrollmentsCollection(); // Assuming you have a collection for enrollments
//            Document query = new Document("courseName", courseName)
//                    .append("studentEmail", studentEmail);
//            List<Enrollment> enrollments = new ArrayList<>();
//            collection.find(query).forEach(document -> {
//                enrollments.add(new Enrollment(
//                        document.getString("courseName"),
//                        document.getString("category"),
//                        document.getString("instructorId"),
//                        document.getString("instructorName"),
//                        document.getString("studentName"),
//                        document.getString("studentEmail"),
//                        document.getString("courseId")
//
//                ));
//            });
//            return enrollments;
//        } catch (Exception e) {
//            System.err.println(e.toString());
//            return Collections.emptyList();
//        }
//    }
//
//    public static String findEnrollmentStatus(String enrollmentId) {
//        try {
//            DB.initializeDatabaseConnection();
//            MongoCollection<Document> collection = DB.getEnrollmentsCollection(); // Assuming you have a collection for enrollments
//            Document query = new Document("_id", new ObjectId(enrollmentId));
//            Document enrollmentDocument = collection.find(query).first();
//            if (enrollmentDocument != null) {
//                return enrollmentDocument.getString("status");
//            } else {
//                return null; // Enrollment not found
//            }
//        } catch (Exception e) {
//            System.err.println(e.toString());
//            return null;
//        }
//    }
//
//
//    public static List<String> findStudentEmailsByCourseId(String courseId) {
//        try {
//            DB.initializeDatabaseConnection();
//            MongoCollection<Document> collection = DB.getEnrollmentsCollection();
//
//            // Query to filter enrollments by courseId
//            Document query = new Document("courseId", courseId);
//
//            // Projection to include only the studentEmail field
//            Document projection = new Document("studentEmail", 1);
//
//            List<String> studentEmails = new ArrayList<>();
//            collection.find(query)
//                    .projection(projection)
//                    .forEach((Document document) -> {
//                        String studentEmail = document.getString("studentEmail");
//                        studentEmails.add(studentEmail);
//                    });
//
//            DB.closeDatabaseConnection();
//            return studentEmails;
//        } catch (Exception e) {
//            System.err.println("Error finding student emails by courseId: " + e.toString());
//            return Collections.emptyList();
//        }
//    }
//
//    public static List<Enrollment> findEnrollmentsByCourseAndInstructor(String courseName, String instructorName) {
//        try {
//            DB.initializeDatabaseConnection();
//            MongoCollection<Document> collection = DB.getEnrollmentsCollection(); // Assuming you have a collection for enrollments
//            Document query = new Document("courseName", courseName)
//                    .append("instructorName", instructorName);
//            List<Enrollment> enrollments = new ArrayList<>();
//            collection.find(query).forEach(document -> {
//                enrollments.add(new Enrollment(
//                        document.getString("courseName"),
//                        document.getString("category"),
//                        document.getString("instructorId"),
//                        document.getString("instructorName"),
//                        document.getString("studentName"),
//                        document.getString("studentEmail"),
//                        document.getString("status")
//
//                ));
//            });
//
//            return enrollments;
//        } catch (Exception e) {
//            System.err.println(e.toString());
//            return Collections.emptyList();
//        }
//    }
//
//
//
//}

package DB_Methods;

import DB_Connection.DB;
import Db.com.Enrollment;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DB_Methods {

    public static boolean saveEnrollment(Enrollment enrollment, String userId) {
        try {
            // Get the Singleton instance of DB
            DB dbInstance = DB.getInstance();

            // Get the enrollments collection
            MongoCollection<Document> enrollmentsCollection = dbInstance.getEnrollmentsCollection();

            // Convert Enrollment object to Document
            Document enrollmentDocument = new Document()
                    .append("courseName", enrollment.getCourseName())
                    .append("category", enrollment.getCategory())
                    .append("instructorId", enrollment.getInstructorId())
                    .append("instructorName", enrollment.getInstructorName())
                    .append("status", enrollment.getStatus())
                    .append("courseId", enrollment.getCourseId())
                    .append("studentName", enrollment.getStudentName())
                    .append("studentEmail", enrollment.getStudentEmail())
                    .append("studentId", userId);

            // Insert the document into the enrollments collection
            enrollmentsCollection.insertOne(enrollmentDocument);

            return true;
        } catch (Exception e) {
            System.err.println("Error saving enrollment: " + e.getMessage());
            return false;
        }
    }

    public static boolean saveEnrollmentWithEmail(Enrollment enrollment, String studentEmail) {
        try {
            // Get the Singleton instance of DB
            DB dbInstance = DB.getInstance();

            // Get the enrollments collection
            MongoCollection<Document> enrollmentsCollection = dbInstance.getEnrollmentsCollection();

            // Convert Enrollment object to Document
            Document enrollmentDocument = new Document()
                    .append("courseName", enrollment.getCourseName())
                    .append("category", enrollment.getCategory())
                    .append("instructorId", enrollment.getInstructorId())
                    .append("instructorName", enrollment.getInstructorName())
                    .append("status", enrollment.getStatus())
                    .append("courseId", enrollment.getCourseId())
                    .append("studentName", enrollment.getStudentName())
                    .append("studentEmail", studentEmail);

            // Insert the document into the enrollments collection
            enrollmentsCollection.insertOne(enrollmentDocument);

            return true;
        } catch (Exception e) {
            System.err.println("Error saving enrollment with email: " + e.getMessage());
            return false;
        }
    }

    public static boolean isUserEnrolled(String userId, String courseName) {
        try {
            // Get the Singleton instance of DB
            DB dbInstance = DB.getInstance();

            // Get the enrollments collection
            MongoCollection<Document> enrollmentsCollection = dbInstance.getEnrollmentsCollection();

            // Construct the query
            Document query = new Document("studentId", userId)
                    .append("courseName", courseName);

            // Execute the query and check if any document matches
            boolean userEnrolled = enrollmentsCollection.find(query).first() != null;

            return userEnrolled;
        } catch (Exception e) {
            System.err.println("Error checking enrollment status: " + e.getMessage());
            return false;
        }
    }

    public static boolean isUserEnrolledByEmail(String studentEmail, String courseName) {
        try {
            // Get the Singleton instance of DB
            DB dbInstance = DB.getInstance();

            // Get the enrollments collection
            MongoCollection<Document> enrollmentsCollection = dbInstance.getEnrollmentsCollection();

            // Construct the query
            Document query = new Document("studentEmail", studentEmail)
                    .append("courseName", courseName);

            // Execute the query and check if any document matches
            boolean userEnrolled = enrollmentsCollection.find(query).first() != null;

            return userEnrolled;
        } catch (Exception e) {
            System.err.println("Error checking enrollment status by email: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteEnrollment(String userId, String courseName) {
        try {
            // Get the Singleton instance of DB
            DB dbInstance = DB.getInstance();

            // Get the enrollments collection
            MongoCollection<Document> enrollmentsCollection = dbInstance.getEnrollmentsCollection();

            // Construct the query
            Document query = new Document("studentId", userId)
                    .append("courseName", courseName);

            // Delete the document matching the query
            DeleteResult deleteResult = enrollmentsCollection.deleteOne(query);

            // Return true if at least one document was deleted
            return deleteResult.getDeletedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error deleting enrollment: " + e.getMessage());
            return false;
        }
    }

    public static List<Document> getAllEnrollmentsByUserId(String userId) {
        List<Document> enrollments = new ArrayList<>();
        try {
            // Get the Singleton instance of DB
            DB dbInstance = DB.getInstance();

            // Get the enrollments collection
            MongoCollection<Document> enrollmentsCollection = dbInstance.getEnrollmentsCollection();

            // Query for enrollments by user ID
            Bson filter = Filters.eq("studentId", userId);
            FindIterable<Document> result = enrollmentsCollection.find(filter);

            // Iterate over the results and add them to the list
            for (Document doc : result) {
                enrollments.add(doc);
            }
        } catch (Exception e) {
            System.err.println("Error fetching enrollments: " + e.getMessage());
        }

        // Check if no enrollments found
        if (enrollments.isEmpty()) {
            // Return a specific message
            enrollments.add(new Document("message", "Sorry, you are not enrolled in any course yet."));
        }

        return enrollments;
    }

    public static boolean updateEnrollmentStatus(String enrollmentId, String userEmail, String newStatus) {
        try {
            // Get the Singleton instance of DB
            DB dbInstance = DB.getInstance();

            // Get the enrollments collection
            MongoCollection<Document> enrollmentCollection = dbInstance.getEnrollmentsCollection();

            // Construct the query to find the enrollment by its ID and student email
            Document query = new Document("_id", new ObjectId(enrollmentId))
                    .append("studentEmail", userEmail);

            // Construct the update operation to set the new status
            Document update = new Document("$set", new Document("status", newStatus));

            // Perform the update operation
            UpdateResult updateResult = enrollmentCollection.updateOne(query, update);

            // Return true if at least one document was updated
            return updateResult.getModifiedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error updating enrollment status: " + e.getMessage());
            return false;
        }
    }

    public static String findEnrollmentIdByName(String courseName, String studentEmail) {
        try {
            // Get the Singleton instance of DB
            DB dbInstance = DB.getInstance();

            // Get the enrollments collection
            MongoCollection<Document> collection = dbInstance.getEnrollmentsCollection();
            Document query = new Document("courseName", courseName)
                    .append("studentEmail", studentEmail);
            Document enrollment = collection.find(query).projection(
                    new Document("_id", 1)
            ).first();
            if (enrollment != null) {
                return enrollment.getObjectId("_id").toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }
    }

    public static List<Enrollment> findEnrollmentsByName(String courseName, String studentEmail) {
        try {
            // Get the Singleton instance of DB
            DB dbInstance = DB.getInstance();

            // Get the enrollments collection
            MongoCollection<Document> collection = dbInstance.getEnrollmentsCollection();
            Document query = new Document("courseName", courseName)
                    .append("studentEmail", studentEmail);
            List<Enrollment> enrollments = new ArrayList<>();
            collection.find(query).forEach(document -> {
                enrollments.add(new Enrollment(
                        document.getString("courseName"),
                        document.getString("category"),
                        document.getString("instructorId"),
                        document.getString("instructorName"),
                        document.getString("studentName"),
                        document.getString("studentEmail"),
                        document.getString("courseId")
                ));
            });
            return enrollments;
        } catch (Exception e) {
            System.err.println(e.toString());
            return Collections.emptyList();
        }
    }

    public static String findEnrollmentStatus(String enrollmentId) {
        try {
            // Get the Singleton instance of DB
            DB dbInstance = DB.getInstance();

            // Get the enrollments collection
            MongoCollection<Document> collection = dbInstance.getEnrollmentsCollection();
            Document query = new Document("_id", new ObjectId(enrollmentId));
            Document enrollmentDocument = collection.find(query).first();
            if (enrollmentDocument != null) {
                return enrollmentDocument.getString("status");
            } else {
                return null; // Enrollment not found
            }
        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }
    }

    public static List<String> findStudentEmailsByCourseId(String courseId) {
        try {
            // Get the Singleton instance of DB
            DB dbInstance = DB.getInstance();

            // Get the enrollments collection
            MongoCollection<Document> collection = dbInstance.getEnrollmentsCollection();

            // Query to filter enrollments by courseId
            Document query = new Document("courseId", courseId);

            // Projection to include only the studentEmail field
            Document projection = new Document("studentEmail", 1);

            List<String> studentEmails = new ArrayList<>();
            collection.find(query)
                    .projection(projection)
                    .forEach((Document document) -> {
                        String studentEmail = document.getString("studentEmail");
                        studentEmails.add(studentEmail);
                    });

            return studentEmails;
        } catch (Exception e) {
            System.err.println("Error finding student emails by courseId: " + e.toString());
            return Collections.emptyList();
        }
    }

    public static List<Enrollment> findEnrollmentsByCourseAndInstructor(String courseName, String instructorName) {
        try {
            // Get the Singleton instance of DB
            DB dbInstance = DB.getInstance();

            // Get the enrollments collection
            MongoCollection<Document> collection = dbInstance.getEnrollmentsCollection();

            Document query = new Document("courseName", courseName)
                    .append("instructorName", instructorName);
            List<Enrollment> enrollments = new ArrayList<>();
            collection.find(query).forEach(document -> {
                enrollments.add(new Enrollment(
                        document.getString("courseName"),
                        document.getString("category"),
                        document.getString("instructorId"),
                        document.getString("instructorName"),
                        document.getString("studentName"),
                        document.getString("studentEmail"),
                        document.getString("status")
                ));
            });

            return enrollments;
        } catch (Exception e) {
            System.err.println(e.toString());
            return Collections.emptyList();
        }
    }
}

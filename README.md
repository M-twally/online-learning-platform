This project implements a web-based online learning platform demonstrating EJBs and microservices architecture.

The system supports multiple user roles: Admins, Instructors, Students, and optionally Test Center Representatives.
Functionalities include course management, user management, enrollments, grading, reviews, and notifications.
The microservices version separates concerns into 4 independent services communicating via REST APIs.

Functional Features
Admins
Manage user accounts (students, instructors).
Review, edit, or remove courses.
Track platform usage and course popularity.
Instructors
Register and login with personal info (name, email, affiliation, bio).
Create courses with name, duration, category, rating, capacity, enrolled students, and reviews.
Accept/Reject student enrollments.
Search and sort courses by name, category, or rating.
Students
Register and login with personal info.
View current and past enrollments.
Search and sort courses by name, category, or rating.
Enroll or cancel enrollment safely (handles server failures).
Receive notifications on enrollments.
Provide course reviews and ratings.

Services communicate only via REST, not by accessing each other’s database.
node js and ejbs

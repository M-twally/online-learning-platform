import { StatusCodes } from "http-status-codes";
import { asyncHandler } from "../../../utils/errorHandling.js";
import { courseModel } from "../../../../DB/models/course.model.js";
import axios from 'axios'
import send_Email from "../../../utils/email.js";

//const response = await axios.get(`http://localhost:5000/user/userInfo)`
// http://localhost:5000/user/userInfo
//http://localhost:8080/demo-1.0-SNAPSHOT/Api/students/enrollment/emails?courseId=${courseId}
// ${process.env.FETCH_STUDENT_EMAIL}/students/enrollment/emails?courseId=${courseId}
// ${process.env.FETCH_USER_DATA}/user/userInfo
/// msh m7tag ///
const fetchUserData = async (token) => {
    try {
        const response = await axios.get(`http://localhost:5000/user/userInfo`, {
            headers: {
                token: token, 
            },
        });
        return response.data;
    } catch (error) {
        console.error('Error fetching user data:', error);
        throw new Error('Failed to fetch user data');
    }
};

/// msh m7tag ///
const fetchStudentEmailsByCourseId = async (courseId) => {
    try {
        const response = await axios.get(`http://localhost:8080/demo-1.0-SNAPSHOT/Api/students/enrollment/emails?courseId=${courseId}`);
        return response.data;
    } catch (error) {
        console.error('Error fetching student emails:', error);
        throw new Error('Failed to fetch student emails');
    }
};

/// instructor only ////
export const createCourse = asyncHandler(async (req, res, next) => {
    try {
        const{courseName,duration,capacity,category}=req.body
        console.log(req.body);
        console.log(req.headers.token);
        if (req.headers.token===undefined || req.headers.token === null) {
            return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'Unauthorized' });
        }
        // Fetch user data from the API
        const userData = await fetchUserData(req.headers.token); 
        console.log(userData);

        const userName = userData.data.name;
        const userId = userData.data._id;
        const userEmail = userData.data.email;
        const userRole = userData.data.role;
        if (userRole !== 'instructor') {
            return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'only instructors can create courses' });
        }
        const checkExistence = await courseModel.findOne({ courseName, 'user.userId': userId });
        if (checkExistence) {
            return res.status(StatusCodes.CONFLICT).json({ message: 'You already Published this course' });
        }
        
        
        console.log(userName,userId,userEmail); 

        // Add user data to the course data
        const courseData = {
            ...req.body,
            user: {
                userId,
                userName,
                userEmail
            },
        };

        const savedCourse = await courseModel.create(courseData);

        return res.status(StatusCodes.CREATED).json({ message: 'Course created or updated successfully', data: savedCourse });

    } catch (error) {
        console.error('Error creating or updating course:', error);
        return next(new Error('Failed to create course', { cause: 500 }));
    }
});

/// admins only /// 
export const editCourse = asyncHandler(async (req, res, next) => {
    try {
        const { instructorName, courseName, duration, category, capacity, enrolledStudents, published } = req.body;

        // Check if the token is provided
        if (!req.headers.token) {
            return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'Unauthorized' });
        }

        if (!instructorName || !courseName) {
            return res.status(StatusCodes.CONFLICT).json({ message: 'Instructor Name and Course Name are required' });
        }

        // Fetch user data from the API
        const userData = await fetchUserData(req.headers.token);
        const { name: userName, _id: userId, email: userEmail, role: userRole } = userData.data;

        // Check if user is admin
        if (userRole !== 'admin') {
            return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'Only admins can edit courses' });
        }
        // Find the course by instructor name and course name
        const existingCourse = await courseModel.findOne({ 'user.userName': instructorName, courseName });

        // Check if the course exists
        if (!existingCourse) {
            return res.status(StatusCodes.NOT_FOUND).json({ message: 'Course not found' });
        }

        const update = {};
        if (published) update.published = published;
        if (duration) update.duration = duration;
        if (category) update.category = category;
        if (capacity) update.capacity = capacity;
        if (enrolledStudents) update.enrolledStudents = enrolledStudents;

        // Update the course
        const updatedCourse = await courseModel.findOneAndUpdate({ 'user.userName': instructorName, courseName }, update, { new: true } 
        );

        if (!updatedCourse) {
            return res.status(StatusCodes.NOT_FOUND).json({ message: 'Course not found' });
        }

        return res.status(StatusCodes.OK).json({ message: 'Course updated successfully', data: updatedCourse });
    } catch (error) {
        console.error('Error updating course:', error);
        return next(new Error('Failed to update course', { cause: 500 }));
    }
});


/// mhsh m7tag ///   lw 3ayz t3mlha e3ml  admin onlyy  
export const getCourses=asyncHandler(async(req,res,next)=>{
    try {
        
        // Check if the token is provided
        if (!req.headers.token) {
            return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'Unauthorized' });
        }

        // Fetch user data from the API
        const userData = await fetchUserData(req.headers.token);
        const { name: userName, _id: userId, email: userEmail, role: userRole } = userData.data;

        // Check if user is admin
        if (userRole !== 'admin') {
            return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'Only admins can seee all  courses' });
        }
        const courses = await courseModel.find();
        return res.status(StatusCodes.OK).json({ message: 'Courses fetched successfully', data: courses });
    } catch (error) {
        console.error('Error fetching courses:', error);
        return next(new Error('Failed to fetch courses', { cause: 500 }));
    }

});

/// mhsh m7tag ///  lw 3ayaz t3mlha e3ml admin only  
export const getPublishedCourses = asyncHandler(async (req, res, next) => {
    try {
        
        // Check if the token is provided
        if (!req.headers.token) {
            return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'Unauthorized' });
        }

        // Fetch user data from the API
        const userData = await fetchUserData(req.headers.token);
        const { name: userName, _id: userId, email: userEmail, role: userRole } = userData.data;
        console.log(userRole);

        // Check if user is admin
        if (userRole !== 'admin' && userRole !== 'instructor' && userRole !== 'student') {
            return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'Only users with roles other than admin, instructor, or student can access this functionality.' });
        }
        
        const courses = await courseModel.find({ published: true }); 
        if (courses.length === 0) {
            return res.status(StatusCodes.NOT_FOUND).json({ message: 'No published courses found' });
        }
        return res.status(StatusCodes.OK).json({ message: 'Published courses fetched successfully', data: courses });
    } catch (error) {
        console.error('Error fetching published courses:', error);
        return next(new Error('Failed to fetch published courses', { cause: 500 }));
    }
});

/// mhsh m7tag ///   lw 3ayz t3mlha e3ml  admin onlyy
export const getUnPublishedCourses = asyncHandler(async (req, res, next) => {
    try {
        
        // Check if the token is provided
        if (!req.headers.token) {
            return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'Unauthorized' });
        }

        // Fetch user data from the API
        const userData = await fetchUserData(req.headers.token);
        const { name: userName, _id: userId, email: userEmail, role: userRole } = userData.data;

        // Check if user is admin
        if (userRole !== 'admin') {
            return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'Only admins can edit courses' });
        }
        const courses = await courseModel.find({ published: false }); 
        if (courses.length === 0) {
            return res.status(StatusCodes.NOT_FOUND).json({ message: 'No unpublished courses found' });
            
        }
        return res.status(StatusCodes.OK).json({ message: 'UnPublished courses fetched successfully', data: courses });
    } catch (error) {
        console.error('Error fetching published courses:', error);
        return next(new Error('Failed to fetch published courses', { cause: 500 }));
    }
});


export const getCourseByName = asyncHandler(async (req, res, next) => {
    try {
        const { courseName } = req.body;
        if (courseName===undefined|| courseName=="null") {
            return res.status(StatusCodes.BAD_REQUEST).json({ message: 'Course name is required' });
        }
        console.log(courseName);
        if (!req.headers.token) {
            return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'Unauthorized' });
        }

        // Fetch user data from the API
        const userData = await fetchUserData(req.headers.token);
        const { name: userName, _id: userId, email: userEmail, role: userRole } = userData.data;
        console.log(userRole);

        // Check if user is student or instructor
        if (userRole !== 'student' && userRole !== 'instructor') {
            return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'Only instructors and students can search courses' });
        }

        // Create a case-insensitive regex for the course name
        const searchRegex = new RegExp(courseName, 'i');

        // Query courses where courseName matches the regex and published is true
        const courses = await courseModel.find({ 
            courseName: searchRegex,
            published: true
        });

        if (courses.length === 0) {
            return res.status(StatusCodes.NOT_FOUND).json({ message: 'Course not found' });
        }

        return res.status(StatusCodes.OK).json({ message: 'Courses fetched successfully', data: courses });
    } catch (error) {
        console.error('Error fetching courses:', error);
        return next(new Error('Failed to fetch courses', { cause: 500 }));
    }
});


export const getCourseByCategory = asyncHandler(async (req, res, next) => {
    try {
        const { category } = req.body;
        console.log(category);
        if (!req.headers.token) {
            return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'Unauthorized' });
        }
        if (category===null||category===undefined) {
            return res.status(StatusCodes.NOT_FOUND).json({ message: 'U must enter the category field ' });
            
        }

        // Fetch user data from the API
        const userData = await fetchUserData(req.headers.token);
        const { name: userName, _id: userId, email: userEmail, role: userRole } = userData.data;

        // Check if user is student or instructor
        if (userRole !== 'student' && userRole !== 'instructor') {
            return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'Only instructors and students can search courses' });
        }

        // Create a case-insensitive regex for the category
        const searchRegex = new RegExp(category, 'i');

        // Query courses where category matches the regex and published is true
        const courses = await courseModel.find({ 
            category: searchRegex,
            published: true
        });

        if (courses.length === 0) {
            return res.status(StatusCodes.NOT_FOUND).json({ message: 'Course not found' });
        }

        return res.status(StatusCodes.OK).json({ message: 'Courses fetched successfully', data: courses });
    } catch (error) {
        console.error('Error fetching courses:', error);
        return next(new Error('Failed to fetch courses', { cause: 500 }));
    }
});

/// msh m7tag ///
export const getCourseById = asyncHandler(async (req, res, next) => {
    const id = req.query.id; // Access id from query parameters
    const course = await courseModel.findById(id);
    if (!course) {
        return next(new Error("Course not found", StatusCodes.NOT_FOUND));
    }
    res.status(StatusCodes.OK).json({
        status: "success",
        course: course,
    });
});

/// msh m7tag ///
export const getCourseByUserAndName = asyncHandler(async (req, res, next) => {
    try {
        console.log("tftkkkkkkkkkk");
        const { instructorName, courseName } = req.query; // Retrieve parameters from query
        console.log(instructorName, courseName);

        // Check if the parameters are provided
        if (!instructorName || !courseName) {
            return res.status(StatusCodes.BAD_REQUEST).json({ message: 'Both userName and courseName are required' });
        }

        // Construct case-insensitive regular expressions for userName and courseName
        const userNameRegex = new RegExp(instructorName, 'i');
        const courseNameRegex = new RegExp(courseName, 'i');

        // Find the course by userName and courseName with case insensitivity
        const course = await courseModel.findOne({ 'user.userName': userNameRegex, courseName: courseNameRegex });

        // Check if the course exists
        if (!course) {
            return res.status(StatusCodes.NOT_FOUND).json({ message: 'Course not found' });
        }

        // Return only specific fields from the course object
        const responseData = {
            userName: course.user.userName,
            userEmail: course.user.userEmail,
            userId: course.user.userId,
            category: course.category,
            courseName: course.courseName,
            courseId: course._id.toString() // Convert ObjectId to string
        };
        console.log(responseData);

        return res.status(StatusCodes.OK).json({ message: 'Course fetched successfully', responseData });
    } catch (error) {
        console.error('Error fetching course:', error);
        return next(new Error('Failed to fetch course', { cause: 500 }));
    }
});

/// msh m7tag ///
export const incrementNumberOfStudents = asyncHandler(async (req, res, next) => {
    try {
        const { courseId } = req.query;
        console.log(courseId);

        // Find the course by its ID
        const existingCourse = await courseModel.findById(courseId);

        // Check if the course exists
        if (!existingCourse) {
            return res.status(StatusCodes.NOT_FOUND).json({ message: 'Course not found' });
        }

        // Increment the number of enrolled students
        existingCourse.enrolledStudents += 1;

        // Save the updated course
        const updatedCourse = await existingCourse.save();

        return res.status(StatusCodes.OK).json({ message: 'Course updated successfully', data: updatedCourse });
    } catch (error) {
        console.error('Error updating course:', error);
        return next(new Error('Failed to update course', { cause: 500 }));
    }
});

/// msh m7tag ///
export const decrementNumberOfStudents = asyncHandler(async (req, res, next) => {
    try {
        const { courseId } = req.query;
        console.log(courseId);

        // Find the course by its ID
        const existingCourse = await courseModel.findById(courseId);

        // Check if the course exists
        if (!existingCourse) {
            return res.status(StatusCodes.NOT_FOUND).json({ message: 'Course not found' });
        }

        // Decrement the number of enrolled students
        if (existingCourse.enrolledStudents > 0) {
            existingCourse.enrolledStudents -= 1;
        } else {
            return res.status(StatusCodes.BAD_REQUEST).json({ message: 'Number of enrolled students is already 0' });
        }

        // Save the updated course
        const updatedCourse = await existingCourse.save();

        return res.status(StatusCodes.OK).json({ message: 'Course updated successfully', data: updatedCourse });
    } catch (error) {
        console.error('Error updating course:', error);
        return next(new Error('Failed to update course', { cause: 500 }));
    }
});

/// msh m7tag ///
export const getCourseByNameR = asyncHandler(async (req, res, next) => {
    try {
        console.log("eft777777777777777777");
        const { courseName ,instructorName} = req.params;
        console.log(courseName);
        console.log(instructorName);

        if (!courseName || !instructorName) {
            return res.status(StatusCodes.BAD_REQUEST).json({ message: 'Course name is required' });
        }

        const course = await courseModel.findOne({ courseName, published: true, 'user.userName': instructorName })
                                         .select(`courseName category user`);

        if (!course) {
            return res.status(StatusCodes.NOT_FOUND).json({ message: 'No published course found with the given name and instructor name' });
        }
        console.log(course);

        return res.status(StatusCodes.OK).json({ message: 'Published course fetched successfully', data: course });
    } catch (error) {
        console.error('Error fetching published courses:', error);
        return next(new Error('Failed to fetch published course', { cause: 500 }));
    }
});


export const sendNotifications=asyncHandler(async (req, res, next) => {
    const { courseName,Update} = req.body;
    console.log(courseName);

    // Check if the token is provided
    if (!req.headers.token) {
        return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'Unauthorized' });
    }
    // Fetch user data from the API
    const userData = await fetchUserData(req.headers.token);
    const { name: userName, _id: userId, email: userEmail, role: userRole } = userData.data;

    // Check if user is admin
    if (userRole !== 'admin') {
        return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'Only admins can send courses Notification' });
    }

    // Find the course by its name
    const existingCourse = await courseModel.findOne({ courseName });

    // Check if the course exists
    if (!existingCourse) {
        return res.status(StatusCodes.NOT_FOUND).json({ message: 'Course not found' });
    }
    console.log(existingCourse._id);
    const emails=await fetchStudentEmailsByCourseId(existingCourse._id)
    console.log(emails);
    if (emails.length==0) {
        return res.status(StatusCodes.NOT_FOUND).json({ message: 'No emails found for this course' });
    }
    await Promise.all(emails.map(async (email) => {
        await send_Email({
            to: email,
            subject: `Course: ${existingCourse.courseName} has been updated`,
            text: `${Update} `,
            html: `Course: ${existingCourse.courseName} has been updated with the following update: ${Update} `,
        });
    }));
    
    return res.status(StatusCodes.OK).json({ message: 'notification sent  successfully',  emails });


});

///!!!!!!!!!!!!!!!!!!!!!!!!!!!!!instructor only!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
export const getMycourses = asyncHandler(async (req, res, next) => {
    try {
        // Check if the token is provided
        if (!req.headers.token) {
            return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'Unauthorized' });
        }
        // Fetch user data from the API
        const userData = await fetchUserData(req.headers.token);
        const userName = userData.data.name;
        const userId = userData.data._id;
        const userEmail = userData.data.email;
        const userRole = userData.data.role;
        if (userRole !== 'instructor') {
            return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'only instructors can see there courses' });
        }
        const courses = await courseModel.find({ 'user.userName': userName });
        if (courses.length === 0) {
            return res.status(StatusCodes.NOT_FOUND).json({ message: 'No courses found' });
        }
        return res.status(StatusCodes.OK).json({ message: 'Courses fetched successfully', data: courses });
        

    } catch (error) {
        console.error('Error fetching your  course :', error);
        return next(new Error('Failed to fetch courses', { cause: 500 }));
    }
});



// export const editCourse = asyncHandler(async (req, res, next) => {
//     try {
//         const { courseName, duration, category, capacity, enrolledStudents, published } = req.body;

//         // Check if the token is provided
//         if (!req.headers.token) {
//             return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'Unauthorized' });
//         }

//         // Fetch user data from the API
//         const userData = await fetchUserData(req.headers.token);
//         const { name: userName, _id: userId, email: userEmail, role: userRole } = userData.data;

//         // Check if user is admin
//         if (userRole !== 'admin') {
//             return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'Only admins can edit courses' });
//         }

//         // Find the course by its name
//         const existingCourse = await courseModel.findOne({ courseName });

//         // Check if the course exists
//         if (!existingCourse) {
//             return res.status(StatusCodes.NOT_FOUND).json({ message: 'Course not found' });
//         }

//         const update = {};
//         if (published) update.published = published;
//         if (duration) update.duration = duration;
//         if (category) update.category = category;
//         if (capacity) update.capacity = capacity;
//         if (enrolledStudents) update.enrolledStudents = enrolledStudents;

//         // Update the course
//         const updatedCourse = await courseModel.findOneAndUpdate(
//             { courseName: courseName }, 
//             update, 
//             { new: true } 
//         );

//         if (!updatedCourse) {
//             return res.status(StatusCodes.NOT_FOUND).json({ message: 'Course not found' });
//         }

//         return res.status(StatusCodes.OK).json({ message: 'Course updated successfully', data: updatedCourse });
//     } catch (error) {
//         console.error('Error updating course:', error);
//         return next(new Error('Failed to update course', { cause: 500 }));
//     }
// });
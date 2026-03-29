import { StatusCodes } from "http-status-codes";
import { asyncHandler } from "../../utils/errorHandling.js";
import axios from 'axios'
import { enrollmentModel } from '../../../DB/models/enrollment.model.js';
import cron from 'node-cron';

export const fetchUserData = async (token) => {
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

export const fetchCourseData = async (courseName, instructorName) => {
    try {
        const response = await axios.get(`http://localhost:5001/course/getCourseByNameR/${courseName}/${instructorName}`);
        return response.data;
    } catch (error) {
        console.error('Error fetching course data:', error);
        throw new Error('Failed to fetch course data');
    }
};

export const fetchEnrollmentData = async (courseName, instructorName) => {
    try {
        const response = await axios.get('http://localhost:8080/demo-1.0-SNAPSHOT/Api/students/enrollment/WithName', {
            data: {
                courseName: courseName,
                instructorName: instructorName
            }
        });
        return response.data;
    } catch (error) {
        // console.error('Error fetching course data:', error);
        console.log('Error fetching enrollment data:');
        throw new Error('Failed to fetch cenrollment data');
    }
};

export const fetchFailedEnrollmentData = async (token, instructorName, courseName) => {
    try {
        const response = await axios.post(
            'http://localhost:8080/demo-1.0-SNAPSHOT/Api/students/enroll',
            {
                instructorName: instructorName,
                courseName: courseName
            },
            {
                headers: {
                    Authorization: token,
                    'Content-Type': 'application/json'
                }
            }
        );
        console.log(`hgeeeeeeeeeeeeeeebk`);
        console.log(response.data);
        return response.data;
    } catch (error) {
        console.log("lllllll");
        if (error.response===undefined) {
            return {
                response: "serverDown"    
            }
        }else{
        console.log('Error fetching the enrollment data:',error.response.data);
        return {
            response: error.response.data
        }
    }
    }
};


export const enroll = asyncHandler(async (req, res, next) => {
    try {
        // Check if user is authenticated
        const token = req.headers.authorization;
        const { instructorName, courseName } = req.body;
        console.log("i have entered");
        console.log(instructorName, courseName);
        console.log(token);
        const mytry=await fetchFailedEnrollmentData(token, instructorName, courseName);
        console.log("assy3 mn bldk");
        console.log(mytry);
        console.log("asy3 mn mamtk");
        console.log(mytry.response);
        if (mytry.response==='serverDown' || mytry==="undefined") {
            // return res.status(StatusCodes.UNAUTHORIZED).json({ error: "a7aaaaaa 3mlthaaa" });
            console.log("tl3 waa23333 ya bashaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            // Check if instructorName and courseName are provided
        if (!instructorName || !courseName) {
            return res.status(StatusCodes.BAD_REQUEST).json({ error: "You must enter instructorName and courseName" });
        }

        if (!token) {
            return res.status(StatusCodes.UNAUTHORIZED).json({ error: "Unauthorized" });
        }

        const userData = await fetchUserData(token);
        console.log(userData);
         const id=userData.data._id
         const Uname=userData.data.name
         const Uemail=userData.data.email
         const Urole=userData.data.role

        // Check if the user role is student
        if (Urole !== "student") {
            return res.status(StatusCodes.BAD_REQUEST).json({ error: "Only students can enroll in courses" });
        }

        // Fetch course data
        const courseData = await fetchCourseData( courseName,instructorName);
        console.log(courseData);


        const existingEnrollment = await enrollmentModel.findOne({
            'studentEmail': userData.data.email,
            'courseId': courseData.data._id
        });
        if (existingEnrollment) {
            return res.status(StatusCodes.BAD_REQUEST).json({ message: 'You have already try to enroll ' });
        }
        // Save enrollment
        const enrollment = new enrollmentModel({
            courseName: courseData.data.courseName,
            category: courseData.data.category,
            instructorId: courseData.data.user.userId,
            instructorName: courseData.data.user.userName,
            courseId: courseData.data._id,
            studentName: userData.data.name,
            studentEmail: userData.data.email,
        });
        await enrollment.save();

        return res.status(StatusCodes.OK).json({ message: "Enrollment back up successfully saved successfully" });
        }
        else{
            if (mytry.response===undefined) {
                return res.status(StatusCodes.OK).json({  status: mytry });
            }
            console.log(mytry.response);
            return res.status(StatusCodes.OK).json({  status: mytry.response });
        }

        
    } catch (error) {
        console.error('Error occurred during enrollment:', error);
        return res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({ error: "Internal server error" });
    }
});


export const getallTemporaryEnrollments = asyncHandler(async (req, res, next) => {
    const data = await enrollmentModel.find().select('-createdAt -updatedAt -__v');
    if (data.length === 0) {
        return res.status(StatusCodes.OK).json({ message: "No temporary enrollments found" });
    } else {
        return res.status(StatusCodes.OK).json({ message: "The temporary enrollments are", data });
    }
});


let dataSent = false; 

async function sendWithDelay() {
    try {
        if (!dataSent) {
            const temporaryEnrollments = await enrollmentModel.find({}, { _id: 0, createdAt: 0, updatedAt: 0, __v: 0 }); // Retrieve temporary enrollments
            console.log('Sending temporary enrollments:', temporaryEnrollments); // Log data being sent
            if (temporaryEnrollments.length > 0) {
                for (const enrollment of temporaryEnrollments) {
                    console.log("Sending enrollment:", enrollment);
                    const response = await axios.post('http://localhost:8080/demo-1.0-SNAPSHOT/Api/students/enroll2', enrollment);
                    console.log("Response:", response.data);
                    if (response.status === 200) {
                        console.log('Temporary enrollment sent successfully:', enrollment);
                        await enrollmentModel.deleteOne({ _id: enrollment._id }); // Clear the sent enrollment
                    } else {
                        console.error('Failed to send temporary enrollment. Status:', response.status);
                    }
                }
                dataSent = true; // Update flag to indicate data has been sent
            } else {
                console.log('No temporary enrollments to send.');
            }
        } else {
            console.log('Data has already been sent.');
        }
    } catch (error) {
        console.error('Error sending temporary enrollments:', error);
    }
}


(async () => {
    try {
        await sendWithDelay();
    } catch (error) {
        console.error('Error on server startup:', error);
    }
})();

export const sendTemporaryEnrollments = asyncHandler(async (req, res, next) => {
    try {
        if (req.method === 'POST') {
            console.log('Received request to send temporary enrollments from client.');
            // Call the function to send temporary enrollments
            const temporaryEnrollments = await enrollmentModel.find({}, { _id: 0, createdAt: 0, updatedAt: 0, __v: 0 });
            if (temporaryEnrollments.length > 0) {
                // Update flag to indicate data is being sent
                dataSent = true;
                return res.status(StatusCodes.OK).json({ message: "Sending temporary enrollments...", temporaryEnrollments });
            } else {
                console.log('No temporary enrollments to send.');
                return res.status(StatusCodes.OK).json({ message: "No temporary enrollments to send." });
            }
        } else {
            console.log('Invalid request method. Only POST requests are accepted.');
            return res.status(StatusCodes.METHOD_NOT_ALLOWED).json({ error: "Invalid request method. Only POST requests are accepted." });
        }
    } catch (error) {
        console.error('Error sending temporary enrollments:', error);
        return res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({ error: error.response.data });
    }
});


let deletionCronScheduled = false;

function scheduleEnrollmentDeletion() {
    if (!deletionCronScheduled) {
        cron.schedule('*/5 * * * *', async () => {
            console.log('Starting cron job to delete enrollments after 5 minutes from project initialization.');
            await deleteAllEnrollments();
            deletionCronScheduled = true; 
        }, {
            scheduled: true,
            timezone: "Africa/Cairo" 
        });
    } else {
        console.log('Deletion cron job has already been scheduled.');
    }
}

async function deleteAllEnrollments() {
    try {
        const result = await enrollmentModel.deleteMany({}); 
        console.log('Deleted all enrollments:', result.deletedCount);
    } catch (error) {
        console.error('Error deleting enrollments:', error);
    }
}

scheduleEnrollmentDeletion();



















// let dataSent = false; // Flag to track if data has been sent

// async function sendWithDelay() {
//     try {
//         if (!dataSent) {
//             const temporaryEnrollments = await enrollmentModel.find({}, { _id: 0, createdAt: 0, updatedAt: 0, __v: 0 }); // Retrieve temporary enrollments
//             console.log('Sending temporary enrollments:', temporaryEnrollments); // Log data being sent
//             if (temporaryEnrollments.length > 0) {
//                 for (const enrollment of temporaryEnrollments) {
//                     // Send each enrollment individually to the endpoint
//                     const response = await axios.post('http://localhost:8080/demo-1.0-SNAPSHOT/Api/students/enroll2', enrollment);
//                     if (response.status === 200) {
//                         console.log('Temporary enrollment sent successfully:', enrollment);
//                         await enrollmentModel.deleteOne({ _id: enrollment._id }); // Clear the sent enrollment
//                     } else {
//                         console.error('Failed to send temporary enrollment. Status:', response.status);
//                     }
//                 }
//                 dataSent = true; // Update flag to indicate data has been sent
//             } else {
//                 console.log('No temporary enrollments to send.');
//             }
//         } else {
//             console.log('Data has already been sent.');
//         }
//     } catch (error) {
//         console.error('Error sending temporary enrollments:', error.response.data);
//     }
// }

// // Request handler
// export const sendTemporaryEnrollments = asyncHandler(async (req, res, next) => {
//     try {
//         if (req.method === 'POST') {
//             console.log('Received request to send temporary enrollments from client.');
//             // Call the function to send temporary enrollments
//             const temporaryEnrollments = await enrollmentModel.find({}, { _id: 0, createdAt: 0, updatedAt: 0, __v: 0 });
//             if (temporaryEnrollments.length > 0) {
//                 // Update flag to indicate data is being sent
//                 dataSent = true;
//                 return res.status(StatusCodes.OK).json({ message: "Sending temporary enrollments...", temporaryEnrollments });
//             } else {
//                 console.log('No temporary enrollments to send.');
//                 return res.status(StatusCodes.OK).json({ message: "No temporary enrollments to send." });
//             }
//         } else {
//             console.log('Invalid request method. Only POST requests are accepted.');
//             return res.status(StatusCodes.METHOD_NOT_ALLOWED).json({ error: "Invalid request method. Only POST requests are accepted." });
//         }
//     } catch (error) {
//         console.error('Error sending temporary enrollments:', error.response.data);
//         return res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({ error: error.response.data });
//     }
// });

// // Request handler
// // Schedule the task to run every 10 minutes
// cron.schedule('*/10 * * * *', async () => {
//     await sendTemporaryEnrollments();
// });
import { StatusCodes } from "http-status-codes";
import { asyncHandler } from "../../../utils/errorHandling.js";
import { userModel } from "../../../../DB/models/user.model.js";
import jwt from "jsonwebtoken"


export const getUserById = asyncHandler(async (req, res, next) => {
    const id=req.user._id;
    const user = await userModel.findById(id);
    if (!user) {
        return next(new Error("User not found", StatusCodes.NOT_FOUND));
    }
    res.status(StatusCodes.OK).json({
        status: "success",
        data: user,
    });
});

export const getAllUsers = asyncHandler(async (req, res, next) => {
    const role=req.user.role
    if (role!="admin") {
        return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'not authorized' })
    }
    const users = await userModel.find();
    if (users.length === 0) {
        return res.status(StatusCodes.NOT_FOUND).json({ message: 'No users found' });
            
    }
    return res.status(StatusCodes.OK).json({status: "success",data: users,});
});

export const getUserByEmail = asyncHandler(async (req, res, next) => {
    console.log("hmmmmmmmmmmmm");
    const { email } = req.query;
    console.log(email);
    const user = await userModel.findOne({ email: email }).select('name email role');

    if (!user) {
        return next(new Error("User not found", StatusCodes.NOT_FOUND));
    }

    return res.status(StatusCodes.OK).json({ message: 'user fetched successfully',  user });
});

export const Newauthentication = asyncHandler(async (req, res, next) => {
    const { authorization } = req.headers;
    console.log(authorization);
    if (!authorization) {
        return next(new Error(`Token not provided`, {cause:401}));
    }
    const decoded = await jwt.verify(authorization, process.env.TOKEN_SIGNATURE);
    console.log(decoded);
    const user = await userModel.findById(decoded.payload.id).select('name email role');
    if (!user) {
        return next(new Error(`invalid token`, {cause:401}));
    }
    return res.status(StatusCodes.OK).json({ message: 'user fetched successfully',  user });
    
   
});


export const getAllUsersWithStatistics = asyncHandler(async (req, res, next) => {
    try {
        const role = req.user.role;
        if (role !== "admin") {
            return res.status(StatusCodes.UNAUTHORIZED).json({ message: 'Not authorized' });
        }
        
        // Retrieve all users
        const users = await userModel.find();
        if (users.length === 0) {
            return res.status(StatusCodes.NOT_FOUND).json({ message: 'No users found' });
        }
        
        // Count the number of instructors and students
        let numberOfInstructors = 0;
        let numberOfStudents = 0;
        users.forEach(user => {
            if (user.role === "instructor") {
                numberOfInstructors++;
            } else if (user.role === "student") {
                numberOfStudents++;
            }
        });

        return res.status(StatusCodes.OK).json({ 
            status: "success",
            data: {
                "number of instructors": numberOfInstructors,
                "number of students": numberOfStudents
            }
        });
    } catch (error) {
        console.error('Error Fetching Users:', error);
        return next(new Error('Failed Fetching Users', error.stack, { cause: 500 }));
    }
});

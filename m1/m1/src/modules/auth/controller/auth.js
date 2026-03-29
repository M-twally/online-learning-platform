import { StatusCodes } from "http-status-codes";
import { ErrorClass } from "../../../utils/errorClass.js";
import { asyncHandler } from "../../../utils/errorHandling.js";
import { generateToken } from "../../../utils/generateAndVerifyToken.js";
import { userModel } from "../../../../DB/models/user.model.js";
// import bcrypt from "bcrypt"



export const signUp = asyncHandler(async (req, res, next) => {
  const { name, email, password, confirmPassword, role, affiliation, bio, yearsOfExperience } = req.body;

  try {
      const userexist = await userModel.findOne({ email: email });

    if (userexist) {
      return next(new Error("Email already exists", StatusCodes.CONFLICT));
    }

    // Check if password matches the confirmation
    if (confirmPassword !== password) {
      return next(new Error("Password confirmation doesn't match", StatusCodes.BAD_REQUEST));
    }

    // Create a new user based on the role specified
    const user=new userModel(req.body)
    await user.save();
  
    // Generate token
    const token = generateToken({
      payload: {
        id: user.id,
        email: user.email,
      }
    });

    // Send success response with user data and token
    return res.status(StatusCodes.CREATED).json({ message: "User signed up successfully", result: user, token });
  } catch (error) {
    console.error('Error during sign-up:', error);
    return next(new Error("Internal server error", StatusCodes.INTERNAL_SERVER_ERROR));
  }
});

export const signIn = asyncHandler(async (req, res, next) => {
  const { email, password } = req.body;
  const user = await userModel.findOne({email:email});
  if (!email || !password ||email==null||password==null) {
    return next(new Error("Please provide email and password"),StatusCodes.BAD_REQUEST);
  }

  if (!user) {
    return next(new Error("Email does not exist sign up first"),StatusCodes.BAD_REQUEST);
  }
  
 
  if (password === user.password) {
    const token = generateToken({
      payload: {
        id: user.id,
        password,
      },
    });
    return res.status(StatusCodes.OK).json({ message: "Successfully signed in", user, token,role :user.role });
  }
   else {
    return next(new ErrorClass("Incorrect data"), StatusCodes.BAD_REQUEST);
  }
});

import { Router } from "express";
import * as UserRouter from "./controller/user.js";
import { allowedTo, authentication } from "../../middleware/auth.js";

const router = Router();

router.get( "/userInfo",authentication,UserRouter.getUserById);
router.get( "/getByMail",UserRouter.getUserByEmail);
router.get( "/getAllUsers",authentication,UserRouter.getAllUsers);
router.get( "/getAllUsersWithStatistics",authentication,UserRouter.getAllUsersWithStatistics);
router.get( "/auth",UserRouter.Newauthentication);


export default router;

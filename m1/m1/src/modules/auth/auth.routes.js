import { Router } from "express";
import * as AuthRouter from "./controller/auth.js";

const router = Router();

router.post( "/signup",AuthRouter.signUp);
router.post("/signIn",AuthRouter.signIn);

export default router;

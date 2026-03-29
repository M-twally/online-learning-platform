import { Router } from "express";
import * as EnrollRouter from "./enrollments/enrollment.js";

const router = Router();
router.post("/enroll", EnrollRouter.enroll);
router.get("/all", EnrollRouter.getallTemporaryEnrollments);
router.get('/temporary-enrollments', EnrollRouter.getallTemporaryEnrollments);
// router.get('/sendTemporary-enrollments', EnrollRouter.sendTemporaryEnrollments);
router.post('/sendTemporary-enrollments', EnrollRouter.sendTemporaryEnrollments);





export default router;

import { Router } from "express";
import * as CourseRouter from "./controller/course.js";

const router = Router();
router.post("/create", CourseRouter.createCourse);
router.put("/update", CourseRouter.editCourse);
router.get("/getCourses", CourseRouter.getCourses);
router.get("/getPublishedCourses", CourseRouter.getPublishedCourses);
router.get("/getUnPublishedCourses", CourseRouter.getUnPublishedCourses);
router.get('/getCourseByNameR/:courseName/:instructorName', CourseRouter.getCourseByNameR);
router.get("/sendNotifications", CourseRouter.sendNotifications);
router.post("/searchName", CourseRouter.getCourseByName);
router.post("/searchCategory", CourseRouter.getCourseByCategory);
router.get("/getCourseById", CourseRouter.getCourseById);
router.get("/getCourseByUserAndName", CourseRouter.getCourseByUserAndName);
router.put("/incrementNumberOfStudents", CourseRouter.incrementNumberOfStudents);
router.put("/decrementNumberOfStudents", CourseRouter.decrementNumberOfStudents);
router.get("/myCourses",CourseRouter.getMycourses)




export default router;

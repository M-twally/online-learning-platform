import connectDB from "../DB/connection.js";
import { globalEroorHandling } from "../src/utils/errorHandling.js"
import CourseRouter from  "./modules/course/course.routes.js"

const initApp = (app, express) => {
  app.use(express.json());
  app.use("/course", CourseRouter);



  app.use(globalEroorHandling);
  app.use("/*", (req, res, next) => {
    return res.json({ message: "In_valid Routing🚫" })
})
  connectDB();
};

export default initApp;

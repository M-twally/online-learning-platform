import connectDB from "../DB/connection.js";
import { globalEroorHandling } from "../src/utils/errorHandling.js"
import AuthRouter from  "./modules/auth/auth.routes.js"
import UserRouter from  "./modules/users/user.routes.js"

const initApp = (app, express) => {
  app.use(express.json());
  app.use('/auth', AuthRouter)
  app.use('/user', UserRouter)



  app.use(globalEroorHandling);
  app.use("/*", (req, res, next) => {
    return res.json({ message: "In_valid Routing🚫" })
})
  connectDB();
};

export default initApp;

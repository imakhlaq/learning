import configureOpenAPI from "@/lib/configure-open-api";
import createApp from "@/lib/create-app";
import index from "@/routes/index.route";
import task from "@/routes/task/task.index";

const app = createApp();

const routes = [index, task];

configureOpenAPI(app);

routes.forEach(route => app.route("/", route));

export default app;
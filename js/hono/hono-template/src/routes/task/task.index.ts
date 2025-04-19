import { createRouter } from "@/lib/create-app";

import * as handlers from "./task.handler";
import * as routes from "./task.routes";

const router = createRouter().openapi(routes.list, handlers.list);
// .openapi(routes.update, handlers.update); // another route

export default router;
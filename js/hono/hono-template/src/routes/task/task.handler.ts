import type { AppRouteHandler } from "@/lib/types";
import type { ListRoute } from "@/routes/task/task.routes";

export const list: AppRouteHandler<ListRoute> = (c) => {
  c.var.logger.info("hello");

  return c.json({ message: "mess" });
};
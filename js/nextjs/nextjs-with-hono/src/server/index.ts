import {OpenAPIHono, createRoute, z} from '@hono/zod-openapi';
import {ContextVariables} from "@/server/types";
import {cors} from 'hono/cors'

//you can validate values and types using Zod and generate OpenAPI Swagger documentation
const app = new OpenAPIHono<{ Variables: ContextVariables }>({

    //default validation failed handler
    defaultHook: (result, c) => {
        if (!result.success) {
            return c.json(
                {
                    ok: false,
                    //          errors: formatZodErrors(result),
                    source: 'custom_error_handler',
                },
                422
            )
        }
    },
});

app.use("/api/*", cors(
    {
        origin: ['https://example.com', 'https://example.org'],
        allowHeaders: ['X-Custom-Header', 'Upgrade-Insecure-Requests'],
        allowMethods: ['POST', 'GET', 'OPTIONS'],
        exposeHeaders: ['Content-Length', 'X-Kuma-Revision'],
        maxAge: 600,
        credentials: true,
    }))

// The OpenAPI documentation will be available at /doc
app.doc31('/docs', {openapi: '3.1.0', info: {title: 'foo', version: '1'}}) // new endpoint
app.getOpenAPI31Document({
    openapi: '3.1.0',
    info: {title: 'foo', version: '1'},
})

const userRoutes = createRoute({
    method: 'post',
    path: '/books',
    request: {
        body: {
            content: {
                'application/json': {
                    schema: z.object({
                        title: z.string(),
                    }),
                },
            },
        },
    },
    responses: {
        200: {
            description: 'Success message',
        },
    },
})

//methods will be decided by the method inside the "userRoutes"
app.openapi(userRoutes, (c) => {
        const validatedBody = c.req.valid('json')
        return c.json(validatedBody) // validatedBody is {}
    },
    //over-riding the default valid faild hadler
    (result, c) => {
        if (!result.success) {
            return c.json(
                {
                    ok: false,
                    source: 'routeHook' as const,
                },
                400
            )
        }
    })

export default app;
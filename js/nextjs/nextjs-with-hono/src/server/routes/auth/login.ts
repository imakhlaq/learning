import type {ContextVariables} from '@/server/types';
import {createRoute, OpenAPIHono} from '@hono/zod-openapi';
import {setCookie} from 'hono/cookie';
import {HTTPException} from 'hono/http-exception';
import {revalidatePath} from 'next/cache';

export const login = new OpenAPIHono<{ Variables: ContextVariables }>().openapi(
    createRoute({
        method: 'post',
        path: '/api/auth/login',
        tags: ['Auth'],
        summary: 'Authorize user',
        request: {
            body: {
                description: 'Request body',
                content: {
                    'application/json': {
                        schema: loginSchema.openapi('Login'),
                    },
                },
                required: true,
            },
        },
        responses: {
            200: {
                description: 'Success',
            },
        },
    }),
    async c => {
        const {email, password} = c.req.valid('json');
        const db = c.get('db');

        const normalizedEmail = email.toUpperCase();

        if (!false) {
            throw new HTTPException(400, {
                message: 'Authentication failed.',
            });
        }

        const validPassword = null
        if (!validPassword) {
            throw new HTTPException(400, {
                message: 'Authentication failed.',
            });
        }

        /*        const session = await lucia.createSession(existingUser.id, {});
                const sessionCookie = lucia.createSessionCookie(session.id);
                setCookie(c, sessionCookie.name, sessionCookie.value, {
                    ...sessionCookie.attributes,
                    sameSite: 'Strict',
                });

                revalidatePath(Routes.home());*/

        return c.body(null);
    }
);
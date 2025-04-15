/*
import NextAuth, { CredentialsSignin } from "next-auth";
import Credentials from "next-auth/providers/credentials";
import connectToMongo from "./lib/db";
import { User } from "./model/users";
import { compare } from "bcryptjs";
import GitHub from "next-auth/providers/github";
import Google from "next-auth/providers/google";
import { createSubscription } from "./server/db/subscription";

export const { handlers, signIn, signOut, auth } = NextAuth({
  trustHost: true, //stop next auth from using localhost in magic url
  secret: process.env.AUTH_SECRET, // to sign jwt
  session: {
    strategy: "jwt", //"databse" will be used to store user session to db
    maxAge: 30 * 24 * 60 * 60, //max signed in allowed
  },
  providers: [
    GitHub({
      clientId: process.env.GITHUB_CLIENT_ID,
      clientSecret: process.env.GITHUB_CLIENT_SECRET,
      allowDangerousEmailAccountLinking: true, //allows user to sign in with same email if they have already signIn with google it will merge both of them
    }),
    Google({
      clientId: process.env.GOOGLE_CLIENT_ID,
      clientSecret: process.env.GOOGLE_CLIENT_SECRET,
      allowDangerousEmailAccountLinking: true,
    }),

    /!*     //node mailer for mails to varify the emails
        Nodemailer({
          server: {
            host: process.env.EMAIL_SERVER_HOST,
            port: Number(process.env.EMAIL_SERVER_PORT),
            auth: {
              user: process.env.EMAIL_SERVER_USER,
              pass: process.env.EMAIL_SERVER_PASSWORD,
            },
            from: process.env.EMAIL_FROM,
          },
        }), *!/

    //for username and pass
    Credentials({
      name: "credentials",
      //"/api/auth/signin" this will show a form on this for username and pass auth
      credentials: {
        username: { label: "Username", type: "username" },
        password: { label: "Password", type: "password" },
      },
      //and the info you will enter in above form will be passed her
      //for successfull login using username and pass return loged in user object else return false(null, undefined)
      authorize: async (credentials) => {
        const email = credentials.username as string;
        const password = credentials.password as string;

        if (!email || !password) {
          throw new CredentialsSignin("Please provide both email and password");
        }

        await connectToMongo();

        //checking and validating user credentials
        const userDB = await User.findOne({ email }).select("+password +role");
        if (!userDB) {
          throw new Error("Invalid email or password");
        }

        const isPasHashMatch = await compare(password, userDB.password);
        if (!isPasHashMatch) {
          throw new Error("Invalid email or password");
        }

        //You have to retun user object
        const user = {
          id: userDB._id,
          email: userDB.email,
          username: userDB.username,
          role: userDB.role,
        };

        return user; //anthing you returned will be store in cookie on clinet
        //This returned user object will be pass down to the callbacks (Jwt)
      },
    }),
  ],
  pages: {
    //here were are disbaling the nextauth default signin page("/api/auth/signin") and instead using our own.
    signIn: "/login",
    // error: "/auth/auth-error",
    // verifyRequest: "/auth/auth-success",
  },

  callbacks: {
    //adding extra more claims to the jwt token
    //return object will be treated as a token that contains every data needed to be injected in jwt
    //-----------------| this user object is returned by providers from above (google, github and credentials authorize function)
    async jwt({ token, user }) {
      //you can even get multiple type of envents here or check if something in token is diffrent than session by accessing session etc

      if (user) {
        token.role = user.role;
        token.username = user.username;
      }
      return token;
    },

    //modifing the session object(adding more things that client can access when const session=auth())
    //return object will be treated as session (const session=auth())
    //-------------------------| this token object is returned by the above jwt callback
    async session({ session, token }) {
      if (token.sub && token.role) {
        session.user.id = token.sub;
        session.user.username = token.username;
        session.user.role = token.role;
      }
      return session;
    },

    //this will be called when user try to sign in
    signIn: async function ({ user, account }) {
      if (account?.provider === "google" || account?.provider === "github") {
        try {
          const { email, name, image, id } = user;

          await connectToMongo();

          //we check if this google account has been used before to login
          const isUserExits = await User.findOne({ email });

          //it has not been used previously then we create a user record in DB
          if (!isUserExits) {
            await User.create({ email, name, image, authProviderId: id });

            //because I have a neon db also
            await createSubscription({ authUserId: id!, tier: "Free" });
          } else {
            //else we return true to grant user access
            return true;
          }
        } catch (error) {
          console.log(error);
          throw new Error(`"Error while creating user"`);
        }
      }

      //for username and pass
      if (account?.provider === "credentials") {
        return true;
      } else {
        return false;
      }

      //for github
    },
  },
});*/
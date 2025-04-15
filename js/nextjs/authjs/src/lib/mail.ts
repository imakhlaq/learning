import { Resend } from "resend";

const resend = new Resend(process.env.RESEND_API_KEY);

export async function sendEmailVerification(email: string, token: string) {
  const confirmationLink = `http://localhost:300/auth/new-verification?token=${token}`;

  const { data, error } = await resend.emails.send({
    from: "onboarding@resend.dev",
    to: email,
    subject: "Confirm your email",
    html: `<p>Click <a href=${confirmationLink}}>here</a> to confirm email</p>`,
  });
}

export async function sendPasswordResetEmail(email: string, token: string) {
  const resetLink = `http://localhost:300/auth/new-verification?token=${token}`;

  const { data, error } = await resend.emails.send({
    from: "onboarding@resend.dev",
    to: email,
    subject: "Reset your password",
    html: `<p>Click <a href=${resetLink}}>here</a> to reset your password</p>`,
  });
}
"use client";
import CardWrapper from "@/components/auth/card-wrapper";
import * as z from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
  FormControl,
} from "@/components/ui/form";
import { LoginSchema } from "@/schemas";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import FormError from "@/components/form-error";
import FormSuccess from "@/components/form-success";
import { useState, useTransition } from "react";
import { login } from "@/actions/login";
import { useSearchParams } from "next/navigation";
import Link from "next/link";

type Props = {};
export default function LoginForm({}: Props) {
  //do async task inside the startTransition and until the work is complete the pending will be tru
  const [isPending, startTransition] = useTransition();

  //access the error from the url
  const searchParam = useSearchParams();
  //when user signed with different provider but same email
  const urlError =
    searchParam.get("error") === "OauthAccountNotLinked"
      ? "Email already in use with different provider"
      : "";

  const [error, setError] = useState<string | undefined>();
  const [success, setSuccess] = useState<string | undefined>();

  const form = useForm<z.infer<typeof LoginSchema>>({
    resolver: zodResolver(LoginSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  });

  function handleSubmit(values: z.infer<typeof LoginSchema>) {
    setError("");
    setSuccess("");
    //doing async work
    startTransition(() => {
      //calling a server actions from client component
      //and inside the server action we form "credentials" signIn
      login(values).then((data) => {
        setError(data?.error);
        setSuccess(data?.success);
      });
    });
  }

  return (
    <CardWrapper
      headerLabel={"Welcome Back"}
      backButtonLabel={"Don't have an account?"}
      backButtonHref={"/auth/register"}
      showSocial
    >
      <Form {...form}>
        <form className={"space-y-6"} onClick={form.handleSubmit(handleSubmit)}>
          <div className={"space-y-4"}>
            {/* email from field */}
            <FormField
              control={form.control}
              name={"email"}
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Email</FormLabel>
                  <FormControl>
                    <Input
                      {...field}
                      placeholder={"exmaple@gmail.com"}
                      type={"email"}
                    />
                  </FormControl>
                  {/* to change error message set in zod schema */}
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* password from field */}
            <FormField
              control={form.control}
              name={"password"}
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Password</FormLabel>
                  <FormControl>
                    <Input
                      {...field}
                      placeholder={"*******"}
                      type={"password"}
                    />
                  </FormControl>
                  {/* forget password */}
                  <Button
                    size={"sm"}
                    variant={"link"}
                    asChild
                    className={"px-0 font-normal"}
                  >
                    <Link href={"/auth/reset"}>Forget Password</Link>
                  </Button>
                  {/* to change error message set in zod schema */}
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>
          <FormError message={error || urlError} />
          <FormSuccess message={success} />
          <Button className={"w-full"} type={"submit"} disabled={isPending}>
            Login
          </Button>
        </form>
      </Form>
    </CardWrapper>
  );
}
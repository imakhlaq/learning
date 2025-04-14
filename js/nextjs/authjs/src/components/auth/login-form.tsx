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
import { LoginSchema } from "../../schemas";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import FormError from "@/components/form-error";
import FormSuccess from "@/components/form-success";
import { useState, useTransition } from "react";
import { login } from "@/actions/login";

type Props = {};
export default function LoginForm({}: Props) {
  //do async task inside the startTransition and until the work is complete the pending will be tru
  const [isPending, startTransition] = useTransition();

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
                  {/* to change error message set in zod schema */}
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>
          <FormError message={error} />
          <FormSuccess message={success} />
          <Button className={"w-full"} type={"submit"} disabled={isPending}>
            Login
          </Button>
        </form>
      </Form>
    </CardWrapper>
  );
}
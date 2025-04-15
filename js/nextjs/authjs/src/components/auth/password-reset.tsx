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
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import FormError from "@/components/form-error";
import FormSuccess from "@/components/form-success";
import { useState, useTransition } from "react";
import { NewPasswordSchema } from "@/schemas";
import { useSearchParams } from "next/navigation";
import { newPassword } from "@/actions/new-password";

type Props = {};
export default function PasswordReset({}: Props) {
  const searchParams = useSearchParams();
  const token = searchParams.get("token");

  const [isPending, startTransition] = useTransition();
  const [error, setError] = useState<string | undefined>();
  const [success, setSuccess] = useState<string | undefined>();

  const form = useForm<z.infer<typeof NewPasswordSchema>>({
    resolver: zodResolver(NewPasswordSchema),
    defaultValues: {
      password: "",
    },
  });

  function handleSubmit(values: z.infer<typeof NewPasswordSchema>) {
    setError("");
    setSuccess("");
    //doing async work
    startTransition(() => {
      //calling a server actions from client component
      //and inside the server action we form "credentials" signIn
      newPassword(values, token).then((data) => {
        setError(data?.error);
        setSuccess(data?.success);
      });
    });
  }

  return (
    <CardWrapper
      headerLabel={"Enter a new password"}
      backButtonLabel={"Back to login"}
      backButtonHref={"/auth/login"}
    >
      <Form {...form}>
        <form className={"space-y-6"} onClick={form.handleSubmit(handleSubmit)}>
          <div className={"space-y-4"}>
            {/* email from field */}
            <FormField
              control={form.control}
              name={"password"}
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Password</FormLabel>
                  <FormControl>
                    <Input
                      {...field}
                      placeholder={"*********"}
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
            Rest Password
          </Button>
        </form>
      </Form>
    </CardWrapper>
  );
}
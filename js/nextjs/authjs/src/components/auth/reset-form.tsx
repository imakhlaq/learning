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
import { LoginSchema, ResetSchema } from "@/schemas";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import FormError from "@/components/form-error";
import FormSuccess from "@/components/form-success";
import { useState, useTransition } from "react";
import { restPassword } from "@/actions/reset";

type Props = {};
export default function ResetForm({}: Props) {
  const [isPending, startTransition] = useTransition();
  const [error, setError] = useState<string | undefined>();
  const [success, setSuccess] = useState<string | undefined>();

  const form = useForm<z.infer<typeof ResetSchema>>({
    resolver: zodResolver(ResetSchema),
    defaultValues: {
      email: "",
    },
  });

  function handleSubmit(values: z.infer<typeof ResetSchema>) {
    setError("");
    setSuccess("");
    //doing async work
    startTransition(() => {
      //calling a server actions from client component
      //and inside the server action we form "credentials" signIn
      restPassword(values).then((data) => {
        setError(data?.error);
        setSuccess(data?.success);
      });
    });
  }

  return (
    <CardWrapper
      headerLabel={"Forget your password"}
      backButtonLabel={"Back to login"}
      backButtonHref={"/auth/login"}
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
          </div>
          <FormError message={error} />
          <FormSuccess message={success} />
          <Button className={"w-full"} type={"submit"} disabled={isPending}>
            Send reset email
          </Button>
        </form>
      </Form>
    </CardWrapper>
  );
}
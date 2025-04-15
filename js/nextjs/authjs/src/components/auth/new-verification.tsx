"use client";
import CardWrapper from "@/components/auth/card-wrapper";
import { BeatLoader } from "react-spinners";
import { useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";
import { newVerification } from "@/actions/new-verification";
import FormSuccess from "@/components/form-success";
import FormError from "@/components/form-error";

type Props = {};
export default function NewVerification({}: Props) {
  const [error, setError] = useState<string | undefined>();
  const [success, setSuccess] = useState<string | undefined>();

  const param = useSearchParams();
  const token = param.get("token");

  useEffect(() => {
    (async function () {
      if (!token) {
        setError("Missing Toke!");
        return;
      }
      await newVerification(token)
        .then((data) => {
          setSuccess(data.success);
          setError(data.error);
        })
        .catch((e) => setError("Something went wrong!"));
    })();
  }, []);

  return (
    <CardWrapper
      headerLabel={"Confirming your email verification"}
      backButtonHref={"/auth/login"}
      backButtonLabel={"Back to login"}
    >
      <div className={"flex items-center justify-center w-full"}>
        {!success && !error && <BeatLoader />}
        <FormSuccess message={success} />
        <FormError message={error} />
      </div>
    </CardWrapper>
  );
}
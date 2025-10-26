import LoginPage from "@/components/login";
import { Suspense } from "react";


export default function Login(){
  return(
    <Suspense>
      <LoginPage/>
    </Suspense>
  )
}
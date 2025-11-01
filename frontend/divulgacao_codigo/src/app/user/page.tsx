"use client"

import UserDataPage from "@/components/User";
import { Suspense } from "react";


export default function EditUser(){
    return (
        <UserDataPage update={true}/>
    )
}
"use client";

import LoginLocalEmail from "@/components/LoginLocalEmail";
import LoginButton from "@/components/LoginButton";
import { signIn } from "next-auth/react";
import Image from "next/image";

export default function Login() {
  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <div className="p-8 bg-white rounded-lg shadow-md w-full max-w-md">
        <h1 className="text-2xl font-bold text-center mb-6 text-black dark:text-white">Welcome</h1>
        <div className="space-y-4">
          <LoginLocalEmail />
          
          <div className="relative">
            <div className="absolute inset-0 flex items-center">
              <div className="w-full border-t border-gray-300"></div>
            </div>
            <div className="relative flex justify-center text-sm">
              <span className="px-2 bg-white text-gray-500">Or continue with</span>
            </div>
          </div>

          <LoginButton />
          
          <button
            onClick={() => signIn("linkedin", { callbackUrl: "/dashboard" })}
            className="flex items-center justify-center w-full px-4 py-2 space-x-2 text-gray-600 transition-colors duration-300 border border-gray-300 rounded-lg hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-200"
          >
            <Image
              src="/icons8-linkedin-48.png"
              alt="LinkedIn Logo"
              width={20}
              height={20}
            />
            <span>Continue with LinkedIn</span>
          </button>
        </div>
      </div>
    </div>
  );
}

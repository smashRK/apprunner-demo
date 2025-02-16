"use client";

import CurdContainer from "@/components/CurdContainer";
import { jwtDecode } from "jwt-decode";
import { signIn, signOut, useSession } from "next-auth/react";
import { useRouter, useSearchParams } from "next/navigation";
import { useEffect, useMemo } from "react";

export default function Dashboard() {
  const { data: session, status } = useSession();
  const searchParams = useSearchParams();
  const token = searchParams.get("token");
  const router = useRouter();

  useEffect(() => {
    const handleToken = async () => {
      if (token && !session) {
        try {
          // Create a session using the token
          await signIn("credentials", {
            token,
            redirect: false,
          });
        } catch (error) {
          console.error("Error creating session:", error);
          router.replace("/login");
        }
      }
    };

    handleToken();
  }, [token, session, router]);

  useEffect(() => {
    if (status === "unauthenticated" && !token) {
      router.replace("/login");
    }
  }, [status, token, router]);

  const decodedToken = useMemo(() => {
    try {
      return token ? jwtDecode(token) : null;
    } catch (error) {
      console.error("Invalid token:", error);
      return null;
    }
  }, [token]);

  if (status === "loading") {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900"></div>
      </div>
    );
  }

  if (!session?.user && !decodedToken) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900"></div>
      </div>
    );
  }

  return (
    <div className="container mx-auto p-4">
      <div className="flex justify-end mb-4">
        <button
          className="border border-1 px-3 py-1 cursor-pointer rounded-lg"
          onClick={() => signOut({ callbackUrl: "/login" })}
        >
          Sign out
        </button>
      </div>
      <div className="bg-white shadow rounded-lg p-6 max-w-2xl mx-auto">
        <div className="flex items-center gap-4 mb-6">
          {(session?.user?.image || decodedToken) && (
            <img
              src={session?.user?.image || "/placeholder.png"}
              alt="Profile"
              className="w-16 h-16 rounded-full"
            />
          )}
          <div>
            <h1 className="text-2xl font-bold">
              Welcome, {session?.user?.name || decodedToken?.given_name}!
            </h1>
            <p className="text-gray-600">
              {session?.user?.email || decodedToken?.emails?.[0]}
            </p>
          </div>
        </div>
      </div>
      <CurdContainer />
    </div>
  );
}

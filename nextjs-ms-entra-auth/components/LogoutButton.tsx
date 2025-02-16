"use client";
import { signOut, useSession } from "next-auth/react";
import { useRouter } from "next/navigation";

function LogoutButton() {
  const { data: session } = useSession();
  const router = useRouter();

  const handleLogout = async () => {
    try {
      // Clear all auth-related cookies
      const cookies = document.cookie.split(";");
      for (let cookie of cookies) {
        const cookieName = cookie.split("=")[0].trim();
        if (cookieName.includes("next-auth") || cookieName === "auth_token") {
          document.cookie = `${cookieName}=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/;`;
        }
      }

      // Sign out with force flag and redirect
      await signOut({
        redirect: true,
        callbackUrl: "/login"
      });

      // Force redirect if signOut doesn't do it
      setTimeout(() => {
        router.replace("/login");
      }, 100);
    } catch (error) {
      console.error("Logout error:", error);
      router.replace("/login");
    }
  };

  return (
    <button
      onClick={handleLogout}
      className="w-full bg-blue-800 text-white p-3 rounded-md hover:opacity-80"
      disabled={!session}
    >
      Logout
    </button>
  );
}

export default LogoutButton;

"use client";
import { signIn } from "next-auth/react";
import { useState, useEffect } from "react";
import Image from "next/image";
import { useRouter } from "next/navigation";

function LoginLocalEmail() {
    const [signInURL, setSignInURL] = useState<string>("");
    const [isLoading, setIsLoading] = useState(false);
    const router = useRouter();

    useEffect(() => {
        // Fetch the sign-in URL from our API
        fetch('/api/auth/signin-url')
            .then(res => res.json())
            .then(data => {
                setSignInURL(data.url);
            })
            .catch(error => {
                console.error('Error fetching sign-in URL:', error);
            });
    }, []);

    const handleSignIn = () => {
        if (!signInURL) {
            console.error('Sign-in URL not yet available');
            return;
        }

        setIsLoading(true);
        
        // Open the sign-in URL in a popup window
        const width = 600;
        const height = 700;
        const left = window.screenX + (window.outerWidth - width) / 2;
        const top = window.screenY + (window.outerHeight - height) / 2;
        
        const popup = window.open(
            signInURL,
            "Sign In",
            `width=${width},height=${height},left=${left},top=${top},toolbar=0,scrollbars=1,status=1,resizable=1,location=1,menuBar=0`
        );

        // Add a check for popup closure and token in URL
        const checkPopup = setInterval(async () => {
            try {
                if (!popup || popup.closed) {
                    clearInterval(checkPopup);
                    setIsLoading(false);
                    return;
                }

                // Check if the popup URL contains a token
                const popupUrl = popup.location.href;
                if (popupUrl.includes('token=')) {
                    clearInterval(checkPopup);
                    popup.close();
                    setIsLoading(false);
                    
                    // Extract token and redirect to dashboard
                    const token = new URL(popupUrl).searchParams.get('token');
                    if (token) {
                        router.push(`/dashboard?token=${token}`);
                    }
                }
            } catch (error) {
                // Ignore cross-origin errors when checking popup URL
                if (!(error instanceof DOMException)) {
                    console.error('Error checking popup:', error);
                }
            }
        }, 1000);
    };

    return (
        <div className="flex flex-col gap-4 w-full">
            <button
                onClick={handleSignIn}
                className="w-full bg-white text-gray-800 border border-gray-300 p-3 rounded-md hover:bg-gray-50 disabled:opacity-75 flex items-center justify-center gap-2"
                disabled={!signInURL || isLoading}
            >
                {isLoading ? (
                    "Signing in..."
                ) : (
                    <>
                        <Image 
                            src="/signin.png" 
                            alt="sign logo" 
                            width={24} 
                            height={24}
                            className="w-6 h-6 bg-white rounded p-1"
                        />
                        <span>Sign in with Email</span>
                    </>
                )}
            </button>
        </div>
    );
}

export default LoginLocalEmail;
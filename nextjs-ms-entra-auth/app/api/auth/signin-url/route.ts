import { NextResponse } from "next/server";

export async function GET() {
    const ClientID = process.env.MICROSOFT_ENTRA_ID ?? '';
    const SignInPolicy = process.env.SIGNIN_POLICY ?? '';
    const CodeChallenge = process.env.CODE_CHALLENGE ?? '';
    const ChallengeMethod = process.env.CODE_CHALLENGE_METHOD ?? '';
    const TenantName = process.env.MICROSOFT_ENTRA_ID_TENANT_NAME ?? '';
    const ChallengeVerifier = process.env.CODE_VERIFIER ?? '';

    const baseUrl = `https://${TenantName}.b2clogin.com/${TenantName}.onmicrosoft.com/oauth2/v2.0/`;

    const redirectUri = `${process.env.NEXTAUTH_URL}/api/auth/get-token`;

    // Build the URL manually to avoid encoding specific parameters
    const signInUrl = `${baseUrl}authorize`;
    
    // Parameters that need encoding
    const encodedParams = new URLSearchParams();
    encodedParams.append("p", SignInPolicy);
    encodedParams.append("client_id", ClientID);
    encodedParams.append("response_type", "code");
    encodedParams.append("prompt", "login");
    encodedParams.append("code_challenge", CodeChallenge);
    encodedParams.append("code_challenge_method", ChallengeMethod);

    // Combine all parameters, keeping openid+offline_access and redirect_uri unencoded
    const url = `${signInUrl}?${encodedParams.toString()}&scope=openid+offline_access&redirect_uri=${redirectUri}`;

    return NextResponse.json({ url });
}

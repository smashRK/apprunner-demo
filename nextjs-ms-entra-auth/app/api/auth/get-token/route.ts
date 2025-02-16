import { NextRequest, NextResponse } from "next/server";

export async function GET(request: NextRequest) {
    // Get the authorization code from the URL
    const searchParams = request.nextUrl.searchParams;
    const code = searchParams.get('code');

    if (!code) {
        return NextResponse.json({ error: 'No authorization code provided' }, { status: 400 });
    }

    const ClientID = process.env.MICROSOFT_ENTRA_ID ?? '';
    const SignInPolicy = process.env.SIGNIN_POLICY ?? '';
    const ChallengeVerifier = process.env.CODE_VERIFIER ?? '';
    const ClientSecret = process.env.MICROSOFT_ENTRA_ID_SECRET ?? '';
    const TenantName = process.env.MICROSOFT_ENTRA_ID_TENANT_NAME ?? '';

    const tokenEndpoint = `https://${TenantName}.b2clogin.com/${TenantName}.onmicrosoft.com/${SignInPolicy}/oauth2/v2.0/token`;

    try {
        // Prepare the token request
        const tokenParams = new URLSearchParams();
        tokenParams.append('grant_type', 'authorization_code');
        tokenParams.append('client_id', ClientID);
        tokenParams.append('code_verifier', ChallengeVerifier);
        tokenParams.append('client_secret', ClientSecret);
        tokenParams.append('code', code);
        tokenParams.append('scope', 'openid profile email');
        tokenParams.append('redirect_uri', `${process.env.NEXTAUTH_URL}/api/auth/get-token`);

        // Make the token request
        const response = await fetch(tokenEndpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: tokenParams.toString()
        });

        // First check if response is ok before trying to parse JSON
        if (!response.ok) {
            const errorText = await response.text();
            console.error('Token exchange failed:', errorText);
            return NextResponse.json({ error: 'Token exchange failed', details: errorText }, { status: response.status });
        }

        const data = await response.json();

        // Extract the id_token from the response
        const { id_token } = data;

        if (!id_token) {
            return NextResponse.json({ error: 'No ID token received' }, { status: 400 });
        }

        // Return an HTML page that will close the popup and redirect the main window
        const html = `
            <!DOCTYPE html>
            <html>
                <head>
                    <title>Authentication Successful</title>
                </head>
                <body>
                    <script>
                        if (window.opener) {
                            // Redirect the main window
                            window.opener.location.href = '/dashboard?token=${id_token}';
                            // Close this popup
                            window.close();
                        } else {
                            // If somehow opened directly, redirect this window
                            window.location.href = '/dashboard?token=${id_token}';
                        }
                    </script>
                    <p>Authentication successful! You can close this window.</p>
                </body>
            </html>
        `;

        return new NextResponse(html, {
            headers: {
                'Content-Type': 'text/html',
            },
        });

    } catch (error) {
        console.error('Error exchanging code for token:', error);
        return NextResponse.json({ error: 'Internal server error' }, { status: 500 });
    }
}

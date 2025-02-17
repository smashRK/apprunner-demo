import NextAuth from "next-auth";
import Google from "next-auth/providers/google";
import MicrosoftEntraID from "next-auth/providers/microsoft-entra-id";
import CredentialsProvider from "next-auth/providers/credentials";
import LinkedIn from "next-auth/providers/linkedin";
import { jwtDecode } from "jwt-decode";

export const { handlers, signIn, signOut, auth } = NextAuth({
  providers: [
    MicrosoftEntraID({
      clientId: process.env.MICROSOFT_ENTRA_ID,
      clientSecret: process.env.MICROSOFT_ENTRA_ID_SECRET,
      issuer: `https://login.microsoftonline.com/${process.env.MICROSOFT_ENTRA_ID_TENANT_ID}/v2.0`,
    }),
    Google({
      clientId: process.env.GOOGLE_CLIENT_ID,
      clientSecret: process.env.GOOGLE_CLIENT_SECRET,
      authorization: {
        params: {
          scope: "openid email profile",
          prompt: "consent",
          access_type: "offline",
          response_type: "code",
        },
      },
    }),
    LinkedIn({
      clientId: process.env.LINKEDIN_CLIENT_ID,
      clientSecret: process.env.LINKEDIN_CLIENT_SECRET,
      authorization: {
        params: {
          scope: "openid profile email",
        },
      },
      callbackUrl: `${process.env.NEXTAUTH_URL}/api/auth/callback/linkedin`,
    }),
    CredentialsProvider({
      id: "credentials",
      name: "Credentials",
      credentials: {
        token: { type: "text" },
      },
      async authorize(credentials) {
        try {
          if (!credentials?.token) return null;

          const decoded = jwtDecode(credentials.token);
          return {
            id: decoded.sub as string,
            name: decoded.given_name as string,
            email: Array.isArray(decoded.emails) ? decoded.emails[0] : decoded.email,
            accessToken: credentials.token,
          };
        } catch (error) {
          console.error("Error in authorize:", error);
          return null;
        }
      },
    }),
  ],
  pages: {
    signIn: "/login",
  },
  callbacks: {
    async jwt({ token, account, profile, user }) {
      if (account) {
        token.accessToken = account.access_token;
        token.idToken = account.id_token;
      }
      if (user) {
        token.id = user.id;
        token.name = user.name;
        token.email = user.email;
        if ('accessToken' in user) {
          token.accessToken = user.accessToken;
        }
      }
      return token;
    },
    async session({ session, token }) {
      if (session.user) {
        session.user.id = token.id as string;
        session.user.accessToken = token.accessToken as string;
        session.user.idToken = token.idToken as string;
      }
      return session;
    },
    async redirect({ url, baseUrl }) {
      const localBaseUrl = process.env.NEXTAUTH_URL;
      
      // Handle sign-out
      if (url.includes('/signout')) {
        return `${localBaseUrl}/login`;
      }
      // After sign in, redirect to dashboard
      if (url.startsWith(baseUrl) || url.startsWith(localBaseUrl)) {
        return `${localBaseUrl}/dashboard`;
      }
      // Allows relative callback URLs
      else if (url.startsWith("/")) {
        return `${localBaseUrl}${url}`;
      }
      // If it's an absolute URL that doesn't match our base, rewrite it to use localhost:3000
      return url.replace(baseUrl, localBaseUrl);
    },
  },
  debug: true,
});

import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

// Define protected and public routes
const protectedRoutes = ["/dashboard", "/profile"];
const authRoutes = ["/login"];
const publicRoutes = ["/", "/api/auth/signin-url", "/api/auth/get-token"];

export function middleware(request: NextRequest) {
  const { pathname, searchParams } = request.nextUrl;
  
  // Allow all public routes
  if (publicRoutes.some(route => pathname.startsWith(route))) {
    return NextResponse.next();
  }

  // Allow all API routes
  if (pathname.startsWith("/api/")) {
    return NextResponse.next();
  }

  // Check for token in URL params (for initial redirect from auth)
  const token = searchParams.get("token");
  
  // Check for session token
  const hasSession = request.cookies.has("next-auth.session-token") || 
                    request.cookies.has("__Secure-next-auth.session-token");

  const isAuthenticated = token || hasSession;

  // Redirect to login if accessing protected route while not authenticated
  if (protectedRoutes.some(route => pathname.startsWith(route)) && !isAuthenticated) {
    return NextResponse.redirect(new URL("/login", request.url));
  }

  // Redirect to dashboard if accessing auth routes while authenticated
  if (authRoutes.includes(pathname) && isAuthenticated) {
    return NextResponse.redirect(new URL("/dashboard", request.url));
  }

  return NextResponse.next();
}

// Configure middleware to match specific paths
export const config = {
  matcher: ['/((?!_next/static|_next/image|favicon.ico).*)'],
};

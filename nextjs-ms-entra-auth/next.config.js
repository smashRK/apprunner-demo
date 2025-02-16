/** @type {import('next').NextConfig} */
const nextConfig = {
  typescript: {
    // !! WARN !!
    // Dangerously allow production builds to successfully complete even if
    // your project has type errors.
    ignoreBuildErrors: true,
  },
  eslint: {
    // Warning: This allows production builds to successfully complete even if
    // your project has ESLint errors.
    ignoreDuringBuilds: true,
  },
  async rewrites() {
    return [
      {
        source: '/api/users/:path*',
        destination: process.env.NODE_ENV === 'production' 
          ? 'http://backend:8080/api/users/:path*'  // Use service name in App Runner
          : 'http://localhost:8080/api/users/:path*', // Use localhost in development
      },
    ];
  },
};

module.exports = nextConfig;

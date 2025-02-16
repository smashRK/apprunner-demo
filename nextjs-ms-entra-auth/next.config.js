/** @type {import('next').NextConfig} */
const nextConfig = {
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

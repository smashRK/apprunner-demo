/** @type {import('next').NextConfig} */
const nextConfig = {
  async rewrites() {
    return [
      {
        source: '/api/users/:path*',
        destination: process.env.NODE_ENV === 'production' 
          ? 'http://localhost:8080/api/users/:path*'
          : process.env.API_BASE_URL + '/api/users/:path*',
      },
    ];
  },
  typescript: {
    ignoreBuildErrors: true,
  },
};

module.exports = nextConfig;

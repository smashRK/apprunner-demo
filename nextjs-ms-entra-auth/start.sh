#!/bin/sh
echo "Starting Next.js application..."
echo "Node version: $(node -v)"
echo "NPM version: $(npm -v)"
echo "Checking environment variables..."
env | grep -E "NEXT|MICROSOFT|GOOGLE|LINKEDIN" | sed 's/=.*/=***/'
echo "Checking directory structure..."
ls -la
echo "Starting server..."
exec node server.js

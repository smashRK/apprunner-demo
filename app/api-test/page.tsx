"use client";

import { useState, useEffect } from 'react';

export default function ApiTest() {
  const [status, setStatus] = useState<string>('Loading...');
  const [error, setError] = useState<string | null>(null);
  const [data, setData] = useState<any>(null);

  const testApi = async () => {
    try {
      setStatus('Testing API connection...');
      setError(null);
      
      const response = await fetch('https://5cgyjpmy6q.ap-south-1.awsapprunner.com/api/users');
      const data = await response.json();
      
      setStatus('Connected successfully!');
      setData(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to connect to API');
      setStatus('Connection failed');
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      <div className="max-w-2xl mx-auto bg-white rounded-lg shadow-md p-6">
        <h1 className="text-2xl font-bold mb-6">API Connection Test</h1>
        
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <span className="font-medium">Status:</span>
            <span 
              className={`px-3 py-1 rounded-full text-sm ${
                status === 'Connected successfully!' 
                  ? 'bg-green-100 text-green-800'
                  : status === 'Connection failed'
                  ? 'bg-red-100 text-red-800'
                  : 'bg-blue-100 text-blue-800'
              }`}
            >
              {status}
            </span>
          </div>

          <button
            onClick={testApi}
            className="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 transition-colors"
          >
            Test Connection
          </button>

          {error && (
            <div className="mt-4 p-4 bg-red-50 border border-red-200 rounded-md">
              <h3 className="text-red-800 font-medium">Error:</h3>
              <p className="text-red-600">{error}</p>
            </div>
          )}

          {data && (
            <div className="mt-4">
              <h3 className="font-medium mb-2">Response Data:</h3>
              <pre className="bg-gray-50 p-4 rounded-md overflow-auto max-h-60">
                {JSON.stringify(data, null, 2)}
              </pre>
            </div>
          )}

          <div className="mt-4 p-4 bg-gray-50 rounded-md">
            <h3 className="font-medium mb-2">API Details:</h3>
            <p><strong>API Base URL:</strong> {process.env.NEXT_PUBLIC_API_BASE_URL || process.env.API_BASE_URL || 'Not configured'}</p>
            <p><strong>Environment:</strong> {process.env.NODE_ENV}</p>
            <p><strong>Endpoint Tested:</strong> /api/users</p>
          </div>
        </div>
      </div>
    </div>
  );
}

import { JwtPayload } from 'jwt-decode'

declare module 'jwt-decode' {
  export interface JwtPayload {
    given_name?: string;
    family_name?: string;
    emails?: string[];
    name?: string;
    email?: string;
    sub?: string;
    oid?: string;
    preferred_username?: string;
    [key: string]: any;
  }
}

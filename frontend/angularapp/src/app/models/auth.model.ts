export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  id: number;
  username: string;
  role: 'ADMIN' | 'REVIEWER' | 'VIEWER';
}
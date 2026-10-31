export interface ApiError {
    success: false;
  message: string;
  timestamp: string;
  path: string;
  errors?: ValidationError[];
}

export interface ValidationError {
  field: string;
  message: string;
}
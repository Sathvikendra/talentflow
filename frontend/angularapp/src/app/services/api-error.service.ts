import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ApiError {
  handleError(error: HttpErrorResponse) {
    let message = 'An unexpected error occurred. Please try again.';

    if (error.status === 0) {
      message = 'Cannot connect to the server. Check your connection.';
    } else if (error.status === 400) {
      message = 'The request is invalid. Please check your input.';
    } else if (error.status === 401) {
      message = 'Authentication is required. Please log in.';
    } else if (error.status === 403) {
      message = 'You do not have permission to perform this action.';
    } else if (error.status === 404) {
      message = 'The requested resource was not found.';
    } else if (error.status === 409) {
      message = 'The request conflicts with existing data.';
    } else if (error.status >= 500) {
      message = 'A server error occurred. Please try again later.';
    } else if (error.error?.message) {
      message = error.error.message;
    }

    console.error('TalentFlow API error:', error);

    return throwError(() => ({
      status: error.status,
      message,
      details: error.error
    }));
  }
}

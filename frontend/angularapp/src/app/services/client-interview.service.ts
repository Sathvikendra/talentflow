import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable } from 'rxjs';
import { ApiError } from './api-error.service';

@Injectable({
  providedIn: 'root',
})
export class ClientInterview {
  private readonly apiUrl = `${environment.apiUrl}/client-interviews`;

  constructor(
    private http: HttpClient,
    private apiErrorService: ApiError
  ) {}

  getAllInterviews(): Observable<unknown[]> {
    return this.http.get<unknown[]>(this.apiUrl).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  getInterviewById(id: number): Observable<unknown> {
    return this.http.get<unknown>(`${this.apiUrl}/${id}`).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  getInterviewsBySubmission(submissionId: number): Observable<unknown[]> {
    return this.http.get<unknown[]>(
      `${this.apiUrl}/submission/${submissionId}`
    ).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  createInterview(request: Record<string, unknown>): Observable<unknown> {
    return this.http.post<unknown>(this.apiUrl, request).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  updateInterview(
    id: number,
    request: Record<string, unknown>
  ): Observable<unknown> {
    return this.http.put<unknown>(`${this.apiUrl}/${id}`, request).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  scheduleInterview(
    id: number,
    request: Record<string, unknown>
  ): Observable<unknown> {
    return this.http.patch<unknown>(
      `${this.apiUrl}/${id}/schedule`,
      request
    ).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  recordResult(
    id: number,
    request: Record<string, unknown>
  ): Observable<unknown> {
    return this.http.patch<unknown>(
      `${this.apiUrl}/${id}/result`,
      request
    ).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }
}

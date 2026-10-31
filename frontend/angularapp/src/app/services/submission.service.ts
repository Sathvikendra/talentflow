import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable } from 'rxjs';
import { ApiError } from './api-error.service';

@Injectable({
  providedIn: 'root',
})
export class Submission {
  private readonly apiUrl = `${environment.apiUrl}/submissions`;

  constructor(
    private http: HttpClient,
    private apiErrorService: ApiError
  ) {}

  getAllSubmissions(): Observable<unknown[]> {
    return this.http.get<unknown[]>(this.apiUrl).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  getSubmissionById(id: number): Observable<unknown> {
    return this.http.get<unknown>(`${this.apiUrl}/${id}`).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  getSubmissionsByCandidate(candidateId: number): Observable<unknown[]> {
    return this.http.get<unknown[]>(
      `${this.apiUrl}/candidate/${candidateId}`
    ).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  getSubmissionsByRequirement(requirementId: number): Observable<unknown[]> {
    return this.http.get<unknown[]>(
      `${this.apiUrl}/requirement/${requirementId}`
    ).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  getSubmissionsByStatus(status: string): Observable<unknown[]> {
    return this.http.get<unknown[]>(
      `${this.apiUrl}/status/${encodeURIComponent(status)}`
    ).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  createSubmission(request: Record<string, unknown>): Observable<unknown> {
    return this.http.post<unknown>(this.apiUrl, request).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  updateSubmission(
    id: number,
    request: Record<string, unknown>
  ): Observable<unknown> {
    return this.http.put<unknown>(`${this.apiUrl}/${id}`, request).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  changeSubmissionStatus(
    id: number,
    request: Record<string, unknown>
  ): Observable<unknown> {
    return this.http.patch<unknown>(
      `${this.apiUrl}/${id}/status`,
      request
    ).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  assignSubmission(
    id: number,
    request: Record<string, unknown>
  ): Observable<unknown> {
    return this.http.patch<unknown>(
      `${this.apiUrl}/${id}/assign`,
      request
    ).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }
}

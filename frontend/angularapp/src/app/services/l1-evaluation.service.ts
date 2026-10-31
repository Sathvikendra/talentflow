import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable } from 'rxjs';
import { ApiError } from './api-error.service';

@Injectable({
  providedIn: 'root',
})
export class L1Evaluation {
  private readonly apiUrl = `${environment.apiUrl}/l1-evaluations`;

  constructor(
    private http: HttpClient,
    private apiErrorService: ApiError
  ) {}

  getAllEvaluations(): Observable<unknown[]> {
    return this.http.get<unknown[]>(this.apiUrl).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  getEvaluationById(id: number): Observable<unknown> {
    return this.http.get<unknown>(`${this.apiUrl}/${id}`).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  getEvaluationsBySubmission(submissionId: number): Observable<unknown[]> {
    return this.http.get<unknown[]>(
      `${this.apiUrl}/submission/${submissionId}`
    ).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  getEvaluationsByInterviewer(interviewerId: number): Observable<unknown[]> {
    return this.http.get<unknown[]>(
      `${this.apiUrl}/interviewer/${interviewerId}`
    ).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  createEvaluation(request: Record<string, unknown>): Observable<unknown> {
    return this.http.post<unknown>(this.apiUrl, request).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  updateEvaluation(
    id: number,
    request: Record<string, unknown>
  ): Observable<unknown> {
    return this.http.put<unknown>(`${this.apiUrl}/${id}`, request).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  scheduleEvaluation(
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

import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable } from 'rxjs';
import { ApiError } from './api-error.service';
import { CreateL1EvaluationRequest, L1Evaluation, RecordL1EvaluationResultRequest, ScheduleL1EvaluationRequest, UpdateL1EvaluationRequest } from '../models/l1-evaluation.model';

@Injectable({
  providedIn: 'root',
})
export class L1EvaluationService {
  private readonly apiUrl = `${environment.apiUrl}/l1-evaluations`;

  constructor(
    private readonly http: HttpClient,
    private readonly apiErrorService: ApiError
  ) {}

  getAllEvaluations(): Observable<L1Evaluation[]> {
    return this.http.get<L1Evaluation[]>(this.apiUrl).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  getEvaluationById(id: number): Observable<L1Evaluation> {
    return this.http.get<L1Evaluation>(`${this.apiUrl}/${id}`).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  getEvaluationsBySubmission(
    submissionId: number
  ): Observable<L1Evaluation[]> {
    return this.http.get<L1Evaluation[]>(
      `${this.apiUrl}/submission/${submissionId}`
    ).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  getEvaluationsByInterviewer(
    interviewerId: number
  ): Observable<L1Evaluation[]> {
    return this.http.get<L1Evaluation[]>(
      `${this.apiUrl}/interviewer/${interviewerId}`
    ).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  createEvaluation(
    request: CreateL1EvaluationRequest
  ): Observable<L1Evaluation> {
    return this.http.post<L1Evaluation>(this.apiUrl, request).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  updateEvaluation(
    id: number,
    request: UpdateL1EvaluationRequest
  ): Observable<L1Evaluation> {
    return this.http.put<L1Evaluation>(
      `${this.apiUrl}/${id}`,
      request
    ).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  scheduleEvaluation(
    id: number,
    request: ScheduleL1EvaluationRequest
  ): Observable<L1Evaluation> {
    return this.http.patch<L1Evaluation>(
      `${this.apiUrl}/${id}/schedule`,
      request
    ).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  recordResult(
    id: number,
    request: RecordL1EvaluationResultRequest
  ): Observable<L1Evaluation> {
    return this.http.patch<L1Evaluation>(
      `${this.apiUrl}/${id}/result`,
      request
    ).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }
}

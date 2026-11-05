import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable } from 'rxjs';
import { ApiError } from './api-error.service';
import { AssignSubmissionRequest, CreateSubmissionRequest, Submission, UpdateSubmissionRequest, UpdateSubmissionStatusRequest } from '../models/submission.model';

@Injectable({
  providedIn: 'root',
})
export class SubmissionService {
  private readonly apiUrl = `${environment.apiUrl}/submissions`;

  constructor(
    private readonly http: HttpClient,
    private readonly apiErrorService: ApiError
  ) {}

  getAllSubmissions(): Observable<Submission[]> {
    return this.http.get<Submission[]>(this.apiUrl).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  getSubmissionById(id: number): Observable<Submission> {
    return this.http.get<Submission>(`${this.apiUrl}/${id}`).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  getSubmissionsByCandidate(candidateId: number): Observable<Submission[]> {
    return this.http.get<Submission[]>(
      `${this.apiUrl}/candidate/${candidateId}`
    ).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  getSubmissionsByRequirement(requirementId: number): Observable<Submission[]> {
    return this.http.get<Submission[]>(
      `${this.apiUrl}/requirement/${requirementId}`
    ).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  getSubmissionsByStatus(status: string): Observable<Submission[]> {
    return this.http.get<Submission[]>(
      `${this.apiUrl}/status/${encodeURIComponent(status)}`
    ).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  createSubmission(request: CreateSubmissionRequest): Observable<Submission> {
    return this.http.post<Submission>(this.apiUrl, request).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  updateSubmission(
    id: number,
    request: UpdateSubmissionRequest
  ): Observable<Submission> {
    return this.http.put<Submission>(`${this.apiUrl}/${id}`, request).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  changeSubmissionStatus(
    id: number,
    request: UpdateSubmissionStatusRequest
  ): Observable<Submission> {
    return this.http.patch<Submission>(
      `${this.apiUrl}/${id}/status`,
      request
    ).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  assignSubmission(
    id: number,
    request: AssignSubmissionRequest
  ): Observable<Submission> {
    return this.http.patch<Submission>(
      `${this.apiUrl}/${id}/assign`,
      request
    ).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }
}

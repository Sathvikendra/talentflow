import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable } from 'rxjs';
import { ApiError } from './api-error.service';
import { ClientInterview, CreateClientInterviewRequest, RecordClientInterviewResultRequest, ScheduleClientInterviewRequest, UpdateClientInterviewRequest } from '../models/client-interview.model';

@Injectable({
  providedIn: 'root',
})
export class ClientInterviewService {
  private readonly apiUrl = `${environment.apiUrl}/client-interviews`;

  constructor(
    private readonly http: HttpClient,
    private readonly apiErrorService: ApiError
  ) {}

  getAllInterviews(): Observable<ClientInterview[]> {
    return this.http.get<ClientInterview[]>(this.apiUrl).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  getInterviewById(id: number): Observable<ClientInterview> {
    return this.http.get<ClientInterview>(`${this.apiUrl}/${id}`).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  getInterviewsBySubmission(
    submissionId: number
  ): Observable<ClientInterview[]> {
    return this.http.get<ClientInterview[]>(
      `${this.apiUrl}/submission/${submissionId}`
    ).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  createInterview(
    request: CreateClientInterviewRequest
  ): Observable<ClientInterview> {
    return this.http.post<ClientInterview>(this.apiUrl, request).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  updateInterview(
    id: number,
    request: UpdateClientInterviewRequest
  ): Observable<ClientInterview> {
    return this.http.put<ClientInterview>(
      `${this.apiUrl}/${id}`,
      request
    ).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  scheduleInterview(
    id: number,
    request: ScheduleClientInterviewRequest
  ): Observable<ClientInterview> {
    return this.http.patch<ClientInterview>(
      `${this.apiUrl}/${id}/schedule`,
      request
    ).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  recordResult(
    id: number,
    request: RecordClientInterviewResultRequest
  ): Observable<ClientInterview> {
    return this.http.patch<ClientInterview>(
      `${this.apiUrl}/${id}/result`,
      request
    ).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }
}

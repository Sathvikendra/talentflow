import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { ApiError } from './api-error.service';
import { StatusHistory } from '../models/status-history.model';

@Injectable({
  providedIn: 'root',
})
export class StatusHistoryService {
  private readonly apiUrl = `${environment.apiUrl}/status-history`;

  constructor(
    private readonly http: HttpClient,
    private readonly apiErrorService: ApiError
  ) {}

  getHistoryBySubmissionId(
    submissionId: number
  ): Observable<StatusHistory[]> {

    return this.http.get<StatusHistory[]>(
      `${this.apiUrl}/submission/${submissionId}`
    ).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }
}

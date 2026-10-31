import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment.development';
import { catchError, Observable } from 'rxjs';
import { ApiError } from './api-error.service';

@Injectable({
  providedIn: 'root',
})
export class Requirement {
  private readonly apiUrl = `${environment.apiUrl}/requirements`;

  constructor(
    private http: HttpClient,
    private apiErrorService: ApiError
  ) {}

  getAllRequirements(): Observable<unknown[]> {
    return this.http.get<unknown[]>(this.apiUrl).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  getRequirementById(id: number): Observable<unknown> {
    return this.http.get<unknown>(`${this.apiUrl}/${id}`).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }

  createRequirement(request: Record<string, unknown>): Observable<unknown> {
    return this.http.post<unknown>(this.apiUrl, request).pipe(
      catchError((error) => this.apiErrorService.handleError(error))
    );
  }
}

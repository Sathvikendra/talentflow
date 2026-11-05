import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError } from 'rxjs';

import { environment } from '../../environments/environment.development';
import { ApiError } from './api-error.service';

import {
  Requirement,
  CreateRequirementRequest
} from '../models/requirement.model';

@Injectable({
  providedIn: 'root'
})
export class RequirementService {

  private readonly apiUrl = `${environment.apiUrl}/requirements`;

  constructor(
    private readonly http: HttpClient,
    private readonly apiError: ApiError
  ) {}

  getAllRequirements(): Observable<Requirement[]> {
    return this.http.get<Requirement[]>(this.apiUrl).pipe(
      catchError(error => this.apiError.handleError(error))
    );
  }

  getRequirementById(id: number): Observable<Requirement> {
    return this.http.get<Requirement>(`${this.apiUrl}/${id}`).pipe(
      catchError(error => this.apiError.handleError(error))
    );
  }

  createRequirement(
    request: CreateRequirementRequest
  ): Observable<Requirement> {
    return this.http.post<Requirement>(this.apiUrl, request).pipe(
      catchError(error => this.apiError.handleError(error))
    );
  }
}
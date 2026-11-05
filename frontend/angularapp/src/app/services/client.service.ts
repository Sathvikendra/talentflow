
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable } from 'rxjs';

import { environment } from '../../environments/environment.development';
import { ApiError } from './api-error.service';
import { Client } from '../models/client.model';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private readonly apiUrl = `${environment.apiUrl}/clients`;

  constructor(
    private readonly http: HttpClient,
    private readonly apiErrorService: ApiError
  ) {}

  getAllClients(): Observable<Client[]> {
    return this.http.get<Client[]>(this.apiUrl).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }

  getClientById(id: number): Observable<Client> {
    return this.http.get<Client>(`${this.apiUrl}/${id}`).pipe(
      catchError(error => this.apiErrorService.handleError(error))
    );
  }
}

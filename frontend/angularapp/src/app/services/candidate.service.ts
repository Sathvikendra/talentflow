import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { Candidate } from '../models/candidate.model';

@Injectable({
  providedIn: 'root'
})
export class CandidateService {

  private http = inject(HttpClient);

  private apiUrl = `${environment.apiUrl}/candidates`;

  getAllCandidates(): Observable<Candidate[]> {
    return this.http.get<Candidate[]>(this.apiUrl);
  }
  createCandidate(candidate: any) {
  return this.http.post(this.apiUrl, candidate);
}
}
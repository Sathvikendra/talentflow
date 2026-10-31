import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  getMetrics(): Observable<any> {
    return of({
      totalCandidates: 15,
      totalRequirements: 10,
      totalSkills: 8,
      totalSubmissions: 5
    });
  }
}
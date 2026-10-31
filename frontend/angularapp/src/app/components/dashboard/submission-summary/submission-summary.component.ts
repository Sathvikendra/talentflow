import { Component, Input } from '@angular/core';

export interface SubmissionStatusSummary {
  label: string;
  status: string;
  count: number;
}

@Component({
  selector: 'app-submission-summary',
  standalone: false,
  styleUrl: './submission-summary.component.scss',
  templateUrl: './submission-summary.component.html',
})
export class SubmissionSummary {
  @Input() statuses: SubmissionStatusSummary[] = [];

  get totalSubmissions(): number {
    return this.statuses.reduce((total, item) => total + item.count, 0);
  }
}

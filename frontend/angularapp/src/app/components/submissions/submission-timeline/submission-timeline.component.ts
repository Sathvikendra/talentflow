import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { StatusHistory } from '../../../models/status-history.model';
import { StatusHistoryService } from '../../../services/status-history.service';

@Component({
  selector: 'app-submission-timeline',
  standalone: false,
  styleUrl: './submission-timeline.component.scss',
  templateUrl: './submission-timeline.component.html',
})
export class SubmissionTimeline implements OnInit, OnChanges {
  @Input() submissionId!: number;

  history: StatusHistory[] = [];

  isLoading = false;
  errorMessage = '';

  constructor(
    private readonly statusHistoryService: StatusHistoryService
  ) {}

  ngOnInit(): void {
    if (this.submissionId) {
      this.loadHistory();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['submissionId'] && !changes['submissionId'].firstChange) {
      this.loadHistory();
    }
  }

  loadHistory(): void {
    if (!this.submissionId) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.statusHistoryService
      .getHistoryBySubmissionId(this.submissionId)
      .subscribe({
        next: (data: StatusHistory[]) => {
          this.history = [...data].sort(
            (a, b) =>
              new Date(a.changedAt).getTime() -
              new Date(b.changedAt).getTime()
          );

          this.isLoading = false;
        },
        error: (error) => {
          this.errorMessage =
            error.message || 'Unable to load status history.';
          this.isLoading = false;
        }
      });
  }

  formatStatus(status: string | null): string {
    return status ? status.replace(/_/g, ' ') : 'Initial Status';
  }
}

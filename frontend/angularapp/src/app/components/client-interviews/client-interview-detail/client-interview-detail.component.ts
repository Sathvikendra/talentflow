import { Component, OnInit } from '@angular/core';
import { ClientInterview, ClientInterviewResult } from '../../../models/client-interview.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ClientInterviewService } from '../../../services/client-interview.service';

@Component({
  selector: 'app-client-interview-detail',
  standalone: false,
  styleUrl: './client-interview-detail.component.scss',
  templateUrl: './client-interview-detail.component.html',
})
export class ClientInterviewDetail implements OnInit {
  interview: ClientInterview | null = null;

  isLoading = false;
  isSaving = false;

  errorMessage = '';
  successMessage = '';

  clientInterviewerName = '';
  clientInterviewerEmail = '';
  scheduledAt = '';
  durationMinutes: number | null = null;
  meetingLink = '';
  comments = '';

  conductedAt = '';
  result: ClientInterviewResult = 'ACCEPTED';

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly interviewService: ClientInterviewService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (!id || Number.isNaN(id)) {
      this.errorMessage = 'Invalid client interview ID.';
      return;
    }

    this.loadInterview(id);
  }

  loadInterview(id: number): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.interviewService.getInterviewById(id).subscribe({
      next: interview => {
        this.interview = interview;
        this.clientInterviewerName = interview.clientInterviewerName ?? '';
        this.clientInterviewerEmail = interview.clientInterviewerEmail ?? '';
        this.scheduledAt = this.toDateTimeLocal(interview.scheduledAt);
        this.durationMinutes = interview.durationMinutes;
        this.meetingLink = interview.meetingLink ?? '';
        this.conductedAt = this.toDateTimeLocal(interview.conductedAt);
        this.comments = interview.comments ?? '';
        this.result = interview.result ?? 'ACCEPTED';
        this.isLoading = false;
      },
      error: error => {
        this.errorMessage =
          error?.message || 'Unable to load client interview.';
        this.isLoading = false;
      }
    });
  }

  updateInterview(): void {
    if (!this.interview || this.interview.result !== null) {
      return;
    }

    this.isSaving = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.interviewService.updateInterview(this.interview.id, {
      clientInterviewerName: this.clientInterviewerName.trim() || null,
      clientInterviewerEmail: this.clientInterviewerEmail.trim() || null,
      comments: this.comments.trim() || null
    }).subscribe({
      next: interview => {
        this.interview = interview;
        this.successMessage = 'Interview details updated successfully.';
        this.isSaving = false;
      },
      error: error => {
        this.errorMessage =
          error?.message || 'Unable to update interview details.';
        this.isSaving = false;
      }
    });
  }

  scheduleInterview(): void {
    if (!this.interview || !this.scheduledAt) {
      this.errorMessage = 'Please provide the scheduled date and time.';
      return;
    }

    if (
      this.durationMinutes !== null &&
      this.durationMinutes <= 0
    ) {
      this.errorMessage = 'Duration must be greater than zero.';
      return;
    }

    this.isSaving = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.interviewService.scheduleInterview(this.interview.id, {
      scheduledAt: this.scheduledAt,
      durationMinutes: this.durationMinutes,
      meetingLink: this.meetingLink.trim() || null
    }).subscribe({
      next: interview => {
        this.interview = interview;
        this.scheduledAt = this.toDateTimeLocal(interview.scheduledAt);
        this.durationMinutes = interview.durationMinutes;
        this.meetingLink = interview.meetingLink ?? '';
        this.successMessage = 'Interview schedule saved successfully.';
        this.isSaving = false;
      },
      error: error => {
        this.errorMessage =
          error?.message || 'Unable to schedule client interview.';
        this.isSaving = false;
      }
    });
  }

  recordResult(): void {
    if (!this.interview || !this.conductedAt) {
      this.errorMessage = 'Please provide the conducted date and time.';
      return;
    }

    if (this.interview.result !== null) {
      this.errorMessage = 'A result has already been recorded.';
      return;
    }

    if (!this.interview.scheduledAt) {
      this.errorMessage = 'Schedule the interview before recording its result.';
      return;
    }

    const confirmed = window.confirm(
      `Record the client interview result as ${this.result}? This will also update the submission status.`
    );

    if (!confirmed) {
      return;
    }

    this.isSaving = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.interviewService.recordResult(this.interview.id, {
      result: this.result,
      conductedAt: this.conductedAt,
      comments: this.comments.trim() || null
    }).subscribe({
      next: interview => {
        this.interview = interview;
        this.successMessage =
          'Interview result recorded. The submission status was updated by the backend.';
        this.isSaving = false;
      },
      error: error => {
        this.errorMessage =
          error?.message || 'Unable to record interview result.';
        this.isSaving = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/client-interviews']);
  }

  private toDateTimeLocal(value: string | null): string {
    return value ? value.substring(0, 16) : '';
  }
}

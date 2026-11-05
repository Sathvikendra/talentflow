import { Component, OnInit } from '@angular/core';
import { Submission } from '../../../models/submission.model';
import { SubmissionService } from '../../../services/submission.service';
import { ClientInterviewService } from '../../../services/client-interview.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-client-interview-form',
  standalone: false,
  styleUrl: './client-interview-form.component.scss',
  templateUrl: './client-interview-form.component.html',
})
export class ClientInterviewForm implements OnInit {
  eligibleSubmissions: Submission[] = [];

  submissionId: number | null = null;
  clientInterviewerName = '';
  clientInterviewerEmail = '';
  scheduledAt = '';
  durationMinutes: number | null = null;
  meetingLink = '';

  isLoadingSubmissions = false;
  isSaving = false;

  errorMessage = '';

  constructor(
    private readonly submissionService: SubmissionService,
    private readonly interviewService: ClientInterviewService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.loadEligibleSubmissions();
  }

  loadEligibleSubmissions(): void {
    this.isLoadingSubmissions = true;
    this.errorMessage = '';

    this.submissionService.getAllSubmissions().subscribe({
      next: submissions => {
        this.eligibleSubmissions = submissions.filter(
          submission =>
            submission.currentStatus === 'CI_PENDING' ||
            submission.currentStatus === 'CI_SCHEDULED'
        );

        this.isLoadingSubmissions = false;
      },
      error: error => {
        this.errorMessage =
          error?.message || 'Unable to load eligible submissions.';
        this.isLoadingSubmissions = false;
      }
    });
  }

  createInterview(): void {
    this.errorMessage = '';

    if (!this.submissionId) {
      this.errorMessage = 'Please select a submission.';
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

    this.interviewService.createInterview({
      submissionId: this.submissionId,
      clientInterviewerName: this.clientInterviewerName.trim() || null,
      clientInterviewerEmail: this.clientInterviewerEmail.trim() || null,
      scheduledAt: this.scheduledAt || null,
      durationMinutes: this.durationMinutes,
      meetingLink: this.meetingLink.trim() || null
    }).subscribe({
      next: interview => {
        this.isSaving = false;
        this.router.navigate(['/client-interviews', interview.id]);
      },
      error: error => {
        this.errorMessage =
          error?.message || 'Unable to create client interview.';
        this.isSaving = false;
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/client-interviews']);
  }
}

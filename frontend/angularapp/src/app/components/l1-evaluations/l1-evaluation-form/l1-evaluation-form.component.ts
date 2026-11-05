import { Component } from '@angular/core';
import { L1EvaluationService } from '../../../services/l1-evaluation.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-l1-evaluation-form',
  standalone: false,
  styleUrl: './l1-evaluation-form.component.scss',
  templateUrl: './l1-evaluation-form.component.html',
})
export class L1EvaluationForm {
  submissionId: number | null = null;
  interviewerId: number | null = null;
  scheduledAt = '';
  durationMinutes: number | null = null;
  meetingLink = '';

  isSaving = false;
  errorMessage = '';

  constructor(
    private readonly evaluationService: L1EvaluationService,
    private readonly router: Router
  ) {}

  createEvaluation(): void {
    this.errorMessage = '';

    if (
      !this.submissionId ||
      this.submissionId <= 0 ||
      !this.interviewerId ||
      this.interviewerId <= 0
    ) {
      this.errorMessage = 'Enter valid submission and interviewer IDs.';
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

    this.evaluationService.createEvaluation({
      submissionId: this.submissionId,
      interviewerId: this.interviewerId,
      scheduledAt: this.scheduledAt || null,
      durationMinutes: this.durationMinutes,
      meetingLink: this.meetingLink.trim() || null
    }).subscribe({
      next: evaluation => {
        this.isSaving = false;
        this.router.navigate(['/l1-evaluations', evaluation.id]);
      },
      error: error => {
        this.errorMessage =
          error?.message || 'Unable to create L1 evaluation.';
        this.isSaving = false;
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/l1-evaluations']);
  }
}

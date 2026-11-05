import { Component, OnInit } from '@angular/core';
import { L1Evaluation, L1EvaluationResult } from '../../../models/l1-evaluation.model';
import { ActivatedRoute, Router } from '@angular/router';
import { L1EvaluationService } from '../../../services/l1-evaluation.service';

@Component({
  selector: 'app-l1-evaluation-detail',
  standalone: false,
  styleUrl: './l1-evaluation-detail.component.scss',
  templateUrl: './l1-evaluation-detail.component.html',
})
export class L1EvaluationDetail implements OnInit {
  evaluation: L1Evaluation | null = null;

  isLoading = false;
  isSaving = false;
  errorMessage = '';
  successMessage = '';

  scheduledAt = '';
  durationMinutes: number | null = null;
  meetingLink = '';

  conductedAt = '';
  result: L1EvaluationResult = 'CLEARED';
  technicalComments = '';
  overallComments = '';
  recommendation = '';

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly evaluationService: L1EvaluationService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (!id || Number.isNaN(id)) {
      this.errorMessage = 'Invalid evaluation ID.';
      return;
    }

    this.loadEvaluation(id);
  }

  loadEvaluation(id: number): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.evaluationService.getEvaluationById(id).subscribe({
      next: evaluation => {
        this.evaluation = evaluation;
        this.scheduledAt = this.toDateTimeLocal(evaluation.scheduledAt);
        this.durationMinutes = evaluation.durationMinutes;
        this.meetingLink = evaluation.meetingLink ?? '';
        this.conductedAt = this.toDateTimeLocal(evaluation.conductedAt);
        this.result = evaluation.result ?? 'CLEARED';
        this.technicalComments = evaluation.technicalComments ?? '';
        this.overallComments = evaluation.overallComments ?? '';
        this.recommendation = evaluation.recommendation ?? '';
        this.isLoading = false;
      },
      error: error => {
        this.errorMessage = error?.message || 'Unable to load evaluation.';
        this.isLoading = false;
      }
    });
  }

  scheduleEvaluation(): void {
    if (!this.evaluation || !this.scheduledAt) {
      this.errorMessage = 'Please provide a scheduled date and time.';
      return;
    }

    this.isSaving = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.evaluationService.scheduleEvaluation(this.evaluation.id, {
      scheduledAt: this.scheduledAt,
      durationMinutes: this.durationMinutes,
      meetingLink: this.meetingLink.trim() || null
    }).subscribe({
      next: evaluation => {
        this.evaluation = evaluation;
        this.successMessage = 'Evaluation schedule updated successfully.';
        this.isSaving = false;
      },
      error: error => {
        this.errorMessage = error?.message || 'Unable to schedule evaluation.';
        this.isSaving = false;
      }
    });
  }

  recordResult(): void {
    if (!this.evaluation || !this.conductedAt) {
      this.errorMessage = 'Please provide the conducted date and time.';
      return;
    }

    if (this.evaluation.result !== null) {
      this.errorMessage = 'A result has already been recorded.';
      return;
    }

    const confirmed = window.confirm(
      `Record the L1 result as ${this.result}? This will also update the submission status.`
    );

    if (!confirmed) {
      return;
    }

    this.isSaving = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.evaluationService.recordResult(this.evaluation.id, {
      result: this.result,
      conductedAt: this.conductedAt,
      technicalComments: this.technicalComments.trim() || null,
      overallComments: this.overallComments.trim() || null,
      recommendation: this.recommendation.trim() || null
    }).subscribe({
      next: evaluation => {
        this.evaluation = evaluation;
        this.successMessage =
          `Result recorded. The submission status has been updated by the backend.`;
        this.isSaving = false;
      },
      error: error => {
        this.errorMessage = error?.message || 'Unable to record evaluation result.';
        this.isSaving = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/l1-evaluations']);
  }

  private toDateTimeLocal(value: string | null): string {
    return value ? value.substring(0, 16) : '';
  }
}

import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Submission, SubmissionStatus, UpdateSubmissionStatusRequest } from '../../../models/submission.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SubmissionService } from '../../../services/submission.service';

@Component({
  selector: 'app-submission-transition-dialog',
  standalone: false,
  styleUrl: './submission-transition-dialog.component.scss',
  templateUrl: './submission-transition-dialog.component.html',
})
export class SubmissionTransitionDialog {
  @Input() submission!: Submission;

  @Output() closed = new EventEmitter<void>();
  @Output() statusUpdated = new EventEmitter<void>();

  transitionForm: FormGroup;

  isSubmitting = false;
  errorMessage = '';

  readonly statuses: SubmissionStatus[] = [
    'PROFILE_RECEIVED',
    'L1_PENDING',
    'L1_SCHEDULED',
    'L1_CLEARED',
    'L1_REJECTED',
    'CI_PENDING',
    'CI_SCHEDULED',
    'CI_ACCEPTED',
    'CI_REJECTED',
    'WITHDRAWN'
  ];

  constructor(
    private readonly fb: FormBuilder,
    private readonly submissionService: SubmissionService
  ) {
    this.transitionForm = this.fb.group({
      status: ['', Validators.required],
      reason: ['', [
        Validators.required,
        Validators.maxLength(500)
      ]],
      comments: ['', Validators.maxLength(2000)]
    });
  }

  ngOnInit(): void {
    if (this.submission) {
      this.transitionForm.patchValue({
        status: this.submission.currentStatus
      });
    }
  }

  submitTransition(): void {
    this.errorMessage = '';

    if (this.transitionForm.invalid) {
      this.transitionForm.markAllAsTouched();
      return;
    }

    if (!this.submission?.id) {
      this.errorMessage = 'Invalid submission.';
      return;
    }

    const formValue = this.transitionForm.getRawValue();

    const request: UpdateSubmissionStatusRequest = {
      status: formValue.status,
      reason: formValue.reason.trim(),
      comments: formValue.comments?.trim() || null
    };

    this.isSubmitting = true;

    this.submissionService
      .changeSubmissionStatus(this.submission.id, request)
      .subscribe({
        next: () => {
          this.isSubmitting = false;
          this.statusUpdated.emit();
        },
        error: (error) => {
          this.errorMessage =
            error.message || 'Unable to update submission status.';
          this.isSubmitting = false;
        }
      });
  }

  cancel(): void {
    this.closed.emit();
  }

  getStatusLabel(status: SubmissionStatus): string {
    return status.replace(/_/g, ' ');
  }
}

import { Component, OnInit } from '@angular/core';
import { Submission, SubmissionStatus } from '../../../models/submission.model';
import { ActivatedRoute, Router } from '@angular/router';
import { SubmissionService } from '../../../services/submission.service';

@Component({
  selector: 'app-submission-detail',
  standalone: false,
  styleUrl: './submission-detail.component.scss',
  templateUrl: './submission-detail.component.html',
})
export class SubmissionDetail implements OnInit {

  submission: Submission | null = null;

  isLoading = false;
  errorMessage = '';
  showTransitionDialog = false;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly submissionService: SubmissionService
  ) {}

  ngOnInit(): void {
    this.loadSubmission();
  }

  loadSubmission(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (!id || Number.isNaN(id)) {
      this.errorMessage = 'Invalid submission ID.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.submissionService.getSubmissionById(id).subscribe({
      next: (data: Submission) => {
        this.submission = data;
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage =
          error.message || 'Unable to load submission.';
        this.isLoading = false;
      }
    });
  }

  openTransitionDialog(): void {
    this.showTransitionDialog = true;
  }

  closeTransitionDialog(): void {
    this.showTransitionDialog = false;
  }

  onStatusUpdated(): void {
    this.showTransitionDialog = false;
    this.loadSubmission();
  }

  getStatusLabel(status: SubmissionStatus): string {
    return status.replace(/_/g, ' ');
  }

  goBack(): void {
    this.router.navigate(['/submissions']);
  }
}

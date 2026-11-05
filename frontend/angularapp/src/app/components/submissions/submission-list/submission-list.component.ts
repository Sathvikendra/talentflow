import { Component, OnInit } from '@angular/core';
import { Submission, SubmissionStatus } from '../../../models/submission.model';
import { SubmissionService } from '../../../services/submission.service';

@Component({
  selector: 'app-submission-list',
  standalone: false,
  styleUrl: './submission-list.component.scss',
  templateUrl: './submission-list.component.html',
})
export class SubmissionList implements OnInit {
  submissions: Submission[] = [];
  filteredSubmissions: Submission[] = [];

  searchText = '';
  selectedStatus: SubmissionStatus | '' = '';

  isLoading = false;
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
    private readonly submissionService: SubmissionService
  ) {}

  ngOnInit(): void {
    this.loadSubmissions();
  }

  loadSubmissions(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.submissionService.getAllSubmissions().subscribe({
      next: (data: Submission[]) => {
        this.submissions = data;
        this.applyFilters();
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage =
          error.message || 'Unable to load submissions.';
        this.isLoading = false;
      }
    });
  }

  applyFilters(): void {
    const search = this.searchText.trim().toLowerCase();

    this.filteredSubmissions = this.submissions.filter(submission => {
      const matchesSearch =
        !search ||
        String(submission.id).includes(search) ||
        String(submission.candidateId).includes(search) ||
        String(submission.requirementId).includes(search);

      const matchesStatus =
        !this.selectedStatus ||
        submission.currentStatus === this.selectedStatus;

      return matchesSearch && matchesStatus;
    });
  }

  clearFilters(): void {
    this.searchText = '';
    this.selectedStatus = '';
    this.applyFilters();
  }

  getStatusLabel(status: SubmissionStatus): string {
    return status.replace(/_/g, ' ');
  }
}

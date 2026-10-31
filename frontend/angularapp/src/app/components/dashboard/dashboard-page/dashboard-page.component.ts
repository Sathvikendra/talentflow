import { Component } from '@angular/core';

interface DashboardMetric {
  title: string;
  value: number;
  description: string;
  icon: string;
}

interface SubmissionStatusSummary {
  label: string;
  status: string;
  count: number;
}

@Component({
  selector: 'app-dashboard-page',
  standalone: false,
  styleUrl: './dashboard-page.component.scss',
  templateUrl: './dashboard-page.component.html',
})
export class DashboardPage {
  readonly pageTitle = 'Dashboard';
  readonly pageDescription =
    'Track talent fulfillment activity and manage your workflow.';

  readonly metrics: DashboardMetric[] = [
    {
      title: 'Open Requirements',
      value: 0,
      description: 'Requirements currently open',
      icon: '▤'
    },
    {
      title: 'Active Candidates',
      value: 0,
      description: 'Candidates in the system',
      icon: '♙'
    },
    {
      title: 'Active Submissions',
      value: 0,
      description: 'Submissions being processed',
      icon: '⇄'
    },
    {
      title: 'Pending L1 Evaluations',
      value: 0,
      description: 'Evaluations awaiting completion',
      icon: '✓'
    }
  ];

  readonly submissionStatuses: SubmissionStatusSummary[] = [
    { label: 'Profile Received', status: 'PROFILE_RECEIVED', count: 0 },
    { label: 'L1 Pending', status: 'L1_PENDING', count: 0 },
    { label: 'L1 Scheduled', status: 'L1_SCHEDULED', count: 0 },
    { label: 'L1 Cleared', status: 'L1_CLEARED', count: 0 },
    { label: 'L1 Rejected', status: 'L1_REJECTED', count: 0 },
    { label: 'Client Interview Pending', status: 'CI_PENDING', count: 0 },
    { label: 'Client Interview Scheduled', status: 'CI_SCHEDULED', count: 0 },
    { label: 'Client Accepted', status: 'CI_ACCEPTED', count: 0 },
    { label: 'Client Rejected', status: 'CI_REJECTED', count: 0 },
    { label: 'Withdrawn', status: 'WITHDRAWN', count: 0 }
  ];

  readonly isLoading = false;
  readonly errorMessage = '';

  get hasSubmissions(): boolean {
    return this.submissionStatuses.some(item => item.count > 0);
  }

  onCreateRequirement(): void {
    // Connect to the requirements route when it is implemented.
  }

  onViewSubmissions(): void {
    // Connect to the submissions route when it is implemented.
  }
}

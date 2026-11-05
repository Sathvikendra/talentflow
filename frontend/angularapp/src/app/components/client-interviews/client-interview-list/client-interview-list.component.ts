import { Component, OnInit } from '@angular/core';
import { ClientInterview, ClientInterviewResult } from '../../../models/client-interview.model';
import { ClientInterviewService } from '../../../services/client-interview.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-client-interview-list',
  standalone: false,
  styleUrl: './client-interview-list.component.scss',
  templateUrl: './client-interview-list.component.html',
})
export class ClientInterviewList implements OnInit {
  interviews: ClientInterview[] = [];
  filteredInterviews: ClientInterview[] = [];

  searchTerm = '';
  selectedResult = 'ALL';

  isLoading = false;
  errorMessage = '';

  constructor(
    private readonly interviewService: ClientInterviewService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.loadInterviews();
  }

  loadInterviews(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.interviewService.getAllInterviews().subscribe({
      next: interviews => {
        this.interviews = interviews;
        this.applyFilters();
        this.isLoading = false;
      },
      error: error => {
        this.errorMessage =
          error?.message || 'Unable to load client interviews.';
        this.isLoading = false;
      }
    });
  }

  applyFilters(): void {
    const term = this.searchTerm.trim().toLowerCase();

    this.filteredInterviews = this.interviews.filter(interview => {
      const matchesSearch =
        !term ||
        interview.id.toString().includes(term) ||
        interview.submissionId.toString().includes(term) ||
        (interview.clientInterviewerName ?? '').toLowerCase().includes(term) ||
        (interview.clientInterviewerEmail ?? '').toLowerCase().includes(term);

      const matchesResult =
        this.selectedResult === 'ALL' ||
        (this.selectedResult === 'PENDING'
          ? interview.result === null
          : interview.result === this.selectedResult);

      return matchesSearch && matchesResult;
    });
  }

  openInterview(id: number): void {
    this.router.navigate(['/client-interviews', id]);
  }

  createInterview(): void {
    this.router.navigate(['/client-interviews/new']);
  }

  getResultLabel(result: ClientInterviewResult | null): string {
    return result ?? 'PENDING';
  }
}

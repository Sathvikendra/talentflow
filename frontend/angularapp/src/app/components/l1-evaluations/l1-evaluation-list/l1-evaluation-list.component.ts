import { Component, OnInit } from '@angular/core';
import { L1Evaluation } from '../../../models/l1-evaluation.model';
import { L1EvaluationService } from '../../../services/l1-evaluation.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-l1-evaluation-list',
  standalone: false,
  styleUrl: './l1-evaluation-list.component.scss',
  templateUrl: './l1-evaluation-list.component.html',
})
export class L1EvaluationList implements OnInit {
  evaluations: L1Evaluation[] = [];
  filteredEvaluations: L1Evaluation[] = [];

  searchTerm = '';
  selectedResult = 'ALL';

  isLoading = false;
  errorMessage = '';

  constructor(
    private readonly evaluationService: L1EvaluationService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.loadEvaluations();
  }

  loadEvaluations(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.evaluationService.getAllEvaluations().subscribe({
      next: evaluations => {
        this.evaluations = evaluations;
        this.applyFilters();
        this.isLoading = false;
      },
      error: error => {
        this.errorMessage = error?.message || 'Unable to load L1 evaluations.';
        this.isLoading = false;
      }
    });
  }

  applyFilters(): void {
    const term = this.searchTerm.trim().toLowerCase();

    this.filteredEvaluations = this.evaluations.filter(evaluation => {
      const matchesSearch =
        !term ||
        evaluation.id.toString().includes(term) ||
        evaluation.submissionId.toString().includes(term) ||
        evaluation.interviewerId.toString().includes(term);

      const matchesResult =
        this.selectedResult === 'ALL' ||
        (this.selectedResult === 'PENDING'
          ? evaluation.result === null
          : evaluation.result === this.selectedResult);

      return matchesSearch && matchesResult;
    });
  }

  onSearchChange(): void {
    this.applyFilters();
  }

  onResultFilterChange(): void {
    this.applyFilters();
  }

  openEvaluation(id: number): void {
    this.router.navigate(['/l1-evaluations', id]);
  }

  createEvaluation(): void {
    this.router.navigate(['/l1-evaluations/new']);
  }

  getResultLabel(result: string | null): string {
    return result ?? 'PENDING';
  }
}

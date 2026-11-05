import { Component, OnInit } from '@angular/core';
import { Requirement } from '../../../models/requirement.model';
import { RequirementService } from '../../../services/requirement.service';
import { RequirementFilterValues } from '../requirement-filters/requirement-filters.component';

@Component({
  selector: 'app-requirement-list',
  standalone: false,
  styleUrl: './requirement-list.component.scss',
  templateUrl: './requirement-list.component.html',
})
export class RequirementList implements OnInit {
  requirements: Requirement[] = [];
  filteredRequirements: Requirement[] = [];

  isLoading = false;
  errorMessage = '';

  constructor(
    private readonly requirementService: RequirementService
  ) {}

  ngOnInit(): void {
    this.loadRequirements();
  }

  loadRequirements(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.requirementService.getAllRequirements().subscribe({
      next: (data) => {
        this.requirements = data;
        this.filteredRequirements = data;
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = error.message ||
          'Unable to load requirements. Please try again.';
        this.isLoading = false;
      }
    });
  }

  retryLoading(): void {
    this.loadRequirements();
  }

  getClientName(requirement: Requirement): string {
    return requirement.client?.clientName || 'Unknown client';
  }

  getExperienceRange(requirement: Requirement): string {
    const min = requirement.minExperienceYears;
    const max = requirement.maxExperienceYears;

    if (min !== null && max !== null) {
      return `${min} - ${max} years`;
    }

    if (min !== null) {
      return `${min}+ years`;
    }

    if (max !== null) {
      return `Up to ${max} years`;
    }

    return 'Not specified';
  }

  applyFilters(filters: RequirementFilterValues): void {
    const search = filters.search.trim().toLowerCase();

    this.filteredRequirements = this.requirements.filter(requirement => {

      const matchesSearch =
        !search ||
        requirement.rrNumber.toLowerCase().includes(search) ||
        requirement.title.toLowerCase().includes(search) ||
        (requirement.location || '').toLowerCase().includes(search);

      const matchesStatus =
        !filters.status || requirement.status === filters.status;

      const matchesPriority =
        !filters.priority || requirement.priority === filters.priority;

      const matchesWorkMode =
        !filters.workMode || requirement.workMode === filters.workMode;

      return matchesSearch &&
        matchesStatus &&
        matchesPriority &&
        matchesWorkMode;
    });
  }
}

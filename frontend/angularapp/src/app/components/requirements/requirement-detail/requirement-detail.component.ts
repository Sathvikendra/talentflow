import { Component, OnInit } from '@angular/core';
import { Requirement } from '../../../models/requirement.model';
import { ActivatedRoute, Router } from '@angular/router';
import { RequirementService } from '../../../services/requirement.service';

@Component({
  selector: 'app-requirement-detail',
  standalone: false,
  styleUrl: './requirement-detail.component.scss',
  templateUrl: './requirement-detail.component.html',
})
export class RequirementDetail implements OnInit {
  requirement: Requirement | null = null;

  isLoading = false;
  errorMessage = '';

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly requirementService: RequirementService
  ) {}

  ngOnInit(): void {
    this.loadRequirement();
  }

  loadRequirement(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (!id || Number.isNaN(id)) {
      this.errorMessage = 'Invalid requirement ID.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.requirementService.getRequirementById(id).subscribe({
      next: (data) => {
        this.requirement = data;
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage =
          error.message || 'Unable to load requirement.';
        this.isLoading = false;
      }
    });
  }

  getClientName(): string {
    return this.requirement?.client?.clientName || 'Unknown client';
  }

  getCreatedBy(): string {
    const user = this.requirement?.createdBy;

    if (!user) {
      return 'Unknown user';
    }

    return `${user.firstName || ''} ${user.lastName || ''}`.trim()
      || user.username
      || 'Unknown user';
  }

  goBack(): void {
    this.router.navigate(['/requirements']);
  }
}

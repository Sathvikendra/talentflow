import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Client } from '../../../models/client.model';
import { RequirementService } from '../../../services/requirement.service';
import { ClientService } from '../../../services/client.service';
import { Router } from '@angular/router';
import { CreateRequirementRequest } from '../../../models/requirement.model';

@Component({
  selector: 'app-requirement-form',
  standalone: false,
  styleUrl: './requirement-form.component.scss',
  templateUrl: './requirement-form.component.html',
})
export class RequirementForm {
  @Input() currentUserId: number | null = null;

  requirementForm!: FormGroup;

  clients: Client[] = [];

  isLoadingClients = false;
  isSubmitting = false;

  errorMessage = '';
  successMessage = '';

  constructor(
    private readonly fb: FormBuilder,
    private readonly requirementService: RequirementService,
    private readonly clientService: ClientService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.initializeForm();
    this.loadClients();
  }

  initializeForm(): void {
    this.requirementForm = this.fb.group({

      rrNumber: ['', [
        Validators.required,
        Validators.maxLength(50)
      ]],

      clientId: [null, Validators.required],

      title: ['', [
        Validators.required,
        Validators.maxLength(200)
      ]],

      description: ['', Validators.required],

      employmentType: [null],

      location: [''],

      workMode: ['', Validators.required],

      onshoreOrOffshore: ['', Validators.required],

      minExperienceYears: [null, [
        Validators.min(0)
      ]],

      maxExperienceYears: [null, [
        Validators.min(0)
      ]],

      positionsCount: [1, [
        Validators.required,
        Validators.min(1)
      ]],

      priority: [null],

      targetFillDate: [null]

    });
  }

  loadClients(): void {
    this.isLoadingClients = true;

    this.clientService.getAllClients().subscribe({
      next: (data) => {
        this.clients = data.filter(client => client.isActive);
        this.isLoadingClients = false;
      },
      error: (error) => {
        this.errorMessage =
          error.message || 'Unable to load clients.';
        this.isLoadingClients = false;
      }
    });
  }

  submitForm(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (this.requirementForm.invalid) {
      this.requirementForm.markAllAsTouched();
      return;
    }

    if (this.currentUserId === null || this.currentUserId <= 0) {
      this.errorMessage =
        'Unable to identify the logged-in user. Please log in again.';
      return;
    }

    const formValue = this.requirementForm.getRawValue();

    const minExperience = formValue.minExperienceYears;
    const maxExperience = formValue.maxExperienceYears;

    if (
      minExperience !== null &&
      maxExperience !== null &&
      Number(minExperience) > Number(maxExperience)
    ) {
      this.errorMessage =
        'Minimum experience cannot exceed maximum experience.';
      return;
    }

    const request: CreateRequirementRequest = {
      rrNumber: formValue.rrNumber.trim(),
      clientId: Number(formValue.clientId),
      title: formValue.title.trim(),
      description: formValue.description.trim(),
      employmentType: formValue.employmentType || null,
      location: formValue.location?.trim() || null,
      workMode: formValue.workMode,
      onshoreOrOffshore: formValue.onshoreOrOffshore,
      minExperienceYears:
        minExperience === null || minExperience === ''
          ? null : Number(minExperience),
      maxExperienceYears:
        maxExperience === null || maxExperience === ''
          ? null : Number(maxExperience),
      positionsCount: Number(formValue.positionsCount),
      priority: formValue.priority || null,
      createdBy: this.currentUserId,
      targetFillDate: formValue.targetFillDate || null
    };

    this.isSubmitting = true;

    this.requirementService.createRequirement(request).subscribe({
      next: (createdRequirement) => {
        this.isSubmitting = false;

        this.router.navigate([
          '/requirements',
          createdRequirement.id
        ]);
      },
      error: (error) => {
        this.errorMessage =
          error.message || 'Unable to create requirement.';
        this.isSubmitting = false;
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/requirements']);
  }

  isInvalid(controlName: string): boolean {
    const control = this.requirementForm.get(controlName);

    return !!control &&
      control.invalid &&
      (control.dirty || control.touched);
  }
}

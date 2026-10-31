import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { CandidateService } from '../../../services/candidate.service';

@Component({
  selector: 'app-candidate-form',
  standalone: false,
  //imports: [FormsModule],
  templateUrl: './candidate-form.component.html',
  styleUrl: './candidate-form.component.scss'
})
export class CandidateFormComponent {

  candidate = {
    candidateReference: '',
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    totalExperienceYears: 0,
    currentTitle: '',
    currentCompany: '',
    currentLocation: '',
    preferredLocation: '',
    noticePeriodDays: 0,
    profileSummary: '',
    createdBy: 7
  };

  constructor(
    private candidateService: CandidateService
  ) {}

  saveCandidate() {

    this.candidateService
      .createCandidate(this.candidate)
      .subscribe({
        next: () => {
          alert('Candidate Created Successfully');
        },
        error: (error) => {
          console.error(error);
        }
      });
  }
}
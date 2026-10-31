import { Component, OnInit } from '@angular/core';
//import { CommonModule } from '@angular/common';

import { CandidateService } from '../../../services/candidate.service';
import { Candidate } from '../../../models/candidate.model';

@Component({
  selector: 'app-candidate-list',
  standalone: false,
 // imports: [CommonModule],
  templateUrl: './candidate-list.component.html',
  styleUrl: './candidate-list.component.scss'
})
export class CandidateListComponent implements OnInit {

  candidates: Candidate[] = [];

  constructor(
    private candidateService: CandidateService
  ) {}

  ngOnInit(): void {
    this.candidateService
      .getAllCandidates()
      .subscribe({
        next: (data) => {
          this.candidates = data;
        },
        error: (error) => {
          console.error(error);
        }
      });
  }
}
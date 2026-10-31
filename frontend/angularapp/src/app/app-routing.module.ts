import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// Authentication
import { LoginComponent } from './components/auth/login/login.component';

// Layout and shared
import { AppShell } from './components/layout/app-shell/app-shell.component';
import { NotFound } from './components/shared/not-found/not-found.component';

// Dashboard
import { DashboardPage } from './components/dashboard/dashboard-page/dashboard-page.component';

// Requirements
import { RequirementList } from './components/requirements/requirement-list/requirement-list.component';
import { RequirementForm } from './components/requirements/requirement-form/requirement-form.component';
import { RequirementDetail } from './components/requirements/requirement-detail/requirement-detail.component';

// Candidates and resumes
import { CandidateListComponent } from './components/candidates/candidate-list/candidate-list.component';
import { CandidateFormComponent } from './components/candidates/candidate-form/candidate-form.component';
import { ResumeUploadComponent } from './components/resumes/resume-upload/resume-upload.component';

// Submissions
import { SubmissionList } from './components/submissions/submission-list/submission-list.component';
import { SubmissionDetail } from './components/submissions/submission-detail/submission-detail.component';

// L1 Evaluations
import { L1EvaluationList } from './components/l1-evaluations/l1-evaluation-list/l1-evaluation-list.component';
import { L1EvaluationForm } from './components/l1-evaluations/l1-evaluation-form/l1-evaluation-form.component';
import { L1EvaluationDetail } from './components/l1-evaluations/l1-evaluation-detail/l1-evaluation-detail.component';

// Client Interviews
import { ClientInterviewList } from './components/client-interviews/client-interview-list/client-interview-list.component';
import { ClientInterviewForm } from './components/client-interviews/client-interview-form/client-interview-form.component';
import { ClientInterviewDetail } from './components/client-interviews/client-interview-detail/client-interview-detail.component';

const routes: Routes = [

  // Default route
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },

  // Public authentication route
  {
    path: 'login',
    component: LoginComponent,
    pathMatch: 'full'
  },

  // Main application layout
  {
    path: '',
    component: AppShell,
    children: [

      // Dashboard
      {
        path: 'home',
        component: DashboardPage,
        data: { breadcrumb: 'Dashboard' }
      },

      // Requirements
      {
        path: 'requirements',
        component: RequirementList,
        data: { breadcrumb: 'Requirements' }
      },
      {
        path: 'requirements/new',
        component: RequirementForm,
        data: { breadcrumb: 'New Requirement' }
      },
      {
        path: 'requirements/:id/edit',
        component: RequirementForm,
        data: { breadcrumb: 'Edit Requirement' }
      },
      {
        path: 'requirements/:id',
        component: RequirementDetail,
        data: { breadcrumb: 'Requirement Details' }
      },

      // Candidates
      {
        path: 'candidates',
        component: CandidateListComponent,
        data: { breadcrumb: 'Candidates' }
      },
      {
        path: 'candidates/new',
        component: CandidateFormComponent,
        data: { breadcrumb: 'Add Candidate' }
      },
      {
        path: 'candidates/:id/edit',
        component: CandidateFormComponent,
        data: { breadcrumb: 'Edit Candidate' }
      },

      // Resume Upload
      {
        path: 'resume-upload',
        component: ResumeUploadComponent,
        data: { breadcrumb: 'Resume Upload' }
      },

      // Submissions
      {
        path: 'submissions',
        component: SubmissionList,
        data: { breadcrumb: 'Submissions' }
      },
      {
        path: 'submissions/:id',
        component: SubmissionDetail,
        data: { breadcrumb: 'Submission Details' }
      },

      // L1 Evaluations
      {
        path: 'l1-evaluations',
        component: L1EvaluationList,
        data: { breadcrumb: 'L1 Evaluations' }
      },
      {
        path: 'l1-evaluations/new',
        component: L1EvaluationForm,
        data: { breadcrumb: 'New L1 Evaluation' }
      },
      {
        path: 'l1-evaluations/:id/edit',
        component: L1EvaluationForm,
        data: { breadcrumb: 'Edit L1 Evaluation' }
      },
      {
        path: 'l1-evaluations/:id',
        component: L1EvaluationDetail,
        data: { breadcrumb: 'Evaluation Details' }
      },

      // Client Interviews
      {
        path: 'client-interviews',
        component: ClientInterviewList,
        data: { breadcrumb: 'Client Interviews' }
      },
      {
        path: 'client-interviews/new',
        component: ClientInterviewForm,
        data: { breadcrumb: 'New Client Interview' }
      },
      {
        path: 'client-interviews/:id/edit',
        component: ClientInterviewForm,
        data: { breadcrumb: 'Edit Client Interview' }
      },
      {
        path: 'client-interviews/:id',
        component: ClientInterviewDetail,
        data: { breadcrumb: 'Interview Details' }
      },

      // Default authenticated page
      {
        path: '',
        redirectTo: 'home',
        pathMatch: 'full'
      }
    ]
  },

  // Unknown routes
  {
    path: '**',
    component: NotFound
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { App } from './app.component';

// Authentication
import { LoginComponent } from './components/auth/login/login.component';

// Layout
import { AppShell } from './components/layout/app-shell/app-shell.component';
import { Sidebar } from './components/layout/sidebar/sidebar.component';
import { Topbar } from './components/layout/topbar/topbar.component';
import { Breadcrumb } from './components/layout/breadcrumb/breadcrumb.component';

// Shared components
import { PageHeader } from './components/shared/page-header/page-header.component';
import { LoadingSpinner } from './components/shared/loading-spinner/loading-spinner.component';
import { EmptyState } from './components/shared/empty-state/empty-state.component';
import { StatusBadge } from './components/shared/status-badge/status-badge.component';
import { ErrorMessage } from './components/shared/error-message/error-message.component';
import { ConfirmDialog } from './components/shared/confirm-dialog/confirm-dialog.component';
import { NotFound } from './components/shared/not-found/not-found.component';

// Dashboard
import { DashboardPage } from './components/dashboard/dashboard-page/dashboard-page.component';
import { MetricCard } from './components/dashboard/metric-card/metric-card.component';
import { SubmissionSummary } from './components/dashboard/submission-summary/submission-summary.component';

// Requirements
import { RequirementList } from './components/requirements/requirement-list/requirement-list.component';
import { RequirementForm } from './components/requirements/requirement-form/requirement-form.component';
import { RequirementDetail } from './components/requirements/requirement-detail/requirement-detail.component';
import { RequirementFilters } from './components/requirements/requirement-filters/requirement-filters.component';
import { RequirementSkillEditor } from './components/requirements/requirement-skill-editor/requirement-skill-editor.component';

// Candidates and resumes
import { CandidateListComponent } from './components/candidates/candidate-list/candidate-list.component';
import { CandidateFormComponent } from './components/candidates/candidate-form/candidate-form.component';
import { ResumeUploadComponent } from './components/resumes/resume-upload/resume-upload.component';

// Submissions
import { SubmissionList } from './components/submissions/submission-list/submission-list.component';
import { SubmissionDetail } from './components/submissions/submission-detail/submission-detail.component';
import { SubmissionTimeline } from './components/submissions/submission-timeline/submission-timeline.component';
import { SubmissionTransitionDialog } from './components/submissions/submission-transition-dialog/submission-transition-dialog.component';

// L1 Evaluations
import { L1EvaluationList } from './components/l1-evaluations/l1-evaluation-list/l1-evaluation-list.component';
import { L1EvaluationDetail } from './components/l1-evaluations/l1-evaluation-detail/l1-evaluation-detail.component';
import { L1EvaluationForm } from './components/l1-evaluations/l1-evaluation-form/l1-evaluation-form.component';

// Client Interviews
import { ClientInterviewList } from './components/client-interviews/client-interview-list/client-interview-list.component';
import { ClientInterviewDetail } from './components/client-interviews/client-interview-detail/client-interview-detail.component';
import { ClientInterviewForm } from './components/client-interviews/client-interview-form/client-interview-form.component';

@NgModule({
  declarations: [
    App,

    // Authentication
    LoginComponent,

    // Layout
    AppShell,
    Sidebar,
    Topbar,
    Breadcrumb,

    // Shared
    PageHeader,
    LoadingSpinner,
    EmptyState,
    StatusBadge,
    ErrorMessage,
    ConfirmDialog,
    NotFound,

    // Dashboard
    DashboardPage,
    MetricCard,
    SubmissionSummary,

    // Requirements
    RequirementList,
    RequirementForm,
    RequirementDetail,
    RequirementFilters,
    RequirementSkillEditor,

    // Candidates and resumes
    CandidateListComponent,
    CandidateFormComponent,
    ResumeUploadComponent,

    // Submissions
    SubmissionList,
    SubmissionDetail,
    SubmissionTimeline,
    SubmissionTransitionDialog,

    // L1 Evaluations
    L1EvaluationList,
    L1EvaluationDetail,
    L1EvaluationForm,

    // Client Interviews
    ClientInterviewList,
    ClientInterviewDetail,
    ClientInterviewForm
  ],

  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],

  providers: [
    provideBrowserGlobalErrorListeners(),
    provideClientHydration()
  ],

  bootstrap: [App]
})
export class AppModule {}
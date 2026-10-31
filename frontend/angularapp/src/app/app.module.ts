import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { App } from './app.component';
import { Login } from './components/auth/login/login.component';
import { AppShell } from './components/layout/app-shell/app-shell.component';
import { Sidebar } from './components/layout/sidebar/sidebar.component';
import { Topbar } from './components/layout/topbar/topbar.component';
import { Breadcrumb } from './components/layout/breadcrumb/breadcrumb.component';
import { PageHeader } from './components/shared/page-header/page-header.component';
import { LoadingSpinner } from './components/shared/loading-spinner/loading-spinner.component';
import { EmptyState } from './components/shared/empty-state/empty-state.component';
import { StatusBadge } from './components/shared/status-badge/status-badge.component';
import { ErrorMessage } from './components/shared/error-message/error-message.component';
import { DashboardPage } from './components/dashboard/dashboard-page/dashboard-page.component';
import { MetricCard } from './components/dashboard/metric-card/metric-card.component';
import { SubmissionSummary } from './components/dashboard/submission-summary/submission-summary.component';
import { ConfirmDialog } from './components/shared/confirm-dialog/confirm-dialog.component';
import { RequirementList } from './components/requirements/requirement-list/requirement-list.component';
import { RequirementForm } from './components/requirements/requirement-form/requirement-form.component';
import { RequirementDetail } from './components/requirements/requirement-detail/requirement-detail.component';
import { RequirementFilters } from './components/requirements/requirement-filters/requirement-filters.component';
import { RequirementSkillEditor } from './components/requirements/requirement-skill-editor/requirement-skill-editor.component';
import { SubmissionList } from './components/submissions/submission-list/submission-list.component';
import { SubmissionDetail } from './components/submissions/submission-detail/submission-detail.component';
import { SubmissionTimeline } from './components/submissions/submission-timeline/submission-timeline.component';
import { SubmissionTransitionDialog } from './components/submissions/submission-transition-dialog/submission-transition-dialog.component';
import { L1EvaluationList } from './components/l1-evaluations/l1-evaluation-list/l1-evaluation-list.component';
import { L1EvaluationDetail } from './components/l1-evaluations/l1-evaluation-detail/l1-evaluation-detail.component';
import { L1EvaluationForm } from './components/l1-evaluations/l1-evaluation-form/l1-evaluation-form.component';
import { ClientInterviewList } from './components/client-interviews/client-interview-list/client-interview-list.component';
import { ClientInterviewDetail } from './components/client-interviews/client-interview-detail/client-interview-detail.component';
import { ClientInterviewForm } from './components/client-interviews/client-interview-form/client-interview-form.component';
import { NotFound } from './components/shared/not-found/not-found.component';

@NgModule({
  declarations: [
    App,
    Login,
    AppShell,
    Sidebar,
    Topbar,
    Breadcrumb,
    PageHeader,
    LoadingSpinner,
    EmptyState,
    StatusBadge,
    ErrorMessage,
    DashboardPage,
    MetricCard,
    SubmissionSummary,
    ConfirmDialog,
    RequirementList,
    RequirementForm,
    RequirementDetail,
    RequirementFilters,
    RequirementSkillEditor,
    SubmissionList,
    SubmissionDetail,
    SubmissionTimeline,
    SubmissionTransitionDialog,
    L1EvaluationList,
    L1EvaluationDetail,
    L1EvaluationForm,
    ClientInterviewList,
    ClientInterviewDetail,
    ClientInterviewForm,
    NotFound,
  ],
  imports: [BrowserModule, AppRoutingModule, HttpClientModule, ReactiveFormsModule, FormsModule],
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideClientHydration(),
    // , { provide: HTTP_INTERCEPTORS, useClass: Auth, multi: true }
  ],
import { LoginComponent } from './components/auth/login/login.component';
import { DashboardPageComponent } from './components/dashboard/dashboard-page/dashboard-page.component';

@NgModule({
  declarations: [App],
  imports: [
    BrowserModule,
    AppRoutingModule,
    LoginComponent,
    DashboardPageComponent],
  providers: [provideBrowserGlobalErrorListeners(), provideClientHydration()],
  bootstrap: [App],
})
export class AppModule {}

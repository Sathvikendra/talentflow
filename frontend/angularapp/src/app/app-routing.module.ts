import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Login } from './components/auth/login/login.component';
import { ErrorMessage } from './components/shared/error-message/error-message.component';
import { AppShell } from './components/layout/app-shell/app-shell.component';
import { NotFound } from './components/shared/not-found/not-found.component';
import { DashboardPage } from './components/dashboard/dashboard-page/dashboard-page.component';
import { L1EvaluationList } from './components/l1-evaluations/l1-evaluation-list/l1-evaluation-list.component';
import { L1EvaluationForm } from './components/l1-evaluations/l1-evaluation-form/l1-evaluation-form.component';
import { L1EvaluationDetail } from './components/l1-evaluations/l1-evaluation-detail/l1-evaluation-detail.component';

const routes: Routes = [
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  {path: 'login', component: Login},
  {
    path: '',
    component: AppShell,
    children: [
      {
        path: 'home',
        component: DashboardPage,
        data: { breadcrumb: 'Dashboard' }
      },
      // {
      //   path: 'l1-evaluations',
      //   component: L1EvaluationList,
      //   data: { breadcrumb: 'L1 Evaluations' }
      // },
      // {
      //   path: 'l1-evaluations/new',
      //   component: L1EvaluationForm,
      //   data: { breadcrumb: 'New L1 Evaluation' }
      // },
      // {
      //   path: 'l1-evaluations/:id/edit',
      //   component: L1EvaluationForm,
      //   data: { breadcrumb: 'Edit L1 Evaluation' }
      // },
      // {
      //   path: 'l1-evaluations/:id',
      //   component: L1EvaluationDetail,
      //   data: { breadcrumb: 'Evaluation Details' }
      // },
      { path: '', redirectTo: 'home', pathMatch: 'full' }
    ]
  },
  {path: '**', component: NotFound}
import { LoginComponent } from './components/auth/login/login.component';
import { DashboardPageComponent } from './components/dashboard/dashboard-page/dashboard-page.component';
import { CandidateListComponent } from './components/candidates/candidate-list/candidate-list.component';
import { CandidateFormComponent } from './components/candidates/candidate-form/candidate-form.component';
import { ResumeUploadComponent } from './components/resumes/resume-upload/resume-upload.component';
const routes: Routes = [
  { path: '', component: LoginComponent },
  {
    path: 'dashboard',
    component: DashboardPageComponent
  },{
  path: 'candidates',
  component: CandidateListComponent
  },
  {
  path: 'candidate-create',
  component: CandidateFormComponent
},{
  path: 'resume-upload',
  component: ResumeUploadComponent
}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
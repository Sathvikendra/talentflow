import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
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
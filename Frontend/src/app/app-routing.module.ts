import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LayoutComponent } from './Components/layout/layout.component';
import { LoginComponent } from './Components/login/login.component';
import { RegisterComponent } from './Components/register/register.component';
import { CardsComponent } from './Components/cards/cards.component';
import { CenterCardsComponent } from './Components/center-cards/center-cards.component';
import { PaperDashboardComponent } from './Components/paper-dashboard/paper-dashboard.component';
import { UploadPapersComponent } from './Components/upload-papers/upload-papers.component';
import { AdminPapersComponent } from './Components/admin-papers/admin-papers.component';
import { CoursesComponent } from './Components/courses/courses.component';
import { MyCoursesComponent } from './Components/my-courses/my-courses.component';
import { SubscriptionsComponent } from './Components/subscriptions/subscriptions.component';
import { AuthGuard } from './Services/auth/auth.guard';
import { AdminGuard } from './Services/auth/admin.guard';

const routes: Routes = [
  { path: 'login',    component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: '',         component: LayoutComponent,        canActivate: [AuthGuard] },
  { path: 'cards',    component: CardsComponent,          canActivate: [AuthGuard] },
  { path: 'center-cards', component: CenterCardsComponent, canActivate: [AuthGuard] },
  { path: 'papers_dashboard', component: PaperDashboardComponent, canActivate: [AuthGuard] },
  { path: 'upload_papers',    component: UploadPapersComponent,   canActivate: [AdminGuard] },
  { path: 'subscriptions',    component: SubscriptionsComponent,  canActivate: [AuthGuard] },
  { path: 'courses',          component: CoursesComponent,        canActivate: [AuthGuard] },
  { path: 'my-courses',       component: MyCoursesComponent,      canActivate: [AuthGuard] },
  { path: 'admin/papers',     component: AdminPapersComponent,    canActivate: [AdminGuard] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    anchorScrolling: 'enabled',
    scrollPositionRestoration: 'enabled'
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }

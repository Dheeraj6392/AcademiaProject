import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HeaderComponent } from './Components/header/header.component';
import { FooterComponent } from './Components/footer/footer.component';
import { HomeComponent } from './Components/home/home.component';
import { CardsComponent } from './Components/cards/cards.component';
import { PapersComponent } from './Components/papers/papers.component';
import { CenterCardsComponent } from './Components/center-cards/center-cards.component';
import { NotesDashboardComponent } from './Components/notes-dashboard/notes-dashboard.component';
import { PaperDashboardComponent } from './Components/paper-dashboard/paper-dashboard.component';
import { LayoutComponent } from './Components/layout/layout.component';
import { UploadPapersComponent } from './Components/upload-papers/upload-papers.component';
import { TestComponent } from './Components/test/test.component';
import { CoursesComponent } from './Components/courses/courses.component';
import { LoginComponent } from './Components/login/login.component';
import { AdminGuard } from './Services/auth/admin.guard';
import { AuthGuard } from './Services/auth/auth.guard';
import { RegisterComponent } from './Components/register/register.component';
import { MyCoursesComponent } from './Components/my-courses/my-courses.component';
import { AdminPapersComponent } from './Components/admin-papers/admin-papers.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'register',
    component: RegisterComponent,
  },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'layout',
    component: LayoutComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'cards',
    component: CardsComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'center-cards',
    component: CenterCardsComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'papers',
    component: PapersComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'upload_papers',
    component: UploadPapersComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'admin/papers',
    component: AdminPapersComponent,
    canActivate: [AdminGuard]
  },
  {
    path: 'notes_dashboard',
    component: NotesDashboardComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'papers_dashboard',
    component: PaperDashboardComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'test',
    component: TestComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'courses',
    component: CoursesComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'my-courses',
    component: MyCoursesComponent,
    canActivate: [AuthGuard]
  },
];
@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      anchorScrolling: 'enabled',
      scrollPositionRestoration: 'enabled'
    })

  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }

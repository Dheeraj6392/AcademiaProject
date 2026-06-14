import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { DatePipe } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthInterceptor } from './Services/auth/auth.interceptor';
import { ToastComponent } from './Components/toast/toast.component';

import { HeaderComponent } from './Components/header/header.component';
import { FooterComponent } from './Components/footer/footer.component';
import { LayoutComponent } from './Components/layout/layout.component';
import { HomeComponent } from './Components/home/home.component';
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

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    LayoutComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    CardsComponent,
    CenterCardsComponent,
    PaperDashboardComponent,
    UploadPapersComponent,
    AdminPapersComponent,
    CoursesComponent,
    MyCoursesComponent,
    SubscriptionsComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    ToastComponent,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    DatePipe,
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  bootstrap: [AppComponent],
})
export class AppModule { }

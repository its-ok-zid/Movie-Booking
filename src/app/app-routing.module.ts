import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard, AdminGuard } from './core/guards/auth.guard';
import { NavbarComponent } from './shared/components/navbar/navbar.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { MoviesComponent } from './movies/movies.component';
import { BookTicketPageComponent } from './movies/book-ticket-page.component';
import { AdminDashboardComponent } from './admin/admin-dashboard.component';
import { ForgotPasswordComponent } from './auth/forgot-password/forgot-password.component';
import { LandingPageComponent } from './auth/landing-page/landing-page.component';

const routes: Routes = [
    { path: '', redirectTo: '/landing', pathMatch: 'full' }, // Default redirect
    { path: 'landing', component: LandingPageComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'forgot-password', component: ForgotPasswordComponent },
    { path: 'movies', component: MoviesComponent, canActivate: [AuthGuard] },
    { path: 'book/:movieName/:theatreName', component: BookTicketPageComponent, canActivate: [AuthGuard] },
    { path: 'admin', component: AdminDashboardComponent, canActivate: [AdminGuard] },
    { path: '**', redirectTo: '/landing' }  // Fallback for undefined URLs
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

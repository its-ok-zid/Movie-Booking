import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  constructor(public authService: AuthService, private router: Router) {}

isUser(): boolean {
    return this.authService.hasRole('ROLE_USER');
  }

  isAdmin(): boolean {
    return this.authService.hasRole('ROLE_ADMIN');
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

}

import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../core/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  username = '';
  password = '';
  confirmPassword = '';
  error = '';

  constructor(private authService: AuthService, private router: Router) {}

  register() {
    if (this.password !== this.confirmPassword) {
      this.error = 'Passwords do not match';
      return;
    }
    this.authService.register(this.username, this.password).subscribe({
      next: () => this.router.navigate(['/login']),
      error: (err: any) => this.error = 'Registration failed. Try a different username.'
    });
  }
  goToLogin() {
    this.router.navigate(['/login']);
  }
}

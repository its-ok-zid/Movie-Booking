
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService, LoginRequest } from '../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  errorMsg: string = '';
  successMsg: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      loginId: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {}

  onSubmit(): void {
    if (this.loginForm.valid) {
      const data: LoginRequest = this.loginForm.value;
      this.authService.login(data).subscribe({
        next: (res) => {
          // Expecting { role: 'USER'|'ADMIN', message: 'Login successful' }
          this.successMsg = res.message || 'Login successful!';
          this.errorMsg = '';
          localStorage.setItem('token', 'dummy-token');
          localStorage.setItem('role', res.role);
          alert('Login successful! Redirecting...');
          setTimeout(() => {
            if (res.role === 'ADMIN') {
              this.router.navigate(['/admin']);
            } else {
              this.router.navigate(['/movies']);
            }
          }, 1000);
        },
        error: (err) => {
          this.successMsg = '';
          this.errorMsg = err.error?.message || 'Login failed';
        }
      });
    }
  }

  goToRegister(): void {
    this.router.navigate(['/register']);
  }

  goToForgotPassword(): void {
    this.router.navigate(['/forgot-password']);
  }
}

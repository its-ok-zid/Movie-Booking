import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';

export interface ResetPasswordRequest {
  loginId: string;
  newPassword: string;
  confirmPassword: string;
}

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent {
  resetForm: FormGroup;
  errorMsg: string = '';
  successMsg: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.resetForm = this.fb.group({
      loginId: ['', Validators.required],
      newPassword: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.resetForm.valid) {
      const data: ResetPasswordRequest = this.resetForm.value;
      this.authService.resetPassword(data).subscribe({
        next: (res: string) => {
          this.successMsg = res;
          this.errorMsg = '';
        },
        error: (err) => {
          this.successMsg = '';
          this.errorMsg = err.error?.message || 'Password reset failed';
        }
      });
    }
  }
}

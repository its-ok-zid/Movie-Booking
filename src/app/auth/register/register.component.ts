import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  error: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      loginId: ['', Validators.required],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
      contactNumber: ['', Validators.required],
      role: ['USER', Validators.required]
    });
  }

  ngOnInit(): void {}

  onSubmit(): void {
    if (this.registerForm.valid) {
      // Remove id if present in the payload
      const { ...data } = this.registerForm.value;
      this.authService.register(data).subscribe({
        next: () => {
          this.router.navigate(['/login']);
        },
        error: (err) => {
          this.error = err.error?.message || 'Registration failed';
        }
      });
    }
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }

  fields = [
  { id: 'firstName', label: 'First Name', control: 'firstName', type: 'text', placeholder: 'Enter first name', error: 'First name is required' },
  { id: 'lastName', label: 'Last Name', control: 'lastName', type: 'text', placeholder: 'Enter last name', error: 'Last name is required' },
  { id: 'email', label: 'Email', control: 'email', type: 'email', placeholder: 'Enter email', error: 'Valid email is required' },
  { id: 'loginId', label: 'Login ID', control: 'loginId', type: 'text', placeholder: 'Enter login ID', error: 'Login ID is required' },
  { id: 'password', label: 'Password', control: 'password', type: 'password', placeholder: 'Enter password', error: 'Password is required' },
  { id: 'confirmPassword', label: 'Confirm Password', control: 'confirmPassword', type: 'password', placeholder: 'Confirm password', error: 'Confirm password is required' },
  { id: 'contactNumber', label: 'Contact Number', control: 'contactNumber', type: 'text', placeholder: 'Enter contact number', error: 'Contact number is required' },
  { id: 'role', label: 'Role', control: 'role', type: 'select' }
];

}

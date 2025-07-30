import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface LoginRequest {
  loginId: string;
  password: string;
}

export interface RegisterRequest {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  loginId: string;
  password: string;
  confirmPassword: string;
  contactNumber: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private http: HttpClient) {}

  login(data: LoginRequest): Observable<any> {
    return this.http.post('/api/v1.0/moviebooking/login', data); // Expect JSON response
  }

  register(data: RegisterRequest): Observable<any> {
    return this.http.post('/api/v1.0/moviebooking/register', data);
  }

  resetPassword(data: { loginId: string; newPassword: string; confirmPassword: string }): Observable<any> {
    return this.http.post('/api/v1.0/moviebooking/reset-password', data, { responseType: 'text' });
  }

  logout(): void {
    localStorage.removeItem('role');
    localStorage.removeItem('token');
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }

  hasRole(role: string): boolean {
    return localStorage.getItem('role') === role;
  }
}

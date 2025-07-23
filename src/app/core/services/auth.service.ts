import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private loggedIn: boolean = false;
  private userRole: string | null = null;

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    return this.http.post('/api/auth/login', { username, password });
  }

  register(username: string, password: string): Observable<any> {
    return this.http.post('/api/auth/register', { username, password });
  }

  logout(): void {
    this.loggedIn = false;
    this.userRole = null;
    localStorage.removeItem('userRole');
  }

  isAuthenticated(): boolean {
    return this.loggedIn;
  }

  hasRole(role: string): boolean {
    return this.userRole === role;
  }

  // Optional: to restore session on reload
  restoreSession(): void {
    const role = localStorage.getItem('userRole');
    if (role) {
      this.loggedIn = true;
      this.userRole = role;
    }
  }

  getCurrentRole(): string | null {
    return this.userRole;
  }
}

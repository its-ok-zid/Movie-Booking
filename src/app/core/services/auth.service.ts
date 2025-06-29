import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private loggedIn: boolean = false;
  private userRole: string | null = null;

  constructor() {}

  login(role: string): void {
    this.loggedIn = true;
    this.userRole = role;
    localStorage.setItem('userRole', role); // Optional: persist role across refresh
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

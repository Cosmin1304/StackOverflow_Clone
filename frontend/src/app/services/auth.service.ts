import { Injectable, inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { User } from '../models/user.model';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // Implicit, considerăm că utilizatorul NU este logat (false)
  private http = inject(HttpClient);
  private platformId = inject(PLATFORM_ID);
  private apiUrl = 'http://localhost:8080/api/auth';
  private loggedIn = new BehaviorSubject<boolean>(this.hasToken());
  // Aceasta este variabila pe care o va "asculta" HTML-ul (Navbar-ul)

  isLoggedIn$ = this.loggedIn.asObservable();
  private currentUser: User | null = this.getUserFromStorage();


  private hasToken(): boolean {
    if (isPlatformBrowser(this.platformId)) {
      return !!localStorage.getItem('user');
    }
    return false;
  }


  private getUserFromStorage(): User | null {
    if (isPlatformBrowser(this.platformId)) {
      const savedUser = localStorage.getItem('user');
      return savedUser ? JSON.parse(savedUser) : null;
    }
    return null;
  }


  login(credentials: { username: string; password: string }): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/login`, credentials).pipe(
      tap((user: User) => {
        this.currentUser = user;
        localStorage.setItem('user', JSON.stringify(user));
        this.loggedIn.next(true);
      })
    );
  }

  getCurrentUser(): User | null {
    return this.currentUser;
  }


  logout() {
    this.currentUser = null;
    localStorage.removeItem('user');
    this.loggedIn.next(false);
  }

  // O funcție care returnează valoarea (true/false) pentru Route Guard
  isAuthenticated(): boolean {
    return this.loggedIn.value;
  }
}

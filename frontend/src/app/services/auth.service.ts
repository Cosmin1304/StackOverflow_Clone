import { Injectable, inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';
import { UserResponseDTO, LoginRequestDTO } from '../models/models';

export interface AuthResponseDTO {
  token: string;
  user: UserResponseDTO;
}


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private platformId = inject(PLATFORM_ID);
  private apiUrl = 'http://localhost:8080/api/auth';

  private loggedIn = new BehaviorSubject<boolean>(this.hasToken());
  isLoggedIn$ = this.loggedIn.asObservable();

  private currentUser: UserResponseDTO | null = this.getUserFromStorage();


  private hasToken(): boolean {
    if (isPlatformBrowser(this.platformId)) {
      return !!localStorage.getItem('user');
    }
    return false;
  }


  private getUserFromStorage(): UserResponseDTO | null {
    if (isPlatformBrowser(this.platformId)) {
      const savedUser = localStorage.getItem('user');
      return savedUser ? JSON.parse(savedUser) : null;
    }
    return null;
  }


  login(credentials: LoginRequestDTO): Observable<AuthResponseDTO> {
    return this.http.post<AuthResponseDTO>(`${this.apiUrl}/login`, credentials).pipe(
      tap((response: AuthResponseDTO) => {

        this.currentUser = response.user;

        if (isPlatformBrowser(this.platformId)) {
          localStorage.setItem('token', response.token);
          localStorage.setItem('user', JSON.stringify(response.user));
        }

        this.loggedIn.next(true);
      })
    );
  }

  getCurrentUser(): UserResponseDTO | null {
    return this.currentUser;
  }

  isModerator(): boolean {
    return this.currentUser?.roles?.includes('MODERATOR') ?? false;
  }

  logout() {
    this.currentUser = null;
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('user');
      localStorage.removeItem('token');
      window.location.reload();
    }
    this.loggedIn.next(false);
  }

  isAuthenticated(): boolean {
    return this.loggedIn.value;
  }
}

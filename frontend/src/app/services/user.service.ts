import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http = inject(HttpClient);

  //URL-ul backend-ului Spring Boot
  private apiUrl = 'http://localhost:8080/api/users';

  //pastram utilizatorul curent logat intr-un BehaviorSubject
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  register(userRequest: any): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/register`, userRequest);
  }

  //deoarece nu avem autentificare cu JWT, simulam login-ul aducand datele userului
  login(username: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${username}`).pipe(
      tap(user => {
        this.currentUserSubject.next(user); //salvam utilizatorul in stare
      })
    );
  }

  updateUser(id: number, updateData: any): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/${id}`, updateData).pipe(
      tap(updatedUser => this.currentUserSubject.next(updatedUser)) //actualizam starea
    );
  }

  logout() {
    this.currentUserSubject.next(null);
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }
}

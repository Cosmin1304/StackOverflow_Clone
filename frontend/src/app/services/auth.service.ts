import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // Implicit, considerăm că utilizatorul NU este logat (false)
  private loggedIn = new BehaviorSubject<boolean>(false);

  // Aceasta este variabila pe care o va "asculta" HTML-ul (Navbar-ul)
  isLoggedIn$ = this.loggedIn.asObservable();

  // Funcție apelată când utilizatorul introduce corect datele
  login() {
    this.loggedIn.next(true);
  }

  // Funcție apelată la deconectare
  logout() {
    this.loggedIn.next(false);
  }

  // O funcție simplă care returnează valoarea (true/false) pentru Route Guard
  isAuthenticated(): boolean {
    return this.loggedIn.value;
  }
}

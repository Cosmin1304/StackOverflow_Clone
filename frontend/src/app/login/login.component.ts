import { Component, inject } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import { FormsModule } from '@angular/forms';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    RouterLink, FormsModule
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent {
  private router = inject(Router);
  private userService = inject(UserService);

  loginData = {
    username: '',
    password: ''
  };

  onLogin() {
    this.userService.login(this.loginData.username).subscribe({
      next: (user) => {
        alert('Te-ai logat cu succes!');
        this.router.navigate(['/profile']); // <-- AICI schimbăm destinația către profil
      },
      error: (err) => {
        alert('Eroare: Utilizatorul nu există sau backend-ul este oprit.');
        console.error(err);
      }
    });
  }
}

import { Component, inject } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '../services/auth.service';
import {FormsModule} from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    RouterLink,
    FormsModule,
    CommonModule
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  private router = inject(Router);
  private authService = inject(AuthService);

  credentials = { username: '', password: '' };

  onLogin() {
    this.authService.login(this.credentials).subscribe({
      next: (user) => {
        console.log('Logat cu succes:', user);
        this.router.navigate(['/']);
      },
      error: (err) => {
        if (err?.status === 403) {
          alert('Contul tău a fost banat. Contactează un moderator.');
        } else {
          alert('Eroare la autentificare! Verifica user-ul si parola.');
        }
        console.error(err);
      }
    });
  }
}

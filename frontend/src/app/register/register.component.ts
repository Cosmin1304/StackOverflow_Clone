import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterLink, FormsModule], // Avem nevoie de RouterLink pentru linkul de întoarcere la Login
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  private router = inject(Router);
  private userService = inject(UserService);

  registerData = {
    userName: '',
    userEmail: '',
    userPassword: '',
  };

  onRegister() {
    // La Assignment 3, aici vom trimite datele către backend-ul de Spring Boot.
    // Acum doar simulăm înregistrarea și redirecționăm la pagina principală.

    this.userService.register(this.registerData).subscribe({
      next: (user) => {
        alert('Cont creat cu succes!');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Eroare la înregistrare:', err);
        alert('Eroare: ' + (err.error?.message || 'Date invalide'));
      }
    });
  }

   // this.router.navigate(['/']);
 // }
}

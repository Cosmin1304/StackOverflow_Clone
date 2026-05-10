import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterLink], // Avem nevoie de RouterLink pentru linkul de întoarcere la Login
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  private router = inject(Router);

  onRegister() {
    // La Assignment 3, aici vom trimite datele către backend-ul de Spring Boot.
    // Acum doar simulăm înregistrarea și redirecționăm la pagina principală.
    this.router.navigate(['/']);
  }
}

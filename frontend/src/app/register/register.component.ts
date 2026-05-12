import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterLink, FormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  private router = inject(Router);

  user = {
    username: '',
    email: '',
    password: ''
  };

  onRegister(isFormValid: boolean | null) {
    if (isFormValid) {
      console.log('Datele pregătite pentru Java:', this.user);
      this.router.navigate(['/']);
    } else {
      alert('Te rugăm să corectezi erorile din formular!');
    }
  }
}

import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserService } from '../services/user.service';


@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterLink, FormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  private router = inject(Router);
  private userService = inject(UserService);

  user = {
    username: '',
    email: '',
    password: '',
    phoneNumber: ''
  };

  onRegister(isFormValid: boolean | null) {
    if (isFormValid) {
      this.userService.registerUser(this.user).subscribe({
        next: (createdUser) => {
          alert('Cont creat cu succes! Te poți loga acum.');
          this.router.navigate(['/login']);
        },
        error: (err) => {
          console.error(err);
          alert('A apărut o eroare la crearea contului. Verifică log-urile.');
        }
      });
    } else {
      alert('Te rugăm să corectezi erorile din formular!');
    }
  }
}

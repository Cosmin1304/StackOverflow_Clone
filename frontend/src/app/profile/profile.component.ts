import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../services/user.service';
import { AuthService } from '../services/auth.service'; // Injectăm AuthService
import { User } from '../models/user.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  private userService = inject(UserService);
  private authService = inject(AuthService); // Injectăm AuthService
  private router = inject(Router);

  currentUser: User | null = null;

  updateData = {
    userName: '',
    userEmail: '',
    userPassword: ''
  };

  ngOnInit() {
    // Preluăm utilizatorul din sursa unică de adevăr (AuthService)
    this.currentUser = this.authService.getCurrentUser();

    if (!this.currentUser) {
      this.router.navigate(['/login']);
    } else {
      this.updateData.userName = this.currentUser.username;
      this.updateData.userEmail = this.currentUser.email;
    }
  }

  onUpdate() {
    if (this.currentUser) {
      this.userService.updateUser(this.currentUser.id, this.updateData).subscribe({
        next: (updatedUser: User) => {
          alert('Profil actualizat cu succes!');
          this.currentUser = updatedUser;
          // Opțional: Actualizează și datele din localStorage prin AuthService dacă e nevoie
        },
        error: (err: any) => {
          alert('Eroare la actualizare!');
        }
      });
    }
  }

  onLogout() {
    this.authService.logout(); // Folosim metoda de logout din AuthService
    this.router.navigate(['/login']);
  }
}

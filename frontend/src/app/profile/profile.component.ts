import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../services/user.service';
import { AuthService } from '../services/auth.service';
import { UserResponseDTO, UserRequestDTO } from '../models/models';
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

  currentUser: UserResponseDTO | null = null;

  updateData: UserRequestDTO = {
    username: '',
    email: '',
    password: ''
  };

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();

    if (!this.currentUser) {
      this.router.navigate(['/login']);
    } else {
      this.updateData.username = this.currentUser.username;
      this.updateData.email = this.currentUser.email;
    }
  }

  onUpdate() {
    if (this.currentUser) {
      // Trimitem datele către backend
      this.userService.updateUser(this.currentUser.id, this.updateData).subscribe({
        next: (updatedUser: UserResponseDTO) => {
          alert('Profil actualizat cu succes!');
          this.currentUser = updatedUser;
        },
        error: (err: any) => {
          alert('Eroare la actualizare!');
          console.error(err);
        }
      });
    }
  }

  onLogout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

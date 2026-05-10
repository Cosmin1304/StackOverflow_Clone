import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../services/user.service';
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
  private router = inject(Router);

  currentUser: User | null = null;

  //date pentru update (doar cele care se pot modifica)
  updateData = {
    userName: '',
    userEmail: '',
    userPassword: ''
  };

  ngOnInit() {
    this.currentUser = this.userService.getCurrentUser();
    if (!this.currentUser) {
      this.router.navigate(['/login']);//protejam ruta
    } else {
      this.updateData.userName = this.currentUser.username;
      this.updateData.userEmail = this.currentUser.email;
    }
  }

  onUpdate() {
    if (this.currentUser) {
      this.userService.updateUser(this.currentUser.id, this.updateData).subscribe({
        next: (updatedUser) => {
          alert('Profil actualizat cu succes!');
          this.currentUser = updatedUser;
        },
        error: (err) => {
          alert('Eroare la actualizare (Verifică restricțiile Regex de pe server!)');
        }
      });
    }
  }

  onLogout() {
    this.userService.logout();
    this.router.navigate(['/login']);
  }
}

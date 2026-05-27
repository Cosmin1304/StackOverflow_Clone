import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../services/user.service';
import { AuthService } from '../services/auth.service';
import { UserResponseDTO } from '../models/models';

@Component({
  selector: 'app-moderator-panel',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './moderator-panel.component.html',
  styleUrl: './moderator-panel.component.scss' // <--- Legătura către noul fișier
})
export class ModeratorPanelComponent implements OnInit {
  private userService = inject(UserService);
  private authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  users: UserResponseDTO[] = [];
  errorMessage = '';
  loading = true;
  pendingId: number | null = null;

  ngOnInit() {
    this.loadUsers();
  }

  private loadUsers() {
    console.log('Trimit cererea către backend...');
    this.loading = true;

    this.userService.getAllUsers().subscribe({
      next: (data) => {
        console.log('Răspuns primit de la server:', data);
        this.users = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Eroare prinsă în Angular:', err);
        this.errorMessage = 'Nu s-a putut încărca lista. Verifică consola (F12).';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  isSelf(user: UserResponseDTO): boolean {
    return this.authService.getCurrentUser()?.id === user.id;
  }

  toggleBan(user: UserResponseDTO) {
    const newStatus = !user.isBanned;
    const action = newStatus ? 'banezi' : 'debanezi';
    if (!confirm(`Sigur vrei să ${action} utilizatorul "${user.username}"?`)) {
      return;
    }

    this.pendingId = user.id;
    this.userService.setBanStatus(user.id, newStatus).subscribe({
      next: (updated) => {
        this.users = this.users.map(u => (u.id === updated.id ? updated : u));
        this.pendingId = null;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Eroare la schimbarea stării de ban:', err);
        alert('Nu s-a putut schimba starea utilizatorului.');
        this.pendingId = null;
        this.cdr.detectChanges();
      }
    });
  }
}

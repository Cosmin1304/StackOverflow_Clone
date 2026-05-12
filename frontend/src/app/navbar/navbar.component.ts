import { Component, inject } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SearchService } from '../services/search.service';
import { AuthService } from '../services/auth.service';
import { CommonModule, AsyncPipe } from '@angular/common';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, FormsModule, CommonModule, AsyncPipe],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  private searchService = inject(SearchService);
  public authService = inject(AuthService);
  public userService = inject(UserService);
  private router = inject(Router);

  // Variabila care se leagă direct de input-ul din HTML
  searchTerm: string = '';
  // Se apelează de fiecare dată când scrii o literă nouă
  onSearchChange() {
    this.searchService.changeSearchTerm(this.searchTerm);
  }

  onLogout() {
    this.authService.logout(); // Schimbă starea în aplicație
    this.router.navigate(['/login']); // Te trimite la pagina de Login
  }
}

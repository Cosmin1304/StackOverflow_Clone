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

  searchTerm: string = '';

  onSearchChange() {
    this.searchService.changeSearchTerm(this.searchTerm);
  }

  onLogout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  goToMyQuestions() {
    const u = this.authService.getCurrentUser();
    if (u) {
      this.router.navigate(['/'], { queryParams: { author: u.username } });
    }
  }
}

import {Component, inject} from '@angular/core';
import { RouterLink } from '@angular/router';
import {FormsModule} from '@angular/forms';
import {SearchService} from '../services/search.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})

export class NavbarComponent {
  private searchService = inject(SearchService);
  // Variabila care se leagă direct de input-ul din HTML
  searchTerm: string = '';
  // Se apelează de fiecare dată când scrii o literă nouă
  onSearchChange() {
    this.searchService.changeSearchTerm(this.searchTerm);
  }
}

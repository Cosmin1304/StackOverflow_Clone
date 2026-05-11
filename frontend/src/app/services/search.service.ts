import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  // BehaviorSubject ține minte ultima valoare. Începem cu un text gol: ''
  private searchTermSource = new BehaviorSubject<string>('');

  // Aceasta este variabila la care se va "abona" lista de întrebări
  currentSearchTerm = this.searchTermSource.asObservable();

  // Funcție folosită de Navbar pentru a actualiza cuvântul căutat
  changeSearchTerm(term: string) {
    this.searchTermSource.next(term);
  }
}

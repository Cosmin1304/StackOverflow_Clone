import {Component, inject, OnInit, Input} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Question } from '../models/question.model';
import { mockQuestions } from '../models/mock-data';
import { SearchService } from '../services/search.service';
import { map, combineLatest, startWith, Observable } from 'rxjs';

@Component({
  selector: 'app-question-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './question-list.component.html',
  styleUrls: ['./question-list.component.scss']
})
export class QuestionListComponent implements OnInit{
  @Input()allQuestions: Question[] = mockQuestions;//lista completa originala
  filteredQuestions$!: Observable<Question[]>;

  private router = inject(Router);
  private searchService = inject(SearchService);

  ngOnInit() {
    // Combinăm lista de întrebări cu termenul de căutare
    this.filteredQuestions$ = this.searchService.currentSearchTerm.pipe(
      startWith(''), // Ne asigurăm că avem o valoare inițială
      map(term => this.getFilteredList(term))
    );
  }

  // Extragem logica de filtrare într-o funcție pură care returnează un array
  private getFilteredList(term: string): Question[] {
    if (!term) return [...this.allQuestions];
    const lowerCaseTerm = term.toLowerCase();
    return this.allQuestions.filter(q =>
      q.title.toLowerCase().includes(lowerCaseTerm) ||
      q.text.toLowerCase().includes(lowerCaseTerm)
    );
  }

  goToQuestion(id: number) {
    this.router.navigate(['/question', id]);
  }

}

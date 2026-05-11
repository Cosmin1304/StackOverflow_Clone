import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Question } from '../models/question.model';
import { mockQuestions } from '../models/mock-data';
import { SearchService } from '../services/search.service';

@Component({
  selector: 'app-question-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './question-list.component.html',
  styleUrls: ['./question-list.component.scss']
})
export class QuestionListComponent implements OnInit{
  allQuestions: Question[] = mockQuestions;//lista completa originala
  filteredQuestions: Question[] = [];

  private router = inject(Router);
  private searchService = inject(SearchService);

  ngOnInit() {
    //ne abonam la schimbarile din bara de cautare
    this.searchService.currentSearchTerm.subscribe(term => {
      this.filterQuestions(term);
    });
  }

  filterQuestions(term: string) {
    if (!term) {
      this.filteredQuestions = [...this.allQuestions];
    } else {
      const lowerCaseTerm = term.toLowerCase();
      //filtram după titlu sau dupa textul intrebari
      this.filteredQuestions = this.allQuestions.filter(q =>
        q.title.toLowerCase().includes(lowerCaseTerm) ||
        q.text.toLowerCase().includes(lowerCaseTerm)
      );
    }
  }

  goToQuestion(id: number) {
    this.router.navigate(['/question', id]);
  }
}

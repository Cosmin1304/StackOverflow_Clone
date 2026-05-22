import {Component, inject, OnInit, Input} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { SearchService } from '../services/search.service';
import { QuestionService } from '../services/question.service';
import { TopicResponseDTO } from '../models/models';
import { map, combineLatest, startWith, Observable } from 'rxjs';

@Component({
  selector: 'app-question-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './question-list.component.html',
  styleUrls: ['./question-list.component.scss']
})
export class QuestionListComponent implements OnInit{
  filteredQuestions$!: Observable<TopicResponseDTO[]>;

  private router = inject(Router);
  private searchService = inject(SearchService);
  private questionService = inject(QuestionService);

  ngOnInit() {
    this.filteredQuestions$ = combineLatest([
      this.questionService.getQuestions(),
      this.searchService.currentSearchTerm.pipe(startWith(''))
    ]).pipe(
      map(([questions, term]) => this.getFilteredList(questions, term))
    );
  }

  // Extragem logica de filtrare într-o funcție pură care returnează un array
  private getFilteredList(questions: TopicResponseDTO[], term: string): TopicResponseDTO[] {
    if (!term) return questions;
    const lowerCaseTerm = term.toLowerCase();

    return questions.filter(q =>
      q.title.toLowerCase().includes(lowerCaseTerm) ||
      q.text.toLowerCase().includes(lowerCaseTerm)
    );
  }

  goToQuestion(id: number) {
    this.router.navigate(['/question', id]);
  }

}

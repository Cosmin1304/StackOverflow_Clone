import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { SearchService } from '../services/search.service';
import { QuestionService } from '../services/question.service';
import { TopicResponseDTO } from '../models/models';
import { map, combineLatest, startWith, Observable, switchMap } from 'rxjs';

@Component({
  selector: 'app-question-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './question-list.component.html',
  styleUrls: ['./question-list.component.scss']
})
export class QuestionListComponent implements OnInit {
  filteredQuestions$!: Observable<TopicResponseDTO[]>;

  activeTag: string | null = null;
  activeAuthor: string | null = null;

  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private searchService = inject(SearchService);
  private questionService = inject(QuestionService);

  ngOnInit() {
    this.filteredQuestions$ = combineLatest([
      this.route.queryParamMap,
      this.searchService.currentSearchTerm.pipe(startWith(''))
    ]).pipe(
      switchMap(([params, term]) => {
        const tag = params.get('tag');
        const author = params.get('author');
        this.activeTag = tag;
        this.activeAuthor = author;

        const source$ = tag
          ? this.questionService.getQuestionsByTag(tag)
          : author
            ? this.questionService.getQuestionsByAuthor(author)
            : this.questionService.getQuestions();

        return source$.pipe(map(questions => this.getFilteredList(questions, term)));
      })
    );
  }

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

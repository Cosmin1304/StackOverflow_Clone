import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Question } from '../models/question.model';
import { mockQuestions } from '../models/mock-data';

@Component({
  selector: 'app-question-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './question-list.component.html',
  styleUrls: ['./question-list.component.scss']
})
export class QuestionListComponent {
  questions: Question[] = mockQuestions;
  private router = inject(Router);

  goToQuestion(id: number) {
    this.router.navigate(['/question', id]);
  }
}

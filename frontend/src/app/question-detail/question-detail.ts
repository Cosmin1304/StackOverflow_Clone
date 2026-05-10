import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import { CommonModule } from '@angular/common';
import { Question } from '../models/question.model';
import { Answer } from '../models/answer.model';
import { mockQuestions, mockAnswers } from '../models/mock-data';


@Component({
  selector: 'app-question-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './question-detail.html',
  styleUrl: './question-detail.scss',
})
export class QuestionDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  question: Question | undefined;
  answers: Answer[] = [];

  ngOnInit() {
    const idParam = this.route.snapshot.paramMap.get('id');
    const questionId = Number(idParam);

    this.question = mockQuestions.find(q => q.id === questionId);

    this.answers = mockAnswers.filter(a => a.topicId === questionId);
  }
}

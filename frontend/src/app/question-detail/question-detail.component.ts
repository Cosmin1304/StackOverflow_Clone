import {Component, inject, OnInit, Input} from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';
import { CommonModule } from '@angular/common';
import { Question } from '../models/question.model';
import { Answer } from '../models/answer.model';
import { QuestionService } from '../services/question.service';


@Component({
  selector: 'app-question-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './question-detail.component.html',
  styleUrl: './question-detail.component.scss',
})
export class QuestionDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private questionService = inject(QuestionService);

  question: Question | undefined;
  answers: Answer[] = [];

  // SIMULARE UTILIZATOR (Aici vei schimba pentru a testa vizibilitatea)
  @Input() currentUser = { id: 1, username: 'cosmin_dev', role: 'USER' };

  ngOnInit() {
    const idParam = this.route.snapshot.paramMap.get('id');
    const questionId = Number(idParam);

    // Cerem datele specifice de la serviciu
    this.question = this.questionService.getQuestionById(questionId);
    this.answers = this.questionService.getAnswersForQuestion(questionId);
  }
  onPostAnswer() {
    alert('Răspunsul tău a fost adăugat!');
    // Aici se va face apelul către Backend în Assignment 3
  }

  onVote(type: string, target: 'question' | 'answer') {
    console.log(`Vot ${type} pentru ${target}`);
    // Verificare conform cerinței: Utilizatorii nu pot vota propriile postări
  }

  onDelete(target: 'question' | 'answer') {
    if(confirm('Ești sigur că vrei să ștergi?')) {
      alert(`${target} șters cu succes!`);
    }
  }

  canModify(authorId: number): boolean {
    if (!this.currentUser) return false;
    return this.currentUser.id === authorId || this.currentUser.role === 'MODERATOR';
  }
}

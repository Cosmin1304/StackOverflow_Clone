import {Component, inject, OnInit, ChangeDetectorRef} from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { QuestionService } from '../services/question.service';
import { TopicResponseDTO, AnswerResponseDTO, UserResponseDTO } from '../models/models';
import { AuthService } from '../services/auth.service';


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
  private authService = inject(AuthService);
  private router = inject(Router);

  question: TopicResponseDTO | undefined;
  answers: AnswerResponseDTO[] = [];

  currentUser: UserResponseDTO | null = null;

  private cdr = inject(ChangeDetectorRef);

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();

    const idParam = this.route.snapshot.paramMap.get('id');
    const questionId = Number(idParam);

    this.questionService.getQuestionById(questionId).subscribe({
      next: (data) => {
        this.question = data;
        this.cdr.detectChanges();
        console.log("Datele primite în componentă:", this.question); // <--- UITĂ-TE ÎN CONSOLĂ
      },
      error: (err) => console.error('Eroare la încărcarea întrebării:', err)
    });

    this.questionService.getAnswersForQuestion(questionId).subscribe({
      next: (data) => this.answers = data,
      error: (err) => console.error('Eroare la încărcarea răspunsurilor:', err)
    });
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
    if (confirm('Ești sigur că vrei să ștergi? Această acțiune este ireversibilă!')) {

      if (target === 'question' && this.question) {
        // Apelăm serviciul pentru a șterge din baza de date
        this.questionService.deleteQuestion(this.question.id).subscribe({
          next: () => {
            alert('Întrebarea a fost ștearsă cu succes!');
            this.router.navigate(['/']); // Ne întoarcem la lista de întrebări
          },
          error: (err) => {
            console.error('Eroare la ștergere:', err);
            alert('Nu s-a putut șterge. Verifică consola.');
          }
        });
      }
    }
  }

  onEdit(target: 'question' | 'answer') {
    if (target === 'question' && this.question) {
      this.router.navigate(['/edit-question', this.question.id]);
    }
  }

  canModify(authorId: number): boolean {
    if (!this.currentUser) return false;
    return this.currentUser.id === authorId || this.currentUser.roles.includes('MODERATOR');  }
}

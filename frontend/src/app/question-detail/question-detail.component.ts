import {Component, inject, OnInit, ChangeDetectorRef} from '@angular/core';
import {ActivatedRoute, RouterLink, RouterModule} from '@angular/router';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { QuestionService } from '../services/question.service';
import { TopicResponseDTO, AnswerResponseDTO, UserResponseDTO, AnswerRequestDTO } from '../models/models';
import { AuthService } from '../services/auth.service';


@Component({
  selector: 'app-question-detail',
  standalone: true,
  imports: [CommonModule,FormsModule, RouterLink],
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

  newAnswer: AnswerRequestDTO = {
    text: '',
    pictureUrl: ''
  };

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
    if (!this.newAnswer.text || this.newAnswer.text.trim() === '') {
      alert('Te rog scrie un conținut pentru răspuns!');
      return;
    }

    if (this.question) {
      this.questionService.addAnswer(this.question.id, this.newAnswer).subscribe({
        next: (savedAnswer) => {
          alert('Răspunsul tău a fost adăugat cu succes!');

          this.newAnswer = { text: '', pictureUrl: '' };

          this.refreshAnswersList();
        },
        error: (err) => {
          console.error('Eroare la salvarea răspunsului:', err);
          alert('A apărut o eroare la salvarea răspunsului.');
        }
      });
    }
  }

  onDeleteAnswer(answerId: number) {
    if (confirm('Ești sigur că vrei să ștergi acest răspuns?')) {
      this.questionService.deleteAnswer(answerId).subscribe({
        next: () => {
          this.refreshAnswersList();
          alert('Răspunsul a fost șters cu succes!');
        },
        error: (err) => {
          console.error('Eroare la ștergerea răspunsului:', err);
          alert('Nu s-a putut șterge răspunsul.');
        }
      });
    }
  }

  onEditAnswer(ans: any) {
    const currentText = ans.text;
    const modifiedText = prompt('Modifică răspunsul tău:', currentText);

    if (modifiedText && modifiedText.trim() !== '' && modifiedText !== currentText) {
      this.questionService.updateAnswer(ans.id, modifiedText).subscribe({
        next: () => {
          this.refreshAnswersList();
          alert('Răspunsul a fost modificat cu succes!');
        },
        error: (err) => {
          console.error('Eroare la modificarea răspunsului:', err);
          alert('Nu s-a putut salva modificarea.');
        }
      });
    }
  }

  private refreshAnswersList() {
    if (this.question) {
      this.questionService.getAnswersForQuestion(this.question.id).subscribe({
        next: (updatedAnswers) => {
          this.answers = updatedAnswers;
        },
        error: (err) => console.error('Eroare la reîmprospătarea listei de răspunsuri', err)
      });
    }
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

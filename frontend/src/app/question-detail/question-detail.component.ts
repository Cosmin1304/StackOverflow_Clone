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

  isUploadingImage: boolean = false;

  editingAnswerId: number | null = null;
  editAnswerData = { text: '', pictureUrl: '' };

  private cdr = inject(ChangeDetectorRef);

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();

    const idParam = this.route.snapshot.paramMap.get('id');
    const questionId = Number(idParam);

    this.questionService.getQuestionById(questionId).subscribe({
      next: (data) => {
        this.question = data;
        this.cdr.detectChanges();
        console.log("Datele primite în componentă:", this.question);
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
    this.editingAnswerId = ans.id;
    this.editAnswerData.text = ans.text;
    this.editAnswerData.pictureUrl = ans.pictureUrl || '';
  }

  onCancelEdit() {
    this.editingAnswerId = null;
  }

  onSaveEditAnswer(answerId: number) {
    if (!this.editAnswerData.text || this.editAnswerData.text.trim() === '') {
      alert('Conținutul nu poate fi gol!');
      return;
    }

    this.questionService.updateAnswer(answerId, this.editAnswerData).subscribe({
      next: () => {
        this.refreshAnswersList();
        alert('Răspunsul a fost modificat cu succes!');
        this.editingAnswerId = null;
      },
      error: (err) => {
        console.error('Eroare la modificarea răspunsului:', err);
        alert('Nu s-a putut salva modificarea.');
      }
    });
  }

  onEditFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      this.isUploadingImage = true;
      this.questionService.uploadImage(file).subscribe({
        next: (response) => {
          this.editAnswerData.pictureUrl = response.url;
          this.isUploadingImage = false;
        },
        error: (err) => {
          console.error('Eroare la upload:', err);
          alert('Nu s-a putut încărca imaginea pe server.');
          this.isUploadingImage = false;
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

  onVote(direction: string, targetType: 'question' | 'answer', targetId: number): void {
    const voteTypeStr = direction === 'up' ? 'UPVOTE' : 'DOWNVOTE';

    if (targetType === 'question') {
      this.questionService.voteTopic(targetId, voteTypeStr).subscribe({
        next: () => {
          this.ngOnInit();
        },
        error: (err) => {
          console.error('Eroare la votare:', err);
        }
      });
    } else {
      this.questionService.voteAnswer(targetId, voteTypeStr).subscribe({
        next: () => {
          this.ngOnInit();
        },
        error: (err) => {
          console.error('Eroare la votare:', err);
        }
      });
    }
  }

  onDelete(target: 'question' | 'answer') {
    if (confirm('Ești sigur că vrei să ștergi? Această acțiune este ireversibilă!')) {

      if (target === 'question' && this.question) {
        this.questionService.deleteQuestion(this.question.id).subscribe({
          next: () => {
            alert('Întrebarea a fost ștearsă cu succes!');
            this.router.navigate(['/']);
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
    return this.currentUser.id === authorId || this.currentUser.roles.includes('MODERATOR');
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];

    if (file) {
      this.isUploadingImage = true;

      this.questionService.uploadImage(file).subscribe({
        next: (response) => {
          this.newAnswer.pictureUrl = response.url;
          this.isUploadingImage = false;
        },
        error: (err) => {
          console.error('Eroare la upload:', err);
          alert('Nu s-a putut încărca imaginea pe server.');
          this.isUploadingImage = false;
        }
      });
    }
  }

  getScore(votes: any[]): number {
    if (!votes || votes.length === 0) return 0;

    let score = 0;
    for (let vote of votes) {
      if (vote.voteType === 'UPVOTE') {
        score++;
      } else if (vote.voteType === 'DOWNVOTE') {
        score--;
      }
    }
    return score;
  }

  hasUserVoted(votes: any[] | undefined, type: string): boolean {
    if (!this.currentUser || !votes) return false;

    const userVote = votes.find(v => v.userId === this.currentUser!.id);
    return userVote?.voteType === type;
  }

}



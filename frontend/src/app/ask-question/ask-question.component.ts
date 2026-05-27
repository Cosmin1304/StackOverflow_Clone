import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { QuestionService } from '../services/question.service';
import { TopicRequestDTO } from '../models/models';

@Component({
  selector: 'app-ask-question',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './ask-question.component.html',
  styleUrls: ['./ask-question.component.scss']
})
export class AskQuestionComponent {
  private router = inject(Router);
  private questionService = inject(QuestionService);

  newQuestion: TopicRequestDTO = {
    title: '',
    text: '',
    pictureUrl: '',
    tagNames: []
  };

  isUploadingImage: boolean = false;

  tagsString: string = '';

  onSubmit() {
    // 1. Verificare: Titlul este gol?
    if (!this.newQuestion.title || this.newQuestion.title.trim() === '') {
      alert('Te rog să introduci un titlu pentru întrebare!');
      return; // Ne oprim aici, nu mergem mai departe
    }

    // 2. Verificare: Textul este gol?
    if (!this.newQuestion.text || this.newQuestion.text.trim() === '') {
      alert('Te rog să adaugi detaliile întrebării!');
      return; // Ne oprim aici
    }

    // 3. Dacă am ajuns aici, datele sunt corecte. Procesăm tag-urile:
    if (this.tagsString.trim()) {
      this.newQuestion.tagNames = this.tagsString.split(',').map(tag => tag.trim());
    }

    // 4. Trimitem către backend
    this.questionService.addQuestion(this.newQuestion).subscribe({
      next: (response) => {
        alert('Întrebarea a fost publicată cu succes!');
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error('Eroare la trimiterea către backend:', err);
        alert('A apărut o eroare la salvare. Verifică conexiunea cu backend-ul!');
      }
    });
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];

    if (file) {
      this.isUploadingImage = true;

      this.questionService.uploadImage(file).subscribe({
        next: (response) => {
          this.newQuestion.pictureUrl = response.url;
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
}

import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { QuestionService } from '../services/question.service';
import { TopicRequestDTO } from '../models/models';

@Component({
  selector: 'app-edit-question',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './edit-question.component.html',
  styleUrl: '../ask-question/ask-question.component.scss'
})
export class EditQuestionComponent {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private questionService = inject(QuestionService);

  questionId!: number;
  tagsString: string = '';

  editData: TopicRequestDTO = {
    title: '',
    text: '',
    pictureUrl: '',
    tagNames: []
  };

  ngOnInit() {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.questionId = Number(idParam);

    this.questionService.getQuestionById(this.questionId).subscribe({
      next: (data) => {
        this.editData.title = data.title;
        this.editData.text = data.text;
        this.editData.pictureUrl = data.pictureUrl || '';

        if (data.tags && data.tags.length > 0) {
          this.tagsString = data.tags.map(t => t.name).join(', ');
        }
      },
      error: (err) => console.error('Eroare la încărcarea întrebării:', err)
    });
  }

  onUpdate() {
    if (!this.editData.title || !this.editData.text) {
      return;
    }

    if (this.tagsString.trim()) {
      this.editData.tagNames = this.tagsString.split(',').map(tag => tag.trim());
    }

    this.questionService.updateQuestion(this.questionId, this.editData).subscribe({
      next: () => {
        alert('Întrebarea a fost actualizată cu succes!');
        this.router.navigate(['/question', this.questionId]); // Ne întoarcem la întrebare
      },
      error: (err) => {
        console.error('Eroare la editare:', err);
        alert('Eroare la salvarea modificărilor.');
      }
    });
  }
}

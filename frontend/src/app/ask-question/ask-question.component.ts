import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-ask-question',
  standalone: true,
  imports: [],
  templateUrl: './ask-question.component.html',
  styleUrls: ['./ask-question.component.scss']
})
export class AskQuestionComponent {
  private router = inject(Router);

  onSubmit() {
    // Aici vom colecta titlul, textul, poza și tagurile pentru a le trimite la backend
    alert('Întrebarea a fost "salvată"! Te întoarcem la lista principală.');
    this.router.navigate(['/']);
  }
}

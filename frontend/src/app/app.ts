import {Component, inject, signal} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {QuestionListComponent} from './question-list/question-list.component';
import {NavbarComponent} from './navbar/navbar.component';
import {AuthService} from './services/auth.service';
import {AsyncPipe, CommonModule} from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, QuestionListComponent, NavbarComponent, CommonModule, AsyncPipe],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('frontend');

  authService = inject(AuthService);
}


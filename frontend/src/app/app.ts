import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {QuestionListComponent} from './question-list/question-list.component';
import {NavbarComponent} from './navbar/navbar.component';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, QuestionListComponent, NavbarComponent, LoginComponent, RegisterComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('frontend');
}


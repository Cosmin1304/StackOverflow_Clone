import { Routes } from '@angular/router';
import { QuestionListComponent } from './question-list/question-list.component';
import { LoginComponent } from './login/login.component';
import { QuestionDetailComponent } from './question-detail/question-detail.component'
import { RegisterComponent } from './register/register.component';
import { ProfileComponent } from './profile/profile.component';
import {AskQuestionComponent} from './ask-question/ask-question.component';

export const routes: Routes = [
  { path: '', component: QuestionListComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'ask', component: AskQuestionComponent },
  { path: 'question/:id', component: QuestionDetailComponent },
  { path: 'profile', component: ProfileComponent }
  ];

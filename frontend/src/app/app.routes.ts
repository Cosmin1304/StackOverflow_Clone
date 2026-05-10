import { Routes } from '@angular/router';
import { QuestionListComponent } from './question-list/question-list.component';
import { LoginComponent } from './login/login.component';
import { QuestionDetailComponent } from './question-detail/question-detail';
import { RegisterComponent } from './register/register.component';
import { ProfileComponent } from './profile/profile.component';

export const routes: Routes = [
  { path: '', component: QuestionListComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'question/:id', component: QuestionDetailComponent },
  { path: 'profile', component: ProfileComponent }
  ];

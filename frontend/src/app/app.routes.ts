import { Routes } from '@angular/router';
import { QuestionListComponent } from './question-list/question-list.component';
import {LoginComponent} from './login/login.component';
import { QuestionDetailComponent } from './question-detail/question-detail.component';
import { RegisterComponent } from './register/register.component';
import { AskQuestionComponent } from './ask-question/ask-question.component';
import {authGuard} from './guards/auth-guard';
import { moderatorGuard } from './guards/moderator-guard';
import { ProfileComponent } from './profile/profile.component';
import { EditQuestionComponent } from './edit-question/edit-question.component';
import { ModeratorPanelComponent } from './moderator-panel/moderator-panel.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  { path: '', component: QuestionListComponent, canActivate: [authGuard] },
  { path: 'ask', component: AskQuestionComponent, canActivate: [authGuard] },
  { path: 'question/:id', component: QuestionDetailComponent, canActivate: [authGuard] },
  { path: 'edit-question/:id', component: EditQuestionComponent, canActivate: [authGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [authGuard] },
  { path: 'moderator', component: ModeratorPanelComponent, canActivate: [moderatorGuard] },
  ];

import { Component, inject } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  private router = inject(Router);
  private authService = inject(AuthService);

  onLogin() {
    this.authService.login();
    this.router.navigate(['/']);
  }
}

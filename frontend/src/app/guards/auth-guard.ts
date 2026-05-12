import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return true; // Permite accesul
  } else {
    // Dacă nu e logat, îl trimitem la pagina de login
    router.navigate(['/login']);
    return false;
  }
};

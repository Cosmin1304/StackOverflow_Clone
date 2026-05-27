import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

// Protejează rutele rezervate moderatorilor (ex: /moderator).
// Dacă utilizatorul nu e logat sau nu are rolul MODERATOR, îl trimitem pe pagina principală.
export const moderatorGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated() && authService.isModerator()) {
    return true;
  }

  router.navigate(['/']);
  return false;
};

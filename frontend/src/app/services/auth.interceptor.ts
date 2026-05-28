import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  let modifiedReq = req;

  // localStorage nu exista pe server (SSR/prerender) — il accesam doar in browser.
  const token = typeof localStorage !== 'undefined' ? localStorage.getItem('token') : null;

  if (token) {
    modifiedReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(modifiedReq);
};

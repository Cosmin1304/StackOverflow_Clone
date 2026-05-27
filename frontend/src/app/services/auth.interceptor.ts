import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  let modifiedReq = req;

  // localStorage nu există pe server (SSR / prerender) — îl accesăm doar în browser.
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

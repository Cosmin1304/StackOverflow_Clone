import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  let modifiedReq = req;

  const userString = localStorage.getItem('user');

  if (userString) {
    const user = JSON.parse(userString);
    modifiedReq = req.clone({
      setHeaders: {
        'X-Username': user.username,
        'X-User-Id': user.id.toString()
      }
    });
  }

  return next(modifiedReq);
};

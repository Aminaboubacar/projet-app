import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDegre } from '../degre.model';
import { DegreService } from '../service/degre.service';

export const degreResolve = (route: ActivatedRouteSnapshot): Observable<null | IDegre> => {
  const id = route.params['id'];
  if (id) {
    return inject(DegreService)
      .find(id)
      .pipe(
        mergeMap((degre: HttpResponse<IDegre>) => {
          if (degre.body) {
            return of(degre.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default degreResolve;

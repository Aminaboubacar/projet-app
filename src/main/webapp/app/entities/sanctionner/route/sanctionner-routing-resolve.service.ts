import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISanctionner } from '../sanctionner.model';
import { SanctionnerService } from '../service/sanctionner.service';

export const sanctionnerResolve = (route: ActivatedRouteSnapshot): Observable<null | ISanctionner> => {
  const id = route.params['id'];
  if (id) {
    return inject(SanctionnerService)
      .find(id)
      .pipe(
        mergeMap((sanctionner: HttpResponse<ISanctionner>) => {
          if (sanctionner.body) {
            return of(sanctionner.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default sanctionnerResolve;

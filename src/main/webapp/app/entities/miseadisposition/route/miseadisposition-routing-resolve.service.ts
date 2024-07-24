import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMiseadisposition } from '../miseadisposition.model';
import { MiseadispositionService } from '../service/miseadisposition.service';

export const miseadispositionResolve = (route: ActivatedRouteSnapshot): Observable<null | IMiseadisposition> => {
  const id = route.params['id'];
  if (id) {
    return inject(MiseadispositionService)
      .find(id)
      .pipe(
        mergeMap((miseadisposition: HttpResponse<IMiseadisposition>) => {
          if (miseadisposition.body) {
            return of(miseadisposition.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default miseadispositionResolve;

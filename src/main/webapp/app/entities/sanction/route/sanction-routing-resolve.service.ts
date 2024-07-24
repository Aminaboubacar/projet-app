import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISanction } from '../sanction.model';
import { SanctionService } from '../service/sanction.service';

export const sanctionResolve = (route: ActivatedRouteSnapshot): Observable<null | ISanction> => {
  const id = route.params['id'];
  if (id) {
    return inject(SanctionService)
      .find(id)
      .pipe(
        mergeMap((sanction: HttpResponse<ISanction>) => {
          if (sanction.body) {
            return of(sanction.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default sanctionResolve;

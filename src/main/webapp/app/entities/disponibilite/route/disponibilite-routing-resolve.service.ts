import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDisponibilite } from '../disponibilite.model';
import { DisponibiliteService } from '../service/disponibilite.service';

export const disponibiliteResolve = (route: ActivatedRouteSnapshot): Observable<null | IDisponibilite> => {
  const id = route.params['id'];
  if (id) {
    return inject(DisponibiliteService)
      .find(id)
      .pipe(
        mergeMap((disponibilite: HttpResponse<IDisponibilite>) => {
          if (disponibilite.body) {
            return of(disponibilite.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default disponibiliteResolve;

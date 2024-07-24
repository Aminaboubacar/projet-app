import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMotifDisponibilite } from '../motif-disponibilite.model';
import { MotifDisponibiliteService } from '../service/motif-disponibilite.service';

export const motifDisponibiliteResolve = (route: ActivatedRouteSnapshot): Observable<null | IMotifDisponibilite> => {
  const id = route.params['id'];
  if (id) {
    return inject(MotifDisponibiliteService)
      .find(id)
      .pipe(
        mergeMap((motifDisponibilite: HttpResponse<IMotifDisponibilite>) => {
          if (motifDisponibilite.body) {
            return of(motifDisponibilite.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default motifDisponibiliteResolve;

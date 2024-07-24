import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMiseadisposition, NewMiseadisposition } from '../miseadisposition.model';

export type PartialUpdateMiseadisposition = Partial<IMiseadisposition> & Pick<IMiseadisposition, 'id'>;

type RestOf<T extends IMiseadisposition | NewMiseadisposition> = Omit<T, 'dateDebut' | 'dateFin' | 'dateRetour'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
  dateRetour?: string | null;
};

export type RestMiseadisposition = RestOf<IMiseadisposition>;

export type NewRestMiseadisposition = RestOf<NewMiseadisposition>;

export type PartialUpdateRestMiseadisposition = RestOf<PartialUpdateMiseadisposition>;

export type EntityResponseType = HttpResponse<IMiseadisposition>;
export type EntityArrayResponseType = HttpResponse<IMiseadisposition[]>;

@Injectable({ providedIn: 'root' })
export class MiseadispositionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/miseadispositions');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(miseadisposition: NewMiseadisposition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(miseadisposition);
    return this.http
      .post<RestMiseadisposition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(miseadisposition: IMiseadisposition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(miseadisposition);
    return this.http
      .put<RestMiseadisposition>(`${this.resourceUrl}/${this.getMiseadispositionIdentifier(miseadisposition)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(miseadisposition: PartialUpdateMiseadisposition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(miseadisposition);
    return this.http
      .patch<RestMiseadisposition>(`${this.resourceUrl}/${this.getMiseadispositionIdentifier(miseadisposition)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMiseadisposition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMiseadisposition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMiseadispositionIdentifier(miseadisposition: Pick<IMiseadisposition, 'id'>): number {
    return miseadisposition.id;
  }

  compareMiseadisposition(o1: Pick<IMiseadisposition, 'id'> | null, o2: Pick<IMiseadisposition, 'id'> | null): boolean {
    return o1 && o2 ? this.getMiseadispositionIdentifier(o1) === this.getMiseadispositionIdentifier(o2) : o1 === o2;
  }

  addMiseadispositionToCollectionIfMissing<Type extends Pick<IMiseadisposition, 'id'>>(
    miseadispositionCollection: Type[],
    ...miseadispositionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const miseadispositions: Type[] = miseadispositionsToCheck.filter(isPresent);
    if (miseadispositions.length > 0) {
      const miseadispositionCollectionIdentifiers = miseadispositionCollection.map(
        miseadispositionItem => this.getMiseadispositionIdentifier(miseadispositionItem)!,
      );
      const miseadispositionsToAdd = miseadispositions.filter(miseadispositionItem => {
        const miseadispositionIdentifier = this.getMiseadispositionIdentifier(miseadispositionItem);
        if (miseadispositionCollectionIdentifiers.includes(miseadispositionIdentifier)) {
          return false;
        }
        miseadispositionCollectionIdentifiers.push(miseadispositionIdentifier);
        return true;
      });
      return [...miseadispositionsToAdd, ...miseadispositionCollection];
    }
    return miseadispositionCollection;
  }

  protected convertDateFromClient<T extends IMiseadisposition | NewMiseadisposition | PartialUpdateMiseadisposition>(
    miseadisposition: T,
  ): RestOf<T> {
    return {
      ...miseadisposition,
      dateDebut: miseadisposition.dateDebut?.toJSON() ?? null,
      dateFin: miseadisposition.dateFin?.toJSON() ?? null,
      dateRetour: miseadisposition.dateRetour?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMiseadisposition: RestMiseadisposition): IMiseadisposition {
    return {
      ...restMiseadisposition,
      dateDebut: restMiseadisposition.dateDebut ? dayjs(restMiseadisposition.dateDebut) : undefined,
      dateFin: restMiseadisposition.dateFin ? dayjs(restMiseadisposition.dateFin) : undefined,
      dateRetour: restMiseadisposition.dateRetour ? dayjs(restMiseadisposition.dateRetour) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMiseadisposition>): HttpResponse<IMiseadisposition> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMiseadisposition[]>): HttpResponse<IMiseadisposition[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

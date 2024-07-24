import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISanctionner, NewSanctionner } from '../sanctionner.model';

export type PartialUpdateSanctionner = Partial<ISanctionner> & Pick<ISanctionner, 'id'>;

type RestOf<T extends ISanctionner | NewSanctionner> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestSanctionner = RestOf<ISanctionner>;

export type NewRestSanctionner = RestOf<NewSanctionner>;

export type PartialUpdateRestSanctionner = RestOf<PartialUpdateSanctionner>;

export type EntityResponseType = HttpResponse<ISanctionner>;
export type EntityArrayResponseType = HttpResponse<ISanctionner[]>;

@Injectable({ providedIn: 'root' })
export class SanctionnerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sanctionners');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(sanctionner: NewSanctionner): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sanctionner);
    return this.http
      .post<RestSanctionner>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(sanctionner: ISanctionner): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sanctionner);
    return this.http
      .put<RestSanctionner>(`${this.resourceUrl}/${this.getSanctionnerIdentifier(sanctionner)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(sanctionner: PartialUpdateSanctionner): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sanctionner);
    return this.http
      .patch<RestSanctionner>(`${this.resourceUrl}/${this.getSanctionnerIdentifier(sanctionner)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSanctionner>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSanctionner[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSanctionnerIdentifier(sanctionner: Pick<ISanctionner, 'id'>): number {
    return sanctionner.id;
  }

  compareSanctionner(o1: Pick<ISanctionner, 'id'> | null, o2: Pick<ISanctionner, 'id'> | null): boolean {
    return o1 && o2 ? this.getSanctionnerIdentifier(o1) === this.getSanctionnerIdentifier(o2) : o1 === o2;
  }

  addSanctionnerToCollectionIfMissing<Type extends Pick<ISanctionner, 'id'>>(
    sanctionnerCollection: Type[],
    ...sanctionnersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sanctionners: Type[] = sanctionnersToCheck.filter(isPresent);
    if (sanctionners.length > 0) {
      const sanctionnerCollectionIdentifiers = sanctionnerCollection.map(
        sanctionnerItem => this.getSanctionnerIdentifier(sanctionnerItem)!,
      );
      const sanctionnersToAdd = sanctionners.filter(sanctionnerItem => {
        const sanctionnerIdentifier = this.getSanctionnerIdentifier(sanctionnerItem);
        if (sanctionnerCollectionIdentifiers.includes(sanctionnerIdentifier)) {
          return false;
        }
        sanctionnerCollectionIdentifiers.push(sanctionnerIdentifier);
        return true;
      });
      return [...sanctionnersToAdd, ...sanctionnerCollection];
    }
    return sanctionnerCollection;
  }

  protected convertDateFromClient<T extends ISanctionner | NewSanctionner | PartialUpdateSanctionner>(sanctionner: T): RestOf<T> {
    return {
      ...sanctionner,
      date: sanctionner.date?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSanctionner: RestSanctionner): ISanctionner {
    return {
      ...restSanctionner,
      date: restSanctionner.date ? dayjs(restSanctionner.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSanctionner>): HttpResponse<ISanctionner> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSanctionner[]>): HttpResponse<ISanctionner[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

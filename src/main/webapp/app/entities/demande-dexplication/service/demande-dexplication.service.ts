import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDemandeDexplication, NewDemandeDexplication } from '../demande-dexplication.model';

export type PartialUpdateDemandeDexplication = Partial<IDemandeDexplication> & Pick<IDemandeDexplication, 'id'>;

type RestOf<T extends IDemandeDexplication | NewDemandeDexplication> = Omit<T, 'date' | 'datappDg'> & {
  date?: string | null;
  datappDg?: string | null;
};

export type RestDemandeDexplication = RestOf<IDemandeDexplication>;

export type NewRestDemandeDexplication = RestOf<NewDemandeDexplication>;

export type PartialUpdateRestDemandeDexplication = RestOf<PartialUpdateDemandeDexplication>;

export type EntityResponseType = HttpResponse<IDemandeDexplication>;
export type EntityArrayResponseType = HttpResponse<IDemandeDexplication[]>;

@Injectable({ providedIn: 'root' })
export class DemandeDexplicationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/demande-dexplications');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(demandeDexplication: NewDemandeDexplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeDexplication);
    return this.http
      .post<RestDemandeDexplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(demandeDexplication: IDemandeDexplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeDexplication);
    return this.http
      .put<RestDemandeDexplication>(`${this.resourceUrl}/${this.getDemandeDexplicationIdentifier(demandeDexplication)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(demandeDexplication: PartialUpdateDemandeDexplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeDexplication);
    return this.http
      .patch<RestDemandeDexplication>(`${this.resourceUrl}/${this.getDemandeDexplicationIdentifier(demandeDexplication)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDemandeDexplication>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDemandeDexplication[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDemandeDexplicationIdentifier(demandeDexplication: Pick<IDemandeDexplication, 'id'>): number {
    return demandeDexplication.id;
  }

  compareDemandeDexplication(o1: Pick<IDemandeDexplication, 'id'> | null, o2: Pick<IDemandeDexplication, 'id'> | null): boolean {
    return o1 && o2 ? this.getDemandeDexplicationIdentifier(o1) === this.getDemandeDexplicationIdentifier(o2) : o1 === o2;
  }

  addDemandeDexplicationToCollectionIfMissing<Type extends Pick<IDemandeDexplication, 'id'>>(
    demandeDexplicationCollection: Type[],
    ...demandeDexplicationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const demandeDexplications: Type[] = demandeDexplicationsToCheck.filter(isPresent);
    if (demandeDexplications.length > 0) {
      const demandeDexplicationCollectionIdentifiers = demandeDexplicationCollection.map(
        demandeDexplicationItem => this.getDemandeDexplicationIdentifier(demandeDexplicationItem)!,
      );
      const demandeDexplicationsToAdd = demandeDexplications.filter(demandeDexplicationItem => {
        const demandeDexplicationIdentifier = this.getDemandeDexplicationIdentifier(demandeDexplicationItem);
        if (demandeDexplicationCollectionIdentifiers.includes(demandeDexplicationIdentifier)) {
          return false;
        }
        demandeDexplicationCollectionIdentifiers.push(demandeDexplicationIdentifier);
        return true;
      });
      return [...demandeDexplicationsToAdd, ...demandeDexplicationCollection];
    }
    return demandeDexplicationCollection;
  }

  protected convertDateFromClient<T extends IDemandeDexplication | NewDemandeDexplication | PartialUpdateDemandeDexplication>(
    demandeDexplication: T,
  ): RestOf<T> {
    return {
      ...demandeDexplication,
      date: demandeDexplication.date?.toJSON() ?? null,
      datappDg: demandeDexplication.datappDg?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDemandeDexplication: RestDemandeDexplication): IDemandeDexplication {
    return {
      ...restDemandeDexplication,
      date: restDemandeDexplication.date ? dayjs(restDemandeDexplication.date) : undefined,
      datappDg: restDemandeDexplication.datappDg ? dayjs(restDemandeDexplication.datappDg) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDemandeDexplication>): HttpResponse<IDemandeDexplication> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDemandeDexplication[]>): HttpResponse<IDemandeDexplication[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

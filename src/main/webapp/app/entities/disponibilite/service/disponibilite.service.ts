import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDisponibilite, NewDisponibilite } from '../disponibilite.model';

export type PartialUpdateDisponibilite = Partial<IDisponibilite> & Pick<IDisponibilite, 'id'>;

type RestOf<T extends IDisponibilite | NewDisponibilite> = Omit<T, 'dateDebut' | 'dateFin' | 'dateRetour'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
  dateRetour?: string | null;
};

export type RestDisponibilite = RestOf<IDisponibilite>;

export type NewRestDisponibilite = RestOf<NewDisponibilite>;

export type PartialUpdateRestDisponibilite = RestOf<PartialUpdateDisponibilite>;

export type EntityResponseType = HttpResponse<IDisponibilite>;
export type EntityArrayResponseType = HttpResponse<IDisponibilite[]>;

@Injectable({ providedIn: 'root' })
export class DisponibiliteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/disponibilites');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(disponibilite: NewDisponibilite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(disponibilite);
    return this.http
      .post<RestDisponibilite>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(disponibilite: IDisponibilite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(disponibilite);
    return this.http
      .put<RestDisponibilite>(`${this.resourceUrl}/${this.getDisponibiliteIdentifier(disponibilite)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(disponibilite: PartialUpdateDisponibilite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(disponibilite);
    return this.http
      .patch<RestDisponibilite>(`${this.resourceUrl}/${this.getDisponibiliteIdentifier(disponibilite)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDisponibilite>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDisponibilite[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDisponibiliteIdentifier(disponibilite: Pick<IDisponibilite, 'id'>): number {
    return disponibilite.id;
  }

  compareDisponibilite(o1: Pick<IDisponibilite, 'id'> | null, o2: Pick<IDisponibilite, 'id'> | null): boolean {
    return o1 && o2 ? this.getDisponibiliteIdentifier(o1) === this.getDisponibiliteIdentifier(o2) : o1 === o2;
  }

  addDisponibiliteToCollectionIfMissing<Type extends Pick<IDisponibilite, 'id'>>(
    disponibiliteCollection: Type[],
    ...disponibilitesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const disponibilites: Type[] = disponibilitesToCheck.filter(isPresent);
    if (disponibilites.length > 0) {
      const disponibiliteCollectionIdentifiers = disponibiliteCollection.map(
        disponibiliteItem => this.getDisponibiliteIdentifier(disponibiliteItem)!,
      );
      const disponibilitesToAdd = disponibilites.filter(disponibiliteItem => {
        const disponibiliteIdentifier = this.getDisponibiliteIdentifier(disponibiliteItem);
        if (disponibiliteCollectionIdentifiers.includes(disponibiliteIdentifier)) {
          return false;
        }
        disponibiliteCollectionIdentifiers.push(disponibiliteIdentifier);
        return true;
      });
      return [...disponibilitesToAdd, ...disponibiliteCollection];
    }
    return disponibiliteCollection;
  }

  protected convertDateFromClient<T extends IDisponibilite | NewDisponibilite | PartialUpdateDisponibilite>(disponibilite: T): RestOf<T> {
    return {
      ...disponibilite,
      dateDebut: disponibilite.dateDebut?.toJSON() ?? null,
      dateFin: disponibilite.dateFin?.toJSON() ?? null,
      dateRetour: disponibilite.dateRetour?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDisponibilite: RestDisponibilite): IDisponibilite {
    return {
      ...restDisponibilite,
      dateDebut: restDisponibilite.dateDebut ? dayjs(restDisponibilite.dateDebut) : undefined,
      dateFin: restDisponibilite.dateFin ? dayjs(restDisponibilite.dateFin) : undefined,
      dateRetour: restDisponibilite.dateRetour ? dayjs(restDisponibilite.dateRetour) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDisponibilite>): HttpResponse<IDisponibilite> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDisponibilite[]>): HttpResponse<IDisponibilite[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

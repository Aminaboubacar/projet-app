import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMotifDisponibilite, NewMotifDisponibilite } from '../motif-disponibilite.model';

export type PartialUpdateMotifDisponibilite = Partial<IMotifDisponibilite> & Pick<IMotifDisponibilite, 'id'>;

export type EntityResponseType = HttpResponse<IMotifDisponibilite>;
export type EntityArrayResponseType = HttpResponse<IMotifDisponibilite[]>;

@Injectable({ providedIn: 'root' })
export class MotifDisponibiliteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/motif-disponibilites');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(motifDisponibilite: NewMotifDisponibilite): Observable<EntityResponseType> {
    return this.http.post<IMotifDisponibilite>(this.resourceUrl, motifDisponibilite, { observe: 'response' });
  }

  update(motifDisponibilite: IMotifDisponibilite): Observable<EntityResponseType> {
    return this.http.put<IMotifDisponibilite>(
      `${this.resourceUrl}/${this.getMotifDisponibiliteIdentifier(motifDisponibilite)}`,
      motifDisponibilite,
      { observe: 'response' },
    );
  }

  partialUpdate(motifDisponibilite: PartialUpdateMotifDisponibilite): Observable<EntityResponseType> {
    return this.http.patch<IMotifDisponibilite>(
      `${this.resourceUrl}/${this.getMotifDisponibiliteIdentifier(motifDisponibilite)}`,
      motifDisponibilite,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMotifDisponibilite>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMotifDisponibilite[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMotifDisponibiliteIdentifier(motifDisponibilite: Pick<IMotifDisponibilite, 'id'>): number {
    return motifDisponibilite.id;
  }

  compareMotifDisponibilite(o1: Pick<IMotifDisponibilite, 'id'> | null, o2: Pick<IMotifDisponibilite, 'id'> | null): boolean {
    return o1 && o2 ? this.getMotifDisponibiliteIdentifier(o1) === this.getMotifDisponibiliteIdentifier(o2) : o1 === o2;
  }

  addMotifDisponibiliteToCollectionIfMissing<Type extends Pick<IMotifDisponibilite, 'id'>>(
    motifDisponibiliteCollection: Type[],
    ...motifDisponibilitesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const motifDisponibilites: Type[] = motifDisponibilitesToCheck.filter(isPresent);
    if (motifDisponibilites.length > 0) {
      const motifDisponibiliteCollectionIdentifiers = motifDisponibiliteCollection.map(
        motifDisponibiliteItem => this.getMotifDisponibiliteIdentifier(motifDisponibiliteItem)!,
      );
      const motifDisponibilitesToAdd = motifDisponibilites.filter(motifDisponibiliteItem => {
        const motifDisponibiliteIdentifier = this.getMotifDisponibiliteIdentifier(motifDisponibiliteItem);
        if (motifDisponibiliteCollectionIdentifiers.includes(motifDisponibiliteIdentifier)) {
          return false;
        }
        motifDisponibiliteCollectionIdentifiers.push(motifDisponibiliteIdentifier);
        return true;
      });
      return [...motifDisponibilitesToAdd, ...motifDisponibiliteCollection];
    }
    return motifDisponibiliteCollection;
  }
}

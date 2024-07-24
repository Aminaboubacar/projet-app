import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDegre, NewDegre } from '../degre.model';

export type PartialUpdateDegre = Partial<IDegre> & Pick<IDegre, 'id'>;

export type EntityResponseType = HttpResponse<IDegre>;
export type EntityArrayResponseType = HttpResponse<IDegre[]>;

@Injectable({ providedIn: 'root' })
export class DegreService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/degres');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(degre: NewDegre): Observable<EntityResponseType> {
    return this.http.post<IDegre>(this.resourceUrl, degre, { observe: 'response' });
  }

  update(degre: IDegre): Observable<EntityResponseType> {
    return this.http.put<IDegre>(`${this.resourceUrl}/${this.getDegreIdentifier(degre)}`, degre, { observe: 'response' });
  }

  partialUpdate(degre: PartialUpdateDegre): Observable<EntityResponseType> {
    return this.http.patch<IDegre>(`${this.resourceUrl}/${this.getDegreIdentifier(degre)}`, degre, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDegre>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDegre[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDegreIdentifier(degre: Pick<IDegre, 'id'>): number {
    return degre.id;
  }

  compareDegre(o1: Pick<IDegre, 'id'> | null, o2: Pick<IDegre, 'id'> | null): boolean {
    return o1 && o2 ? this.getDegreIdentifier(o1) === this.getDegreIdentifier(o2) : o1 === o2;
  }

  addDegreToCollectionIfMissing<Type extends Pick<IDegre, 'id'>>(
    degreCollection: Type[],
    ...degresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const degres: Type[] = degresToCheck.filter(isPresent);
    if (degres.length > 0) {
      const degreCollectionIdentifiers = degreCollection.map(degreItem => this.getDegreIdentifier(degreItem)!);
      const degresToAdd = degres.filter(degreItem => {
        const degreIdentifier = this.getDegreIdentifier(degreItem);
        if (degreCollectionIdentifiers.includes(degreIdentifier)) {
          return false;
        }
        degreCollectionIdentifiers.push(degreIdentifier);
        return true;
      });
      return [...degresToAdd, ...degreCollection];
    }
    return degreCollection;
  }
}

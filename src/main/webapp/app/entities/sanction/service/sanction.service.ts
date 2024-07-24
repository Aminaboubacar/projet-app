import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISanction, NewSanction } from '../sanction.model';

export type PartialUpdateSanction = Partial<ISanction> & Pick<ISanction, 'id'>;

export type EntityResponseType = HttpResponse<ISanction>;
export type EntityArrayResponseType = HttpResponse<ISanction[]>;

@Injectable({ providedIn: 'root' })
export class SanctionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sanctions');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(sanction: NewSanction): Observable<EntityResponseType> {
    return this.http.post<ISanction>(this.resourceUrl, sanction, { observe: 'response' });
  }

  update(sanction: ISanction): Observable<EntityResponseType> {
    return this.http.put<ISanction>(`${this.resourceUrl}/${this.getSanctionIdentifier(sanction)}`, sanction, { observe: 'response' });
  }

  partialUpdate(sanction: PartialUpdateSanction): Observable<EntityResponseType> {
    return this.http.patch<ISanction>(`${this.resourceUrl}/${this.getSanctionIdentifier(sanction)}`, sanction, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISanction>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISanction[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSanctionIdentifier(sanction: Pick<ISanction, 'id'>): number {
    return sanction.id;
  }

  compareSanction(o1: Pick<ISanction, 'id'> | null, o2: Pick<ISanction, 'id'> | null): boolean {
    return o1 && o2 ? this.getSanctionIdentifier(o1) === this.getSanctionIdentifier(o2) : o1 === o2;
  }

  addSanctionToCollectionIfMissing<Type extends Pick<ISanction, 'id'>>(
    sanctionCollection: Type[],
    ...sanctionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sanctions: Type[] = sanctionsToCheck.filter(isPresent);
    if (sanctions.length > 0) {
      const sanctionCollectionIdentifiers = sanctionCollection.map(sanctionItem => this.getSanctionIdentifier(sanctionItem)!);
      const sanctionsToAdd = sanctions.filter(sanctionItem => {
        const sanctionIdentifier = this.getSanctionIdentifier(sanctionItem);
        if (sanctionCollectionIdentifiers.includes(sanctionIdentifier)) {
          return false;
        }
        sanctionCollectionIdentifiers.push(sanctionIdentifier);
        return true;
      });
      return [...sanctionsToAdd, ...sanctionCollection];
    }
    return sanctionCollection;
  }
}

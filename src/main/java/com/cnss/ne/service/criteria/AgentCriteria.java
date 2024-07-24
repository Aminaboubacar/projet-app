package com.cnss.ne.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.cnss.ne.domain.Agent} entity. This class is used
 * in {@link com.cnss.ne.web.rest.AgentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /agents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter matricule;

    private StringFilter nom;

    private StringFilter prenom;

    private InstantFilter dateNaissance;

    private StringFilter lieuNaissance;

    private StringFilter nationalite;

    private StringFilter telephone;

    private StringFilter adresse;

    private InstantFilter dateDece;

    private StringFilter causeDece;

    private LongFilter disponibiliteId;

    private LongFilter miseadispositionId;

    private LongFilter demandeDexplicationId;

    private LongFilter posteId;

    private Boolean distinct;

    public AgentCriteria() {}

    public AgentCriteria(AgentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.matricule = other.matricule == null ? null : other.matricule.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.prenom = other.prenom == null ? null : other.prenom.copy();
        this.dateNaissance = other.dateNaissance == null ? null : other.dateNaissance.copy();
        this.lieuNaissance = other.lieuNaissance == null ? null : other.lieuNaissance.copy();
        this.nationalite = other.nationalite == null ? null : other.nationalite.copy();
        this.telephone = other.telephone == null ? null : other.telephone.copy();
        this.adresse = other.adresse == null ? null : other.adresse.copy();
        this.dateDece = other.dateDece == null ? null : other.dateDece.copy();
        this.causeDece = other.causeDece == null ? null : other.causeDece.copy();
        this.disponibiliteId = other.disponibiliteId == null ? null : other.disponibiliteId.copy();
        this.miseadispositionId = other.miseadispositionId == null ? null : other.miseadispositionId.copy();
        this.demandeDexplicationId = other.demandeDexplicationId == null ? null : other.demandeDexplicationId.copy();
        this.posteId = other.posteId == null ? null : other.posteId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AgentCriteria copy() {
        return new AgentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMatricule() {
        return matricule;
    }

    public StringFilter matricule() {
        if (matricule == null) {
            matricule = new StringFilter();
        }
        return matricule;
    }

    public void setMatricule(StringFilter matricule) {
        this.matricule = matricule;
    }

    public StringFilter getNom() {
        return nom;
    }

    public StringFilter nom() {
        if (nom == null) {
            nom = new StringFilter();
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getPrenom() {
        return prenom;
    }

    public StringFilter prenom() {
        if (prenom == null) {
            prenom = new StringFilter();
        }
        return prenom;
    }

    public void setPrenom(StringFilter prenom) {
        this.prenom = prenom;
    }

    public InstantFilter getDateNaissance() {
        return dateNaissance;
    }

    public InstantFilter dateNaissance() {
        if (dateNaissance == null) {
            dateNaissance = new InstantFilter();
        }
        return dateNaissance;
    }

    public void setDateNaissance(InstantFilter dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public StringFilter getLieuNaissance() {
        return lieuNaissance;
    }

    public StringFilter lieuNaissance() {
        if (lieuNaissance == null) {
            lieuNaissance = new StringFilter();
        }
        return lieuNaissance;
    }

    public void setLieuNaissance(StringFilter lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public StringFilter getNationalite() {
        return nationalite;
    }

    public StringFilter nationalite() {
        if (nationalite == null) {
            nationalite = new StringFilter();
        }
        return nationalite;
    }

    public void setNationalite(StringFilter nationalite) {
        this.nationalite = nationalite;
    }

    public StringFilter getTelephone() {
        return telephone;
    }

    public StringFilter telephone() {
        if (telephone == null) {
            telephone = new StringFilter();
        }
        return telephone;
    }

    public void setTelephone(StringFilter telephone) {
        this.telephone = telephone;
    }

    public StringFilter getAdresse() {
        return adresse;
    }

    public StringFilter adresse() {
        if (adresse == null) {
            adresse = new StringFilter();
        }
        return adresse;
    }

    public void setAdresse(StringFilter adresse) {
        this.adresse = adresse;
    }

    public InstantFilter getDateDece() {
        return dateDece;
    }

    public InstantFilter dateDece() {
        if (dateDece == null) {
            dateDece = new InstantFilter();
        }
        return dateDece;
    }

    public void setDateDece(InstantFilter dateDece) {
        this.dateDece = dateDece;
    }

    public StringFilter getCauseDece() {
        return causeDece;
    }

    public StringFilter causeDece() {
        if (causeDece == null) {
            causeDece = new StringFilter();
        }
        return causeDece;
    }

    public void setCauseDece(StringFilter causeDece) {
        this.causeDece = causeDece;
    }

    public LongFilter getDisponibiliteId() {
        return disponibiliteId;
    }

    public LongFilter disponibiliteId() {
        if (disponibiliteId == null) {
            disponibiliteId = new LongFilter();
        }
        return disponibiliteId;
    }

    public void setDisponibiliteId(LongFilter disponibiliteId) {
        this.disponibiliteId = disponibiliteId;
    }

    public LongFilter getMiseadispositionId() {
        return miseadispositionId;
    }

    public LongFilter miseadispositionId() {
        if (miseadispositionId == null) {
            miseadispositionId = new LongFilter();
        }
        return miseadispositionId;
    }

    public void setMiseadispositionId(LongFilter miseadispositionId) {
        this.miseadispositionId = miseadispositionId;
    }

    public LongFilter getDemandeDexplicationId() {
        return demandeDexplicationId;
    }

    public LongFilter demandeDexplicationId() {
        if (demandeDexplicationId == null) {
            demandeDexplicationId = new LongFilter();
        }
        return demandeDexplicationId;
    }

    public void setDemandeDexplicationId(LongFilter demandeDexplicationId) {
        this.demandeDexplicationId = demandeDexplicationId;
    }

    public LongFilter getPosteId() {
        return posteId;
    }

    public LongFilter posteId() {
        if (posteId == null) {
            posteId = new LongFilter();
        }
        return posteId;
    }

    public void setPosteId(LongFilter posteId) {
        this.posteId = posteId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AgentCriteria that = (AgentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(matricule, that.matricule) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(dateNaissance, that.dateNaissance) &&
            Objects.equals(lieuNaissance, that.lieuNaissance) &&
            Objects.equals(nationalite, that.nationalite) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(adresse, that.adresse) &&
            Objects.equals(dateDece, that.dateDece) &&
            Objects.equals(causeDece, that.causeDece) &&
            Objects.equals(disponibiliteId, that.disponibiliteId) &&
            Objects.equals(miseadispositionId, that.miseadispositionId) &&
            Objects.equals(demandeDexplicationId, that.demandeDexplicationId) &&
            Objects.equals(posteId, that.posteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            matricule,
            nom,
            prenom,
            dateNaissance,
            lieuNaissance,
            nationalite,
            telephone,
            adresse,
            dateDece,
            causeDece,
            disponibiliteId,
            miseadispositionId,
            demandeDexplicationId,
            posteId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (matricule != null ? "matricule=" + matricule + ", " : "") +
            (nom != null ? "nom=" + nom + ", " : "") +
            (prenom != null ? "prenom=" + prenom + ", " : "") +
            (dateNaissance != null ? "dateNaissance=" + dateNaissance + ", " : "") +
            (lieuNaissance != null ? "lieuNaissance=" + lieuNaissance + ", " : "") +
            (nationalite != null ? "nationalite=" + nationalite + ", " : "") +
            (telephone != null ? "telephone=" + telephone + ", " : "") +
            (adresse != null ? "adresse=" + adresse + ", " : "") +
            (dateDece != null ? "dateDece=" + dateDece + ", " : "") +
            (causeDece != null ? "causeDece=" + causeDece + ", " : "") +
            (disponibiliteId != null ? "disponibiliteId=" + disponibiliteId + ", " : "") +
            (miseadispositionId != null ? "miseadispositionId=" + miseadispositionId + ", " : "") +
            (demandeDexplicationId != null ? "demandeDexplicationId=" + demandeDexplicationId + ", " : "") +
            (posteId != null ? "posteId=" + posteId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

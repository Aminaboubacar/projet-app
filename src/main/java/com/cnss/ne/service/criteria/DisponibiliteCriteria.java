package com.cnss.ne.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.cnss.ne.domain.Disponibilite} entity. This class is used
 * in {@link com.cnss.ne.web.rest.DisponibiliteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /disponibilites?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DisponibiliteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private InstantFilter dateDebut;

    private InstantFilter dateFin;

    private InstantFilter dateRetour;

    private LongFilter motifDisponibiliteId;

    private LongFilter agentId;

    private Boolean distinct;

    public DisponibiliteCriteria() {}

    public DisponibiliteCriteria(DisponibiliteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.dateDebut = other.dateDebut == null ? null : other.dateDebut.copy();
        this.dateFin = other.dateFin == null ? null : other.dateFin.copy();
        this.dateRetour = other.dateRetour == null ? null : other.dateRetour.copy();
        this.motifDisponibiliteId = other.motifDisponibiliteId == null ? null : other.motifDisponibiliteId.copy();
        this.agentId = other.agentId == null ? null : other.agentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DisponibiliteCriteria copy() {
        return new DisponibiliteCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public InstantFilter getDateDebut() {
        return dateDebut;
    }

    public InstantFilter dateDebut() {
        if (dateDebut == null) {
            dateDebut = new InstantFilter();
        }
        return dateDebut;
    }

    public void setDateDebut(InstantFilter dateDebut) {
        this.dateDebut = dateDebut;
    }

    public InstantFilter getDateFin() {
        return dateFin;
    }

    public InstantFilter dateFin() {
        if (dateFin == null) {
            dateFin = new InstantFilter();
        }
        return dateFin;
    }

    public void setDateFin(InstantFilter dateFin) {
        this.dateFin = dateFin;
    }

    public InstantFilter getDateRetour() {
        return dateRetour;
    }

    public InstantFilter dateRetour() {
        if (dateRetour == null) {
            dateRetour = new InstantFilter();
        }
        return dateRetour;
    }

    public void setDateRetour(InstantFilter dateRetour) {
        this.dateRetour = dateRetour;
    }

    public LongFilter getMotifDisponibiliteId() {
        return motifDisponibiliteId;
    }

    public LongFilter motifDisponibiliteId() {
        if (motifDisponibiliteId == null) {
            motifDisponibiliteId = new LongFilter();
        }
        return motifDisponibiliteId;
    }

    public void setMotifDisponibiliteId(LongFilter motifDisponibiliteId) {
        this.motifDisponibiliteId = motifDisponibiliteId;
    }

    public LongFilter getAgentId() {
        return agentId;
    }

    public LongFilter agentId() {
        if (agentId == null) {
            agentId = new LongFilter();
        }
        return agentId;
    }

    public void setAgentId(LongFilter agentId) {
        this.agentId = agentId;
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
        final DisponibiliteCriteria that = (DisponibiliteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(dateFin, that.dateFin) &&
            Objects.equals(dateRetour, that.dateRetour) &&
            Objects.equals(motifDisponibiliteId, that.motifDisponibiliteId) &&
            Objects.equals(agentId, that.agentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, dateDebut, dateFin, dateRetour, motifDisponibiliteId, agentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DisponibiliteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (dateDebut != null ? "dateDebut=" + dateDebut + ", " : "") +
            (dateFin != null ? "dateFin=" + dateFin + ", " : "") +
            (dateRetour != null ? "dateRetour=" + dateRetour + ", " : "") +
            (motifDisponibiliteId != null ? "motifDisponibiliteId=" + motifDisponibiliteId + ", " : "") +
            (agentId != null ? "agentId=" + agentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

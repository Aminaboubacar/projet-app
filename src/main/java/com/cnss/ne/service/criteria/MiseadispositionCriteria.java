package com.cnss.ne.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.cnss.ne.domain.Miseadisposition} entity. This class is used
 * in {@link com.cnss.ne.web.rest.MiseadispositionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /miseadispositions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MiseadispositionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter organisme;

    private InstantFilter dateDebut;

    private InstantFilter dateFin;

    private StringFilter sensMouvement;

    private InstantFilter dateRetour;

    private LongFilter agentId;

    private Boolean distinct;

    public MiseadispositionCriteria() {}

    public MiseadispositionCriteria(MiseadispositionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.organisme = other.organisme == null ? null : other.organisme.copy();
        this.dateDebut = other.dateDebut == null ? null : other.dateDebut.copy();
        this.dateFin = other.dateFin == null ? null : other.dateFin.copy();
        this.sensMouvement = other.sensMouvement == null ? null : other.sensMouvement.copy();
        this.dateRetour = other.dateRetour == null ? null : other.dateRetour.copy();
        this.agentId = other.agentId == null ? null : other.agentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MiseadispositionCriteria copy() {
        return new MiseadispositionCriteria(this);
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

    public StringFilter getOrganisme() {
        return organisme;
    }

    public StringFilter organisme() {
        if (organisme == null) {
            organisme = new StringFilter();
        }
        return organisme;
    }

    public void setOrganisme(StringFilter organisme) {
        this.organisme = organisme;
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

    public StringFilter getSensMouvement() {
        return sensMouvement;
    }

    public StringFilter sensMouvement() {
        if (sensMouvement == null) {
            sensMouvement = new StringFilter();
        }
        return sensMouvement;
    }

    public void setSensMouvement(StringFilter sensMouvement) {
        this.sensMouvement = sensMouvement;
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
        final MiseadispositionCriteria that = (MiseadispositionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(organisme, that.organisme) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(dateFin, that.dateFin) &&
            Objects.equals(sensMouvement, that.sensMouvement) &&
            Objects.equals(dateRetour, that.dateRetour) &&
            Objects.equals(agentId, that.agentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, organisme, dateDebut, dateFin, sensMouvement, dateRetour, agentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MiseadispositionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (organisme != null ? "organisme=" + organisme + ", " : "") +
            (dateDebut != null ? "dateDebut=" + dateDebut + ", " : "") +
            (dateFin != null ? "dateFin=" + dateFin + ", " : "") +
            (sensMouvement != null ? "sensMouvement=" + sensMouvement + ", " : "") +
            (dateRetour != null ? "dateRetour=" + dateRetour + ", " : "") +
            (agentId != null ? "agentId=" + agentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

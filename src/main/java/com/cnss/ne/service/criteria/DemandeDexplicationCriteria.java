package com.cnss.ne.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.cnss.ne.domain.DemandeDexplication} entity. This class is used
 * in {@link com.cnss.ne.web.rest.DemandeDexplicationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /demande-dexplications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DemandeDexplicationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private InstantFilter date;

    private StringFilter objet;

    private StringFilter appDg;

    private InstantFilter datappDg;

    private LongFilter sanctionnerId;

    private LongFilter agentId;

    private Boolean distinct;

    public DemandeDexplicationCriteria() {}

    public DemandeDexplicationCriteria(DemandeDexplicationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.objet = other.objet == null ? null : other.objet.copy();
        this.appDg = other.appDg == null ? null : other.appDg.copy();
        this.datappDg = other.datappDg == null ? null : other.datappDg.copy();
        this.sanctionnerId = other.sanctionnerId == null ? null : other.sanctionnerId.copy();
        this.agentId = other.agentId == null ? null : other.agentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DemandeDexplicationCriteria copy() {
        return new DemandeDexplicationCriteria(this);
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

    public InstantFilter getDate() {
        return date;
    }

    public InstantFilter date() {
        if (date == null) {
            date = new InstantFilter();
        }
        return date;
    }

    public void setDate(InstantFilter date) {
        this.date = date;
    }

    public StringFilter getObjet() {
        return objet;
    }

    public StringFilter objet() {
        if (objet == null) {
            objet = new StringFilter();
        }
        return objet;
    }

    public void setObjet(StringFilter objet) {
        this.objet = objet;
    }

    public StringFilter getAppDg() {
        return appDg;
    }

    public StringFilter appDg() {
        if (appDg == null) {
            appDg = new StringFilter();
        }
        return appDg;
    }

    public void setAppDg(StringFilter appDg) {
        this.appDg = appDg;
    }

    public InstantFilter getDatappDg() {
        return datappDg;
    }

    public InstantFilter datappDg() {
        if (datappDg == null) {
            datappDg = new InstantFilter();
        }
        return datappDg;
    }

    public void setDatappDg(InstantFilter datappDg) {
        this.datappDg = datappDg;
    }

    public LongFilter getSanctionnerId() {
        return sanctionnerId;
    }

    public LongFilter sanctionnerId() {
        if (sanctionnerId == null) {
            sanctionnerId = new LongFilter();
        }
        return sanctionnerId;
    }

    public void setSanctionnerId(LongFilter sanctionnerId) {
        this.sanctionnerId = sanctionnerId;
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
        final DemandeDexplicationCriteria that = (DemandeDexplicationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(date, that.date) &&
            Objects.equals(objet, that.objet) &&
            Objects.equals(appDg, that.appDg) &&
            Objects.equals(datappDg, that.datappDg) &&
            Objects.equals(sanctionnerId, that.sanctionnerId) &&
            Objects.equals(agentId, that.agentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, date, objet, appDg, datappDg, sanctionnerId, agentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemandeDexplicationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (objet != null ? "objet=" + objet + ", " : "") +
            (appDg != null ? "appDg=" + appDg + ", " : "") +
            (datappDg != null ? "datappDg=" + datappDg + ", " : "") +
            (sanctionnerId != null ? "sanctionnerId=" + sanctionnerId + ", " : "") +
            (agentId != null ? "agentId=" + agentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

package com.cnss.ne.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.cnss.ne.domain.Sanctionner} entity. This class is used
 * in {@link com.cnss.ne.web.rest.SanctionnerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sanctionners?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SanctionnerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter date;

    private LongFilter sanctionId;

    private LongFilter demandeDexplicationId;

    private Boolean distinct;

    public SanctionnerCriteria() {}

    public SanctionnerCriteria(SanctionnerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.sanctionId = other.sanctionId == null ? null : other.sanctionId.copy();
        this.demandeDexplicationId = other.demandeDexplicationId == null ? null : other.demandeDexplicationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SanctionnerCriteria copy() {
        return new SanctionnerCriteria(this);
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

    public LongFilter getSanctionId() {
        return sanctionId;
    }

    public LongFilter sanctionId() {
        if (sanctionId == null) {
            sanctionId = new LongFilter();
        }
        return sanctionId;
    }

    public void setSanctionId(LongFilter sanctionId) {
        this.sanctionId = sanctionId;
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
        final SanctionnerCriteria that = (SanctionnerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(sanctionId, that.sanctionId) &&
            Objects.equals(demandeDexplicationId, that.demandeDexplicationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, sanctionId, demandeDexplicationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SanctionnerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (sanctionId != null ? "sanctionId=" + sanctionId + ", " : "") +
            (demandeDexplicationId != null ? "demandeDexplicationId=" + demandeDexplicationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

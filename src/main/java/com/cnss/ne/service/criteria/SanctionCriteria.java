package com.cnss.ne.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.cnss.ne.domain.Sanction} entity. This class is used
 * in {@link com.cnss.ne.web.rest.SanctionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sanctions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SanctionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter libelle;

    private LongFilter sanctionnerId;

    private LongFilter degreId;

    private Boolean distinct;

    public SanctionCriteria() {}

    public SanctionCriteria(SanctionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.libelle = other.libelle == null ? null : other.libelle.copy();
        this.sanctionnerId = other.sanctionnerId == null ? null : other.sanctionnerId.copy();
        this.degreId = other.degreId == null ? null : other.degreId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SanctionCriteria copy() {
        return new SanctionCriteria(this);
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

    public StringFilter getLibelle() {
        return libelle;
    }

    public StringFilter libelle() {
        if (libelle == null) {
            libelle = new StringFilter();
        }
        return libelle;
    }

    public void setLibelle(StringFilter libelle) {
        this.libelle = libelle;
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

    public LongFilter getDegreId() {
        return degreId;
    }

    public LongFilter degreId() {
        if (degreId == null) {
            degreId = new LongFilter();
        }
        return degreId;
    }

    public void setDegreId(LongFilter degreId) {
        this.degreId = degreId;
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
        final SanctionCriteria that = (SanctionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(sanctionnerId, that.sanctionnerId) &&
            Objects.equals(degreId, that.degreId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, libelle, sanctionnerId, degreId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SanctionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (libelle != null ? "libelle=" + libelle + ", " : "") +
            (sanctionnerId != null ? "sanctionnerId=" + sanctionnerId + ", " : "") +
            (degreId != null ? "degreId=" + degreId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

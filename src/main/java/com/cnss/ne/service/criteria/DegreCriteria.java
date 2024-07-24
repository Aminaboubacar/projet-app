package com.cnss.ne.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.cnss.ne.domain.Degre} entity. This class is used
 * in {@link com.cnss.ne.web.rest.DegreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /degres?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DegreCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter libelle;

    private LongFilter sanctionId;

    private Boolean distinct;

    public DegreCriteria() {}

    public DegreCriteria(DegreCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.libelle = other.libelle == null ? null : other.libelle.copy();
        this.sanctionId = other.sanctionId == null ? null : other.sanctionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DegreCriteria copy() {
        return new DegreCriteria(this);
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
        final DegreCriteria that = (DegreCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(sanctionId, that.sanctionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, libelle, sanctionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DegreCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (libelle != null ? "libelle=" + libelle + ", " : "") +
            (sanctionId != null ? "sanctionId=" + sanctionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

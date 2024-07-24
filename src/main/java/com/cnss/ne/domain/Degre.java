package com.cnss.ne.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Degre.
 */
@Entity
@Table(name = "degre")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Degre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "degre")
    @JsonIgnoreProperties(value = { "sanctionners", "degre" }, allowSetters = true)
    private Set<Sanction> sanctions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Degre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Degre code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Degre libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Set<Sanction> getSanctions() {
        return this.sanctions;
    }

    public void setSanctions(Set<Sanction> sanctions) {
        if (this.sanctions != null) {
            this.sanctions.forEach(i -> i.setDegre(null));
        }
        if (sanctions != null) {
            sanctions.forEach(i -> i.setDegre(this));
        }
        this.sanctions = sanctions;
    }

    public Degre sanctions(Set<Sanction> sanctions) {
        this.setSanctions(sanctions);
        return this;
    }

    public Degre addSanction(Sanction sanction) {
        this.sanctions.add(sanction);
        sanction.setDegre(this);
        return this;
    }

    public Degre removeSanction(Sanction sanction) {
        this.sanctions.remove(sanction);
        sanction.setDegre(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Degre)) {
            return false;
        }
        return getId() != null && getId().equals(((Degre) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Degre{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}

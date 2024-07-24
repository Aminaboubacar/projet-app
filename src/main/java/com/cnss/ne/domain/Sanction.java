package com.cnss.ne.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Sanction.
 */
@Entity
@Table(name = "sanction")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Sanction implements Serializable {

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sanction")
    @JsonIgnoreProperties(value = { "sanction", "demandeDexplication" }, allowSetters = true)
    private Set<Sanctionner> sanctionners = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sanctions" }, allowSetters = true)
    private Degre degre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sanction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Sanction code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Sanction libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Set<Sanctionner> getSanctionners() {
        return this.sanctionners;
    }

    public void setSanctionners(Set<Sanctionner> sanctionners) {
        if (this.sanctionners != null) {
            this.sanctionners.forEach(i -> i.setSanction(null));
        }
        if (sanctionners != null) {
            sanctionners.forEach(i -> i.setSanction(this));
        }
        this.sanctionners = sanctionners;
    }

    public Sanction sanctionners(Set<Sanctionner> sanctionners) {
        this.setSanctionners(sanctionners);
        return this;
    }

    public Sanction addSanctionner(Sanctionner sanctionner) {
        this.sanctionners.add(sanctionner);
        sanctionner.setSanction(this);
        return this;
    }

    public Sanction removeSanctionner(Sanctionner sanctionner) {
        this.sanctionners.remove(sanctionner);
        sanctionner.setSanction(null);
        return this;
    }

    public Degre getDegre() {
        return this.degre;
    }

    public void setDegre(Degre degre) {
        this.degre = degre;
    }

    public Sanction degre(Degre degre) {
        this.setDegre(degre);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sanction)) {
            return false;
        }
        return getId() != null && getId().equals(((Sanction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sanction{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}

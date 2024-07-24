package com.cnss.ne.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A MotifDisponibilite.
 */
@Entity
@Table(name = "motif_disponibilite")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MotifDisponibilite implements Serializable {

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "motifDisponibilite")
    @JsonIgnoreProperties(value = { "motifDisponibilite", "agent" }, allowSetters = true)
    private Set<Disponibilite> disponibilites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MotifDisponibilite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public MotifDisponibilite code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public MotifDisponibilite libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Set<Disponibilite> getDisponibilites() {
        return this.disponibilites;
    }

    public void setDisponibilites(Set<Disponibilite> disponibilites) {
        if (this.disponibilites != null) {
            this.disponibilites.forEach(i -> i.setMotifDisponibilite(null));
        }
        if (disponibilites != null) {
            disponibilites.forEach(i -> i.setMotifDisponibilite(this));
        }
        this.disponibilites = disponibilites;
    }

    public MotifDisponibilite disponibilites(Set<Disponibilite> disponibilites) {
        this.setDisponibilites(disponibilites);
        return this;
    }

    public MotifDisponibilite addDisponibilite(Disponibilite disponibilite) {
        this.disponibilites.add(disponibilite);
        disponibilite.setMotifDisponibilite(this);
        return this;
    }

    public MotifDisponibilite removeDisponibilite(Disponibilite disponibilite) {
        this.disponibilites.remove(disponibilite);
        disponibilite.setMotifDisponibilite(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MotifDisponibilite)) {
            return false;
        }
        return getId() != null && getId().equals(((MotifDisponibilite) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MotifDisponibilite{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}

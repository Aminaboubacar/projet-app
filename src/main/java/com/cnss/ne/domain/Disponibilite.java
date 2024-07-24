package com.cnss.ne.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Disponibilite.
 */
@Entity
@Table(name = "disponibilite")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Disponibilite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "date_debut")
    private Instant dateDebut;

    @Column(name = "date_fin")
    private Instant dateFin;

    @Column(name = "date_retour")
    private Instant dateRetour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "disponibilites" }, allowSetters = true)
    private MotifDisponibilite motifDisponibilite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "disponibilites", "miseadispositions", "demandeDexplications", "poste" }, allowSetters = true)
    private Agent agent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Disponibilite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Disponibilite code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getDateDebut() {
        return this.dateDebut;
    }

    public Disponibilite dateDebut(Instant dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return this.dateFin;
    }

    public Disponibilite dateFin(Instant dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
    }

    public Instant getDateRetour() {
        return this.dateRetour;
    }

    public Disponibilite dateRetour(Instant dateRetour) {
        this.setDateRetour(dateRetour);
        return this;
    }

    public void setDateRetour(Instant dateRetour) {
        this.dateRetour = dateRetour;
    }

    public MotifDisponibilite getMotifDisponibilite() {
        return this.motifDisponibilite;
    }

    public void setMotifDisponibilite(MotifDisponibilite motifDisponibilite) {
        this.motifDisponibilite = motifDisponibilite;
    }

    public Disponibilite motifDisponibilite(MotifDisponibilite motifDisponibilite) {
        this.setMotifDisponibilite(motifDisponibilite);
        return this;
    }

    public Agent getAgent() {
        return this.agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Disponibilite agent(Agent agent) {
        this.setAgent(agent);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Disponibilite)) {
            return false;
        }
        return getId() != null && getId().equals(((Disponibilite) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Disponibilite{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", dateRetour='" + getDateRetour() + "'" +
            "}";
    }
}

package com.cnss.ne.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Miseadisposition.
 */
@Entity
@Table(name = "miseadisposition")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Miseadisposition implements Serializable {

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
    @Column(name = "organisme", nullable = false)
    private String organisme;

    @Column(name = "date_debut")
    private Instant dateDebut;

    @Column(name = "date_fin")
    private Instant dateFin;

    @Column(name = "sens_mouvement")
    private String sensMouvement;

    @Column(name = "date_retour")
    private Instant dateRetour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "disponibilites", "miseadispositions", "demandeDexplications", "poste" }, allowSetters = true)
    private Agent agent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Miseadisposition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Miseadisposition code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrganisme() {
        return this.organisme;
    }

    public Miseadisposition organisme(String organisme) {
        this.setOrganisme(organisme);
        return this;
    }

    public void setOrganisme(String organisme) {
        this.organisme = organisme;
    }

    public Instant getDateDebut() {
        return this.dateDebut;
    }

    public Miseadisposition dateDebut(Instant dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return this.dateFin;
    }

    public Miseadisposition dateFin(Instant dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
    }

    public String getSensMouvement() {
        return this.sensMouvement;
    }

    public Miseadisposition sensMouvement(String sensMouvement) {
        this.setSensMouvement(sensMouvement);
        return this;
    }

    public void setSensMouvement(String sensMouvement) {
        this.sensMouvement = sensMouvement;
    }

    public Instant getDateRetour() {
        return this.dateRetour;
    }

    public Miseadisposition dateRetour(Instant dateRetour) {
        this.setDateRetour(dateRetour);
        return this;
    }

    public void setDateRetour(Instant dateRetour) {
        this.dateRetour = dateRetour;
    }

    public Agent getAgent() {
        return this.agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Miseadisposition agent(Agent agent) {
        this.setAgent(agent);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Miseadisposition)) {
            return false;
        }
        return getId() != null && getId().equals(((Miseadisposition) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Miseadisposition{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", organisme='" + getOrganisme() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", sensMouvement='" + getSensMouvement() + "'" +
            ", dateRetour='" + getDateRetour() + "'" +
            "}";
    }
}

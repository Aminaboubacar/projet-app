package com.cnss.ne.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Agent.
 */
@Entity
@Table(name = "agent")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Agent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "matricule", nullable = false)
    private String matricule;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "date_naissance")
    private Instant dateNaissance;

    @Column(name = "lieu_naissance")
    private String lieuNaissance;

    @Column(name = "nationalite")
    private String nationalite;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "date_dece")
    private Instant dateDece;

    @Column(name = "cause_dece")
    private String causeDece;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agent")
    @JsonIgnoreProperties(value = { "motifDisponibilite", "agent" }, allowSetters = true)
    private Set<Disponibilite> disponibilites = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agent")
    @JsonIgnoreProperties(value = { "agent" }, allowSetters = true)
    private Set<Miseadisposition> miseadispositions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agent")
    @JsonIgnoreProperties(value = { "sanctionners", "agent" }, allowSetters = true)
    private Set<DemandeDexplication> demandeDexplications = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "agents" }, allowSetters = true)
    private Poste poste;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Agent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricule() {
        return this.matricule;
    }

    public Agent matricule(String matricule) {
        this.setMatricule(matricule);
        return this;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return this.nom;
    }

    public Agent nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Agent prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Instant getDateNaissance() {
        return this.dateNaissance;
    }

    public Agent dateNaissance(Instant dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(Instant dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getLieuNaissance() {
        return this.lieuNaissance;
    }

    public Agent lieuNaissance(String lieuNaissance) {
        this.setLieuNaissance(lieuNaissance);
        return this;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public String getNationalite() {
        return this.nationalite;
    }

    public Agent nationalite(String nationalite) {
        this.setNationalite(nationalite);
        return this;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Agent telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Agent adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Instant getDateDece() {
        return this.dateDece;
    }

    public Agent dateDece(Instant dateDece) {
        this.setDateDece(dateDece);
        return this;
    }

    public void setDateDece(Instant dateDece) {
        this.dateDece = dateDece;
    }

    public String getCauseDece() {
        return this.causeDece;
    }

    public Agent causeDece(String causeDece) {
        this.setCauseDece(causeDece);
        return this;
    }

    public void setCauseDece(String causeDece) {
        this.causeDece = causeDece;
    }

    public Set<Disponibilite> getDisponibilites() {
        return this.disponibilites;
    }

    public void setDisponibilites(Set<Disponibilite> disponibilites) {
        if (this.disponibilites != null) {
            this.disponibilites.forEach(i -> i.setAgent(null));
        }
        if (disponibilites != null) {
            disponibilites.forEach(i -> i.setAgent(this));
        }
        this.disponibilites = disponibilites;
    }

    public Agent disponibilites(Set<Disponibilite> disponibilites) {
        this.setDisponibilites(disponibilites);
        return this;
    }

    public Agent addDisponibilite(Disponibilite disponibilite) {
        this.disponibilites.add(disponibilite);
        disponibilite.setAgent(this);
        return this;
    }

    public Agent removeDisponibilite(Disponibilite disponibilite) {
        this.disponibilites.remove(disponibilite);
        disponibilite.setAgent(null);
        return this;
    }

    public Set<Miseadisposition> getMiseadispositions() {
        return this.miseadispositions;
    }

    public void setMiseadispositions(Set<Miseadisposition> miseadispositions) {
        if (this.miseadispositions != null) {
            this.miseadispositions.forEach(i -> i.setAgent(null));
        }
        if (miseadispositions != null) {
            miseadispositions.forEach(i -> i.setAgent(this));
        }
        this.miseadispositions = miseadispositions;
    }

    public Agent miseadispositions(Set<Miseadisposition> miseadispositions) {
        this.setMiseadispositions(miseadispositions);
        return this;
    }

    public Agent addMiseadisposition(Miseadisposition miseadisposition) {
        this.miseadispositions.add(miseadisposition);
        miseadisposition.setAgent(this);
        return this;
    }

    public Agent removeMiseadisposition(Miseadisposition miseadisposition) {
        this.miseadispositions.remove(miseadisposition);
        miseadisposition.setAgent(null);
        return this;
    }

    public Set<DemandeDexplication> getDemandeDexplications() {
        return this.demandeDexplications;
    }

    public void setDemandeDexplications(Set<DemandeDexplication> demandeDexplications) {
        if (this.demandeDexplications != null) {
            this.demandeDexplications.forEach(i -> i.setAgent(null));
        }
        if (demandeDexplications != null) {
            demandeDexplications.forEach(i -> i.setAgent(this));
        }
        this.demandeDexplications = demandeDexplications;
    }

    public Agent demandeDexplications(Set<DemandeDexplication> demandeDexplications) {
        this.setDemandeDexplications(demandeDexplications);
        return this;
    }

    public Agent addDemandeDexplication(DemandeDexplication demandeDexplication) {
        this.demandeDexplications.add(demandeDexplication);
        demandeDexplication.setAgent(this);
        return this;
    }

    public Agent removeDemandeDexplication(DemandeDexplication demandeDexplication) {
        this.demandeDexplications.remove(demandeDexplication);
        demandeDexplication.setAgent(null);
        return this;
    }

    public Poste getPoste() {
        return this.poste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }

    public Agent poste(Poste poste) {
        this.setPoste(poste);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Agent)) {
            return false;
        }
        return getId() != null && getId().equals(((Agent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Agent{" +
            "id=" + getId() +
            ", matricule='" + getMatricule() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", lieuNaissance='" + getLieuNaissance() + "'" +
            ", nationalite='" + getNationalite() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", dateDece='" + getDateDece() + "'" +
            ", causeDece='" + getCauseDece() + "'" +
            "}";
    }
}

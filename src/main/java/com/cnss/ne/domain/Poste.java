package com.cnss.ne.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Poste.
 */
@Entity
@Table(name = "poste")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Poste implements Serializable {

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
    @Size(max = 30)
    @Column(name = "libelle", length = 30, nullable = false)
    private String libelle;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "poste")
    @JsonIgnoreProperties(value = { "disponibilites", "miseadispositions", "demandeDexplications", "poste" }, allowSetters = true)
    private Set<Agent> agents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Poste id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Poste code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Poste libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Set<Agent> getAgents() {
        return this.agents;
    }

    public void setAgents(Set<Agent> agents) {
        if (this.agents != null) {
            this.agents.forEach(i -> i.setPoste(null));
        }
        if (agents != null) {
            agents.forEach(i -> i.setPoste(this));
        }
        this.agents = agents;
    }

    public Poste agents(Set<Agent> agents) {
        this.setAgents(agents);
        return this;
    }

    public Poste addAgent(Agent agent) {
        this.agents.add(agent);
        agent.setPoste(this);
        return this;
    }

    public Poste removeAgent(Agent agent) {
        this.agents.remove(agent);
        agent.setPoste(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Poste)) {
            return false;
        }
        return getId() != null && getId().equals(((Poste) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Poste{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}

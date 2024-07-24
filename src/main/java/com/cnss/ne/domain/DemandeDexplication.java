package com.cnss.ne.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A DemandeDexplication.
 */
@Entity
@Table(name = "demande_dexplication")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DemandeDexplication implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "date")
    private Instant date;

    @NotNull
    @Column(name = "objet", nullable = false)
    private String objet;

    @Column(name = "app_dg")
    private String appDg;

    @Column(name = "datapp_dg")
    private Instant datappDg;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "demandeDexplication")
    @JsonIgnoreProperties(value = { "sanction", "demandeDexplication" }, allowSetters = true)
    private Set<Sanctionner> sanctionners = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "disponibilites", "miseadispositions", "demandeDexplications", "poste" }, allowSetters = true)
    private Agent agent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DemandeDexplication id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public DemandeDexplication code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getDate() {
        return this.date;
    }

    public DemandeDexplication date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getObjet() {
        return this.objet;
    }

    public DemandeDexplication objet(String objet) {
        this.setObjet(objet);
        return this;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getAppDg() {
        return this.appDg;
    }

    public DemandeDexplication appDg(String appDg) {
        this.setAppDg(appDg);
        return this;
    }

    public void setAppDg(String appDg) {
        this.appDg = appDg;
    }

    public Instant getDatappDg() {
        return this.datappDg;
    }

    public DemandeDexplication datappDg(Instant datappDg) {
        this.setDatappDg(datappDg);
        return this;
    }

    public void setDatappDg(Instant datappDg) {
        this.datappDg = datappDg;
    }

    public Set<Sanctionner> getSanctionners() {
        return this.sanctionners;
    }

    public void setSanctionners(Set<Sanctionner> sanctionners) {
        if (this.sanctionners != null) {
            this.sanctionners.forEach(i -> i.setDemandeDexplication(null));
        }
        if (sanctionners != null) {
            sanctionners.forEach(i -> i.setDemandeDexplication(this));
        }
        this.sanctionners = sanctionners;
    }

    public DemandeDexplication sanctionners(Set<Sanctionner> sanctionners) {
        this.setSanctionners(sanctionners);
        return this;
    }

    public DemandeDexplication addSanctionner(Sanctionner sanctionner) {
        this.sanctionners.add(sanctionner);
        sanctionner.setDemandeDexplication(this);
        return this;
    }

    public DemandeDexplication removeSanctionner(Sanctionner sanctionner) {
        this.sanctionners.remove(sanctionner);
        sanctionner.setDemandeDexplication(null);
        return this;
    }

    public Agent getAgent() {
        return this.agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public DemandeDexplication agent(Agent agent) {
        this.setAgent(agent);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemandeDexplication)) {
            return false;
        }
        return getId() != null && getId().equals(((DemandeDexplication) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemandeDexplication{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", date='" + getDate() + "'" +
            ", objet='" + getObjet() + "'" +
            ", appDg='" + getAppDg() + "'" +
            ", datappDg='" + getDatappDg() + "'" +
            "}";
    }
}

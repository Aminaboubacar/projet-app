package com.cnss.ne.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Sanctionner.
 */
@Entity
@Table(name = "sanctionner")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Sanctionner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private Instant date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sanctionners", "degre" }, allowSetters = true)
    private Sanction sanction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sanctionners", "agent" }, allowSetters = true)
    private DemandeDexplication demandeDexplication;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sanctionner id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public Sanctionner date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Sanction getSanction() {
        return this.sanction;
    }

    public void setSanction(Sanction sanction) {
        this.sanction = sanction;
    }

    public Sanctionner sanction(Sanction sanction) {
        this.setSanction(sanction);
        return this;
    }

    public DemandeDexplication getDemandeDexplication() {
        return this.demandeDexplication;
    }

    public void setDemandeDexplication(DemandeDexplication demandeDexplication) {
        this.demandeDexplication = demandeDexplication;
    }

    public Sanctionner demandeDexplication(DemandeDexplication demandeDexplication) {
        this.setDemandeDexplication(demandeDexplication);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sanctionner)) {
            return false;
        }
        return getId() != null && getId().equals(((Sanctionner) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sanctionner{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}

package com.cnss.ne.service;

import com.cnss.ne.domain.*; // for static metamodels
import com.cnss.ne.domain.Disponibilite;
import com.cnss.ne.repository.DisponibiliteRepository;
import com.cnss.ne.service.criteria.DisponibiliteCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Disponibilite} entities in the database.
 * The main input is a {@link DisponibiliteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Disponibilite} or a {@link Page} of {@link Disponibilite} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DisponibiliteQueryService extends QueryService<Disponibilite> {

    private final Logger log = LoggerFactory.getLogger(DisponibiliteQueryService.class);

    private final DisponibiliteRepository disponibiliteRepository;

    public DisponibiliteQueryService(DisponibiliteRepository disponibiliteRepository) {
        this.disponibiliteRepository = disponibiliteRepository;
    }

    /**
     * Return a {@link List} of {@link Disponibilite} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Disponibilite> findByCriteria(DisponibiliteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Disponibilite> specification = createSpecification(criteria);
        return disponibiliteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Disponibilite} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Disponibilite> findByCriteria(DisponibiliteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Disponibilite> specification = createSpecification(criteria);
        return disponibiliteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DisponibiliteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Disponibilite> specification = createSpecification(criteria);
        return disponibiliteRepository.count(specification);
    }

    /**
     * Function to convert {@link DisponibiliteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Disponibilite> createSpecification(DisponibiliteCriteria criteria) {
        Specification<Disponibilite> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Disponibilite_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Disponibilite_.code));
            }
            if (criteria.getDateDebut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDebut(), Disponibilite_.dateDebut));
            }
            if (criteria.getDateFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFin(), Disponibilite_.dateFin));
            }
            if (criteria.getDateRetour() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateRetour(), Disponibilite_.dateRetour));
            }
            if (criteria.getMotifDisponibiliteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMotifDisponibiliteId(),
                            root -> root.join(Disponibilite_.motifDisponibilite, JoinType.LEFT).get(MotifDisponibilite_.id)
                        )
                    );
            }
            if (criteria.getAgentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAgentId(), root -> root.join(Disponibilite_.agent, JoinType.LEFT).get(Agent_.id))
                    );
            }
        }
        return specification;
    }
}

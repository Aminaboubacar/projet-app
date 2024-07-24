package com.cnss.ne.service;

import com.cnss.ne.domain.*; // for static metamodels
import com.cnss.ne.domain.Miseadisposition;
import com.cnss.ne.repository.MiseadispositionRepository;
import com.cnss.ne.service.criteria.MiseadispositionCriteria;
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
 * Service for executing complex queries for {@link Miseadisposition} entities in the database.
 * The main input is a {@link MiseadispositionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Miseadisposition} or a {@link Page} of {@link Miseadisposition} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MiseadispositionQueryService extends QueryService<Miseadisposition> {

    private final Logger log = LoggerFactory.getLogger(MiseadispositionQueryService.class);

    private final MiseadispositionRepository miseadispositionRepository;

    public MiseadispositionQueryService(MiseadispositionRepository miseadispositionRepository) {
        this.miseadispositionRepository = miseadispositionRepository;
    }

    /**
     * Return a {@link List} of {@link Miseadisposition} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Miseadisposition> findByCriteria(MiseadispositionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Miseadisposition> specification = createSpecification(criteria);
        return miseadispositionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Miseadisposition} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Miseadisposition> findByCriteria(MiseadispositionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Miseadisposition> specification = createSpecification(criteria);
        return miseadispositionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MiseadispositionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Miseadisposition> specification = createSpecification(criteria);
        return miseadispositionRepository.count(specification);
    }

    /**
     * Function to convert {@link MiseadispositionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Miseadisposition> createSpecification(MiseadispositionCriteria criteria) {
        Specification<Miseadisposition> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Miseadisposition_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Miseadisposition_.code));
            }
            if (criteria.getOrganisme() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrganisme(), Miseadisposition_.organisme));
            }
            if (criteria.getDateDebut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDebut(), Miseadisposition_.dateDebut));
            }
            if (criteria.getDateFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFin(), Miseadisposition_.dateFin));
            }
            if (criteria.getSensMouvement() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSensMouvement(), Miseadisposition_.sensMouvement));
            }
            if (criteria.getDateRetour() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateRetour(), Miseadisposition_.dateRetour));
            }
            if (criteria.getAgentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAgentId(), root -> root.join(Miseadisposition_.agent, JoinType.LEFT).get(Agent_.id))
                    );
            }
        }
        return specification;
    }
}

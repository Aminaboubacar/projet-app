package com.cnss.ne.service;

import com.cnss.ne.domain.*; // for static metamodels
import com.cnss.ne.domain.Sanction;
import com.cnss.ne.repository.SanctionRepository;
import com.cnss.ne.service.criteria.SanctionCriteria;
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
 * Service for executing complex queries for {@link Sanction} entities in the database.
 * The main input is a {@link SanctionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Sanction} or a {@link Page} of {@link Sanction} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SanctionQueryService extends QueryService<Sanction> {

    private final Logger log = LoggerFactory.getLogger(SanctionQueryService.class);

    private final SanctionRepository sanctionRepository;

    public SanctionQueryService(SanctionRepository sanctionRepository) {
        this.sanctionRepository = sanctionRepository;
    }

    /**
     * Return a {@link List} of {@link Sanction} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Sanction> findByCriteria(SanctionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Sanction> specification = createSpecification(criteria);
        return sanctionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Sanction} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Sanction> findByCriteria(SanctionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Sanction> specification = createSpecification(criteria);
        return sanctionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SanctionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Sanction> specification = createSpecification(criteria);
        return sanctionRepository.count(specification);
    }

    /**
     * Function to convert {@link SanctionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Sanction> createSpecification(SanctionCriteria criteria) {
        Specification<Sanction> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Sanction_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Sanction_.code));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Sanction_.libelle));
            }
            if (criteria.getSanctionnerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSanctionnerId(),
                            root -> root.join(Sanction_.sanctionners, JoinType.LEFT).get(Sanctionner_.id)
                        )
                    );
            }
            if (criteria.getDegreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDegreId(), root -> root.join(Sanction_.degre, JoinType.LEFT).get(Degre_.id))
                    );
            }
        }
        return specification;
    }
}

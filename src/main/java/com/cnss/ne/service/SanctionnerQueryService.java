package com.cnss.ne.service;

import com.cnss.ne.domain.*; // for static metamodels
import com.cnss.ne.domain.Sanctionner;
import com.cnss.ne.repository.SanctionnerRepository;
import com.cnss.ne.service.criteria.SanctionnerCriteria;
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
 * Service for executing complex queries for {@link Sanctionner} entities in the database.
 * The main input is a {@link SanctionnerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Sanctionner} or a {@link Page} of {@link Sanctionner} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SanctionnerQueryService extends QueryService<Sanctionner> {

    private final Logger log = LoggerFactory.getLogger(SanctionnerQueryService.class);

    private final SanctionnerRepository sanctionnerRepository;

    public SanctionnerQueryService(SanctionnerRepository sanctionnerRepository) {
        this.sanctionnerRepository = sanctionnerRepository;
    }

    /**
     * Return a {@link List} of {@link Sanctionner} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Sanctionner> findByCriteria(SanctionnerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Sanctionner> specification = createSpecification(criteria);
        return sanctionnerRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Sanctionner} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Sanctionner> findByCriteria(SanctionnerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Sanctionner> specification = createSpecification(criteria);
        return sanctionnerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SanctionnerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Sanctionner> specification = createSpecification(criteria);
        return sanctionnerRepository.count(specification);
    }

    /**
     * Function to convert {@link SanctionnerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Sanctionner> createSpecification(SanctionnerCriteria criteria) {
        Specification<Sanctionner> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Sanctionner_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Sanctionner_.date));
            }
            if (criteria.getSanctionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSanctionId(),
                            root -> root.join(Sanctionner_.sanction, JoinType.LEFT).get(Sanction_.id)
                        )
                    );
            }
            if (criteria.getDemandeDexplicationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDemandeDexplicationId(),
                            root -> root.join(Sanctionner_.demandeDexplication, JoinType.LEFT).get(DemandeDexplication_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

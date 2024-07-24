package com.cnss.ne.service;

import com.cnss.ne.domain.*; // for static metamodels
import com.cnss.ne.domain.DemandeDexplication;
import com.cnss.ne.repository.DemandeDexplicationRepository;
import com.cnss.ne.service.criteria.DemandeDexplicationCriteria;
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
 * Service for executing complex queries for {@link DemandeDexplication} entities in the database.
 * The main input is a {@link DemandeDexplicationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DemandeDexplication} or a {@link Page} of {@link DemandeDexplication} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DemandeDexplicationQueryService extends QueryService<DemandeDexplication> {

    private final Logger log = LoggerFactory.getLogger(DemandeDexplicationQueryService.class);

    private final DemandeDexplicationRepository demandeDexplicationRepository;

    public DemandeDexplicationQueryService(DemandeDexplicationRepository demandeDexplicationRepository) {
        this.demandeDexplicationRepository = demandeDexplicationRepository;
    }

    /**
     * Return a {@link List} of {@link DemandeDexplication} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DemandeDexplication> findByCriteria(DemandeDexplicationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DemandeDexplication> specification = createSpecification(criteria);
        return demandeDexplicationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DemandeDexplication} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DemandeDexplication> findByCriteria(DemandeDexplicationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DemandeDexplication> specification = createSpecification(criteria);
        return demandeDexplicationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DemandeDexplicationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DemandeDexplication> specification = createSpecification(criteria);
        return demandeDexplicationRepository.count(specification);
    }

    /**
     * Function to convert {@link DemandeDexplicationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DemandeDexplication> createSpecification(DemandeDexplicationCriteria criteria) {
        Specification<DemandeDexplication> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DemandeDexplication_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), DemandeDexplication_.code));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), DemandeDexplication_.date));
            }
            if (criteria.getObjet() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObjet(), DemandeDexplication_.objet));
            }
            if (criteria.getAppDg() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAppDg(), DemandeDexplication_.appDg));
            }
            if (criteria.getDatappDg() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatappDg(), DemandeDexplication_.datappDg));
            }
            if (criteria.getSanctionnerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSanctionnerId(),
                            root -> root.join(DemandeDexplication_.sanctionners, JoinType.LEFT).get(Sanctionner_.id)
                        )
                    );
            }
            if (criteria.getAgentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAgentId(),
                            root -> root.join(DemandeDexplication_.agent, JoinType.LEFT).get(Agent_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

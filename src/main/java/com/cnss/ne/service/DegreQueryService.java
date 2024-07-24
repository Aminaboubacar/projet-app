package com.cnss.ne.service;

import com.cnss.ne.domain.*; // for static metamodels
import com.cnss.ne.domain.Degre;
import com.cnss.ne.repository.DegreRepository;
import com.cnss.ne.service.criteria.DegreCriteria;
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
 * Service for executing complex queries for {@link Degre} entities in the database.
 * The main input is a {@link DegreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Degre} or a {@link Page} of {@link Degre} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DegreQueryService extends QueryService<Degre> {

    private final Logger log = LoggerFactory.getLogger(DegreQueryService.class);

    private final DegreRepository degreRepository;

    public DegreQueryService(DegreRepository degreRepository) {
        this.degreRepository = degreRepository;
    }

    /**
     * Return a {@link List} of {@link Degre} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Degre> findByCriteria(DegreCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Degre> specification = createSpecification(criteria);
        return degreRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Degre} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Degre> findByCriteria(DegreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Degre> specification = createSpecification(criteria);
        return degreRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DegreCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Degre> specification = createSpecification(criteria);
        return degreRepository.count(specification);
    }

    /**
     * Function to convert {@link DegreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Degre> createSpecification(DegreCriteria criteria) {
        Specification<Degre> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Degre_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Degre_.code));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Degre_.libelle));
            }
            if (criteria.getSanctionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSanctionId(), root -> root.join(Degre_.sanctions, JoinType.LEFT).get(Sanction_.id))
                    );
            }
        }
        return specification;
    }
}

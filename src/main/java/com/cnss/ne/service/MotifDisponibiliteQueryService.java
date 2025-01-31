package com.cnss.ne.service;

import com.cnss.ne.domain.*; // for static metamodels
import com.cnss.ne.domain.MotifDisponibilite;
import com.cnss.ne.repository.MotifDisponibiliteRepository;
import com.cnss.ne.service.criteria.MotifDisponibiliteCriteria;
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
 * Service for executing complex queries for {@link MotifDisponibilite} entities in the database.
 * The main input is a {@link MotifDisponibiliteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MotifDisponibilite} or a {@link Page} of {@link MotifDisponibilite} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MotifDisponibiliteQueryService extends QueryService<MotifDisponibilite> {

    private final Logger log = LoggerFactory.getLogger(MotifDisponibiliteQueryService.class);

    private final MotifDisponibiliteRepository motifDisponibiliteRepository;

    public MotifDisponibiliteQueryService(MotifDisponibiliteRepository motifDisponibiliteRepository) {
        this.motifDisponibiliteRepository = motifDisponibiliteRepository;
    }

    /**
     * Return a {@link List} of {@link MotifDisponibilite} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MotifDisponibilite> findByCriteria(MotifDisponibiliteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MotifDisponibilite> specification = createSpecification(criteria);
        return motifDisponibiliteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MotifDisponibilite} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MotifDisponibilite> findByCriteria(MotifDisponibiliteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MotifDisponibilite> specification = createSpecification(criteria);
        return motifDisponibiliteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MotifDisponibiliteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MotifDisponibilite> specification = createSpecification(criteria);
        return motifDisponibiliteRepository.count(specification);
    }

    /**
     * Function to convert {@link MotifDisponibiliteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MotifDisponibilite> createSpecification(MotifDisponibiliteCriteria criteria) {
        Specification<MotifDisponibilite> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MotifDisponibilite_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), MotifDisponibilite_.code));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), MotifDisponibilite_.libelle));
            }
            if (criteria.getDisponibiliteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDisponibiliteId(),
                            root -> root.join(MotifDisponibilite_.disponibilites, JoinType.LEFT).get(Disponibilite_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

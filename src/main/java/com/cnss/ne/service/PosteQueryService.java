package com.cnss.ne.service;

import com.cnss.ne.domain.*; // for static metamodels
import com.cnss.ne.domain.Poste;
import com.cnss.ne.repository.PosteRepository;
import com.cnss.ne.service.criteria.PosteCriteria;
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
 * Service for executing complex queries for {@link Poste} entities in the database.
 * The main input is a {@link PosteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Poste} or a {@link Page} of {@link Poste} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PosteQueryService extends QueryService<Poste> {

    private final Logger log = LoggerFactory.getLogger(PosteQueryService.class);

    private final PosteRepository posteRepository;

    public PosteQueryService(PosteRepository posteRepository) {
        this.posteRepository = posteRepository;
    }

    /**
     * Return a {@link List} of {@link Poste} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Poste> findByCriteria(PosteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Poste> specification = createSpecification(criteria);
        return posteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Poste} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Poste> findByCriteria(PosteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Poste> specification = createSpecification(criteria);
        return posteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PosteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Poste> specification = createSpecification(criteria);
        return posteRepository.count(specification);
    }

    /**
     * Function to convert {@link PosteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Poste> createSpecification(PosteCriteria criteria) {
        Specification<Poste> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Poste_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Poste_.code));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Poste_.libelle));
            }
            if (criteria.getAgentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAgentId(), root -> root.join(Poste_.agents, JoinType.LEFT).get(Agent_.id))
                    );
            }
        }
        return specification;
    }
}

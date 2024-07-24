package com.cnss.ne.service;

import com.cnss.ne.domain.*; // for static metamodels
import com.cnss.ne.domain.Agent;
import com.cnss.ne.repository.AgentRepository;
import com.cnss.ne.service.criteria.AgentCriteria;
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
 * Service for executing complex queries for {@link Agent} entities in the database.
 * The main input is a {@link AgentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Agent} or a {@link Page} of {@link Agent} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AgentQueryService extends QueryService<Agent> {

    private final Logger log = LoggerFactory.getLogger(AgentQueryService.class);

    private final AgentRepository agentRepository;

    public AgentQueryService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    /**
     * Return a {@link List} of {@link Agent} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Agent> findByCriteria(AgentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Agent> specification = createSpecification(criteria);
        return agentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Agent} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Agent> findByCriteria(AgentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Agent> specification = createSpecification(criteria);
        return agentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AgentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Agent> specification = createSpecification(criteria);
        return agentRepository.count(specification);
    }

    /**
     * Function to convert {@link AgentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Agent> createSpecification(AgentCriteria criteria) {
        Specification<Agent> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Agent_.id));
            }
            if (criteria.getMatricule() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMatricule(), Agent_.matricule));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Agent_.nom));
            }
            if (criteria.getPrenom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenom(), Agent_.prenom));
            }
            if (criteria.getDateNaissance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateNaissance(), Agent_.dateNaissance));
            }
            if (criteria.getLieuNaissance() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLieuNaissance(), Agent_.lieuNaissance));
            }
            if (criteria.getNationalite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNationalite(), Agent_.nationalite));
            }
            if (criteria.getTelephone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelephone(), Agent_.telephone));
            }
            if (criteria.getAdresse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdresse(), Agent_.adresse));
            }
            if (criteria.getDateDece() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDece(), Agent_.dateDece));
            }
            if (criteria.getCauseDece() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCauseDece(), Agent_.causeDece));
            }
            if (criteria.getDisponibiliteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDisponibiliteId(),
                            root -> root.join(Agent_.disponibilites, JoinType.LEFT).get(Disponibilite_.id)
                        )
                    );
            }
            if (criteria.getMiseadispositionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMiseadispositionId(),
                            root -> root.join(Agent_.miseadispositions, JoinType.LEFT).get(Miseadisposition_.id)
                        )
                    );
            }
            if (criteria.getDemandeDexplicationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDemandeDexplicationId(),
                            root -> root.join(Agent_.demandeDexplications, JoinType.LEFT).get(DemandeDexplication_.id)
                        )
                    );
            }
            if (criteria.getPosteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPosteId(), root -> root.join(Agent_.poste, JoinType.LEFT).get(Poste_.id))
                    );
            }
        }
        return specification;
    }
}

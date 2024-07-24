package com.cnss.ne.service.impl;

import com.cnss.ne.domain.Agent;
import com.cnss.ne.repository.AgentRepository;
import com.cnss.ne.service.AgentService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cnss.ne.domain.Agent}.
 */
@Service
@Transactional
public class AgentServiceImpl implements AgentService {

    private final Logger log = LoggerFactory.getLogger(AgentServiceImpl.class);

    private final AgentRepository agentRepository;

    public AgentServiceImpl(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    @Override
    public Agent save(Agent agent) {
        log.debug("Request to save Agent : {}", agent);
        return agentRepository.save(agent);
    }

    @Override
    public Agent update(Agent agent) {
        log.debug("Request to update Agent : {}", agent);
        return agentRepository.save(agent);
    }

    @Override
    public Optional<Agent> partialUpdate(Agent agent) {
        log.debug("Request to partially update Agent : {}", agent);

        return agentRepository
            .findById(agent.getId())
            .map(existingAgent -> {
                if (agent.getMatricule() != null) {
                    existingAgent.setMatricule(agent.getMatricule());
                }
                if (agent.getNom() != null) {
                    existingAgent.setNom(agent.getNom());
                }
                if (agent.getPrenom() != null) {
                    existingAgent.setPrenom(agent.getPrenom());
                }
                if (agent.getDateNaissance() != null) {
                    existingAgent.setDateNaissance(agent.getDateNaissance());
                }
                if (agent.getLieuNaissance() != null) {
                    existingAgent.setLieuNaissance(agent.getLieuNaissance());
                }
                if (agent.getNationalite() != null) {
                    existingAgent.setNationalite(agent.getNationalite());
                }
                if (agent.getTelephone() != null) {
                    existingAgent.setTelephone(agent.getTelephone());
                }
                if (agent.getAdresse() != null) {
                    existingAgent.setAdresse(agent.getAdresse());
                }
                if (agent.getDateDece() != null) {
                    existingAgent.setDateDece(agent.getDateDece());
                }
                if (agent.getCauseDece() != null) {
                    existingAgent.setCauseDece(agent.getCauseDece());
                }

                return existingAgent;
            })
            .map(agentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Agent> findAll(Pageable pageable) {
        log.debug("Request to get all Agents");
        return agentRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Agent> findOne(Long id) {
        log.debug("Request to get Agent : {}", id);
        return agentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Agent : {}", id);
        agentRepository.deleteById(id);
    }
}

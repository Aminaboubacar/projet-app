package com.cnss.ne.service.impl;

import com.cnss.ne.domain.MotifDisponibilite;
import com.cnss.ne.repository.MotifDisponibiliteRepository;
import com.cnss.ne.service.MotifDisponibiliteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cnss.ne.domain.MotifDisponibilite}.
 */
@Service
@Transactional
public class MotifDisponibiliteServiceImpl implements MotifDisponibiliteService {

    private final Logger log = LoggerFactory.getLogger(MotifDisponibiliteServiceImpl.class);

    private final MotifDisponibiliteRepository motifDisponibiliteRepository;

    public MotifDisponibiliteServiceImpl(MotifDisponibiliteRepository motifDisponibiliteRepository) {
        this.motifDisponibiliteRepository = motifDisponibiliteRepository;
    }

    @Override
    public MotifDisponibilite save(MotifDisponibilite motifDisponibilite) {
        log.debug("Request to save MotifDisponibilite : {}", motifDisponibilite);
        return motifDisponibiliteRepository.save(motifDisponibilite);
    }

    @Override
    public MotifDisponibilite update(MotifDisponibilite motifDisponibilite) {
        log.debug("Request to update MotifDisponibilite : {}", motifDisponibilite);
        return motifDisponibiliteRepository.save(motifDisponibilite);
    }

    @Override
    public Optional<MotifDisponibilite> partialUpdate(MotifDisponibilite motifDisponibilite) {
        log.debug("Request to partially update MotifDisponibilite : {}", motifDisponibilite);

        return motifDisponibiliteRepository
            .findById(motifDisponibilite.getId())
            .map(existingMotifDisponibilite -> {
                if (motifDisponibilite.getCode() != null) {
                    existingMotifDisponibilite.setCode(motifDisponibilite.getCode());
                }
                if (motifDisponibilite.getLibelle() != null) {
                    existingMotifDisponibilite.setLibelle(motifDisponibilite.getLibelle());
                }

                return existingMotifDisponibilite;
            })
            .map(motifDisponibiliteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MotifDisponibilite> findAll(Pageable pageable) {
        log.debug("Request to get all MotifDisponibilites");
        return motifDisponibiliteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MotifDisponibilite> findOne(Long id) {
        log.debug("Request to get MotifDisponibilite : {}", id);
        return motifDisponibiliteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MotifDisponibilite : {}", id);
        motifDisponibiliteRepository.deleteById(id);
    }
}

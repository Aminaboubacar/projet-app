package com.cnss.ne.service.impl;

import com.cnss.ne.domain.Disponibilite;
import com.cnss.ne.repository.DisponibiliteRepository;
import com.cnss.ne.service.DisponibiliteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cnss.ne.domain.Disponibilite}.
 */
@Service
@Transactional
public class DisponibiliteServiceImpl implements DisponibiliteService {

    private final Logger log = LoggerFactory.getLogger(DisponibiliteServiceImpl.class);

    private final DisponibiliteRepository disponibiliteRepository;

    public DisponibiliteServiceImpl(DisponibiliteRepository disponibiliteRepository) {
        this.disponibiliteRepository = disponibiliteRepository;
    }

    @Override
    public Disponibilite save(Disponibilite disponibilite) {
        log.debug("Request to save Disponibilite : {}", disponibilite);
        return disponibiliteRepository.save(disponibilite);
    }

    @Override
    public Disponibilite update(Disponibilite disponibilite) {
        log.debug("Request to update Disponibilite : {}", disponibilite);
        return disponibiliteRepository.save(disponibilite);
    }

    @Override
    public Optional<Disponibilite> partialUpdate(Disponibilite disponibilite) {
        log.debug("Request to partially update Disponibilite : {}", disponibilite);

        return disponibiliteRepository
            .findById(disponibilite.getId())
            .map(existingDisponibilite -> {
                if (disponibilite.getCode() != null) {
                    existingDisponibilite.setCode(disponibilite.getCode());
                }
                if (disponibilite.getDateDebut() != null) {
                    existingDisponibilite.setDateDebut(disponibilite.getDateDebut());
                }
                if (disponibilite.getDateFin() != null) {
                    existingDisponibilite.setDateFin(disponibilite.getDateFin());
                }
                if (disponibilite.getDateRetour() != null) {
                    existingDisponibilite.setDateRetour(disponibilite.getDateRetour());
                }

                return existingDisponibilite;
            })
            .map(disponibiliteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Disponibilite> findAll(Pageable pageable) {
        log.debug("Request to get all Disponibilites");
        return disponibiliteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Disponibilite> findOne(Long id) {
        log.debug("Request to get Disponibilite : {}", id);
        return disponibiliteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Disponibilite : {}", id);
        disponibiliteRepository.deleteById(id);
    }
}

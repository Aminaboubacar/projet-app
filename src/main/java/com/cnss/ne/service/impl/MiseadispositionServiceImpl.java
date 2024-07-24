package com.cnss.ne.service.impl;

import com.cnss.ne.domain.Miseadisposition;
import com.cnss.ne.repository.MiseadispositionRepository;
import com.cnss.ne.service.MiseadispositionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cnss.ne.domain.Miseadisposition}.
 */
@Service
@Transactional
public class MiseadispositionServiceImpl implements MiseadispositionService {

    private final Logger log = LoggerFactory.getLogger(MiseadispositionServiceImpl.class);

    private final MiseadispositionRepository miseadispositionRepository;

    public MiseadispositionServiceImpl(MiseadispositionRepository miseadispositionRepository) {
        this.miseadispositionRepository = miseadispositionRepository;
    }

    @Override
    public Miseadisposition save(Miseadisposition miseadisposition) {
        log.debug("Request to save Miseadisposition : {}", miseadisposition);
        return miseadispositionRepository.save(miseadisposition);
    }

    @Override
    public Miseadisposition update(Miseadisposition miseadisposition) {
        log.debug("Request to update Miseadisposition : {}", miseadisposition);
        return miseadispositionRepository.save(miseadisposition);
    }

    @Override
    public Optional<Miseadisposition> partialUpdate(Miseadisposition miseadisposition) {
        log.debug("Request to partially update Miseadisposition : {}", miseadisposition);

        return miseadispositionRepository
            .findById(miseadisposition.getId())
            .map(existingMiseadisposition -> {
                if (miseadisposition.getCode() != null) {
                    existingMiseadisposition.setCode(miseadisposition.getCode());
                }
                if (miseadisposition.getOrganisme() != null) {
                    existingMiseadisposition.setOrganisme(miseadisposition.getOrganisme());
                }
                if (miseadisposition.getDateDebut() != null) {
                    existingMiseadisposition.setDateDebut(miseadisposition.getDateDebut());
                }
                if (miseadisposition.getDateFin() != null) {
                    existingMiseadisposition.setDateFin(miseadisposition.getDateFin());
                }
                if (miseadisposition.getSensMouvement() != null) {
                    existingMiseadisposition.setSensMouvement(miseadisposition.getSensMouvement());
                }
                if (miseadisposition.getDateRetour() != null) {
                    existingMiseadisposition.setDateRetour(miseadisposition.getDateRetour());
                }

                return existingMiseadisposition;
            })
            .map(miseadispositionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Miseadisposition> findAll(Pageable pageable) {
        log.debug("Request to get all Miseadispositions");
        return miseadispositionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Miseadisposition> findOne(Long id) {
        log.debug("Request to get Miseadisposition : {}", id);
        return miseadispositionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Miseadisposition : {}", id);
        miseadispositionRepository.deleteById(id);
    }
}

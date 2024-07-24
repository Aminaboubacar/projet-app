package com.cnss.ne.service.impl;

import com.cnss.ne.domain.Sanction;
import com.cnss.ne.repository.SanctionRepository;
import com.cnss.ne.service.SanctionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cnss.ne.domain.Sanction}.
 */
@Service
@Transactional
public class SanctionServiceImpl implements SanctionService {

    private final Logger log = LoggerFactory.getLogger(SanctionServiceImpl.class);

    private final SanctionRepository sanctionRepository;

    public SanctionServiceImpl(SanctionRepository sanctionRepository) {
        this.sanctionRepository = sanctionRepository;
    }

    @Override
    public Sanction save(Sanction sanction) {
        log.debug("Request to save Sanction : {}", sanction);
        return sanctionRepository.save(sanction);
    }

    @Override
    public Sanction update(Sanction sanction) {
        log.debug("Request to update Sanction : {}", sanction);
        return sanctionRepository.save(sanction);
    }

    @Override
    public Optional<Sanction> partialUpdate(Sanction sanction) {
        log.debug("Request to partially update Sanction : {}", sanction);

        return sanctionRepository
            .findById(sanction.getId())
            .map(existingSanction -> {
                if (sanction.getCode() != null) {
                    existingSanction.setCode(sanction.getCode());
                }
                if (sanction.getLibelle() != null) {
                    existingSanction.setLibelle(sanction.getLibelle());
                }

                return existingSanction;
            })
            .map(sanctionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sanction> findAll(Pageable pageable) {
        log.debug("Request to get all Sanctions");
        return sanctionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sanction> findOne(Long id) {
        log.debug("Request to get Sanction : {}", id);
        return sanctionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sanction : {}", id);
        sanctionRepository.deleteById(id);
    }
}

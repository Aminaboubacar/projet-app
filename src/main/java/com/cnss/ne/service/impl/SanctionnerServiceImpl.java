package com.cnss.ne.service.impl;

import com.cnss.ne.domain.Sanctionner;
import com.cnss.ne.repository.SanctionnerRepository;
import com.cnss.ne.service.SanctionnerService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cnss.ne.domain.Sanctionner}.
 */
@Service
@Transactional
public class SanctionnerServiceImpl implements SanctionnerService {

    private final Logger log = LoggerFactory.getLogger(SanctionnerServiceImpl.class);

    private final SanctionnerRepository sanctionnerRepository;

    public SanctionnerServiceImpl(SanctionnerRepository sanctionnerRepository) {
        this.sanctionnerRepository = sanctionnerRepository;
    }

    @Override
    public Sanctionner save(Sanctionner sanctionner) {
        log.debug("Request to save Sanctionner : {}", sanctionner);
        return sanctionnerRepository.save(sanctionner);
    }

    @Override
    public Sanctionner update(Sanctionner sanctionner) {
        log.debug("Request to update Sanctionner : {}", sanctionner);
        return sanctionnerRepository.save(sanctionner);
    }

    @Override
    public Optional<Sanctionner> partialUpdate(Sanctionner sanctionner) {
        log.debug("Request to partially update Sanctionner : {}", sanctionner);

        return sanctionnerRepository
            .findById(sanctionner.getId())
            .map(existingSanctionner -> {
                if (sanctionner.getDate() != null) {
                    existingSanctionner.setDate(sanctionner.getDate());
                }

                return existingSanctionner;
            })
            .map(sanctionnerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sanctionner> findAll(Pageable pageable) {
        log.debug("Request to get all Sanctionners");
        return sanctionnerRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sanctionner> findOne(Long id) {
        log.debug("Request to get Sanctionner : {}", id);
        return sanctionnerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sanctionner : {}", id);
        sanctionnerRepository.deleteById(id);
    }
}

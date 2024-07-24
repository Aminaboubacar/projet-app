package com.cnss.ne.service.impl;

import com.cnss.ne.domain.Degre;
import com.cnss.ne.repository.DegreRepository;
import com.cnss.ne.service.DegreService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cnss.ne.domain.Degre}.
 */
@Service
@Transactional
public class DegreServiceImpl implements DegreService {

    private final Logger log = LoggerFactory.getLogger(DegreServiceImpl.class);

    private final DegreRepository degreRepository;

    public DegreServiceImpl(DegreRepository degreRepository) {
        this.degreRepository = degreRepository;
    }

    @Override
    public Degre save(Degre degre) {
        log.debug("Request to save Degre : {}", degre);
        return degreRepository.save(degre);
    }

    @Override
    public Degre update(Degre degre) {
        log.debug("Request to update Degre : {}", degre);
        return degreRepository.save(degre);
    }

    @Override
    public Optional<Degre> partialUpdate(Degre degre) {
        log.debug("Request to partially update Degre : {}", degre);

        return degreRepository
            .findById(degre.getId())
            .map(existingDegre -> {
                if (degre.getCode() != null) {
                    existingDegre.setCode(degre.getCode());
                }
                if (degre.getLibelle() != null) {
                    existingDegre.setLibelle(degre.getLibelle());
                }

                return existingDegre;
            })
            .map(degreRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Degre> findAll(Pageable pageable) {
        log.debug("Request to get all Degres");
        return degreRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Degre> findOne(Long id) {
        log.debug("Request to get Degre : {}", id);
        return degreRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Degre : {}", id);
        degreRepository.deleteById(id);
    }
}

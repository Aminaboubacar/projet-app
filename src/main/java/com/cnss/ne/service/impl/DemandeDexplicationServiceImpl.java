package com.cnss.ne.service.impl;

import com.cnss.ne.domain.DemandeDexplication;
import com.cnss.ne.repository.DemandeDexplicationRepository;
import com.cnss.ne.service.DemandeDexplicationService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cnss.ne.domain.DemandeDexplication}.
 */
@Service
@Transactional
public class DemandeDexplicationServiceImpl implements DemandeDexplicationService {

    private final Logger log = LoggerFactory.getLogger(DemandeDexplicationServiceImpl.class);

    private final DemandeDexplicationRepository demandeDexplicationRepository;

    public DemandeDexplicationServiceImpl(DemandeDexplicationRepository demandeDexplicationRepository) {
        this.demandeDexplicationRepository = demandeDexplicationRepository;
    }

    @Override
    public DemandeDexplication save(DemandeDexplication demandeDexplication) {
        log.debug("Request to save DemandeDexplication : {}", demandeDexplication);
        return demandeDexplicationRepository.save(demandeDexplication);
    }

    @Override
    public DemandeDexplication update(DemandeDexplication demandeDexplication) {
        log.debug("Request to update DemandeDexplication : {}", demandeDexplication);
        return demandeDexplicationRepository.save(demandeDexplication);
    }

    @Override
    public Optional<DemandeDexplication> partialUpdate(DemandeDexplication demandeDexplication) {
        log.debug("Request to partially update DemandeDexplication : {}", demandeDexplication);

        return demandeDexplicationRepository
            .findById(demandeDexplication.getId())
            .map(existingDemandeDexplication -> {
                if (demandeDexplication.getCode() != null) {
                    existingDemandeDexplication.setCode(demandeDexplication.getCode());
                }
                if (demandeDexplication.getDate() != null) {
                    existingDemandeDexplication.setDate(demandeDexplication.getDate());
                }
                if (demandeDexplication.getObjet() != null) {
                    existingDemandeDexplication.setObjet(demandeDexplication.getObjet());
                }
                if (demandeDexplication.getAppDg() != null) {
                    existingDemandeDexplication.setAppDg(demandeDexplication.getAppDg());
                }
                if (demandeDexplication.getDatappDg() != null) {
                    existingDemandeDexplication.setDatappDg(demandeDexplication.getDatappDg());
                }

                return existingDemandeDexplication;
            })
            .map(demandeDexplicationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DemandeDexplication> findAll(Pageable pageable) {
        log.debug("Request to get all DemandeDexplications");
        return demandeDexplicationRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DemandeDexplication> findOne(Long id) {
        log.debug("Request to get DemandeDexplication : {}", id);
        return demandeDexplicationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DemandeDexplication : {}", id);
        demandeDexplicationRepository.deleteById(id);
    }
}

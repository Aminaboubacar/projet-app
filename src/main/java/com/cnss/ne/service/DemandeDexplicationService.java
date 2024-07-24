package com.cnss.ne.service;

import com.cnss.ne.domain.DemandeDexplication;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.cnss.ne.domain.DemandeDexplication}.
 */
public interface DemandeDexplicationService {
    /**
     * Save a demandeDexplication.
     *
     * @param demandeDexplication the entity to save.
     * @return the persisted entity.
     */
    DemandeDexplication save(DemandeDexplication demandeDexplication);

    /**
     * Updates a demandeDexplication.
     *
     * @param demandeDexplication the entity to update.
     * @return the persisted entity.
     */
    DemandeDexplication update(DemandeDexplication demandeDexplication);

    /**
     * Partially updates a demandeDexplication.
     *
     * @param demandeDexplication the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DemandeDexplication> partialUpdate(DemandeDexplication demandeDexplication);

    /**
     * Get all the demandeDexplications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DemandeDexplication> findAll(Pageable pageable);

    /**
     * Get the "id" demandeDexplication.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DemandeDexplication> findOne(Long id);

    /**
     * Delete the "id" demandeDexplication.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

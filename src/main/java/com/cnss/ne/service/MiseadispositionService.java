package com.cnss.ne.service;

import com.cnss.ne.domain.Miseadisposition;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.cnss.ne.domain.Miseadisposition}.
 */
public interface MiseadispositionService {
    /**
     * Save a miseadisposition.
     *
     * @param miseadisposition the entity to save.
     * @return the persisted entity.
     */
    Miseadisposition save(Miseadisposition miseadisposition);

    /**
     * Updates a miseadisposition.
     *
     * @param miseadisposition the entity to update.
     * @return the persisted entity.
     */
    Miseadisposition update(Miseadisposition miseadisposition);

    /**
     * Partially updates a miseadisposition.
     *
     * @param miseadisposition the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Miseadisposition> partialUpdate(Miseadisposition miseadisposition);

    /**
     * Get all the miseadispositions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Miseadisposition> findAll(Pageable pageable);

    /**
     * Get the "id" miseadisposition.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Miseadisposition> findOne(Long id);

    /**
     * Delete the "id" miseadisposition.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

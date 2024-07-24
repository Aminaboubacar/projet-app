package com.cnss.ne.service;

import com.cnss.ne.domain.Sanction;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.cnss.ne.domain.Sanction}.
 */
public interface SanctionService {
    /**
     * Save a sanction.
     *
     * @param sanction the entity to save.
     * @return the persisted entity.
     */
    Sanction save(Sanction sanction);

    /**
     * Updates a sanction.
     *
     * @param sanction the entity to update.
     * @return the persisted entity.
     */
    Sanction update(Sanction sanction);

    /**
     * Partially updates a sanction.
     *
     * @param sanction the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Sanction> partialUpdate(Sanction sanction);

    /**
     * Get all the sanctions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Sanction> findAll(Pageable pageable);

    /**
     * Get the "id" sanction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Sanction> findOne(Long id);

    /**
     * Delete the "id" sanction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

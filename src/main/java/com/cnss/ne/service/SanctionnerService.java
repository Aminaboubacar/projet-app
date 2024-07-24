package com.cnss.ne.service;

import com.cnss.ne.domain.Sanctionner;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.cnss.ne.domain.Sanctionner}.
 */
public interface SanctionnerService {
    /**
     * Save a sanctionner.
     *
     * @param sanctionner the entity to save.
     * @return the persisted entity.
     */
    Sanctionner save(Sanctionner sanctionner);

    /**
     * Updates a sanctionner.
     *
     * @param sanctionner the entity to update.
     * @return the persisted entity.
     */
    Sanctionner update(Sanctionner sanctionner);

    /**
     * Partially updates a sanctionner.
     *
     * @param sanctionner the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Sanctionner> partialUpdate(Sanctionner sanctionner);

    /**
     * Get all the sanctionners.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Sanctionner> findAll(Pageable pageable);

    /**
     * Get the "id" sanctionner.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Sanctionner> findOne(Long id);

    /**
     * Delete the "id" sanctionner.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

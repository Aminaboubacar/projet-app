package com.cnss.ne.service;

import com.cnss.ne.domain.Degre;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.cnss.ne.domain.Degre}.
 */
public interface DegreService {
    /**
     * Save a degre.
     *
     * @param degre the entity to save.
     * @return the persisted entity.
     */
    Degre save(Degre degre);

    /**
     * Updates a degre.
     *
     * @param degre the entity to update.
     * @return the persisted entity.
     */
    Degre update(Degre degre);

    /**
     * Partially updates a degre.
     *
     * @param degre the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Degre> partialUpdate(Degre degre);

    /**
     * Get all the degres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Degre> findAll(Pageable pageable);

    /**
     * Get the "id" degre.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Degre> findOne(Long id);

    /**
     * Delete the "id" degre.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

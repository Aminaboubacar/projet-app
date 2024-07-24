package com.cnss.ne.service;

import com.cnss.ne.domain.MotifDisponibilite;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.cnss.ne.domain.MotifDisponibilite}.
 */
public interface MotifDisponibiliteService {
    /**
     * Save a motifDisponibilite.
     *
     * @param motifDisponibilite the entity to save.
     * @return the persisted entity.
     */
    MotifDisponibilite save(MotifDisponibilite motifDisponibilite);

    /**
     * Updates a motifDisponibilite.
     *
     * @param motifDisponibilite the entity to update.
     * @return the persisted entity.
     */
    MotifDisponibilite update(MotifDisponibilite motifDisponibilite);

    /**
     * Partially updates a motifDisponibilite.
     *
     * @param motifDisponibilite the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MotifDisponibilite> partialUpdate(MotifDisponibilite motifDisponibilite);

    /**
     * Get all the motifDisponibilites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MotifDisponibilite> findAll(Pageable pageable);

    /**
     * Get the "id" motifDisponibilite.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MotifDisponibilite> findOne(Long id);

    /**
     * Delete the "id" motifDisponibilite.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

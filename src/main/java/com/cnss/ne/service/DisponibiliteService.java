package com.cnss.ne.service;

import com.cnss.ne.domain.Disponibilite;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.cnss.ne.domain.Disponibilite}.
 */
public interface DisponibiliteService {
    /**
     * Save a disponibilite.
     *
     * @param disponibilite the entity to save.
     * @return the persisted entity.
     */
    Disponibilite save(Disponibilite disponibilite);

    /**
     * Updates a disponibilite.
     *
     * @param disponibilite the entity to update.
     * @return the persisted entity.
     */
    Disponibilite update(Disponibilite disponibilite);

    /**
     * Partially updates a disponibilite.
     *
     * @param disponibilite the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Disponibilite> partialUpdate(Disponibilite disponibilite);

    /**
     * Get all the disponibilites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Disponibilite> findAll(Pageable pageable);

    /**
     * Get the "id" disponibilite.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Disponibilite> findOne(Long id);

    /**
     * Delete the "id" disponibilite.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

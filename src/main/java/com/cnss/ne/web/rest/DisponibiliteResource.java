package com.cnss.ne.web.rest;

import com.cnss.ne.domain.Disponibilite;
import com.cnss.ne.repository.DisponibiliteRepository;
import com.cnss.ne.service.DisponibiliteQueryService;
import com.cnss.ne.service.DisponibiliteService;
import com.cnss.ne.service.criteria.DisponibiliteCriteria;
import com.cnss.ne.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.cnss.ne.domain.Disponibilite}.
 */
@RestController
@RequestMapping("/api/disponibilites")
public class DisponibiliteResource {

    private final Logger log = LoggerFactory.getLogger(DisponibiliteResource.class);

    private static final String ENTITY_NAME = "disponibilite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DisponibiliteService disponibiliteService;

    private final DisponibiliteRepository disponibiliteRepository;

    private final DisponibiliteQueryService disponibiliteQueryService;

    public DisponibiliteResource(
        DisponibiliteService disponibiliteService,
        DisponibiliteRepository disponibiliteRepository,
        DisponibiliteQueryService disponibiliteQueryService
    ) {
        this.disponibiliteService = disponibiliteService;
        this.disponibiliteRepository = disponibiliteRepository;
        this.disponibiliteQueryService = disponibiliteQueryService;
    }

    /**
     * {@code POST  /disponibilites} : Create a new disponibilite.
     *
     * @param disponibilite the disponibilite to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new disponibilite, or with status {@code 400 (Bad Request)} if the disponibilite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Disponibilite> createDisponibilite(@Valid @RequestBody Disponibilite disponibilite) throws URISyntaxException {
        log.debug("REST request to save Disponibilite : {}", disponibilite);
        if (disponibilite.getId() != null) {
            throw new BadRequestAlertException("A new disponibilite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Disponibilite result = disponibiliteService.save(disponibilite);
        return ResponseEntity
            .created(new URI("/api/disponibilites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /disponibilites/:id} : Updates an existing disponibilite.
     *
     * @param id the id of the disponibilite to save.
     * @param disponibilite the disponibilite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disponibilite,
     * or with status {@code 400 (Bad Request)} if the disponibilite is not valid,
     * or with status {@code 500 (Internal Server Error)} if the disponibilite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Disponibilite> updateDisponibilite(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Disponibilite disponibilite
    ) throws URISyntaxException {
        log.debug("REST request to update Disponibilite : {}, {}", id, disponibilite);
        if (disponibilite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disponibilite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disponibiliteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Disponibilite result = disponibiliteService.update(disponibilite);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, disponibilite.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /disponibilites/:id} : Partial updates given fields of an existing disponibilite, field will ignore if it is null
     *
     * @param id the id of the disponibilite to save.
     * @param disponibilite the disponibilite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disponibilite,
     * or with status {@code 400 (Bad Request)} if the disponibilite is not valid,
     * or with status {@code 404 (Not Found)} if the disponibilite is not found,
     * or with status {@code 500 (Internal Server Error)} if the disponibilite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Disponibilite> partialUpdateDisponibilite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Disponibilite disponibilite
    ) throws URISyntaxException {
        log.debug("REST request to partial update Disponibilite partially : {}, {}", id, disponibilite);
        if (disponibilite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disponibilite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disponibiliteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Disponibilite> result = disponibiliteService.partialUpdate(disponibilite);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, disponibilite.getId().toString())
        );
    }

    /**
     * {@code GET  /disponibilites} : get all the disponibilites.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of disponibilites in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Disponibilite>> getAllDisponibilites(
        DisponibiliteCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Disponibilites by criteria: {}", criteria);

        Page<Disponibilite> page = disponibiliteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /disponibilites/count} : count all the disponibilites.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDisponibilites(DisponibiliteCriteria criteria) {
        log.debug("REST request to count Disponibilites by criteria: {}", criteria);
        return ResponseEntity.ok().body(disponibiliteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /disponibilites/:id} : get the "id" disponibilite.
     *
     * @param id the id of the disponibilite to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the disponibilite, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Disponibilite> getDisponibilite(@PathVariable Long id) {
        log.debug("REST request to get Disponibilite : {}", id);
        Optional<Disponibilite> disponibilite = disponibiliteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(disponibilite);
    }

    /**
     * {@code DELETE  /disponibilites/:id} : delete the "id" disponibilite.
     *
     * @param id the id of the disponibilite to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisponibilite(@PathVariable Long id) {
        log.debug("REST request to delete Disponibilite : {}", id);
        disponibiliteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

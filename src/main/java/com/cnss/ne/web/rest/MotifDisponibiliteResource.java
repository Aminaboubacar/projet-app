package com.cnss.ne.web.rest;

import com.cnss.ne.domain.MotifDisponibilite;
import com.cnss.ne.repository.MotifDisponibiliteRepository;
import com.cnss.ne.service.MotifDisponibiliteQueryService;
import com.cnss.ne.service.MotifDisponibiliteService;
import com.cnss.ne.service.criteria.MotifDisponibiliteCriteria;
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
 * REST controller for managing {@link com.cnss.ne.domain.MotifDisponibilite}.
 */
@RestController
@RequestMapping("/api/motif-disponibilites")
public class MotifDisponibiliteResource {

    private final Logger log = LoggerFactory.getLogger(MotifDisponibiliteResource.class);

    private static final String ENTITY_NAME = "motifDisponibilite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MotifDisponibiliteService motifDisponibiliteService;

    private final MotifDisponibiliteRepository motifDisponibiliteRepository;

    private final MotifDisponibiliteQueryService motifDisponibiliteQueryService;

    public MotifDisponibiliteResource(
        MotifDisponibiliteService motifDisponibiliteService,
        MotifDisponibiliteRepository motifDisponibiliteRepository,
        MotifDisponibiliteQueryService motifDisponibiliteQueryService
    ) {
        this.motifDisponibiliteService = motifDisponibiliteService;
        this.motifDisponibiliteRepository = motifDisponibiliteRepository;
        this.motifDisponibiliteQueryService = motifDisponibiliteQueryService;
    }

    /**
     * {@code POST  /motif-disponibilites} : Create a new motifDisponibilite.
     *
     * @param motifDisponibilite the motifDisponibilite to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new motifDisponibilite, or with status {@code 400 (Bad Request)} if the motifDisponibilite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MotifDisponibilite> createMotifDisponibilite(@Valid @RequestBody MotifDisponibilite motifDisponibilite)
        throws URISyntaxException {
        log.debug("REST request to save MotifDisponibilite : {}", motifDisponibilite);
        if (motifDisponibilite.getId() != null) {
            throw new BadRequestAlertException("A new motifDisponibilite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MotifDisponibilite result = motifDisponibiliteService.save(motifDisponibilite);
        return ResponseEntity
            .created(new URI("/api/motif-disponibilites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /motif-disponibilites/:id} : Updates an existing motifDisponibilite.
     *
     * @param id the id of the motifDisponibilite to save.
     * @param motifDisponibilite the motifDisponibilite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated motifDisponibilite,
     * or with status {@code 400 (Bad Request)} if the motifDisponibilite is not valid,
     * or with status {@code 500 (Internal Server Error)} if the motifDisponibilite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MotifDisponibilite> updateMotifDisponibilite(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MotifDisponibilite motifDisponibilite
    ) throws URISyntaxException {
        log.debug("REST request to update MotifDisponibilite : {}, {}", id, motifDisponibilite);
        if (motifDisponibilite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, motifDisponibilite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!motifDisponibiliteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MotifDisponibilite result = motifDisponibiliteService.update(motifDisponibilite);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, motifDisponibilite.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /motif-disponibilites/:id} : Partial updates given fields of an existing motifDisponibilite, field will ignore if it is null
     *
     * @param id the id of the motifDisponibilite to save.
     * @param motifDisponibilite the motifDisponibilite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated motifDisponibilite,
     * or with status {@code 400 (Bad Request)} if the motifDisponibilite is not valid,
     * or with status {@code 404 (Not Found)} if the motifDisponibilite is not found,
     * or with status {@code 500 (Internal Server Error)} if the motifDisponibilite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MotifDisponibilite> partialUpdateMotifDisponibilite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MotifDisponibilite motifDisponibilite
    ) throws URISyntaxException {
        log.debug("REST request to partial update MotifDisponibilite partially : {}, {}", id, motifDisponibilite);
        if (motifDisponibilite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, motifDisponibilite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!motifDisponibiliteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MotifDisponibilite> result = motifDisponibiliteService.partialUpdate(motifDisponibilite);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, motifDisponibilite.getId().toString())
        );
    }

    /**
     * {@code GET  /motif-disponibilites} : get all the motifDisponibilites.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of motifDisponibilites in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MotifDisponibilite>> getAllMotifDisponibilites(
        MotifDisponibiliteCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get MotifDisponibilites by criteria: {}", criteria);

        Page<MotifDisponibilite> page = motifDisponibiliteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /motif-disponibilites/count} : count all the motifDisponibilites.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMotifDisponibilites(MotifDisponibiliteCriteria criteria) {
        log.debug("REST request to count MotifDisponibilites by criteria: {}", criteria);
        return ResponseEntity.ok().body(motifDisponibiliteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /motif-disponibilites/:id} : get the "id" motifDisponibilite.
     *
     * @param id the id of the motifDisponibilite to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the motifDisponibilite, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MotifDisponibilite> getMotifDisponibilite(@PathVariable Long id) {
        log.debug("REST request to get MotifDisponibilite : {}", id);
        Optional<MotifDisponibilite> motifDisponibilite = motifDisponibiliteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(motifDisponibilite);
    }

    /**
     * {@code DELETE  /motif-disponibilites/:id} : delete the "id" motifDisponibilite.
     *
     * @param id the id of the motifDisponibilite to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMotifDisponibilite(@PathVariable Long id) {
        log.debug("REST request to delete MotifDisponibilite : {}", id);
        motifDisponibiliteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

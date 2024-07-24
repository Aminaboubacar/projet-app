package com.cnss.ne.web.rest;

import com.cnss.ne.domain.Miseadisposition;
import com.cnss.ne.repository.MiseadispositionRepository;
import com.cnss.ne.service.MiseadispositionQueryService;
import com.cnss.ne.service.MiseadispositionService;
import com.cnss.ne.service.criteria.MiseadispositionCriteria;
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
 * REST controller for managing {@link com.cnss.ne.domain.Miseadisposition}.
 */
@RestController
@RequestMapping("/api/miseadispositions")
public class MiseadispositionResource {

    private final Logger log = LoggerFactory.getLogger(MiseadispositionResource.class);

    private static final String ENTITY_NAME = "miseadisposition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MiseadispositionService miseadispositionService;

    private final MiseadispositionRepository miseadispositionRepository;

    private final MiseadispositionQueryService miseadispositionQueryService;

    public MiseadispositionResource(
        MiseadispositionService miseadispositionService,
        MiseadispositionRepository miseadispositionRepository,
        MiseadispositionQueryService miseadispositionQueryService
    ) {
        this.miseadispositionService = miseadispositionService;
        this.miseadispositionRepository = miseadispositionRepository;
        this.miseadispositionQueryService = miseadispositionQueryService;
    }

    /**
     * {@code POST  /miseadispositions} : Create a new miseadisposition.
     *
     * @param miseadisposition the miseadisposition to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new miseadisposition, or with status {@code 400 (Bad Request)} if the miseadisposition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Miseadisposition> createMiseadisposition(@Valid @RequestBody Miseadisposition miseadisposition)
        throws URISyntaxException {
        log.debug("REST request to save Miseadisposition : {}", miseadisposition);
        if (miseadisposition.getId() != null) {
            throw new BadRequestAlertException("A new miseadisposition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Miseadisposition result = miseadispositionService.save(miseadisposition);
        return ResponseEntity
            .created(new URI("/api/miseadispositions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /miseadispositions/:id} : Updates an existing miseadisposition.
     *
     * @param id the id of the miseadisposition to save.
     * @param miseadisposition the miseadisposition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated miseadisposition,
     * or with status {@code 400 (Bad Request)} if the miseadisposition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the miseadisposition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Miseadisposition> updateMiseadisposition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Miseadisposition miseadisposition
    ) throws URISyntaxException {
        log.debug("REST request to update Miseadisposition : {}, {}", id, miseadisposition);
        if (miseadisposition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, miseadisposition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!miseadispositionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Miseadisposition result = miseadispositionService.update(miseadisposition);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, miseadisposition.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /miseadispositions/:id} : Partial updates given fields of an existing miseadisposition, field will ignore if it is null
     *
     * @param id the id of the miseadisposition to save.
     * @param miseadisposition the miseadisposition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated miseadisposition,
     * or with status {@code 400 (Bad Request)} if the miseadisposition is not valid,
     * or with status {@code 404 (Not Found)} if the miseadisposition is not found,
     * or with status {@code 500 (Internal Server Error)} if the miseadisposition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Miseadisposition> partialUpdateMiseadisposition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Miseadisposition miseadisposition
    ) throws URISyntaxException {
        log.debug("REST request to partial update Miseadisposition partially : {}, {}", id, miseadisposition);
        if (miseadisposition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, miseadisposition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!miseadispositionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Miseadisposition> result = miseadispositionService.partialUpdate(miseadisposition);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, miseadisposition.getId().toString())
        );
    }

    /**
     * {@code GET  /miseadispositions} : get all the miseadispositions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of miseadispositions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Miseadisposition>> getAllMiseadispositions(
        MiseadispositionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Miseadispositions by criteria: {}", criteria);

        Page<Miseadisposition> page = miseadispositionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /miseadispositions/count} : count all the miseadispositions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMiseadispositions(MiseadispositionCriteria criteria) {
        log.debug("REST request to count Miseadispositions by criteria: {}", criteria);
        return ResponseEntity.ok().body(miseadispositionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /miseadispositions/:id} : get the "id" miseadisposition.
     *
     * @param id the id of the miseadisposition to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the miseadisposition, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Miseadisposition> getMiseadisposition(@PathVariable Long id) {
        log.debug("REST request to get Miseadisposition : {}", id);
        Optional<Miseadisposition> miseadisposition = miseadispositionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(miseadisposition);
    }

    /**
     * {@code DELETE  /miseadispositions/:id} : delete the "id" miseadisposition.
     *
     * @param id the id of the miseadisposition to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMiseadisposition(@PathVariable Long id) {
        log.debug("REST request to delete Miseadisposition : {}", id);
        miseadispositionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

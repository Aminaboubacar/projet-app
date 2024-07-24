package com.cnss.ne.web.rest;

import com.cnss.ne.domain.Sanction;
import com.cnss.ne.repository.SanctionRepository;
import com.cnss.ne.service.SanctionQueryService;
import com.cnss.ne.service.SanctionService;
import com.cnss.ne.service.criteria.SanctionCriteria;
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
 * REST controller for managing {@link com.cnss.ne.domain.Sanction}.
 */
@RestController
@RequestMapping("/api/sanctions")
public class SanctionResource {

    private final Logger log = LoggerFactory.getLogger(SanctionResource.class);

    private static final String ENTITY_NAME = "sanction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SanctionService sanctionService;

    private final SanctionRepository sanctionRepository;

    private final SanctionQueryService sanctionQueryService;

    public SanctionResource(
        SanctionService sanctionService,
        SanctionRepository sanctionRepository,
        SanctionQueryService sanctionQueryService
    ) {
        this.sanctionService = sanctionService;
        this.sanctionRepository = sanctionRepository;
        this.sanctionQueryService = sanctionQueryService;
    }

    /**
     * {@code POST  /sanctions} : Create a new sanction.
     *
     * @param sanction the sanction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sanction, or with status {@code 400 (Bad Request)} if the sanction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Sanction> createSanction(@Valid @RequestBody Sanction sanction) throws URISyntaxException {
        log.debug("REST request to save Sanction : {}", sanction);
        if (sanction.getId() != null) {
            throw new BadRequestAlertException("A new sanction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sanction result = sanctionService.save(sanction);
        return ResponseEntity
            .created(new URI("/api/sanctions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sanctions/:id} : Updates an existing sanction.
     *
     * @param id the id of the sanction to save.
     * @param sanction the sanction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sanction,
     * or with status {@code 400 (Bad Request)} if the sanction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sanction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Sanction> updateSanction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Sanction sanction
    ) throws URISyntaxException {
        log.debug("REST request to update Sanction : {}, {}", id, sanction);
        if (sanction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sanction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sanctionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Sanction result = sanctionService.update(sanction);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sanction.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sanctions/:id} : Partial updates given fields of an existing sanction, field will ignore if it is null
     *
     * @param id the id of the sanction to save.
     * @param sanction the sanction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sanction,
     * or with status {@code 400 (Bad Request)} if the sanction is not valid,
     * or with status {@code 404 (Not Found)} if the sanction is not found,
     * or with status {@code 500 (Internal Server Error)} if the sanction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Sanction> partialUpdateSanction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Sanction sanction
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sanction partially : {}, {}", id, sanction);
        if (sanction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sanction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sanctionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Sanction> result = sanctionService.partialUpdate(sanction);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sanction.getId().toString())
        );
    }

    /**
     * {@code GET  /sanctions} : get all the sanctions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sanctions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Sanction>> getAllSanctions(
        SanctionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Sanctions by criteria: {}", criteria);

        Page<Sanction> page = sanctionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sanctions/count} : count all the sanctions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSanctions(SanctionCriteria criteria) {
        log.debug("REST request to count Sanctions by criteria: {}", criteria);
        return ResponseEntity.ok().body(sanctionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sanctions/:id} : get the "id" sanction.
     *
     * @param id the id of the sanction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sanction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Sanction> getSanction(@PathVariable Long id) {
        log.debug("REST request to get Sanction : {}", id);
        Optional<Sanction> sanction = sanctionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sanction);
    }

    /**
     * {@code DELETE  /sanctions/:id} : delete the "id" sanction.
     *
     * @param id the id of the sanction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSanction(@PathVariable Long id) {
        log.debug("REST request to delete Sanction : {}", id);
        sanctionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

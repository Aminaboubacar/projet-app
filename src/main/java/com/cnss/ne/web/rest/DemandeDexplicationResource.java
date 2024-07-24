package com.cnss.ne.web.rest;

import com.cnss.ne.domain.DemandeDexplication;
import com.cnss.ne.repository.DemandeDexplicationRepository;
import com.cnss.ne.service.DemandeDexplicationQueryService;
import com.cnss.ne.service.DemandeDexplicationService;
import com.cnss.ne.service.criteria.DemandeDexplicationCriteria;
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
 * REST controller for managing {@link com.cnss.ne.domain.DemandeDexplication}.
 */
@RestController
@RequestMapping("/api/demande-dexplications")
public class DemandeDexplicationResource {

    private final Logger log = LoggerFactory.getLogger(DemandeDexplicationResource.class);

    private static final String ENTITY_NAME = "demandeDexplication";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DemandeDexplicationService demandeDexplicationService;

    private final DemandeDexplicationRepository demandeDexplicationRepository;

    private final DemandeDexplicationQueryService demandeDexplicationQueryService;

    public DemandeDexplicationResource(
        DemandeDexplicationService demandeDexplicationService,
        DemandeDexplicationRepository demandeDexplicationRepository,
        DemandeDexplicationQueryService demandeDexplicationQueryService
    ) {
        this.demandeDexplicationService = demandeDexplicationService;
        this.demandeDexplicationRepository = demandeDexplicationRepository;
        this.demandeDexplicationQueryService = demandeDexplicationQueryService;
    }

    /**
     * {@code POST  /demande-dexplications} : Create a new demandeDexplication.
     *
     * @param demandeDexplication the demandeDexplication to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new demandeDexplication, or with status {@code 400 (Bad Request)} if the demandeDexplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DemandeDexplication> createDemandeDexplication(@Valid @RequestBody DemandeDexplication demandeDexplication)
        throws URISyntaxException {
        log.debug("REST request to save DemandeDexplication : {}", demandeDexplication);
        if (demandeDexplication.getId() != null) {
            throw new BadRequestAlertException("A new demandeDexplication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DemandeDexplication result = demandeDexplicationService.save(demandeDexplication);
        return ResponseEntity
            .created(new URI("/api/demande-dexplications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /demande-dexplications/:id} : Updates an existing demandeDexplication.
     *
     * @param id the id of the demandeDexplication to save.
     * @param demandeDexplication the demandeDexplication to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeDexplication,
     * or with status {@code 400 (Bad Request)} if the demandeDexplication is not valid,
     * or with status {@code 500 (Internal Server Error)} if the demandeDexplication couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DemandeDexplication> updateDemandeDexplication(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DemandeDexplication demandeDexplication
    ) throws URISyntaxException {
        log.debug("REST request to update DemandeDexplication : {}, {}", id, demandeDexplication);
        if (demandeDexplication.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeDexplication.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeDexplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DemandeDexplication result = demandeDexplicationService.update(demandeDexplication);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, demandeDexplication.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /demande-dexplications/:id} : Partial updates given fields of an existing demandeDexplication, field will ignore if it is null
     *
     * @param id the id of the demandeDexplication to save.
     * @param demandeDexplication the demandeDexplication to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeDexplication,
     * or with status {@code 400 (Bad Request)} if the demandeDexplication is not valid,
     * or with status {@code 404 (Not Found)} if the demandeDexplication is not found,
     * or with status {@code 500 (Internal Server Error)} if the demandeDexplication couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DemandeDexplication> partialUpdateDemandeDexplication(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DemandeDexplication demandeDexplication
    ) throws URISyntaxException {
        log.debug("REST request to partial update DemandeDexplication partially : {}, {}", id, demandeDexplication);
        if (demandeDexplication.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeDexplication.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeDexplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DemandeDexplication> result = demandeDexplicationService.partialUpdate(demandeDexplication);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, demandeDexplication.getId().toString())
        );
    }

    /**
     * {@code GET  /demande-dexplications} : get all the demandeDexplications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demandeDexplications in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DemandeDexplication>> getAllDemandeDexplications(
        DemandeDexplicationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DemandeDexplications by criteria: {}", criteria);

        Page<DemandeDexplication> page = demandeDexplicationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /demande-dexplications/count} : count all the demandeDexplications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDemandeDexplications(DemandeDexplicationCriteria criteria) {
        log.debug("REST request to count DemandeDexplications by criteria: {}", criteria);
        return ResponseEntity.ok().body(demandeDexplicationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /demande-dexplications/:id} : get the "id" demandeDexplication.
     *
     * @param id the id of the demandeDexplication to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the demandeDexplication, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DemandeDexplication> getDemandeDexplication(@PathVariable Long id) {
        log.debug("REST request to get DemandeDexplication : {}", id);
        Optional<DemandeDexplication> demandeDexplication = demandeDexplicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(demandeDexplication);
    }

    /**
     * {@code DELETE  /demande-dexplications/:id} : delete the "id" demandeDexplication.
     *
     * @param id the id of the demandeDexplication to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDemandeDexplication(@PathVariable Long id) {
        log.debug("REST request to delete DemandeDexplication : {}", id);
        demandeDexplicationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

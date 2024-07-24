package com.cnss.ne.web.rest;

import com.cnss.ne.domain.Sanctionner;
import com.cnss.ne.repository.SanctionnerRepository;
import com.cnss.ne.service.SanctionnerQueryService;
import com.cnss.ne.service.SanctionnerService;
import com.cnss.ne.service.criteria.SanctionnerCriteria;
import com.cnss.ne.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.cnss.ne.domain.Sanctionner}.
 */
@RestController
@RequestMapping("/api/sanctionners")
public class SanctionnerResource {

    private final Logger log = LoggerFactory.getLogger(SanctionnerResource.class);

    private static final String ENTITY_NAME = "sanctionner";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SanctionnerService sanctionnerService;

    private final SanctionnerRepository sanctionnerRepository;

    private final SanctionnerQueryService sanctionnerQueryService;

    public SanctionnerResource(
        SanctionnerService sanctionnerService,
        SanctionnerRepository sanctionnerRepository,
        SanctionnerQueryService sanctionnerQueryService
    ) {
        this.sanctionnerService = sanctionnerService;
        this.sanctionnerRepository = sanctionnerRepository;
        this.sanctionnerQueryService = sanctionnerQueryService;
    }

    /**
     * {@code POST  /sanctionners} : Create a new sanctionner.
     *
     * @param sanctionner the sanctionner to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sanctionner, or with status {@code 400 (Bad Request)} if the sanctionner has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Sanctionner> createSanctionner(@RequestBody Sanctionner sanctionner) throws URISyntaxException {
        log.debug("REST request to save Sanctionner : {}", sanctionner);
        if (sanctionner.getId() != null) {
            throw new BadRequestAlertException("A new sanctionner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sanctionner result = sanctionnerService.save(sanctionner);
        return ResponseEntity
            .created(new URI("/api/sanctionners/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sanctionners/:id} : Updates an existing sanctionner.
     *
     * @param id the id of the sanctionner to save.
     * @param sanctionner the sanctionner to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sanctionner,
     * or with status {@code 400 (Bad Request)} if the sanctionner is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sanctionner couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Sanctionner> updateSanctionner(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Sanctionner sanctionner
    ) throws URISyntaxException {
        log.debug("REST request to update Sanctionner : {}, {}", id, sanctionner);
        if (sanctionner.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sanctionner.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sanctionnerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Sanctionner result = sanctionnerService.update(sanctionner);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sanctionner.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sanctionners/:id} : Partial updates given fields of an existing sanctionner, field will ignore if it is null
     *
     * @param id the id of the sanctionner to save.
     * @param sanctionner the sanctionner to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sanctionner,
     * or with status {@code 400 (Bad Request)} if the sanctionner is not valid,
     * or with status {@code 404 (Not Found)} if the sanctionner is not found,
     * or with status {@code 500 (Internal Server Error)} if the sanctionner couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Sanctionner> partialUpdateSanctionner(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Sanctionner sanctionner
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sanctionner partially : {}, {}", id, sanctionner);
        if (sanctionner.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sanctionner.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sanctionnerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Sanctionner> result = sanctionnerService.partialUpdate(sanctionner);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sanctionner.getId().toString())
        );
    }

    /**
     * {@code GET  /sanctionners} : get all the sanctionners.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sanctionners in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Sanctionner>> getAllSanctionners(
        SanctionnerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Sanctionners by criteria: {}", criteria);

        Page<Sanctionner> page = sanctionnerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sanctionners/count} : count all the sanctionners.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSanctionners(SanctionnerCriteria criteria) {
        log.debug("REST request to count Sanctionners by criteria: {}", criteria);
        return ResponseEntity.ok().body(sanctionnerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sanctionners/:id} : get the "id" sanctionner.
     *
     * @param id the id of the sanctionner to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sanctionner, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Sanctionner> getSanctionner(@PathVariable Long id) {
        log.debug("REST request to get Sanctionner : {}", id);
        Optional<Sanctionner> sanctionner = sanctionnerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sanctionner);
    }

    /**
     * {@code DELETE  /sanctionners/:id} : delete the "id" sanctionner.
     *
     * @param id the id of the sanctionner to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSanctionner(@PathVariable Long id) {
        log.debug("REST request to delete Sanctionner : {}", id);
        sanctionnerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

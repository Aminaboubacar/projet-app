package com.cnss.ne.web.rest;

import com.cnss.ne.domain.Degre;
import com.cnss.ne.repository.DegreRepository;
import com.cnss.ne.service.DegreQueryService;
import com.cnss.ne.service.DegreService;
import com.cnss.ne.service.criteria.DegreCriteria;
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
 * REST controller for managing {@link com.cnss.ne.domain.Degre}.
 */
@RestController
@RequestMapping("/api/degres")
public class DegreResource {

    private final Logger log = LoggerFactory.getLogger(DegreResource.class);

    private static final String ENTITY_NAME = "degre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DegreService degreService;

    private final DegreRepository degreRepository;

    private final DegreQueryService degreQueryService;

    public DegreResource(DegreService degreService, DegreRepository degreRepository, DegreQueryService degreQueryService) {
        this.degreService = degreService;
        this.degreRepository = degreRepository;
        this.degreQueryService = degreQueryService;
    }

    /**
     * {@code POST  /degres} : Create a new degre.
     *
     * @param degre the degre to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new degre, or with status {@code 400 (Bad Request)} if the degre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Degre> createDegre(@Valid @RequestBody Degre degre) throws URISyntaxException {
        log.debug("REST request to save Degre : {}", degre);
        if (degre.getId() != null) {
            throw new BadRequestAlertException("A new degre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Degre result = degreService.save(degre);
        return ResponseEntity
            .created(new URI("/api/degres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /degres/:id} : Updates an existing degre.
     *
     * @param id the id of the degre to save.
     * @param degre the degre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated degre,
     * or with status {@code 400 (Bad Request)} if the degre is not valid,
     * or with status {@code 500 (Internal Server Error)} if the degre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Degre> updateDegre(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Degre degre)
        throws URISyntaxException {
        log.debug("REST request to update Degre : {}, {}", id, degre);
        if (degre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, degre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!degreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Degre result = degreService.update(degre);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, degre.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /degres/:id} : Partial updates given fields of an existing degre, field will ignore if it is null
     *
     * @param id the id of the degre to save.
     * @param degre the degre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated degre,
     * or with status {@code 400 (Bad Request)} if the degre is not valid,
     * or with status {@code 404 (Not Found)} if the degre is not found,
     * or with status {@code 500 (Internal Server Error)} if the degre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Degre> partialUpdateDegre(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Degre degre
    ) throws URISyntaxException {
        log.debug("REST request to partial update Degre partially : {}, {}", id, degre);
        if (degre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, degre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!degreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Degre> result = degreService.partialUpdate(degre);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, degre.getId().toString())
        );
    }

    /**
     * {@code GET  /degres} : get all the degres.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of degres in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Degre>> getAllDegres(
        DegreCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Degres by criteria: {}", criteria);

        Page<Degre> page = degreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /degres/count} : count all the degres.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDegres(DegreCriteria criteria) {
        log.debug("REST request to count Degres by criteria: {}", criteria);
        return ResponseEntity.ok().body(degreQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /degres/:id} : get the "id" degre.
     *
     * @param id the id of the degre to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the degre, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Degre> getDegre(@PathVariable Long id) {
        log.debug("REST request to get Degre : {}", id);
        Optional<Degre> degre = degreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(degre);
    }

    /**
     * {@code DELETE  /degres/:id} : delete the "id" degre.
     *
     * @param id the id of the degre to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDegre(@PathVariable Long id) {
        log.debug("REST request to delete Degre : {}", id);
        degreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

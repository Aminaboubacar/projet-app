package com.cnss.ne.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cnss.ne.IntegrationTest;
import com.cnss.ne.domain.Disponibilite;
import com.cnss.ne.domain.MotifDisponibilite;
import com.cnss.ne.repository.MotifDisponibiliteRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MotifDisponibiliteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MotifDisponibiliteResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/motif-disponibilites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MotifDisponibiliteRepository motifDisponibiliteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMotifDisponibiliteMockMvc;

    private MotifDisponibilite motifDisponibilite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MotifDisponibilite createEntity(EntityManager em) {
        MotifDisponibilite motifDisponibilite = new MotifDisponibilite().code(DEFAULT_CODE).libelle(DEFAULT_LIBELLE);
        return motifDisponibilite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MotifDisponibilite createUpdatedEntity(EntityManager em) {
        MotifDisponibilite motifDisponibilite = new MotifDisponibilite().code(UPDATED_CODE).libelle(UPDATED_LIBELLE);
        return motifDisponibilite;
    }

    @BeforeEach
    public void initTest() {
        motifDisponibilite = createEntity(em);
    }

    @Test
    @Transactional
    void createMotifDisponibilite() throws Exception {
        int databaseSizeBeforeCreate = motifDisponibiliteRepository.findAll().size();
        // Create the MotifDisponibilite
        restMotifDisponibiliteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(motifDisponibilite))
            )
            .andExpect(status().isCreated());

        // Validate the MotifDisponibilite in the database
        List<MotifDisponibilite> motifDisponibiliteList = motifDisponibiliteRepository.findAll();
        assertThat(motifDisponibiliteList).hasSize(databaseSizeBeforeCreate + 1);
        MotifDisponibilite testMotifDisponibilite = motifDisponibiliteList.get(motifDisponibiliteList.size() - 1);
        assertThat(testMotifDisponibilite.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMotifDisponibilite.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void createMotifDisponibiliteWithExistingId() throws Exception {
        // Create the MotifDisponibilite with an existing ID
        motifDisponibilite.setId(1L);

        int databaseSizeBeforeCreate = motifDisponibiliteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMotifDisponibiliteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(motifDisponibilite))
            )
            .andExpect(status().isBadRequest());

        // Validate the MotifDisponibilite in the database
        List<MotifDisponibilite> motifDisponibiliteList = motifDisponibiliteRepository.findAll();
        assertThat(motifDisponibiliteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = motifDisponibiliteRepository.findAll().size();
        // set the field null
        motifDisponibilite.setCode(null);

        // Create the MotifDisponibilite, which fails.

        restMotifDisponibiliteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(motifDisponibilite))
            )
            .andExpect(status().isBadRequest());

        List<MotifDisponibilite> motifDisponibiliteList = motifDisponibiliteRepository.findAll();
        assertThat(motifDisponibiliteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = motifDisponibiliteRepository.findAll().size();
        // set the field null
        motifDisponibilite.setLibelle(null);

        // Create the MotifDisponibilite, which fails.

        restMotifDisponibiliteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(motifDisponibilite))
            )
            .andExpect(status().isBadRequest());

        List<MotifDisponibilite> motifDisponibiliteList = motifDisponibiliteRepository.findAll();
        assertThat(motifDisponibiliteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMotifDisponibilites() throws Exception {
        // Initialize the database
        motifDisponibiliteRepository.saveAndFlush(motifDisponibilite);

        // Get all the motifDisponibiliteList
        restMotifDisponibiliteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motifDisponibilite.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getMotifDisponibilite() throws Exception {
        // Initialize the database
        motifDisponibiliteRepository.saveAndFlush(motifDisponibilite);

        // Get the motifDisponibilite
        restMotifDisponibiliteMockMvc
            .perform(get(ENTITY_API_URL_ID, motifDisponibilite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(motifDisponibilite.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getMotifDisponibilitesByIdFiltering() throws Exception {
        // Initialize the database
        motifDisponibiliteRepository.saveAndFlush(motifDisponibilite);

        Long id = motifDisponibilite.getId();

        defaultMotifDisponibiliteShouldBeFound("id.equals=" + id);
        defaultMotifDisponibiliteShouldNotBeFound("id.notEquals=" + id);

        defaultMotifDisponibiliteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMotifDisponibiliteShouldNotBeFound("id.greaterThan=" + id);

        defaultMotifDisponibiliteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMotifDisponibiliteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMotifDisponibilitesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        motifDisponibiliteRepository.saveAndFlush(motifDisponibilite);

        // Get all the motifDisponibiliteList where code equals to DEFAULT_CODE
        defaultMotifDisponibiliteShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the motifDisponibiliteList where code equals to UPDATED_CODE
        defaultMotifDisponibiliteShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMotifDisponibilitesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        motifDisponibiliteRepository.saveAndFlush(motifDisponibilite);

        // Get all the motifDisponibiliteList where code in DEFAULT_CODE or UPDATED_CODE
        defaultMotifDisponibiliteShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the motifDisponibiliteList where code equals to UPDATED_CODE
        defaultMotifDisponibiliteShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMotifDisponibilitesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        motifDisponibiliteRepository.saveAndFlush(motifDisponibilite);

        // Get all the motifDisponibiliteList where code is not null
        defaultMotifDisponibiliteShouldBeFound("code.specified=true");

        // Get all the motifDisponibiliteList where code is null
        defaultMotifDisponibiliteShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllMotifDisponibilitesByCodeContainsSomething() throws Exception {
        // Initialize the database
        motifDisponibiliteRepository.saveAndFlush(motifDisponibilite);

        // Get all the motifDisponibiliteList where code contains DEFAULT_CODE
        defaultMotifDisponibiliteShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the motifDisponibiliteList where code contains UPDATED_CODE
        defaultMotifDisponibiliteShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMotifDisponibilitesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        motifDisponibiliteRepository.saveAndFlush(motifDisponibilite);

        // Get all the motifDisponibiliteList where code does not contain DEFAULT_CODE
        defaultMotifDisponibiliteShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the motifDisponibiliteList where code does not contain UPDATED_CODE
        defaultMotifDisponibiliteShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMotifDisponibilitesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        motifDisponibiliteRepository.saveAndFlush(motifDisponibilite);

        // Get all the motifDisponibiliteList where libelle equals to DEFAULT_LIBELLE
        defaultMotifDisponibiliteShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the motifDisponibiliteList where libelle equals to UPDATED_LIBELLE
        defaultMotifDisponibiliteShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllMotifDisponibilitesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        motifDisponibiliteRepository.saveAndFlush(motifDisponibilite);

        // Get all the motifDisponibiliteList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultMotifDisponibiliteShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the motifDisponibiliteList where libelle equals to UPDATED_LIBELLE
        defaultMotifDisponibiliteShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllMotifDisponibilitesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        motifDisponibiliteRepository.saveAndFlush(motifDisponibilite);

        // Get all the motifDisponibiliteList where libelle is not null
        defaultMotifDisponibiliteShouldBeFound("libelle.specified=true");

        // Get all the motifDisponibiliteList where libelle is null
        defaultMotifDisponibiliteShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllMotifDisponibilitesByLibelleContainsSomething() throws Exception {
        // Initialize the database
        motifDisponibiliteRepository.saveAndFlush(motifDisponibilite);

        // Get all the motifDisponibiliteList where libelle contains DEFAULT_LIBELLE
        defaultMotifDisponibiliteShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the motifDisponibiliteList where libelle contains UPDATED_LIBELLE
        defaultMotifDisponibiliteShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllMotifDisponibilitesByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        motifDisponibiliteRepository.saveAndFlush(motifDisponibilite);

        // Get all the motifDisponibiliteList where libelle does not contain DEFAULT_LIBELLE
        defaultMotifDisponibiliteShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the motifDisponibiliteList where libelle does not contain UPDATED_LIBELLE
        defaultMotifDisponibiliteShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllMotifDisponibilitesByDisponibiliteIsEqualToSomething() throws Exception {
        Disponibilite disponibilite;
        if (TestUtil.findAll(em, Disponibilite.class).isEmpty()) {
            motifDisponibiliteRepository.saveAndFlush(motifDisponibilite);
            disponibilite = DisponibiliteResourceIT.createEntity(em);
        } else {
            disponibilite = TestUtil.findAll(em, Disponibilite.class).get(0);
        }
        em.persist(disponibilite);
        em.flush();
        motifDisponibilite.addDisponibilite(disponibilite);
        motifDisponibiliteRepository.saveAndFlush(motifDisponibilite);
        Long disponibiliteId = disponibilite.getId();
        // Get all the motifDisponibiliteList where disponibilite equals to disponibiliteId
        defaultMotifDisponibiliteShouldBeFound("disponibiliteId.equals=" + disponibiliteId);

        // Get all the motifDisponibiliteList where disponibilite equals to (disponibiliteId + 1)
        defaultMotifDisponibiliteShouldNotBeFound("disponibiliteId.equals=" + (disponibiliteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMotifDisponibiliteShouldBeFound(String filter) throws Exception {
        restMotifDisponibiliteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motifDisponibilite.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));

        // Check, that the count call also returns 1
        restMotifDisponibiliteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMotifDisponibiliteShouldNotBeFound(String filter) throws Exception {
        restMotifDisponibiliteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMotifDisponibiliteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMotifDisponibilite() throws Exception {
        // Get the motifDisponibilite
        restMotifDisponibiliteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMotifDisponibilite() throws Exception {
        // Initialize the database
        motifDisponibiliteRepository.saveAndFlush(motifDisponibilite);

        int databaseSizeBeforeUpdate = motifDisponibiliteRepository.findAll().size();

        // Update the motifDisponibilite
        MotifDisponibilite updatedMotifDisponibilite = motifDisponibiliteRepository.findById(motifDisponibilite.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMotifDisponibilite are not directly saved in db
        em.detach(updatedMotifDisponibilite);
        updatedMotifDisponibilite.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);

        restMotifDisponibiliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMotifDisponibilite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMotifDisponibilite))
            )
            .andExpect(status().isOk());

        // Validate the MotifDisponibilite in the database
        List<MotifDisponibilite> motifDisponibiliteList = motifDisponibiliteRepository.findAll();
        assertThat(motifDisponibiliteList).hasSize(databaseSizeBeforeUpdate);
        MotifDisponibilite testMotifDisponibilite = motifDisponibiliteList.get(motifDisponibiliteList.size() - 1);
        assertThat(testMotifDisponibilite.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMotifDisponibilite.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void putNonExistingMotifDisponibilite() throws Exception {
        int databaseSizeBeforeUpdate = motifDisponibiliteRepository.findAll().size();
        motifDisponibilite.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMotifDisponibiliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, motifDisponibilite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motifDisponibilite))
            )
            .andExpect(status().isBadRequest());

        // Validate the MotifDisponibilite in the database
        List<MotifDisponibilite> motifDisponibiliteList = motifDisponibiliteRepository.findAll();
        assertThat(motifDisponibiliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMotifDisponibilite() throws Exception {
        int databaseSizeBeforeUpdate = motifDisponibiliteRepository.findAll().size();
        motifDisponibilite.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotifDisponibiliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motifDisponibilite))
            )
            .andExpect(status().isBadRequest());

        // Validate the MotifDisponibilite in the database
        List<MotifDisponibilite> motifDisponibiliteList = motifDisponibiliteRepository.findAll();
        assertThat(motifDisponibiliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMotifDisponibilite() throws Exception {
        int databaseSizeBeforeUpdate = motifDisponibiliteRepository.findAll().size();
        motifDisponibilite.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotifDisponibiliteMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(motifDisponibilite))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MotifDisponibilite in the database
        List<MotifDisponibilite> motifDisponibiliteList = motifDisponibiliteRepository.findAll();
        assertThat(motifDisponibiliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMotifDisponibiliteWithPatch() throws Exception {
        // Initialize the database
        motifDisponibiliteRepository.saveAndFlush(motifDisponibilite);

        int databaseSizeBeforeUpdate = motifDisponibiliteRepository.findAll().size();

        // Update the motifDisponibilite using partial update
        MotifDisponibilite partialUpdatedMotifDisponibilite = new MotifDisponibilite();
        partialUpdatedMotifDisponibilite.setId(motifDisponibilite.getId());

        restMotifDisponibiliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMotifDisponibilite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMotifDisponibilite))
            )
            .andExpect(status().isOk());

        // Validate the MotifDisponibilite in the database
        List<MotifDisponibilite> motifDisponibiliteList = motifDisponibiliteRepository.findAll();
        assertThat(motifDisponibiliteList).hasSize(databaseSizeBeforeUpdate);
        MotifDisponibilite testMotifDisponibilite = motifDisponibiliteList.get(motifDisponibiliteList.size() - 1);
        assertThat(testMotifDisponibilite.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMotifDisponibilite.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateMotifDisponibiliteWithPatch() throws Exception {
        // Initialize the database
        motifDisponibiliteRepository.saveAndFlush(motifDisponibilite);

        int databaseSizeBeforeUpdate = motifDisponibiliteRepository.findAll().size();

        // Update the motifDisponibilite using partial update
        MotifDisponibilite partialUpdatedMotifDisponibilite = new MotifDisponibilite();
        partialUpdatedMotifDisponibilite.setId(motifDisponibilite.getId());

        partialUpdatedMotifDisponibilite.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);

        restMotifDisponibiliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMotifDisponibilite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMotifDisponibilite))
            )
            .andExpect(status().isOk());

        // Validate the MotifDisponibilite in the database
        List<MotifDisponibilite> motifDisponibiliteList = motifDisponibiliteRepository.findAll();
        assertThat(motifDisponibiliteList).hasSize(databaseSizeBeforeUpdate);
        MotifDisponibilite testMotifDisponibilite = motifDisponibiliteList.get(motifDisponibiliteList.size() - 1);
        assertThat(testMotifDisponibilite.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMotifDisponibilite.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingMotifDisponibilite() throws Exception {
        int databaseSizeBeforeUpdate = motifDisponibiliteRepository.findAll().size();
        motifDisponibilite.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMotifDisponibiliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, motifDisponibilite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(motifDisponibilite))
            )
            .andExpect(status().isBadRequest());

        // Validate the MotifDisponibilite in the database
        List<MotifDisponibilite> motifDisponibiliteList = motifDisponibiliteRepository.findAll();
        assertThat(motifDisponibiliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMotifDisponibilite() throws Exception {
        int databaseSizeBeforeUpdate = motifDisponibiliteRepository.findAll().size();
        motifDisponibilite.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotifDisponibiliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(motifDisponibilite))
            )
            .andExpect(status().isBadRequest());

        // Validate the MotifDisponibilite in the database
        List<MotifDisponibilite> motifDisponibiliteList = motifDisponibiliteRepository.findAll();
        assertThat(motifDisponibiliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMotifDisponibilite() throws Exception {
        int databaseSizeBeforeUpdate = motifDisponibiliteRepository.findAll().size();
        motifDisponibilite.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotifDisponibiliteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(motifDisponibilite))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MotifDisponibilite in the database
        List<MotifDisponibilite> motifDisponibiliteList = motifDisponibiliteRepository.findAll();
        assertThat(motifDisponibiliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMotifDisponibilite() throws Exception {
        // Initialize the database
        motifDisponibiliteRepository.saveAndFlush(motifDisponibilite);

        int databaseSizeBeforeDelete = motifDisponibiliteRepository.findAll().size();

        // Delete the motifDisponibilite
        restMotifDisponibiliteMockMvc
            .perform(delete(ENTITY_API_URL_ID, motifDisponibilite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MotifDisponibilite> motifDisponibiliteList = motifDisponibiliteRepository.findAll();
        assertThat(motifDisponibiliteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

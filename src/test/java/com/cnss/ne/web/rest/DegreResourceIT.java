package com.cnss.ne.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cnss.ne.IntegrationTest;
import com.cnss.ne.domain.Degre;
import com.cnss.ne.domain.Sanction;
import com.cnss.ne.repository.DegreRepository;
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
 * Integration tests for the {@link DegreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DegreResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/degres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DegreRepository degreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDegreMockMvc;

    private Degre degre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Degre createEntity(EntityManager em) {
        Degre degre = new Degre().code(DEFAULT_CODE).libelle(DEFAULT_LIBELLE);
        return degre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Degre createUpdatedEntity(EntityManager em) {
        Degre degre = new Degre().code(UPDATED_CODE).libelle(UPDATED_LIBELLE);
        return degre;
    }

    @BeforeEach
    public void initTest() {
        degre = createEntity(em);
    }

    @Test
    @Transactional
    void createDegre() throws Exception {
        int databaseSizeBeforeCreate = degreRepository.findAll().size();
        // Create the Degre
        restDegreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(degre)))
            .andExpect(status().isCreated());

        // Validate the Degre in the database
        List<Degre> degreList = degreRepository.findAll();
        assertThat(degreList).hasSize(databaseSizeBeforeCreate + 1);
        Degre testDegre = degreList.get(degreList.size() - 1);
        assertThat(testDegre.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDegre.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void createDegreWithExistingId() throws Exception {
        // Create the Degre with an existing ID
        degre.setId(1L);

        int databaseSizeBeforeCreate = degreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDegreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(degre)))
            .andExpect(status().isBadRequest());

        // Validate the Degre in the database
        List<Degre> degreList = degreRepository.findAll();
        assertThat(degreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = degreRepository.findAll().size();
        // set the field null
        degre.setCode(null);

        // Create the Degre, which fails.

        restDegreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(degre)))
            .andExpect(status().isBadRequest());

        List<Degre> degreList = degreRepository.findAll();
        assertThat(degreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = degreRepository.findAll().size();
        // set the field null
        degre.setLibelle(null);

        // Create the Degre, which fails.

        restDegreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(degre)))
            .andExpect(status().isBadRequest());

        List<Degre> degreList = degreRepository.findAll();
        assertThat(degreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDegres() throws Exception {
        // Initialize the database
        degreRepository.saveAndFlush(degre);

        // Get all the degreList
        restDegreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(degre.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getDegre() throws Exception {
        // Initialize the database
        degreRepository.saveAndFlush(degre);

        // Get the degre
        restDegreMockMvc
            .perform(get(ENTITY_API_URL_ID, degre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(degre.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getDegresByIdFiltering() throws Exception {
        // Initialize the database
        degreRepository.saveAndFlush(degre);

        Long id = degre.getId();

        defaultDegreShouldBeFound("id.equals=" + id);
        defaultDegreShouldNotBeFound("id.notEquals=" + id);

        defaultDegreShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDegreShouldNotBeFound("id.greaterThan=" + id);

        defaultDegreShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDegreShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDegresByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        degreRepository.saveAndFlush(degre);

        // Get all the degreList where code equals to DEFAULT_CODE
        defaultDegreShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the degreList where code equals to UPDATED_CODE
        defaultDegreShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDegresByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        degreRepository.saveAndFlush(degre);

        // Get all the degreList where code in DEFAULT_CODE or UPDATED_CODE
        defaultDegreShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the degreList where code equals to UPDATED_CODE
        defaultDegreShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDegresByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        degreRepository.saveAndFlush(degre);

        // Get all the degreList where code is not null
        defaultDegreShouldBeFound("code.specified=true");

        // Get all the degreList where code is null
        defaultDegreShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllDegresByCodeContainsSomething() throws Exception {
        // Initialize the database
        degreRepository.saveAndFlush(degre);

        // Get all the degreList where code contains DEFAULT_CODE
        defaultDegreShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the degreList where code contains UPDATED_CODE
        defaultDegreShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDegresByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        degreRepository.saveAndFlush(degre);

        // Get all the degreList where code does not contain DEFAULT_CODE
        defaultDegreShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the degreList where code does not contain UPDATED_CODE
        defaultDegreShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDegresByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        degreRepository.saveAndFlush(degre);

        // Get all the degreList where libelle equals to DEFAULT_LIBELLE
        defaultDegreShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the degreList where libelle equals to UPDATED_LIBELLE
        defaultDegreShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllDegresByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        degreRepository.saveAndFlush(degre);

        // Get all the degreList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultDegreShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the degreList where libelle equals to UPDATED_LIBELLE
        defaultDegreShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllDegresByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        degreRepository.saveAndFlush(degre);

        // Get all the degreList where libelle is not null
        defaultDegreShouldBeFound("libelle.specified=true");

        // Get all the degreList where libelle is null
        defaultDegreShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllDegresByLibelleContainsSomething() throws Exception {
        // Initialize the database
        degreRepository.saveAndFlush(degre);

        // Get all the degreList where libelle contains DEFAULT_LIBELLE
        defaultDegreShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the degreList where libelle contains UPDATED_LIBELLE
        defaultDegreShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllDegresByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        degreRepository.saveAndFlush(degre);

        // Get all the degreList where libelle does not contain DEFAULT_LIBELLE
        defaultDegreShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the degreList where libelle does not contain UPDATED_LIBELLE
        defaultDegreShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllDegresBySanctionIsEqualToSomething() throws Exception {
        Sanction sanction;
        if (TestUtil.findAll(em, Sanction.class).isEmpty()) {
            degreRepository.saveAndFlush(degre);
            sanction = SanctionResourceIT.createEntity(em);
        } else {
            sanction = TestUtil.findAll(em, Sanction.class).get(0);
        }
        em.persist(sanction);
        em.flush();
        degre.addSanction(sanction);
        degreRepository.saveAndFlush(degre);
        Long sanctionId = sanction.getId();
        // Get all the degreList where sanction equals to sanctionId
        defaultDegreShouldBeFound("sanctionId.equals=" + sanctionId);

        // Get all the degreList where sanction equals to (sanctionId + 1)
        defaultDegreShouldNotBeFound("sanctionId.equals=" + (sanctionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDegreShouldBeFound(String filter) throws Exception {
        restDegreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(degre.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));

        // Check, that the count call also returns 1
        restDegreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDegreShouldNotBeFound(String filter) throws Exception {
        restDegreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDegreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDegre() throws Exception {
        // Get the degre
        restDegreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDegre() throws Exception {
        // Initialize the database
        degreRepository.saveAndFlush(degre);

        int databaseSizeBeforeUpdate = degreRepository.findAll().size();

        // Update the degre
        Degre updatedDegre = degreRepository.findById(degre.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDegre are not directly saved in db
        em.detach(updatedDegre);
        updatedDegre.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);

        restDegreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDegre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDegre))
            )
            .andExpect(status().isOk());

        // Validate the Degre in the database
        List<Degre> degreList = degreRepository.findAll();
        assertThat(degreList).hasSize(databaseSizeBeforeUpdate);
        Degre testDegre = degreList.get(degreList.size() - 1);
        assertThat(testDegre.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDegre.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void putNonExistingDegre() throws Exception {
        int databaseSizeBeforeUpdate = degreRepository.findAll().size();
        degre.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDegreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, degre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(degre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Degre in the database
        List<Degre> degreList = degreRepository.findAll();
        assertThat(degreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDegre() throws Exception {
        int databaseSizeBeforeUpdate = degreRepository.findAll().size();
        degre.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDegreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(degre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Degre in the database
        List<Degre> degreList = degreRepository.findAll();
        assertThat(degreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDegre() throws Exception {
        int databaseSizeBeforeUpdate = degreRepository.findAll().size();
        degre.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDegreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(degre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Degre in the database
        List<Degre> degreList = degreRepository.findAll();
        assertThat(degreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDegreWithPatch() throws Exception {
        // Initialize the database
        degreRepository.saveAndFlush(degre);

        int databaseSizeBeforeUpdate = degreRepository.findAll().size();

        // Update the degre using partial update
        Degre partialUpdatedDegre = new Degre();
        partialUpdatedDegre.setId(degre.getId());

        partialUpdatedDegre.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);

        restDegreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDegre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDegre))
            )
            .andExpect(status().isOk());

        // Validate the Degre in the database
        List<Degre> degreList = degreRepository.findAll();
        assertThat(degreList).hasSize(databaseSizeBeforeUpdate);
        Degre testDegre = degreList.get(degreList.size() - 1);
        assertThat(testDegre.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDegre.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateDegreWithPatch() throws Exception {
        // Initialize the database
        degreRepository.saveAndFlush(degre);

        int databaseSizeBeforeUpdate = degreRepository.findAll().size();

        // Update the degre using partial update
        Degre partialUpdatedDegre = new Degre();
        partialUpdatedDegre.setId(degre.getId());

        partialUpdatedDegre.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);

        restDegreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDegre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDegre))
            )
            .andExpect(status().isOk());

        // Validate the Degre in the database
        List<Degre> degreList = degreRepository.findAll();
        assertThat(degreList).hasSize(databaseSizeBeforeUpdate);
        Degre testDegre = degreList.get(degreList.size() - 1);
        assertThat(testDegre.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDegre.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingDegre() throws Exception {
        int databaseSizeBeforeUpdate = degreRepository.findAll().size();
        degre.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDegreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, degre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(degre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Degre in the database
        List<Degre> degreList = degreRepository.findAll();
        assertThat(degreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDegre() throws Exception {
        int databaseSizeBeforeUpdate = degreRepository.findAll().size();
        degre.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDegreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(degre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Degre in the database
        List<Degre> degreList = degreRepository.findAll();
        assertThat(degreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDegre() throws Exception {
        int databaseSizeBeforeUpdate = degreRepository.findAll().size();
        degre.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDegreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(degre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Degre in the database
        List<Degre> degreList = degreRepository.findAll();
        assertThat(degreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDegre() throws Exception {
        // Initialize the database
        degreRepository.saveAndFlush(degre);

        int databaseSizeBeforeDelete = degreRepository.findAll().size();

        // Delete the degre
        restDegreMockMvc
            .perform(delete(ENTITY_API_URL_ID, degre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Degre> degreList = degreRepository.findAll();
        assertThat(degreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

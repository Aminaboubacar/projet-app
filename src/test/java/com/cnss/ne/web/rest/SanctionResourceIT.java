package com.cnss.ne.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cnss.ne.IntegrationTest;
import com.cnss.ne.domain.Degre;
import com.cnss.ne.domain.Sanction;
import com.cnss.ne.domain.Sanctionner;
import com.cnss.ne.repository.SanctionRepository;
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
 * Integration tests for the {@link SanctionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SanctionResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sanctions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SanctionRepository sanctionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSanctionMockMvc;

    private Sanction sanction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sanction createEntity(EntityManager em) {
        Sanction sanction = new Sanction().code(DEFAULT_CODE).libelle(DEFAULT_LIBELLE);
        return sanction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sanction createUpdatedEntity(EntityManager em) {
        Sanction sanction = new Sanction().code(UPDATED_CODE).libelle(UPDATED_LIBELLE);
        return sanction;
    }

    @BeforeEach
    public void initTest() {
        sanction = createEntity(em);
    }

    @Test
    @Transactional
    void createSanction() throws Exception {
        int databaseSizeBeforeCreate = sanctionRepository.findAll().size();
        // Create the Sanction
        restSanctionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sanction)))
            .andExpect(status().isCreated());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeCreate + 1);
        Sanction testSanction = sanctionList.get(sanctionList.size() - 1);
        assertThat(testSanction.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSanction.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void createSanctionWithExistingId() throws Exception {
        // Create the Sanction with an existing ID
        sanction.setId(1L);

        int databaseSizeBeforeCreate = sanctionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSanctionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sanction)))
            .andExpect(status().isBadRequest());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sanctionRepository.findAll().size();
        // set the field null
        sanction.setCode(null);

        // Create the Sanction, which fails.

        restSanctionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sanction)))
            .andExpect(status().isBadRequest());

        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = sanctionRepository.findAll().size();
        // set the field null
        sanction.setLibelle(null);

        // Create the Sanction, which fails.

        restSanctionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sanction)))
            .andExpect(status().isBadRequest());

        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSanctions() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList
        restSanctionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sanction.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getSanction() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get the sanction
        restSanctionMockMvc
            .perform(get(ENTITY_API_URL_ID, sanction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sanction.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getSanctionsByIdFiltering() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        Long id = sanction.getId();

        defaultSanctionShouldBeFound("id.equals=" + id);
        defaultSanctionShouldNotBeFound("id.notEquals=" + id);

        defaultSanctionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSanctionShouldNotBeFound("id.greaterThan=" + id);

        defaultSanctionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSanctionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSanctionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where code equals to DEFAULT_CODE
        defaultSanctionShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the sanctionList where code equals to UPDATED_CODE
        defaultSanctionShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSanctionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where code in DEFAULT_CODE or UPDATED_CODE
        defaultSanctionShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the sanctionList where code equals to UPDATED_CODE
        defaultSanctionShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSanctionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where code is not null
        defaultSanctionShouldBeFound("code.specified=true");

        // Get all the sanctionList where code is null
        defaultSanctionShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllSanctionsByCodeContainsSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where code contains DEFAULT_CODE
        defaultSanctionShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the sanctionList where code contains UPDATED_CODE
        defaultSanctionShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSanctionsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where code does not contain DEFAULT_CODE
        defaultSanctionShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the sanctionList where code does not contain UPDATED_CODE
        defaultSanctionShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSanctionsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where libelle equals to DEFAULT_LIBELLE
        defaultSanctionShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the sanctionList where libelle equals to UPDATED_LIBELLE
        defaultSanctionShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSanctionsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultSanctionShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the sanctionList where libelle equals to UPDATED_LIBELLE
        defaultSanctionShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSanctionsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where libelle is not null
        defaultSanctionShouldBeFound("libelle.specified=true");

        // Get all the sanctionList where libelle is null
        defaultSanctionShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllSanctionsByLibelleContainsSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where libelle contains DEFAULT_LIBELLE
        defaultSanctionShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the sanctionList where libelle contains UPDATED_LIBELLE
        defaultSanctionShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSanctionsByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where libelle does not contain DEFAULT_LIBELLE
        defaultSanctionShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the sanctionList where libelle does not contain UPDATED_LIBELLE
        defaultSanctionShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSanctionsBySanctionnerIsEqualToSomething() throws Exception {
        Sanctionner sanctionner;
        if (TestUtil.findAll(em, Sanctionner.class).isEmpty()) {
            sanctionRepository.saveAndFlush(sanction);
            sanctionner = SanctionnerResourceIT.createEntity(em);
        } else {
            sanctionner = TestUtil.findAll(em, Sanctionner.class).get(0);
        }
        em.persist(sanctionner);
        em.flush();
        sanction.addSanctionner(sanctionner);
        sanctionRepository.saveAndFlush(sanction);
        Long sanctionnerId = sanctionner.getId();
        // Get all the sanctionList where sanctionner equals to sanctionnerId
        defaultSanctionShouldBeFound("sanctionnerId.equals=" + sanctionnerId);

        // Get all the sanctionList where sanctionner equals to (sanctionnerId + 1)
        defaultSanctionShouldNotBeFound("sanctionnerId.equals=" + (sanctionnerId + 1));
    }

    @Test
    @Transactional
    void getAllSanctionsByDegreIsEqualToSomething() throws Exception {
        Degre degre;
        if (TestUtil.findAll(em, Degre.class).isEmpty()) {
            sanctionRepository.saveAndFlush(sanction);
            degre = DegreResourceIT.createEntity(em);
        } else {
            degre = TestUtil.findAll(em, Degre.class).get(0);
        }
        em.persist(degre);
        em.flush();
        sanction.setDegre(degre);
        sanctionRepository.saveAndFlush(sanction);
        Long degreId = degre.getId();
        // Get all the sanctionList where degre equals to degreId
        defaultSanctionShouldBeFound("degreId.equals=" + degreId);

        // Get all the sanctionList where degre equals to (degreId + 1)
        defaultSanctionShouldNotBeFound("degreId.equals=" + (degreId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSanctionShouldBeFound(String filter) throws Exception {
        restSanctionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sanction.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));

        // Check, that the count call also returns 1
        restSanctionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSanctionShouldNotBeFound(String filter) throws Exception {
        restSanctionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSanctionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSanction() throws Exception {
        // Get the sanction
        restSanctionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSanction() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        int databaseSizeBeforeUpdate = sanctionRepository.findAll().size();

        // Update the sanction
        Sanction updatedSanction = sanctionRepository.findById(sanction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSanction are not directly saved in db
        em.detach(updatedSanction);
        updatedSanction.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);

        restSanctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSanction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSanction))
            )
            .andExpect(status().isOk());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeUpdate);
        Sanction testSanction = sanctionList.get(sanctionList.size() - 1);
        assertThat(testSanction.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSanction.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void putNonExistingSanction() throws Exception {
        int databaseSizeBeforeUpdate = sanctionRepository.findAll().size();
        sanction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSanctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sanction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sanction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSanction() throws Exception {
        int databaseSizeBeforeUpdate = sanctionRepository.findAll().size();
        sanction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sanction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSanction() throws Exception {
        int databaseSizeBeforeUpdate = sanctionRepository.findAll().size();
        sanction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanctionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sanction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSanctionWithPatch() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        int databaseSizeBeforeUpdate = sanctionRepository.findAll().size();

        // Update the sanction using partial update
        Sanction partialUpdatedSanction = new Sanction();
        partialUpdatedSanction.setId(sanction.getId());

        restSanctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSanction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSanction))
            )
            .andExpect(status().isOk());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeUpdate);
        Sanction testSanction = sanctionList.get(sanctionList.size() - 1);
        assertThat(testSanction.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSanction.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateSanctionWithPatch() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        int databaseSizeBeforeUpdate = sanctionRepository.findAll().size();

        // Update the sanction using partial update
        Sanction partialUpdatedSanction = new Sanction();
        partialUpdatedSanction.setId(sanction.getId());

        partialUpdatedSanction.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);

        restSanctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSanction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSanction))
            )
            .andExpect(status().isOk());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeUpdate);
        Sanction testSanction = sanctionList.get(sanctionList.size() - 1);
        assertThat(testSanction.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSanction.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingSanction() throws Exception {
        int databaseSizeBeforeUpdate = sanctionRepository.findAll().size();
        sanction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSanctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sanction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sanction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSanction() throws Exception {
        int databaseSizeBeforeUpdate = sanctionRepository.findAll().size();
        sanction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sanction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSanction() throws Exception {
        int databaseSizeBeforeUpdate = sanctionRepository.findAll().size();
        sanction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanctionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sanction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSanction() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        int databaseSizeBeforeDelete = sanctionRepository.findAll().size();

        // Delete the sanction
        restSanctionMockMvc
            .perform(delete(ENTITY_API_URL_ID, sanction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

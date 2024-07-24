package com.cnss.ne.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cnss.ne.IntegrationTest;
import com.cnss.ne.domain.DemandeDexplication;
import com.cnss.ne.domain.Sanction;
import com.cnss.ne.domain.Sanctionner;
import com.cnss.ne.repository.SanctionnerRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link SanctionnerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SanctionnerResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/sanctionners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SanctionnerRepository sanctionnerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSanctionnerMockMvc;

    private Sanctionner sanctionner;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sanctionner createEntity(EntityManager em) {
        Sanctionner sanctionner = new Sanctionner().date(DEFAULT_DATE);
        return sanctionner;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sanctionner createUpdatedEntity(EntityManager em) {
        Sanctionner sanctionner = new Sanctionner().date(UPDATED_DATE);
        return sanctionner;
    }

    @BeforeEach
    public void initTest() {
        sanctionner = createEntity(em);
    }

    @Test
    @Transactional
    void createSanctionner() throws Exception {
        int databaseSizeBeforeCreate = sanctionnerRepository.findAll().size();
        // Create the Sanctionner
        restSanctionnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sanctionner)))
            .andExpect(status().isCreated());

        // Validate the Sanctionner in the database
        List<Sanctionner> sanctionnerList = sanctionnerRepository.findAll();
        assertThat(sanctionnerList).hasSize(databaseSizeBeforeCreate + 1);
        Sanctionner testSanctionner = sanctionnerList.get(sanctionnerList.size() - 1);
        assertThat(testSanctionner.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createSanctionnerWithExistingId() throws Exception {
        // Create the Sanctionner with an existing ID
        sanctionner.setId(1L);

        int databaseSizeBeforeCreate = sanctionnerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSanctionnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sanctionner)))
            .andExpect(status().isBadRequest());

        // Validate the Sanctionner in the database
        List<Sanctionner> sanctionnerList = sanctionnerRepository.findAll();
        assertThat(sanctionnerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSanctionners() throws Exception {
        // Initialize the database
        sanctionnerRepository.saveAndFlush(sanctionner);

        // Get all the sanctionnerList
        restSanctionnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sanctionner.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getSanctionner() throws Exception {
        // Initialize the database
        sanctionnerRepository.saveAndFlush(sanctionner);

        // Get the sanctionner
        restSanctionnerMockMvc
            .perform(get(ENTITY_API_URL_ID, sanctionner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sanctionner.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getSanctionnersByIdFiltering() throws Exception {
        // Initialize the database
        sanctionnerRepository.saveAndFlush(sanctionner);

        Long id = sanctionner.getId();

        defaultSanctionnerShouldBeFound("id.equals=" + id);
        defaultSanctionnerShouldNotBeFound("id.notEquals=" + id);

        defaultSanctionnerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSanctionnerShouldNotBeFound("id.greaterThan=" + id);

        defaultSanctionnerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSanctionnerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSanctionnersByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        sanctionnerRepository.saveAndFlush(sanctionner);

        // Get all the sanctionnerList where date equals to DEFAULT_DATE
        defaultSanctionnerShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the sanctionnerList where date equals to UPDATED_DATE
        defaultSanctionnerShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllSanctionnersByDateIsInShouldWork() throws Exception {
        // Initialize the database
        sanctionnerRepository.saveAndFlush(sanctionner);

        // Get all the sanctionnerList where date in DEFAULT_DATE or UPDATED_DATE
        defaultSanctionnerShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the sanctionnerList where date equals to UPDATED_DATE
        defaultSanctionnerShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllSanctionnersByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        sanctionnerRepository.saveAndFlush(sanctionner);

        // Get all the sanctionnerList where date is not null
        defaultSanctionnerShouldBeFound("date.specified=true");

        // Get all the sanctionnerList where date is null
        defaultSanctionnerShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllSanctionnersBySanctionIsEqualToSomething() throws Exception {
        Sanction sanction;
        if (TestUtil.findAll(em, Sanction.class).isEmpty()) {
            sanctionnerRepository.saveAndFlush(sanctionner);
            sanction = SanctionResourceIT.createEntity(em);
        } else {
            sanction = TestUtil.findAll(em, Sanction.class).get(0);
        }
        em.persist(sanction);
        em.flush();
        sanctionner.setSanction(sanction);
        sanctionnerRepository.saveAndFlush(sanctionner);
        Long sanctionId = sanction.getId();
        // Get all the sanctionnerList where sanction equals to sanctionId
        defaultSanctionnerShouldBeFound("sanctionId.equals=" + sanctionId);

        // Get all the sanctionnerList where sanction equals to (sanctionId + 1)
        defaultSanctionnerShouldNotBeFound("sanctionId.equals=" + (sanctionId + 1));
    }

    @Test
    @Transactional
    void getAllSanctionnersByDemandeDexplicationIsEqualToSomething() throws Exception {
        DemandeDexplication demandeDexplication;
        if (TestUtil.findAll(em, DemandeDexplication.class).isEmpty()) {
            sanctionnerRepository.saveAndFlush(sanctionner);
            demandeDexplication = DemandeDexplicationResourceIT.createEntity(em);
        } else {
            demandeDexplication = TestUtil.findAll(em, DemandeDexplication.class).get(0);
        }
        em.persist(demandeDexplication);
        em.flush();
        sanctionner.setDemandeDexplication(demandeDexplication);
        sanctionnerRepository.saveAndFlush(sanctionner);
        Long demandeDexplicationId = demandeDexplication.getId();
        // Get all the sanctionnerList where demandeDexplication equals to demandeDexplicationId
        defaultSanctionnerShouldBeFound("demandeDexplicationId.equals=" + demandeDexplicationId);

        // Get all the sanctionnerList where demandeDexplication equals to (demandeDexplicationId + 1)
        defaultSanctionnerShouldNotBeFound("demandeDexplicationId.equals=" + (demandeDexplicationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSanctionnerShouldBeFound(String filter) throws Exception {
        restSanctionnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sanctionner.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restSanctionnerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSanctionnerShouldNotBeFound(String filter) throws Exception {
        restSanctionnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSanctionnerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSanctionner() throws Exception {
        // Get the sanctionner
        restSanctionnerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSanctionner() throws Exception {
        // Initialize the database
        sanctionnerRepository.saveAndFlush(sanctionner);

        int databaseSizeBeforeUpdate = sanctionnerRepository.findAll().size();

        // Update the sanctionner
        Sanctionner updatedSanctionner = sanctionnerRepository.findById(sanctionner.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSanctionner are not directly saved in db
        em.detach(updatedSanctionner);
        updatedSanctionner.date(UPDATED_DATE);

        restSanctionnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSanctionner.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSanctionner))
            )
            .andExpect(status().isOk());

        // Validate the Sanctionner in the database
        List<Sanctionner> sanctionnerList = sanctionnerRepository.findAll();
        assertThat(sanctionnerList).hasSize(databaseSizeBeforeUpdate);
        Sanctionner testSanctionner = sanctionnerList.get(sanctionnerList.size() - 1);
        assertThat(testSanctionner.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSanctionner() throws Exception {
        int databaseSizeBeforeUpdate = sanctionnerRepository.findAll().size();
        sanctionner.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSanctionnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sanctionner.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sanctionner))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sanctionner in the database
        List<Sanctionner> sanctionnerList = sanctionnerRepository.findAll();
        assertThat(sanctionnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSanctionner() throws Exception {
        int databaseSizeBeforeUpdate = sanctionnerRepository.findAll().size();
        sanctionner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanctionnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sanctionner))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sanctionner in the database
        List<Sanctionner> sanctionnerList = sanctionnerRepository.findAll();
        assertThat(sanctionnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSanctionner() throws Exception {
        int databaseSizeBeforeUpdate = sanctionnerRepository.findAll().size();
        sanctionner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanctionnerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sanctionner)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sanctionner in the database
        List<Sanctionner> sanctionnerList = sanctionnerRepository.findAll();
        assertThat(sanctionnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSanctionnerWithPatch() throws Exception {
        // Initialize the database
        sanctionnerRepository.saveAndFlush(sanctionner);

        int databaseSizeBeforeUpdate = sanctionnerRepository.findAll().size();

        // Update the sanctionner using partial update
        Sanctionner partialUpdatedSanctionner = new Sanctionner();
        partialUpdatedSanctionner.setId(sanctionner.getId());

        partialUpdatedSanctionner.date(UPDATED_DATE);

        restSanctionnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSanctionner.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSanctionner))
            )
            .andExpect(status().isOk());

        // Validate the Sanctionner in the database
        List<Sanctionner> sanctionnerList = sanctionnerRepository.findAll();
        assertThat(sanctionnerList).hasSize(databaseSizeBeforeUpdate);
        Sanctionner testSanctionner = sanctionnerList.get(sanctionnerList.size() - 1);
        assertThat(testSanctionner.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSanctionnerWithPatch() throws Exception {
        // Initialize the database
        sanctionnerRepository.saveAndFlush(sanctionner);

        int databaseSizeBeforeUpdate = sanctionnerRepository.findAll().size();

        // Update the sanctionner using partial update
        Sanctionner partialUpdatedSanctionner = new Sanctionner();
        partialUpdatedSanctionner.setId(sanctionner.getId());

        partialUpdatedSanctionner.date(UPDATED_DATE);

        restSanctionnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSanctionner.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSanctionner))
            )
            .andExpect(status().isOk());

        // Validate the Sanctionner in the database
        List<Sanctionner> sanctionnerList = sanctionnerRepository.findAll();
        assertThat(sanctionnerList).hasSize(databaseSizeBeforeUpdate);
        Sanctionner testSanctionner = sanctionnerList.get(sanctionnerList.size() - 1);
        assertThat(testSanctionner.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSanctionner() throws Exception {
        int databaseSizeBeforeUpdate = sanctionnerRepository.findAll().size();
        sanctionner.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSanctionnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sanctionner.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sanctionner))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sanctionner in the database
        List<Sanctionner> sanctionnerList = sanctionnerRepository.findAll();
        assertThat(sanctionnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSanctionner() throws Exception {
        int databaseSizeBeforeUpdate = sanctionnerRepository.findAll().size();
        sanctionner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanctionnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sanctionner))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sanctionner in the database
        List<Sanctionner> sanctionnerList = sanctionnerRepository.findAll();
        assertThat(sanctionnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSanctionner() throws Exception {
        int databaseSizeBeforeUpdate = sanctionnerRepository.findAll().size();
        sanctionner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanctionnerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sanctionner))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sanctionner in the database
        List<Sanctionner> sanctionnerList = sanctionnerRepository.findAll();
        assertThat(sanctionnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSanctionner() throws Exception {
        // Initialize the database
        sanctionnerRepository.saveAndFlush(sanctionner);

        int databaseSizeBeforeDelete = sanctionnerRepository.findAll().size();

        // Delete the sanctionner
        restSanctionnerMockMvc
            .perform(delete(ENTITY_API_URL_ID, sanctionner.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sanctionner> sanctionnerList = sanctionnerRepository.findAll();
        assertThat(sanctionnerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

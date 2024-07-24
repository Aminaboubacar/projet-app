package com.cnss.ne.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cnss.ne.IntegrationTest;
import com.cnss.ne.domain.Agent;
import com.cnss.ne.domain.Disponibilite;
import com.cnss.ne.domain.MotifDisponibilite;
import com.cnss.ne.repository.DisponibiliteRepository;
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
 * Integration tests for the {@link DisponibiliteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DisponibiliteResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_DEBUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEBUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_RETOUR = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_RETOUR = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/disponibilites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DisponibiliteRepository disponibiliteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDisponibiliteMockMvc;

    private Disponibilite disponibilite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disponibilite createEntity(EntityManager em) {
        Disponibilite disponibilite = new Disponibilite()
            .code(DEFAULT_CODE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .dateRetour(DEFAULT_DATE_RETOUR);
        return disponibilite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disponibilite createUpdatedEntity(EntityManager em) {
        Disponibilite disponibilite = new Disponibilite()
            .code(UPDATED_CODE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .dateRetour(UPDATED_DATE_RETOUR);
        return disponibilite;
    }

    @BeforeEach
    public void initTest() {
        disponibilite = createEntity(em);
    }

    @Test
    @Transactional
    void createDisponibilite() throws Exception {
        int databaseSizeBeforeCreate = disponibiliteRepository.findAll().size();
        // Create the Disponibilite
        restDisponibiliteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(disponibilite)))
            .andExpect(status().isCreated());

        // Validate the Disponibilite in the database
        List<Disponibilite> disponibiliteList = disponibiliteRepository.findAll();
        assertThat(disponibiliteList).hasSize(databaseSizeBeforeCreate + 1);
        Disponibilite testDisponibilite = disponibiliteList.get(disponibiliteList.size() - 1);
        assertThat(testDisponibilite.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDisponibilite.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testDisponibilite.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testDisponibilite.getDateRetour()).isEqualTo(DEFAULT_DATE_RETOUR);
    }

    @Test
    @Transactional
    void createDisponibiliteWithExistingId() throws Exception {
        // Create the Disponibilite with an existing ID
        disponibilite.setId(1L);

        int databaseSizeBeforeCreate = disponibiliteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisponibiliteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(disponibilite)))
            .andExpect(status().isBadRequest());

        // Validate the Disponibilite in the database
        List<Disponibilite> disponibiliteList = disponibiliteRepository.findAll();
        assertThat(disponibiliteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = disponibiliteRepository.findAll().size();
        // set the field null
        disponibilite.setCode(null);

        // Create the Disponibilite, which fails.

        restDisponibiliteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(disponibilite)))
            .andExpect(status().isBadRequest());

        List<Disponibilite> disponibiliteList = disponibiliteRepository.findAll();
        assertThat(disponibiliteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDisponibilites() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList
        restDisponibiliteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disponibilite.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].dateRetour").value(hasItem(DEFAULT_DATE_RETOUR.toString())));
    }

    @Test
    @Transactional
    void getDisponibilite() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        // Get the disponibilite
        restDisponibiliteMockMvc
            .perform(get(ENTITY_API_URL_ID, disponibilite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(disponibilite.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.dateRetour").value(DEFAULT_DATE_RETOUR.toString()));
    }

    @Test
    @Transactional
    void getDisponibilitesByIdFiltering() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        Long id = disponibilite.getId();

        defaultDisponibiliteShouldBeFound("id.equals=" + id);
        defaultDisponibiliteShouldNotBeFound("id.notEquals=" + id);

        defaultDisponibiliteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDisponibiliteShouldNotBeFound("id.greaterThan=" + id);

        defaultDisponibiliteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDisponibiliteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDisponibilitesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where code equals to DEFAULT_CODE
        defaultDisponibiliteShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the disponibiliteList where code equals to UPDATED_CODE
        defaultDisponibiliteShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDisponibilitesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where code in DEFAULT_CODE or UPDATED_CODE
        defaultDisponibiliteShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the disponibiliteList where code equals to UPDATED_CODE
        defaultDisponibiliteShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDisponibilitesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where code is not null
        defaultDisponibiliteShouldBeFound("code.specified=true");

        // Get all the disponibiliteList where code is null
        defaultDisponibiliteShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllDisponibilitesByCodeContainsSomething() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where code contains DEFAULT_CODE
        defaultDisponibiliteShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the disponibiliteList where code contains UPDATED_CODE
        defaultDisponibiliteShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDisponibilitesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where code does not contain DEFAULT_CODE
        defaultDisponibiliteShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the disponibiliteList where code does not contain UPDATED_CODE
        defaultDisponibiliteShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDisponibilitesByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where dateDebut equals to DEFAULT_DATE_DEBUT
        defaultDisponibiliteShouldBeFound("dateDebut.equals=" + DEFAULT_DATE_DEBUT);

        // Get all the disponibiliteList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultDisponibiliteShouldNotBeFound("dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllDisponibilitesByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where dateDebut in DEFAULT_DATE_DEBUT or UPDATED_DATE_DEBUT
        defaultDisponibiliteShouldBeFound("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT);

        // Get all the disponibiliteList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultDisponibiliteShouldNotBeFound("dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllDisponibilitesByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where dateDebut is not null
        defaultDisponibiliteShouldBeFound("dateDebut.specified=true");

        // Get all the disponibiliteList where dateDebut is null
        defaultDisponibiliteShouldNotBeFound("dateDebut.specified=false");
    }

    @Test
    @Transactional
    void getAllDisponibilitesByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where dateFin equals to DEFAULT_DATE_FIN
        defaultDisponibiliteShouldBeFound("dateFin.equals=" + DEFAULT_DATE_FIN);

        // Get all the disponibiliteList where dateFin equals to UPDATED_DATE_FIN
        defaultDisponibiliteShouldNotBeFound("dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllDisponibilitesByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where dateFin in DEFAULT_DATE_FIN or UPDATED_DATE_FIN
        defaultDisponibiliteShouldBeFound("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN);

        // Get all the disponibiliteList where dateFin equals to UPDATED_DATE_FIN
        defaultDisponibiliteShouldNotBeFound("dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllDisponibilitesByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where dateFin is not null
        defaultDisponibiliteShouldBeFound("dateFin.specified=true");

        // Get all the disponibiliteList where dateFin is null
        defaultDisponibiliteShouldNotBeFound("dateFin.specified=false");
    }

    @Test
    @Transactional
    void getAllDisponibilitesByDateRetourIsEqualToSomething() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where dateRetour equals to DEFAULT_DATE_RETOUR
        defaultDisponibiliteShouldBeFound("dateRetour.equals=" + DEFAULT_DATE_RETOUR);

        // Get all the disponibiliteList where dateRetour equals to UPDATED_DATE_RETOUR
        defaultDisponibiliteShouldNotBeFound("dateRetour.equals=" + UPDATED_DATE_RETOUR);
    }

    @Test
    @Transactional
    void getAllDisponibilitesByDateRetourIsInShouldWork() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where dateRetour in DEFAULT_DATE_RETOUR or UPDATED_DATE_RETOUR
        defaultDisponibiliteShouldBeFound("dateRetour.in=" + DEFAULT_DATE_RETOUR + "," + UPDATED_DATE_RETOUR);

        // Get all the disponibiliteList where dateRetour equals to UPDATED_DATE_RETOUR
        defaultDisponibiliteShouldNotBeFound("dateRetour.in=" + UPDATED_DATE_RETOUR);
    }

    @Test
    @Transactional
    void getAllDisponibilitesByDateRetourIsNullOrNotNull() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList where dateRetour is not null
        defaultDisponibiliteShouldBeFound("dateRetour.specified=true");

        // Get all the disponibiliteList where dateRetour is null
        defaultDisponibiliteShouldNotBeFound("dateRetour.specified=false");
    }

    @Test
    @Transactional
    void getAllDisponibilitesByMotifDisponibiliteIsEqualToSomething() throws Exception {
        MotifDisponibilite motifDisponibilite;
        if (TestUtil.findAll(em, MotifDisponibilite.class).isEmpty()) {
            disponibiliteRepository.saveAndFlush(disponibilite);
            motifDisponibilite = MotifDisponibiliteResourceIT.createEntity(em);
        } else {
            motifDisponibilite = TestUtil.findAll(em, MotifDisponibilite.class).get(0);
        }
        em.persist(motifDisponibilite);
        em.flush();
        disponibilite.setMotifDisponibilite(motifDisponibilite);
        disponibiliteRepository.saveAndFlush(disponibilite);
        Long motifDisponibiliteId = motifDisponibilite.getId();
        // Get all the disponibiliteList where motifDisponibilite equals to motifDisponibiliteId
        defaultDisponibiliteShouldBeFound("motifDisponibiliteId.equals=" + motifDisponibiliteId);

        // Get all the disponibiliteList where motifDisponibilite equals to (motifDisponibiliteId + 1)
        defaultDisponibiliteShouldNotBeFound("motifDisponibiliteId.equals=" + (motifDisponibiliteId + 1));
    }

    @Test
    @Transactional
    void getAllDisponibilitesByAgentIsEqualToSomething() throws Exception {
        Agent agent;
        if (TestUtil.findAll(em, Agent.class).isEmpty()) {
            disponibiliteRepository.saveAndFlush(disponibilite);
            agent = AgentResourceIT.createEntity(em);
        } else {
            agent = TestUtil.findAll(em, Agent.class).get(0);
        }
        em.persist(agent);
        em.flush();
        disponibilite.setAgent(agent);
        disponibiliteRepository.saveAndFlush(disponibilite);
        Long agentId = agent.getId();
        // Get all the disponibiliteList where agent equals to agentId
        defaultDisponibiliteShouldBeFound("agentId.equals=" + agentId);

        // Get all the disponibiliteList where agent equals to (agentId + 1)
        defaultDisponibiliteShouldNotBeFound("agentId.equals=" + (agentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDisponibiliteShouldBeFound(String filter) throws Exception {
        restDisponibiliteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disponibilite.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].dateRetour").value(hasItem(DEFAULT_DATE_RETOUR.toString())));

        // Check, that the count call also returns 1
        restDisponibiliteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDisponibiliteShouldNotBeFound(String filter) throws Exception {
        restDisponibiliteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDisponibiliteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDisponibilite() throws Exception {
        // Get the disponibilite
        restDisponibiliteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDisponibilite() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        int databaseSizeBeforeUpdate = disponibiliteRepository.findAll().size();

        // Update the disponibilite
        Disponibilite updatedDisponibilite = disponibiliteRepository.findById(disponibilite.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDisponibilite are not directly saved in db
        em.detach(updatedDisponibilite);
        updatedDisponibilite.code(UPDATED_CODE).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN).dateRetour(UPDATED_DATE_RETOUR);

        restDisponibiliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDisponibilite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDisponibilite))
            )
            .andExpect(status().isOk());

        // Validate the Disponibilite in the database
        List<Disponibilite> disponibiliteList = disponibiliteRepository.findAll();
        assertThat(disponibiliteList).hasSize(databaseSizeBeforeUpdate);
        Disponibilite testDisponibilite = disponibiliteList.get(disponibiliteList.size() - 1);
        assertThat(testDisponibilite.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDisponibilite.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testDisponibilite.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testDisponibilite.getDateRetour()).isEqualTo(UPDATED_DATE_RETOUR);
    }

    @Test
    @Transactional
    void putNonExistingDisponibilite() throws Exception {
        int databaseSizeBeforeUpdate = disponibiliteRepository.findAll().size();
        disponibilite.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisponibiliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disponibilite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disponibilite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disponibilite in the database
        List<Disponibilite> disponibiliteList = disponibiliteRepository.findAll();
        assertThat(disponibiliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDisponibilite() throws Exception {
        int databaseSizeBeforeUpdate = disponibiliteRepository.findAll().size();
        disponibilite.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibiliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disponibilite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disponibilite in the database
        List<Disponibilite> disponibiliteList = disponibiliteRepository.findAll();
        assertThat(disponibiliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDisponibilite() throws Exception {
        int databaseSizeBeforeUpdate = disponibiliteRepository.findAll().size();
        disponibilite.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibiliteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(disponibilite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Disponibilite in the database
        List<Disponibilite> disponibiliteList = disponibiliteRepository.findAll();
        assertThat(disponibiliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDisponibiliteWithPatch() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        int databaseSizeBeforeUpdate = disponibiliteRepository.findAll().size();

        // Update the disponibilite using partial update
        Disponibilite partialUpdatedDisponibilite = new Disponibilite();
        partialUpdatedDisponibilite.setId(disponibilite.getId());

        partialUpdatedDisponibilite.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN).dateRetour(UPDATED_DATE_RETOUR);

        restDisponibiliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisponibilite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDisponibilite))
            )
            .andExpect(status().isOk());

        // Validate the Disponibilite in the database
        List<Disponibilite> disponibiliteList = disponibiliteRepository.findAll();
        assertThat(disponibiliteList).hasSize(databaseSizeBeforeUpdate);
        Disponibilite testDisponibilite = disponibiliteList.get(disponibiliteList.size() - 1);
        assertThat(testDisponibilite.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDisponibilite.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testDisponibilite.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testDisponibilite.getDateRetour()).isEqualTo(UPDATED_DATE_RETOUR);
    }

    @Test
    @Transactional
    void fullUpdateDisponibiliteWithPatch() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        int databaseSizeBeforeUpdate = disponibiliteRepository.findAll().size();

        // Update the disponibilite using partial update
        Disponibilite partialUpdatedDisponibilite = new Disponibilite();
        partialUpdatedDisponibilite.setId(disponibilite.getId());

        partialUpdatedDisponibilite
            .code(UPDATED_CODE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .dateRetour(UPDATED_DATE_RETOUR);

        restDisponibiliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisponibilite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDisponibilite))
            )
            .andExpect(status().isOk());

        // Validate the Disponibilite in the database
        List<Disponibilite> disponibiliteList = disponibiliteRepository.findAll();
        assertThat(disponibiliteList).hasSize(databaseSizeBeforeUpdate);
        Disponibilite testDisponibilite = disponibiliteList.get(disponibiliteList.size() - 1);
        assertThat(testDisponibilite.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDisponibilite.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testDisponibilite.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testDisponibilite.getDateRetour()).isEqualTo(UPDATED_DATE_RETOUR);
    }

    @Test
    @Transactional
    void patchNonExistingDisponibilite() throws Exception {
        int databaseSizeBeforeUpdate = disponibiliteRepository.findAll().size();
        disponibilite.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisponibiliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, disponibilite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(disponibilite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disponibilite in the database
        List<Disponibilite> disponibiliteList = disponibiliteRepository.findAll();
        assertThat(disponibiliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDisponibilite() throws Exception {
        int databaseSizeBeforeUpdate = disponibiliteRepository.findAll().size();
        disponibilite.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibiliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(disponibilite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disponibilite in the database
        List<Disponibilite> disponibiliteList = disponibiliteRepository.findAll();
        assertThat(disponibiliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDisponibilite() throws Exception {
        int databaseSizeBeforeUpdate = disponibiliteRepository.findAll().size();
        disponibilite.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibiliteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(disponibilite))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Disponibilite in the database
        List<Disponibilite> disponibiliteList = disponibiliteRepository.findAll();
        assertThat(disponibiliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDisponibilite() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        int databaseSizeBeforeDelete = disponibiliteRepository.findAll().size();

        // Delete the disponibilite
        restDisponibiliteMockMvc
            .perform(delete(ENTITY_API_URL_ID, disponibilite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Disponibilite> disponibiliteList = disponibiliteRepository.findAll();
        assertThat(disponibiliteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

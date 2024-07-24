package com.cnss.ne.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cnss.ne.IntegrationTest;
import com.cnss.ne.domain.Agent;
import com.cnss.ne.domain.Poste;
import com.cnss.ne.repository.PosteRepository;
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
 * Integration tests for the {@link PosteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PosteResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/postes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PosteRepository posteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPosteMockMvc;

    private Poste poste;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Poste createEntity(EntityManager em) {
        Poste poste = new Poste().code(DEFAULT_CODE).libelle(DEFAULT_LIBELLE);
        return poste;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Poste createUpdatedEntity(EntityManager em) {
        Poste poste = new Poste().code(UPDATED_CODE).libelle(UPDATED_LIBELLE);
        return poste;
    }

    @BeforeEach
    public void initTest() {
        poste = createEntity(em);
    }

    @Test
    @Transactional
    void createPoste() throws Exception {
        int databaseSizeBeforeCreate = posteRepository.findAll().size();
        // Create the Poste
        restPosteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poste)))
            .andExpect(status().isCreated());

        // Validate the Poste in the database
        List<Poste> posteList = posteRepository.findAll();
        assertThat(posteList).hasSize(databaseSizeBeforeCreate + 1);
        Poste testPoste = posteList.get(posteList.size() - 1);
        assertThat(testPoste.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPoste.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void createPosteWithExistingId() throws Exception {
        // Create the Poste with an existing ID
        poste.setId(1L);

        int databaseSizeBeforeCreate = posteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPosteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poste)))
            .andExpect(status().isBadRequest());

        // Validate the Poste in the database
        List<Poste> posteList = posteRepository.findAll();
        assertThat(posteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = posteRepository.findAll().size();
        // set the field null
        poste.setCode(null);

        // Create the Poste, which fails.

        restPosteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poste)))
            .andExpect(status().isBadRequest());

        List<Poste> posteList = posteRepository.findAll();
        assertThat(posteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = posteRepository.findAll().size();
        // set the field null
        poste.setLibelle(null);

        // Create the Poste, which fails.

        restPosteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poste)))
            .andExpect(status().isBadRequest());

        List<Poste> posteList = posteRepository.findAll();
        assertThat(posteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPostes() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);

        // Get all the posteList
        restPosteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(poste.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getPoste() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);

        // Get the poste
        restPosteMockMvc
            .perform(get(ENTITY_API_URL_ID, poste.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(poste.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getPostesByIdFiltering() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);

        Long id = poste.getId();

        defaultPosteShouldBeFound("id.equals=" + id);
        defaultPosteShouldNotBeFound("id.notEquals=" + id);

        defaultPosteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPosteShouldNotBeFound("id.greaterThan=" + id);

        defaultPosteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPosteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPostesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);

        // Get all the posteList where code equals to DEFAULT_CODE
        defaultPosteShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the posteList where code equals to UPDATED_CODE
        defaultPosteShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPostesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);

        // Get all the posteList where code in DEFAULT_CODE or UPDATED_CODE
        defaultPosteShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the posteList where code equals to UPDATED_CODE
        defaultPosteShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPostesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);

        // Get all the posteList where code is not null
        defaultPosteShouldBeFound("code.specified=true");

        // Get all the posteList where code is null
        defaultPosteShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllPostesByCodeContainsSomething() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);

        // Get all the posteList where code contains DEFAULT_CODE
        defaultPosteShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the posteList where code contains UPDATED_CODE
        defaultPosteShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPostesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);

        // Get all the posteList where code does not contain DEFAULT_CODE
        defaultPosteShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the posteList where code does not contain UPDATED_CODE
        defaultPosteShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPostesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);

        // Get all the posteList where libelle equals to DEFAULT_LIBELLE
        defaultPosteShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the posteList where libelle equals to UPDATED_LIBELLE
        defaultPosteShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllPostesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);

        // Get all the posteList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultPosteShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the posteList where libelle equals to UPDATED_LIBELLE
        defaultPosteShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllPostesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);

        // Get all the posteList where libelle is not null
        defaultPosteShouldBeFound("libelle.specified=true");

        // Get all the posteList where libelle is null
        defaultPosteShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllPostesByLibelleContainsSomething() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);

        // Get all the posteList where libelle contains DEFAULT_LIBELLE
        defaultPosteShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the posteList where libelle contains UPDATED_LIBELLE
        defaultPosteShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllPostesByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);

        // Get all the posteList where libelle does not contain DEFAULT_LIBELLE
        defaultPosteShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the posteList where libelle does not contain UPDATED_LIBELLE
        defaultPosteShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllPostesByAgentIsEqualToSomething() throws Exception {
        Agent agent;
        if (TestUtil.findAll(em, Agent.class).isEmpty()) {
            posteRepository.saveAndFlush(poste);
            agent = AgentResourceIT.createEntity(em);
        } else {
            agent = TestUtil.findAll(em, Agent.class).get(0);
        }
        em.persist(agent);
        em.flush();
        poste.addAgent(agent);
        posteRepository.saveAndFlush(poste);
        Long agentId = agent.getId();
        // Get all the posteList where agent equals to agentId
        defaultPosteShouldBeFound("agentId.equals=" + agentId);

        // Get all the posteList where agent equals to (agentId + 1)
        defaultPosteShouldNotBeFound("agentId.equals=" + (agentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPosteShouldBeFound(String filter) throws Exception {
        restPosteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(poste.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));

        // Check, that the count call also returns 1
        restPosteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPosteShouldNotBeFound(String filter) throws Exception {
        restPosteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPosteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPoste() throws Exception {
        // Get the poste
        restPosteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPoste() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);

        int databaseSizeBeforeUpdate = posteRepository.findAll().size();

        // Update the poste
        Poste updatedPoste = posteRepository.findById(poste.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPoste are not directly saved in db
        em.detach(updatedPoste);
        updatedPoste.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);

        restPosteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPoste.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPoste))
            )
            .andExpect(status().isOk());

        // Validate the Poste in the database
        List<Poste> posteList = posteRepository.findAll();
        assertThat(posteList).hasSize(databaseSizeBeforeUpdate);
        Poste testPoste = posteList.get(posteList.size() - 1);
        assertThat(testPoste.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPoste.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void putNonExistingPoste() throws Exception {
        int databaseSizeBeforeUpdate = posteRepository.findAll().size();
        poste.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPosteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, poste.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poste))
            )
            .andExpect(status().isBadRequest());

        // Validate the Poste in the database
        List<Poste> posteList = posteRepository.findAll();
        assertThat(posteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPoste() throws Exception {
        int databaseSizeBeforeUpdate = posteRepository.findAll().size();
        poste.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPosteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poste))
            )
            .andExpect(status().isBadRequest());

        // Validate the Poste in the database
        List<Poste> posteList = posteRepository.findAll();
        assertThat(posteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPoste() throws Exception {
        int databaseSizeBeforeUpdate = posteRepository.findAll().size();
        poste.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPosteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poste)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Poste in the database
        List<Poste> posteList = posteRepository.findAll();
        assertThat(posteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePosteWithPatch() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);

        int databaseSizeBeforeUpdate = posteRepository.findAll().size();

        // Update the poste using partial update
        Poste partialUpdatedPoste = new Poste();
        partialUpdatedPoste.setId(poste.getId());

        partialUpdatedPoste.code(UPDATED_CODE);

        restPosteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoste.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoste))
            )
            .andExpect(status().isOk());

        // Validate the Poste in the database
        List<Poste> posteList = posteRepository.findAll();
        assertThat(posteList).hasSize(databaseSizeBeforeUpdate);
        Poste testPoste = posteList.get(posteList.size() - 1);
        assertThat(testPoste.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPoste.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdatePosteWithPatch() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);

        int databaseSizeBeforeUpdate = posteRepository.findAll().size();

        // Update the poste using partial update
        Poste partialUpdatedPoste = new Poste();
        partialUpdatedPoste.setId(poste.getId());

        partialUpdatedPoste.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);

        restPosteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoste.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoste))
            )
            .andExpect(status().isOk());

        // Validate the Poste in the database
        List<Poste> posteList = posteRepository.findAll();
        assertThat(posteList).hasSize(databaseSizeBeforeUpdate);
        Poste testPoste = posteList.get(posteList.size() - 1);
        assertThat(testPoste.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPoste.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingPoste() throws Exception {
        int databaseSizeBeforeUpdate = posteRepository.findAll().size();
        poste.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPosteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, poste.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(poste))
            )
            .andExpect(status().isBadRequest());

        // Validate the Poste in the database
        List<Poste> posteList = posteRepository.findAll();
        assertThat(posteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPoste() throws Exception {
        int databaseSizeBeforeUpdate = posteRepository.findAll().size();
        poste.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPosteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(poste))
            )
            .andExpect(status().isBadRequest());

        // Validate the Poste in the database
        List<Poste> posteList = posteRepository.findAll();
        assertThat(posteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPoste() throws Exception {
        int databaseSizeBeforeUpdate = posteRepository.findAll().size();
        poste.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPosteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(poste)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Poste in the database
        List<Poste> posteList = posteRepository.findAll();
        assertThat(posteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePoste() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);

        int databaseSizeBeforeDelete = posteRepository.findAll().size();

        // Delete the poste
        restPosteMockMvc
            .perform(delete(ENTITY_API_URL_ID, poste.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Poste> posteList = posteRepository.findAll();
        assertThat(posteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

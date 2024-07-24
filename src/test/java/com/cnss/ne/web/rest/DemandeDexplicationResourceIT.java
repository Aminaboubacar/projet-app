package com.cnss.ne.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cnss.ne.IntegrationTest;
import com.cnss.ne.domain.Agent;
import com.cnss.ne.domain.DemandeDexplication;
import com.cnss.ne.domain.Sanctionner;
import com.cnss.ne.repository.DemandeDexplicationRepository;
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
 * Integration tests for the {@link DemandeDexplicationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DemandeDexplicationResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_OBJET = "AAAAAAAAAA";
    private static final String UPDATED_OBJET = "BBBBBBBBBB";

    private static final String DEFAULT_APP_DG = "AAAAAAAAAA";
    private static final String UPDATED_APP_DG = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATAPP_DG = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATAPP_DG = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/demande-dexplications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DemandeDexplicationRepository demandeDexplicationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDemandeDexplicationMockMvc;

    private DemandeDexplication demandeDexplication;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandeDexplication createEntity(EntityManager em) {
        DemandeDexplication demandeDexplication = new DemandeDexplication()
            .code(DEFAULT_CODE)
            .date(DEFAULT_DATE)
            .objet(DEFAULT_OBJET)
            .appDg(DEFAULT_APP_DG)
            .datappDg(DEFAULT_DATAPP_DG);
        return demandeDexplication;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandeDexplication createUpdatedEntity(EntityManager em) {
        DemandeDexplication demandeDexplication = new DemandeDexplication()
            .code(UPDATED_CODE)
            .date(UPDATED_DATE)
            .objet(UPDATED_OBJET)
            .appDg(UPDATED_APP_DG)
            .datappDg(UPDATED_DATAPP_DG);
        return demandeDexplication;
    }

    @BeforeEach
    public void initTest() {
        demandeDexplication = createEntity(em);
    }

    @Test
    @Transactional
    void createDemandeDexplication() throws Exception {
        int databaseSizeBeforeCreate = demandeDexplicationRepository.findAll().size();
        // Create the DemandeDexplication
        restDemandeDexplicationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeDexplication))
            )
            .andExpect(status().isCreated());

        // Validate the DemandeDexplication in the database
        List<DemandeDexplication> demandeDexplicationList = demandeDexplicationRepository.findAll();
        assertThat(demandeDexplicationList).hasSize(databaseSizeBeforeCreate + 1);
        DemandeDexplication testDemandeDexplication = demandeDexplicationList.get(demandeDexplicationList.size() - 1);
        assertThat(testDemandeDexplication.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDemandeDexplication.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDemandeDexplication.getObjet()).isEqualTo(DEFAULT_OBJET);
        assertThat(testDemandeDexplication.getAppDg()).isEqualTo(DEFAULT_APP_DG);
        assertThat(testDemandeDexplication.getDatappDg()).isEqualTo(DEFAULT_DATAPP_DG);
    }

    @Test
    @Transactional
    void createDemandeDexplicationWithExistingId() throws Exception {
        // Create the DemandeDexplication with an existing ID
        demandeDexplication.setId(1L);

        int databaseSizeBeforeCreate = demandeDexplicationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemandeDexplicationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeDexplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeDexplication in the database
        List<DemandeDexplication> demandeDexplicationList = demandeDexplicationRepository.findAll();
        assertThat(demandeDexplicationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeDexplicationRepository.findAll().size();
        // set the field null
        demandeDexplication.setCode(null);

        // Create the DemandeDexplication, which fails.

        restDemandeDexplicationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeDexplication))
            )
            .andExpect(status().isBadRequest());

        List<DemandeDexplication> demandeDexplicationList = demandeDexplicationRepository.findAll();
        assertThat(demandeDexplicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkObjetIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeDexplicationRepository.findAll().size();
        // set the field null
        demandeDexplication.setObjet(null);

        // Create the DemandeDexplication, which fails.

        restDemandeDexplicationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeDexplication))
            )
            .andExpect(status().isBadRequest());

        List<DemandeDexplication> demandeDexplicationList = demandeDexplicationRepository.findAll();
        assertThat(demandeDexplicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDemandeDexplications() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList
        restDemandeDexplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demandeDexplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].objet").value(hasItem(DEFAULT_OBJET)))
            .andExpect(jsonPath("$.[*].appDg").value(hasItem(DEFAULT_APP_DG)))
            .andExpect(jsonPath("$.[*].datappDg").value(hasItem(DEFAULT_DATAPP_DG.toString())));
    }

    @Test
    @Transactional
    void getDemandeDexplication() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get the demandeDexplication
        restDemandeDexplicationMockMvc
            .perform(get(ENTITY_API_URL_ID, demandeDexplication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(demandeDexplication.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.objet").value(DEFAULT_OBJET))
            .andExpect(jsonPath("$.appDg").value(DEFAULT_APP_DG))
            .andExpect(jsonPath("$.datappDg").value(DEFAULT_DATAPP_DG.toString()));
    }

    @Test
    @Transactional
    void getDemandeDexplicationsByIdFiltering() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        Long id = demandeDexplication.getId();

        defaultDemandeDexplicationShouldBeFound("id.equals=" + id);
        defaultDemandeDexplicationShouldNotBeFound("id.notEquals=" + id);

        defaultDemandeDexplicationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDemandeDexplicationShouldNotBeFound("id.greaterThan=" + id);

        defaultDemandeDexplicationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDemandeDexplicationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where code equals to DEFAULT_CODE
        defaultDemandeDexplicationShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the demandeDexplicationList where code equals to UPDATED_CODE
        defaultDemandeDexplicationShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where code in DEFAULT_CODE or UPDATED_CODE
        defaultDemandeDexplicationShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the demandeDexplicationList where code equals to UPDATED_CODE
        defaultDemandeDexplicationShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where code is not null
        defaultDemandeDexplicationShouldBeFound("code.specified=true");

        // Get all the demandeDexplicationList where code is null
        defaultDemandeDexplicationShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByCodeContainsSomething() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where code contains DEFAULT_CODE
        defaultDemandeDexplicationShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the demandeDexplicationList where code contains UPDATED_CODE
        defaultDemandeDexplicationShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where code does not contain DEFAULT_CODE
        defaultDemandeDexplicationShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the demandeDexplicationList where code does not contain UPDATED_CODE
        defaultDemandeDexplicationShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where date equals to DEFAULT_DATE
        defaultDemandeDexplicationShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the demandeDexplicationList where date equals to UPDATED_DATE
        defaultDemandeDexplicationShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where date in DEFAULT_DATE or UPDATED_DATE
        defaultDemandeDexplicationShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the demandeDexplicationList where date equals to UPDATED_DATE
        defaultDemandeDexplicationShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where date is not null
        defaultDemandeDexplicationShouldBeFound("date.specified=true");

        // Get all the demandeDexplicationList where date is null
        defaultDemandeDexplicationShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByObjetIsEqualToSomething() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where objet equals to DEFAULT_OBJET
        defaultDemandeDexplicationShouldBeFound("objet.equals=" + DEFAULT_OBJET);

        // Get all the demandeDexplicationList where objet equals to UPDATED_OBJET
        defaultDemandeDexplicationShouldNotBeFound("objet.equals=" + UPDATED_OBJET);
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByObjetIsInShouldWork() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where objet in DEFAULT_OBJET or UPDATED_OBJET
        defaultDemandeDexplicationShouldBeFound("objet.in=" + DEFAULT_OBJET + "," + UPDATED_OBJET);

        // Get all the demandeDexplicationList where objet equals to UPDATED_OBJET
        defaultDemandeDexplicationShouldNotBeFound("objet.in=" + UPDATED_OBJET);
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByObjetIsNullOrNotNull() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where objet is not null
        defaultDemandeDexplicationShouldBeFound("objet.specified=true");

        // Get all the demandeDexplicationList where objet is null
        defaultDemandeDexplicationShouldNotBeFound("objet.specified=false");
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByObjetContainsSomething() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where objet contains DEFAULT_OBJET
        defaultDemandeDexplicationShouldBeFound("objet.contains=" + DEFAULT_OBJET);

        // Get all the demandeDexplicationList where objet contains UPDATED_OBJET
        defaultDemandeDexplicationShouldNotBeFound("objet.contains=" + UPDATED_OBJET);
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByObjetNotContainsSomething() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where objet does not contain DEFAULT_OBJET
        defaultDemandeDexplicationShouldNotBeFound("objet.doesNotContain=" + DEFAULT_OBJET);

        // Get all the demandeDexplicationList where objet does not contain UPDATED_OBJET
        defaultDemandeDexplicationShouldBeFound("objet.doesNotContain=" + UPDATED_OBJET);
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByAppDgIsEqualToSomething() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where appDg equals to DEFAULT_APP_DG
        defaultDemandeDexplicationShouldBeFound("appDg.equals=" + DEFAULT_APP_DG);

        // Get all the demandeDexplicationList where appDg equals to UPDATED_APP_DG
        defaultDemandeDexplicationShouldNotBeFound("appDg.equals=" + UPDATED_APP_DG);
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByAppDgIsInShouldWork() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where appDg in DEFAULT_APP_DG or UPDATED_APP_DG
        defaultDemandeDexplicationShouldBeFound("appDg.in=" + DEFAULT_APP_DG + "," + UPDATED_APP_DG);

        // Get all the demandeDexplicationList where appDg equals to UPDATED_APP_DG
        defaultDemandeDexplicationShouldNotBeFound("appDg.in=" + UPDATED_APP_DG);
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByAppDgIsNullOrNotNull() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where appDg is not null
        defaultDemandeDexplicationShouldBeFound("appDg.specified=true");

        // Get all the demandeDexplicationList where appDg is null
        defaultDemandeDexplicationShouldNotBeFound("appDg.specified=false");
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByAppDgContainsSomething() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where appDg contains DEFAULT_APP_DG
        defaultDemandeDexplicationShouldBeFound("appDg.contains=" + DEFAULT_APP_DG);

        // Get all the demandeDexplicationList where appDg contains UPDATED_APP_DG
        defaultDemandeDexplicationShouldNotBeFound("appDg.contains=" + UPDATED_APP_DG);
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByAppDgNotContainsSomething() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where appDg does not contain DEFAULT_APP_DG
        defaultDemandeDexplicationShouldNotBeFound("appDg.doesNotContain=" + DEFAULT_APP_DG);

        // Get all the demandeDexplicationList where appDg does not contain UPDATED_APP_DG
        defaultDemandeDexplicationShouldBeFound("appDg.doesNotContain=" + UPDATED_APP_DG);
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByDatappDgIsEqualToSomething() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where datappDg equals to DEFAULT_DATAPP_DG
        defaultDemandeDexplicationShouldBeFound("datappDg.equals=" + DEFAULT_DATAPP_DG);

        // Get all the demandeDexplicationList where datappDg equals to UPDATED_DATAPP_DG
        defaultDemandeDexplicationShouldNotBeFound("datappDg.equals=" + UPDATED_DATAPP_DG);
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByDatappDgIsInShouldWork() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where datappDg in DEFAULT_DATAPP_DG or UPDATED_DATAPP_DG
        defaultDemandeDexplicationShouldBeFound("datappDg.in=" + DEFAULT_DATAPP_DG + "," + UPDATED_DATAPP_DG);

        // Get all the demandeDexplicationList where datappDg equals to UPDATED_DATAPP_DG
        defaultDemandeDexplicationShouldNotBeFound("datappDg.in=" + UPDATED_DATAPP_DG);
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByDatappDgIsNullOrNotNull() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        // Get all the demandeDexplicationList where datappDg is not null
        defaultDemandeDexplicationShouldBeFound("datappDg.specified=true");

        // Get all the demandeDexplicationList where datappDg is null
        defaultDemandeDexplicationShouldNotBeFound("datappDg.specified=false");
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsBySanctionnerIsEqualToSomething() throws Exception {
        Sanctionner sanctionner;
        if (TestUtil.findAll(em, Sanctionner.class).isEmpty()) {
            demandeDexplicationRepository.saveAndFlush(demandeDexplication);
            sanctionner = SanctionnerResourceIT.createEntity(em);
        } else {
            sanctionner = TestUtil.findAll(em, Sanctionner.class).get(0);
        }
        em.persist(sanctionner);
        em.flush();
        demandeDexplication.addSanctionner(sanctionner);
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);
        Long sanctionnerId = sanctionner.getId();
        // Get all the demandeDexplicationList where sanctionner equals to sanctionnerId
        defaultDemandeDexplicationShouldBeFound("sanctionnerId.equals=" + sanctionnerId);

        // Get all the demandeDexplicationList where sanctionner equals to (sanctionnerId + 1)
        defaultDemandeDexplicationShouldNotBeFound("sanctionnerId.equals=" + (sanctionnerId + 1));
    }

    @Test
    @Transactional
    void getAllDemandeDexplicationsByAgentIsEqualToSomething() throws Exception {
        Agent agent;
        if (TestUtil.findAll(em, Agent.class).isEmpty()) {
            demandeDexplicationRepository.saveAndFlush(demandeDexplication);
            agent = AgentResourceIT.createEntity(em);
        } else {
            agent = TestUtil.findAll(em, Agent.class).get(0);
        }
        em.persist(agent);
        em.flush();
        demandeDexplication.setAgent(agent);
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);
        Long agentId = agent.getId();
        // Get all the demandeDexplicationList where agent equals to agentId
        defaultDemandeDexplicationShouldBeFound("agentId.equals=" + agentId);

        // Get all the demandeDexplicationList where agent equals to (agentId + 1)
        defaultDemandeDexplicationShouldNotBeFound("agentId.equals=" + (agentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDemandeDexplicationShouldBeFound(String filter) throws Exception {
        restDemandeDexplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demandeDexplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].objet").value(hasItem(DEFAULT_OBJET)))
            .andExpect(jsonPath("$.[*].appDg").value(hasItem(DEFAULT_APP_DG)))
            .andExpect(jsonPath("$.[*].datappDg").value(hasItem(DEFAULT_DATAPP_DG.toString())));

        // Check, that the count call also returns 1
        restDemandeDexplicationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDemandeDexplicationShouldNotBeFound(String filter) throws Exception {
        restDemandeDexplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDemandeDexplicationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDemandeDexplication() throws Exception {
        // Get the demandeDexplication
        restDemandeDexplicationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDemandeDexplication() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        int databaseSizeBeforeUpdate = demandeDexplicationRepository.findAll().size();

        // Update the demandeDexplication
        DemandeDexplication updatedDemandeDexplication = demandeDexplicationRepository.findById(demandeDexplication.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDemandeDexplication are not directly saved in db
        em.detach(updatedDemandeDexplication);
        updatedDemandeDexplication
            .code(UPDATED_CODE)
            .date(UPDATED_DATE)
            .objet(UPDATED_OBJET)
            .appDg(UPDATED_APP_DG)
            .datappDg(UPDATED_DATAPP_DG);

        restDemandeDexplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDemandeDexplication.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDemandeDexplication))
            )
            .andExpect(status().isOk());

        // Validate the DemandeDexplication in the database
        List<DemandeDexplication> demandeDexplicationList = demandeDexplicationRepository.findAll();
        assertThat(demandeDexplicationList).hasSize(databaseSizeBeforeUpdate);
        DemandeDexplication testDemandeDexplication = demandeDexplicationList.get(demandeDexplicationList.size() - 1);
        assertThat(testDemandeDexplication.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDemandeDexplication.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDemandeDexplication.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testDemandeDexplication.getAppDg()).isEqualTo(UPDATED_APP_DG);
        assertThat(testDemandeDexplication.getDatappDg()).isEqualTo(UPDATED_DATAPP_DG);
    }

    @Test
    @Transactional
    void putNonExistingDemandeDexplication() throws Exception {
        int databaseSizeBeforeUpdate = demandeDexplicationRepository.findAll().size();
        demandeDexplication.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeDexplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demandeDexplication.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeDexplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeDexplication in the database
        List<DemandeDexplication> demandeDexplicationList = demandeDexplicationRepository.findAll();
        assertThat(demandeDexplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDemandeDexplication() throws Exception {
        int databaseSizeBeforeUpdate = demandeDexplicationRepository.findAll().size();
        demandeDexplication.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeDexplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeDexplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeDexplication in the database
        List<DemandeDexplication> demandeDexplicationList = demandeDexplicationRepository.findAll();
        assertThat(demandeDexplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDemandeDexplication() throws Exception {
        int databaseSizeBeforeUpdate = demandeDexplicationRepository.findAll().size();
        demandeDexplication.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeDexplicationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeDexplication))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandeDexplication in the database
        List<DemandeDexplication> demandeDexplicationList = demandeDexplicationRepository.findAll();
        assertThat(demandeDexplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDemandeDexplicationWithPatch() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        int databaseSizeBeforeUpdate = demandeDexplicationRepository.findAll().size();

        // Update the demandeDexplication using partial update
        DemandeDexplication partialUpdatedDemandeDexplication = new DemandeDexplication();
        partialUpdatedDemandeDexplication.setId(demandeDexplication.getId());

        partialUpdatedDemandeDexplication.code(UPDATED_CODE).appDg(UPDATED_APP_DG).datappDg(UPDATED_DATAPP_DG);

        restDemandeDexplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandeDexplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandeDexplication))
            )
            .andExpect(status().isOk());

        // Validate the DemandeDexplication in the database
        List<DemandeDexplication> demandeDexplicationList = demandeDexplicationRepository.findAll();
        assertThat(demandeDexplicationList).hasSize(databaseSizeBeforeUpdate);
        DemandeDexplication testDemandeDexplication = demandeDexplicationList.get(demandeDexplicationList.size() - 1);
        assertThat(testDemandeDexplication.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDemandeDexplication.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDemandeDexplication.getObjet()).isEqualTo(DEFAULT_OBJET);
        assertThat(testDemandeDexplication.getAppDg()).isEqualTo(UPDATED_APP_DG);
        assertThat(testDemandeDexplication.getDatappDg()).isEqualTo(UPDATED_DATAPP_DG);
    }

    @Test
    @Transactional
    void fullUpdateDemandeDexplicationWithPatch() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        int databaseSizeBeforeUpdate = demandeDexplicationRepository.findAll().size();

        // Update the demandeDexplication using partial update
        DemandeDexplication partialUpdatedDemandeDexplication = new DemandeDexplication();
        partialUpdatedDemandeDexplication.setId(demandeDexplication.getId());

        partialUpdatedDemandeDexplication
            .code(UPDATED_CODE)
            .date(UPDATED_DATE)
            .objet(UPDATED_OBJET)
            .appDg(UPDATED_APP_DG)
            .datappDg(UPDATED_DATAPP_DG);

        restDemandeDexplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandeDexplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandeDexplication))
            )
            .andExpect(status().isOk());

        // Validate the DemandeDexplication in the database
        List<DemandeDexplication> demandeDexplicationList = demandeDexplicationRepository.findAll();
        assertThat(demandeDexplicationList).hasSize(databaseSizeBeforeUpdate);
        DemandeDexplication testDemandeDexplication = demandeDexplicationList.get(demandeDexplicationList.size() - 1);
        assertThat(testDemandeDexplication.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDemandeDexplication.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDemandeDexplication.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testDemandeDexplication.getAppDg()).isEqualTo(UPDATED_APP_DG);
        assertThat(testDemandeDexplication.getDatappDg()).isEqualTo(UPDATED_DATAPP_DG);
    }

    @Test
    @Transactional
    void patchNonExistingDemandeDexplication() throws Exception {
        int databaseSizeBeforeUpdate = demandeDexplicationRepository.findAll().size();
        demandeDexplication.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeDexplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, demandeDexplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeDexplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeDexplication in the database
        List<DemandeDexplication> demandeDexplicationList = demandeDexplicationRepository.findAll();
        assertThat(demandeDexplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDemandeDexplication() throws Exception {
        int databaseSizeBeforeUpdate = demandeDexplicationRepository.findAll().size();
        demandeDexplication.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeDexplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeDexplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeDexplication in the database
        List<DemandeDexplication> demandeDexplicationList = demandeDexplicationRepository.findAll();
        assertThat(demandeDexplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDemandeDexplication() throws Exception {
        int databaseSizeBeforeUpdate = demandeDexplicationRepository.findAll().size();
        demandeDexplication.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeDexplicationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeDexplication))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandeDexplication in the database
        List<DemandeDexplication> demandeDexplicationList = demandeDexplicationRepository.findAll();
        assertThat(demandeDexplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDemandeDexplication() throws Exception {
        // Initialize the database
        demandeDexplicationRepository.saveAndFlush(demandeDexplication);

        int databaseSizeBeforeDelete = demandeDexplicationRepository.findAll().size();

        // Delete the demandeDexplication
        restDemandeDexplicationMockMvc
            .perform(delete(ENTITY_API_URL_ID, demandeDexplication.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DemandeDexplication> demandeDexplicationList = demandeDexplicationRepository.findAll();
        assertThat(demandeDexplicationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

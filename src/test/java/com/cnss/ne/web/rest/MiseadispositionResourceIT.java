package com.cnss.ne.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cnss.ne.IntegrationTest;
import com.cnss.ne.domain.Agent;
import com.cnss.ne.domain.Miseadisposition;
import com.cnss.ne.repository.MiseadispositionRepository;
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
 * Integration tests for the {@link MiseadispositionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MiseadispositionResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANISME = "AAAAAAAAAA";
    private static final String UPDATED_ORGANISME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_DEBUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEBUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SENS_MOUVEMENT = "AAAAAAAAAA";
    private static final String UPDATED_SENS_MOUVEMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_RETOUR = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_RETOUR = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/miseadispositions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MiseadispositionRepository miseadispositionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMiseadispositionMockMvc;

    private Miseadisposition miseadisposition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Miseadisposition createEntity(EntityManager em) {
        Miseadisposition miseadisposition = new Miseadisposition()
            .code(DEFAULT_CODE)
            .organisme(DEFAULT_ORGANISME)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .sensMouvement(DEFAULT_SENS_MOUVEMENT)
            .dateRetour(DEFAULT_DATE_RETOUR);
        return miseadisposition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Miseadisposition createUpdatedEntity(EntityManager em) {
        Miseadisposition miseadisposition = new Miseadisposition()
            .code(UPDATED_CODE)
            .organisme(UPDATED_ORGANISME)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .sensMouvement(UPDATED_SENS_MOUVEMENT)
            .dateRetour(UPDATED_DATE_RETOUR);
        return miseadisposition;
    }

    @BeforeEach
    public void initTest() {
        miseadisposition = createEntity(em);
    }

    @Test
    @Transactional
    void createMiseadisposition() throws Exception {
        int databaseSizeBeforeCreate = miseadispositionRepository.findAll().size();
        // Create the Miseadisposition
        restMiseadispositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(miseadisposition))
            )
            .andExpect(status().isCreated());

        // Validate the Miseadisposition in the database
        List<Miseadisposition> miseadispositionList = miseadispositionRepository.findAll();
        assertThat(miseadispositionList).hasSize(databaseSizeBeforeCreate + 1);
        Miseadisposition testMiseadisposition = miseadispositionList.get(miseadispositionList.size() - 1);
        assertThat(testMiseadisposition.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMiseadisposition.getOrganisme()).isEqualTo(DEFAULT_ORGANISME);
        assertThat(testMiseadisposition.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testMiseadisposition.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testMiseadisposition.getSensMouvement()).isEqualTo(DEFAULT_SENS_MOUVEMENT);
        assertThat(testMiseadisposition.getDateRetour()).isEqualTo(DEFAULT_DATE_RETOUR);
    }

    @Test
    @Transactional
    void createMiseadispositionWithExistingId() throws Exception {
        // Create the Miseadisposition with an existing ID
        miseadisposition.setId(1L);

        int databaseSizeBeforeCreate = miseadispositionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMiseadispositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(miseadisposition))
            )
            .andExpect(status().isBadRequest());

        // Validate the Miseadisposition in the database
        List<Miseadisposition> miseadispositionList = miseadispositionRepository.findAll();
        assertThat(miseadispositionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = miseadispositionRepository.findAll().size();
        // set the field null
        miseadisposition.setCode(null);

        // Create the Miseadisposition, which fails.

        restMiseadispositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(miseadisposition))
            )
            .andExpect(status().isBadRequest());

        List<Miseadisposition> miseadispositionList = miseadispositionRepository.findAll();
        assertThat(miseadispositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrganismeIsRequired() throws Exception {
        int databaseSizeBeforeTest = miseadispositionRepository.findAll().size();
        // set the field null
        miseadisposition.setOrganisme(null);

        // Create the Miseadisposition, which fails.

        restMiseadispositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(miseadisposition))
            )
            .andExpect(status().isBadRequest());

        List<Miseadisposition> miseadispositionList = miseadispositionRepository.findAll();
        assertThat(miseadispositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMiseadispositions() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList
        restMiseadispositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(miseadisposition.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].organisme").value(hasItem(DEFAULT_ORGANISME)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].sensMouvement").value(hasItem(DEFAULT_SENS_MOUVEMENT)))
            .andExpect(jsonPath("$.[*].dateRetour").value(hasItem(DEFAULT_DATE_RETOUR.toString())));
    }

    @Test
    @Transactional
    void getMiseadisposition() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get the miseadisposition
        restMiseadispositionMockMvc
            .perform(get(ENTITY_API_URL_ID, miseadisposition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(miseadisposition.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.organisme").value(DEFAULT_ORGANISME))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.sensMouvement").value(DEFAULT_SENS_MOUVEMENT))
            .andExpect(jsonPath("$.dateRetour").value(DEFAULT_DATE_RETOUR.toString()));
    }

    @Test
    @Transactional
    void getMiseadispositionsByIdFiltering() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        Long id = miseadisposition.getId();

        defaultMiseadispositionShouldBeFound("id.equals=" + id);
        defaultMiseadispositionShouldNotBeFound("id.notEquals=" + id);

        defaultMiseadispositionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMiseadispositionShouldNotBeFound("id.greaterThan=" + id);

        defaultMiseadispositionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMiseadispositionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMiseadispositionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where code equals to DEFAULT_CODE
        defaultMiseadispositionShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the miseadispositionList where code equals to UPDATED_CODE
        defaultMiseadispositionShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMiseadispositionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where code in DEFAULT_CODE or UPDATED_CODE
        defaultMiseadispositionShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the miseadispositionList where code equals to UPDATED_CODE
        defaultMiseadispositionShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMiseadispositionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where code is not null
        defaultMiseadispositionShouldBeFound("code.specified=true");

        // Get all the miseadispositionList where code is null
        defaultMiseadispositionShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllMiseadispositionsByCodeContainsSomething() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where code contains DEFAULT_CODE
        defaultMiseadispositionShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the miseadispositionList where code contains UPDATED_CODE
        defaultMiseadispositionShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMiseadispositionsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where code does not contain DEFAULT_CODE
        defaultMiseadispositionShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the miseadispositionList where code does not contain UPDATED_CODE
        defaultMiseadispositionShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMiseadispositionsByOrganismeIsEqualToSomething() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where organisme equals to DEFAULT_ORGANISME
        defaultMiseadispositionShouldBeFound("organisme.equals=" + DEFAULT_ORGANISME);

        // Get all the miseadispositionList where organisme equals to UPDATED_ORGANISME
        defaultMiseadispositionShouldNotBeFound("organisme.equals=" + UPDATED_ORGANISME);
    }

    @Test
    @Transactional
    void getAllMiseadispositionsByOrganismeIsInShouldWork() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where organisme in DEFAULT_ORGANISME or UPDATED_ORGANISME
        defaultMiseadispositionShouldBeFound("organisme.in=" + DEFAULT_ORGANISME + "," + UPDATED_ORGANISME);

        // Get all the miseadispositionList where organisme equals to UPDATED_ORGANISME
        defaultMiseadispositionShouldNotBeFound("organisme.in=" + UPDATED_ORGANISME);
    }

    @Test
    @Transactional
    void getAllMiseadispositionsByOrganismeIsNullOrNotNull() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where organisme is not null
        defaultMiseadispositionShouldBeFound("organisme.specified=true");

        // Get all the miseadispositionList where organisme is null
        defaultMiseadispositionShouldNotBeFound("organisme.specified=false");
    }

    @Test
    @Transactional
    void getAllMiseadispositionsByOrganismeContainsSomething() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where organisme contains DEFAULT_ORGANISME
        defaultMiseadispositionShouldBeFound("organisme.contains=" + DEFAULT_ORGANISME);

        // Get all the miseadispositionList where organisme contains UPDATED_ORGANISME
        defaultMiseadispositionShouldNotBeFound("organisme.contains=" + UPDATED_ORGANISME);
    }

    @Test
    @Transactional
    void getAllMiseadispositionsByOrganismeNotContainsSomething() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where organisme does not contain DEFAULT_ORGANISME
        defaultMiseadispositionShouldNotBeFound("organisme.doesNotContain=" + DEFAULT_ORGANISME);

        // Get all the miseadispositionList where organisme does not contain UPDATED_ORGANISME
        defaultMiseadispositionShouldBeFound("organisme.doesNotContain=" + UPDATED_ORGANISME);
    }

    @Test
    @Transactional
    void getAllMiseadispositionsByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where dateDebut equals to DEFAULT_DATE_DEBUT
        defaultMiseadispositionShouldBeFound("dateDebut.equals=" + DEFAULT_DATE_DEBUT);

        // Get all the miseadispositionList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultMiseadispositionShouldNotBeFound("dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllMiseadispositionsByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where dateDebut in DEFAULT_DATE_DEBUT or UPDATED_DATE_DEBUT
        defaultMiseadispositionShouldBeFound("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT);

        // Get all the miseadispositionList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultMiseadispositionShouldNotBeFound("dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllMiseadispositionsByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where dateDebut is not null
        defaultMiseadispositionShouldBeFound("dateDebut.specified=true");

        // Get all the miseadispositionList where dateDebut is null
        defaultMiseadispositionShouldNotBeFound("dateDebut.specified=false");
    }

    @Test
    @Transactional
    void getAllMiseadispositionsByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where dateFin equals to DEFAULT_DATE_FIN
        defaultMiseadispositionShouldBeFound("dateFin.equals=" + DEFAULT_DATE_FIN);

        // Get all the miseadispositionList where dateFin equals to UPDATED_DATE_FIN
        defaultMiseadispositionShouldNotBeFound("dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllMiseadispositionsByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where dateFin in DEFAULT_DATE_FIN or UPDATED_DATE_FIN
        defaultMiseadispositionShouldBeFound("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN);

        // Get all the miseadispositionList where dateFin equals to UPDATED_DATE_FIN
        defaultMiseadispositionShouldNotBeFound("dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllMiseadispositionsByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where dateFin is not null
        defaultMiseadispositionShouldBeFound("dateFin.specified=true");

        // Get all the miseadispositionList where dateFin is null
        defaultMiseadispositionShouldNotBeFound("dateFin.specified=false");
    }

    @Test
    @Transactional
    void getAllMiseadispositionsBySensMouvementIsEqualToSomething() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where sensMouvement equals to DEFAULT_SENS_MOUVEMENT
        defaultMiseadispositionShouldBeFound("sensMouvement.equals=" + DEFAULT_SENS_MOUVEMENT);

        // Get all the miseadispositionList where sensMouvement equals to UPDATED_SENS_MOUVEMENT
        defaultMiseadispositionShouldNotBeFound("sensMouvement.equals=" + UPDATED_SENS_MOUVEMENT);
    }

    @Test
    @Transactional
    void getAllMiseadispositionsBySensMouvementIsInShouldWork() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where sensMouvement in DEFAULT_SENS_MOUVEMENT or UPDATED_SENS_MOUVEMENT
        defaultMiseadispositionShouldBeFound("sensMouvement.in=" + DEFAULT_SENS_MOUVEMENT + "," + UPDATED_SENS_MOUVEMENT);

        // Get all the miseadispositionList where sensMouvement equals to UPDATED_SENS_MOUVEMENT
        defaultMiseadispositionShouldNotBeFound("sensMouvement.in=" + UPDATED_SENS_MOUVEMENT);
    }

    @Test
    @Transactional
    void getAllMiseadispositionsBySensMouvementIsNullOrNotNull() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where sensMouvement is not null
        defaultMiseadispositionShouldBeFound("sensMouvement.specified=true");

        // Get all the miseadispositionList where sensMouvement is null
        defaultMiseadispositionShouldNotBeFound("sensMouvement.specified=false");
    }

    @Test
    @Transactional
    void getAllMiseadispositionsBySensMouvementContainsSomething() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where sensMouvement contains DEFAULT_SENS_MOUVEMENT
        defaultMiseadispositionShouldBeFound("sensMouvement.contains=" + DEFAULT_SENS_MOUVEMENT);

        // Get all the miseadispositionList where sensMouvement contains UPDATED_SENS_MOUVEMENT
        defaultMiseadispositionShouldNotBeFound("sensMouvement.contains=" + UPDATED_SENS_MOUVEMENT);
    }

    @Test
    @Transactional
    void getAllMiseadispositionsBySensMouvementNotContainsSomething() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where sensMouvement does not contain DEFAULT_SENS_MOUVEMENT
        defaultMiseadispositionShouldNotBeFound("sensMouvement.doesNotContain=" + DEFAULT_SENS_MOUVEMENT);

        // Get all the miseadispositionList where sensMouvement does not contain UPDATED_SENS_MOUVEMENT
        defaultMiseadispositionShouldBeFound("sensMouvement.doesNotContain=" + UPDATED_SENS_MOUVEMENT);
    }

    @Test
    @Transactional
    void getAllMiseadispositionsByDateRetourIsEqualToSomething() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where dateRetour equals to DEFAULT_DATE_RETOUR
        defaultMiseadispositionShouldBeFound("dateRetour.equals=" + DEFAULT_DATE_RETOUR);

        // Get all the miseadispositionList where dateRetour equals to UPDATED_DATE_RETOUR
        defaultMiseadispositionShouldNotBeFound("dateRetour.equals=" + UPDATED_DATE_RETOUR);
    }

    @Test
    @Transactional
    void getAllMiseadispositionsByDateRetourIsInShouldWork() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where dateRetour in DEFAULT_DATE_RETOUR or UPDATED_DATE_RETOUR
        defaultMiseadispositionShouldBeFound("dateRetour.in=" + DEFAULT_DATE_RETOUR + "," + UPDATED_DATE_RETOUR);

        // Get all the miseadispositionList where dateRetour equals to UPDATED_DATE_RETOUR
        defaultMiseadispositionShouldNotBeFound("dateRetour.in=" + UPDATED_DATE_RETOUR);
    }

    @Test
    @Transactional
    void getAllMiseadispositionsByDateRetourIsNullOrNotNull() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        // Get all the miseadispositionList where dateRetour is not null
        defaultMiseadispositionShouldBeFound("dateRetour.specified=true");

        // Get all the miseadispositionList where dateRetour is null
        defaultMiseadispositionShouldNotBeFound("dateRetour.specified=false");
    }

    @Test
    @Transactional
    void getAllMiseadispositionsByAgentIsEqualToSomething() throws Exception {
        Agent agent;
        if (TestUtil.findAll(em, Agent.class).isEmpty()) {
            miseadispositionRepository.saveAndFlush(miseadisposition);
            agent = AgentResourceIT.createEntity(em);
        } else {
            agent = TestUtil.findAll(em, Agent.class).get(0);
        }
        em.persist(agent);
        em.flush();
        miseadisposition.setAgent(agent);
        miseadispositionRepository.saveAndFlush(miseadisposition);
        Long agentId = agent.getId();
        // Get all the miseadispositionList where agent equals to agentId
        defaultMiseadispositionShouldBeFound("agentId.equals=" + agentId);

        // Get all the miseadispositionList where agent equals to (agentId + 1)
        defaultMiseadispositionShouldNotBeFound("agentId.equals=" + (agentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMiseadispositionShouldBeFound(String filter) throws Exception {
        restMiseadispositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(miseadisposition.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].organisme").value(hasItem(DEFAULT_ORGANISME)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].sensMouvement").value(hasItem(DEFAULT_SENS_MOUVEMENT)))
            .andExpect(jsonPath("$.[*].dateRetour").value(hasItem(DEFAULT_DATE_RETOUR.toString())));

        // Check, that the count call also returns 1
        restMiseadispositionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMiseadispositionShouldNotBeFound(String filter) throws Exception {
        restMiseadispositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMiseadispositionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMiseadisposition() throws Exception {
        // Get the miseadisposition
        restMiseadispositionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMiseadisposition() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        int databaseSizeBeforeUpdate = miseadispositionRepository.findAll().size();

        // Update the miseadisposition
        Miseadisposition updatedMiseadisposition = miseadispositionRepository.findById(miseadisposition.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMiseadisposition are not directly saved in db
        em.detach(updatedMiseadisposition);
        updatedMiseadisposition
            .code(UPDATED_CODE)
            .organisme(UPDATED_ORGANISME)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .sensMouvement(UPDATED_SENS_MOUVEMENT)
            .dateRetour(UPDATED_DATE_RETOUR);

        restMiseadispositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMiseadisposition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMiseadisposition))
            )
            .andExpect(status().isOk());

        // Validate the Miseadisposition in the database
        List<Miseadisposition> miseadispositionList = miseadispositionRepository.findAll();
        assertThat(miseadispositionList).hasSize(databaseSizeBeforeUpdate);
        Miseadisposition testMiseadisposition = miseadispositionList.get(miseadispositionList.size() - 1);
        assertThat(testMiseadisposition.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMiseadisposition.getOrganisme()).isEqualTo(UPDATED_ORGANISME);
        assertThat(testMiseadisposition.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testMiseadisposition.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testMiseadisposition.getSensMouvement()).isEqualTo(UPDATED_SENS_MOUVEMENT);
        assertThat(testMiseadisposition.getDateRetour()).isEqualTo(UPDATED_DATE_RETOUR);
    }

    @Test
    @Transactional
    void putNonExistingMiseadisposition() throws Exception {
        int databaseSizeBeforeUpdate = miseadispositionRepository.findAll().size();
        miseadisposition.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMiseadispositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, miseadisposition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(miseadisposition))
            )
            .andExpect(status().isBadRequest());

        // Validate the Miseadisposition in the database
        List<Miseadisposition> miseadispositionList = miseadispositionRepository.findAll();
        assertThat(miseadispositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMiseadisposition() throws Exception {
        int databaseSizeBeforeUpdate = miseadispositionRepository.findAll().size();
        miseadisposition.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMiseadispositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(miseadisposition))
            )
            .andExpect(status().isBadRequest());

        // Validate the Miseadisposition in the database
        List<Miseadisposition> miseadispositionList = miseadispositionRepository.findAll();
        assertThat(miseadispositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMiseadisposition() throws Exception {
        int databaseSizeBeforeUpdate = miseadispositionRepository.findAll().size();
        miseadisposition.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMiseadispositionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(miseadisposition))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Miseadisposition in the database
        List<Miseadisposition> miseadispositionList = miseadispositionRepository.findAll();
        assertThat(miseadispositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMiseadispositionWithPatch() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        int databaseSizeBeforeUpdate = miseadispositionRepository.findAll().size();

        // Update the miseadisposition using partial update
        Miseadisposition partialUpdatedMiseadisposition = new Miseadisposition();
        partialUpdatedMiseadisposition.setId(miseadisposition.getId());

        partialUpdatedMiseadisposition
            .code(UPDATED_CODE)
            .organisme(UPDATED_ORGANISME)
            .dateFin(UPDATED_DATE_FIN)
            .sensMouvement(UPDATED_SENS_MOUVEMENT);

        restMiseadispositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMiseadisposition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMiseadisposition))
            )
            .andExpect(status().isOk());

        // Validate the Miseadisposition in the database
        List<Miseadisposition> miseadispositionList = miseadispositionRepository.findAll();
        assertThat(miseadispositionList).hasSize(databaseSizeBeforeUpdate);
        Miseadisposition testMiseadisposition = miseadispositionList.get(miseadispositionList.size() - 1);
        assertThat(testMiseadisposition.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMiseadisposition.getOrganisme()).isEqualTo(UPDATED_ORGANISME);
        assertThat(testMiseadisposition.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testMiseadisposition.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testMiseadisposition.getSensMouvement()).isEqualTo(UPDATED_SENS_MOUVEMENT);
        assertThat(testMiseadisposition.getDateRetour()).isEqualTo(DEFAULT_DATE_RETOUR);
    }

    @Test
    @Transactional
    void fullUpdateMiseadispositionWithPatch() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        int databaseSizeBeforeUpdate = miseadispositionRepository.findAll().size();

        // Update the miseadisposition using partial update
        Miseadisposition partialUpdatedMiseadisposition = new Miseadisposition();
        partialUpdatedMiseadisposition.setId(miseadisposition.getId());

        partialUpdatedMiseadisposition
            .code(UPDATED_CODE)
            .organisme(UPDATED_ORGANISME)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .sensMouvement(UPDATED_SENS_MOUVEMENT)
            .dateRetour(UPDATED_DATE_RETOUR);

        restMiseadispositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMiseadisposition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMiseadisposition))
            )
            .andExpect(status().isOk());

        // Validate the Miseadisposition in the database
        List<Miseadisposition> miseadispositionList = miseadispositionRepository.findAll();
        assertThat(miseadispositionList).hasSize(databaseSizeBeforeUpdate);
        Miseadisposition testMiseadisposition = miseadispositionList.get(miseadispositionList.size() - 1);
        assertThat(testMiseadisposition.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMiseadisposition.getOrganisme()).isEqualTo(UPDATED_ORGANISME);
        assertThat(testMiseadisposition.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testMiseadisposition.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testMiseadisposition.getSensMouvement()).isEqualTo(UPDATED_SENS_MOUVEMENT);
        assertThat(testMiseadisposition.getDateRetour()).isEqualTo(UPDATED_DATE_RETOUR);
    }

    @Test
    @Transactional
    void patchNonExistingMiseadisposition() throws Exception {
        int databaseSizeBeforeUpdate = miseadispositionRepository.findAll().size();
        miseadisposition.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMiseadispositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, miseadisposition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(miseadisposition))
            )
            .andExpect(status().isBadRequest());

        // Validate the Miseadisposition in the database
        List<Miseadisposition> miseadispositionList = miseadispositionRepository.findAll();
        assertThat(miseadispositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMiseadisposition() throws Exception {
        int databaseSizeBeforeUpdate = miseadispositionRepository.findAll().size();
        miseadisposition.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMiseadispositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(miseadisposition))
            )
            .andExpect(status().isBadRequest());

        // Validate the Miseadisposition in the database
        List<Miseadisposition> miseadispositionList = miseadispositionRepository.findAll();
        assertThat(miseadispositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMiseadisposition() throws Exception {
        int databaseSizeBeforeUpdate = miseadispositionRepository.findAll().size();
        miseadisposition.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMiseadispositionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(miseadisposition))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Miseadisposition in the database
        List<Miseadisposition> miseadispositionList = miseadispositionRepository.findAll();
        assertThat(miseadispositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMiseadisposition() throws Exception {
        // Initialize the database
        miseadispositionRepository.saveAndFlush(miseadisposition);

        int databaseSizeBeforeDelete = miseadispositionRepository.findAll().size();

        // Delete the miseadisposition
        restMiseadispositionMockMvc
            .perform(delete(ENTITY_API_URL_ID, miseadisposition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Miseadisposition> miseadispositionList = miseadispositionRepository.findAll();
        assertThat(miseadispositionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

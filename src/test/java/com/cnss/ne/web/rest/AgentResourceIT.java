package com.cnss.ne.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cnss.ne.IntegrationTest;
import com.cnss.ne.domain.Agent;
import com.cnss.ne.domain.DemandeDexplication;
import com.cnss.ne.domain.Disponibilite;
import com.cnss.ne.domain.Miseadisposition;
import com.cnss.ne.domain.Poste;
import com.cnss.ne.repository.AgentRepository;
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
 * Integration tests for the {@link AgentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AgentResourceIT {

    private static final String DEFAULT_MATRICULE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_NAISSANCE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_NAISSANCE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LIEU_NAISSANCE = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_NAISSANCE = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITE = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_DECE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DECE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CAUSE_DECE = "AAAAAAAAAA";
    private static final String UPDATED_CAUSE_DECE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/agents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgentMockMvc;

    private Agent agent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agent createEntity(EntityManager em) {
        Agent agent = new Agent()
            .matricule(DEFAULT_MATRICULE)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .lieuNaissance(DEFAULT_LIEU_NAISSANCE)
            .nationalite(DEFAULT_NATIONALITE)
            .telephone(DEFAULT_TELEPHONE)
            .adresse(DEFAULT_ADRESSE)
            .dateDece(DEFAULT_DATE_DECE)
            .causeDece(DEFAULT_CAUSE_DECE);
        return agent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agent createUpdatedEntity(EntityManager em) {
        Agent agent = new Agent()
            .matricule(UPDATED_MATRICULE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .nationalite(UPDATED_NATIONALITE)
            .telephone(UPDATED_TELEPHONE)
            .adresse(UPDATED_ADRESSE)
            .dateDece(UPDATED_DATE_DECE)
            .causeDece(UPDATED_CAUSE_DECE);
        return agent;
    }

    @BeforeEach
    public void initTest() {
        agent = createEntity(em);
    }

    @Test
    @Transactional
    void createAgent() throws Exception {
        int databaseSizeBeforeCreate = agentRepository.findAll().size();
        // Create the Agent
        restAgentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agent)))
            .andExpect(status().isCreated());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeCreate + 1);
        Agent testAgent = agentList.get(agentList.size() - 1);
        assertThat(testAgent.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testAgent.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testAgent.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testAgent.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testAgent.getLieuNaissance()).isEqualTo(DEFAULT_LIEU_NAISSANCE);
        assertThat(testAgent.getNationalite()).isEqualTo(DEFAULT_NATIONALITE);
        assertThat(testAgent.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testAgent.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testAgent.getDateDece()).isEqualTo(DEFAULT_DATE_DECE);
        assertThat(testAgent.getCauseDece()).isEqualTo(DEFAULT_CAUSE_DECE);
    }

    @Test
    @Transactional
    void createAgentWithExistingId() throws Exception {
        // Create the Agent with an existing ID
        agent.setId(1L);

        int databaseSizeBeforeCreate = agentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agent)))
            .andExpect(status().isBadRequest());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMatriculeIsRequired() throws Exception {
        int databaseSizeBeforeTest = agentRepository.findAll().size();
        // set the field null
        agent.setMatricule(null);

        // Create the Agent, which fails.

        restAgentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agent)))
            .andExpect(status().isBadRequest());

        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = agentRepository.findAll().size();
        // set the field null
        agent.setNom(null);

        // Create the Agent, which fails.

        restAgentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agent)))
            .andExpect(status().isBadRequest());

        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = agentRepository.findAll().size();
        // set the field null
        agent.setPrenom(null);

        // Create the Agent, which fails.

        restAgentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agent)))
            .andExpect(status().isBadRequest());

        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAgents() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList
        restAgentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agent.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].lieuNaissance").value(hasItem(DEFAULT_LIEU_NAISSANCE)))
            .andExpect(jsonPath("$.[*].nationalite").value(hasItem(DEFAULT_NATIONALITE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].dateDece").value(hasItem(DEFAULT_DATE_DECE.toString())))
            .andExpect(jsonPath("$.[*].causeDece").value(hasItem(DEFAULT_CAUSE_DECE)));
    }

    @Test
    @Transactional
    void getAgent() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get the agent
        restAgentMockMvc
            .perform(get(ENTITY_API_URL_ID, agent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agent.getId().intValue()))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.lieuNaissance").value(DEFAULT_LIEU_NAISSANCE))
            .andExpect(jsonPath("$.nationalite").value(DEFAULT_NATIONALITE))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.dateDece").value(DEFAULT_DATE_DECE.toString()))
            .andExpect(jsonPath("$.causeDece").value(DEFAULT_CAUSE_DECE));
    }

    @Test
    @Transactional
    void getAgentsByIdFiltering() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        Long id = agent.getId();

        defaultAgentShouldBeFound("id.equals=" + id);
        defaultAgentShouldNotBeFound("id.notEquals=" + id);

        defaultAgentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAgentShouldNotBeFound("id.greaterThan=" + id);

        defaultAgentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAgentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAgentsByMatriculeIsEqualToSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where matricule equals to DEFAULT_MATRICULE
        defaultAgentShouldBeFound("matricule.equals=" + DEFAULT_MATRICULE);

        // Get all the agentList where matricule equals to UPDATED_MATRICULE
        defaultAgentShouldNotBeFound("matricule.equals=" + UPDATED_MATRICULE);
    }

    @Test
    @Transactional
    void getAllAgentsByMatriculeIsInShouldWork() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where matricule in DEFAULT_MATRICULE or UPDATED_MATRICULE
        defaultAgentShouldBeFound("matricule.in=" + DEFAULT_MATRICULE + "," + UPDATED_MATRICULE);

        // Get all the agentList where matricule equals to UPDATED_MATRICULE
        defaultAgentShouldNotBeFound("matricule.in=" + UPDATED_MATRICULE);
    }

    @Test
    @Transactional
    void getAllAgentsByMatriculeIsNullOrNotNull() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where matricule is not null
        defaultAgentShouldBeFound("matricule.specified=true");

        // Get all the agentList where matricule is null
        defaultAgentShouldNotBeFound("matricule.specified=false");
    }

    @Test
    @Transactional
    void getAllAgentsByMatriculeContainsSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where matricule contains DEFAULT_MATRICULE
        defaultAgentShouldBeFound("matricule.contains=" + DEFAULT_MATRICULE);

        // Get all the agentList where matricule contains UPDATED_MATRICULE
        defaultAgentShouldNotBeFound("matricule.contains=" + UPDATED_MATRICULE);
    }

    @Test
    @Transactional
    void getAllAgentsByMatriculeNotContainsSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where matricule does not contain DEFAULT_MATRICULE
        defaultAgentShouldNotBeFound("matricule.doesNotContain=" + DEFAULT_MATRICULE);

        // Get all the agentList where matricule does not contain UPDATED_MATRICULE
        defaultAgentShouldBeFound("matricule.doesNotContain=" + UPDATED_MATRICULE);
    }

    @Test
    @Transactional
    void getAllAgentsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where nom equals to DEFAULT_NOM
        defaultAgentShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the agentList where nom equals to UPDATED_NOM
        defaultAgentShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllAgentsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultAgentShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the agentList where nom equals to UPDATED_NOM
        defaultAgentShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllAgentsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where nom is not null
        defaultAgentShouldBeFound("nom.specified=true");

        // Get all the agentList where nom is null
        defaultAgentShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllAgentsByNomContainsSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where nom contains DEFAULT_NOM
        defaultAgentShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the agentList where nom contains UPDATED_NOM
        defaultAgentShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllAgentsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where nom does not contain DEFAULT_NOM
        defaultAgentShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the agentList where nom does not contain UPDATED_NOM
        defaultAgentShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllAgentsByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where prenom equals to DEFAULT_PRENOM
        defaultAgentShouldBeFound("prenom.equals=" + DEFAULT_PRENOM);

        // Get all the agentList where prenom equals to UPDATED_PRENOM
        defaultAgentShouldNotBeFound("prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllAgentsByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where prenom in DEFAULT_PRENOM or UPDATED_PRENOM
        defaultAgentShouldBeFound("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM);

        // Get all the agentList where prenom equals to UPDATED_PRENOM
        defaultAgentShouldNotBeFound("prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllAgentsByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where prenom is not null
        defaultAgentShouldBeFound("prenom.specified=true");

        // Get all the agentList where prenom is null
        defaultAgentShouldNotBeFound("prenom.specified=false");
    }

    @Test
    @Transactional
    void getAllAgentsByPrenomContainsSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where prenom contains DEFAULT_PRENOM
        defaultAgentShouldBeFound("prenom.contains=" + DEFAULT_PRENOM);

        // Get all the agentList where prenom contains UPDATED_PRENOM
        defaultAgentShouldNotBeFound("prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllAgentsByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where prenom does not contain DEFAULT_PRENOM
        defaultAgentShouldNotBeFound("prenom.doesNotContain=" + DEFAULT_PRENOM);

        // Get all the agentList where prenom does not contain UPDATED_PRENOM
        defaultAgentShouldBeFound("prenom.doesNotContain=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllAgentsByDateNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where dateNaissance equals to DEFAULT_DATE_NAISSANCE
        defaultAgentShouldBeFound("dateNaissance.equals=" + DEFAULT_DATE_NAISSANCE);

        // Get all the agentList where dateNaissance equals to UPDATED_DATE_NAISSANCE
        defaultAgentShouldNotBeFound("dateNaissance.equals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllAgentsByDateNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where dateNaissance in DEFAULT_DATE_NAISSANCE or UPDATED_DATE_NAISSANCE
        defaultAgentShouldBeFound("dateNaissance.in=" + DEFAULT_DATE_NAISSANCE + "," + UPDATED_DATE_NAISSANCE);

        // Get all the agentList where dateNaissance equals to UPDATED_DATE_NAISSANCE
        defaultAgentShouldNotBeFound("dateNaissance.in=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllAgentsByDateNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where dateNaissance is not null
        defaultAgentShouldBeFound("dateNaissance.specified=true");

        // Get all the agentList where dateNaissance is null
        defaultAgentShouldNotBeFound("dateNaissance.specified=false");
    }

    @Test
    @Transactional
    void getAllAgentsByLieuNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where lieuNaissance equals to DEFAULT_LIEU_NAISSANCE
        defaultAgentShouldBeFound("lieuNaissance.equals=" + DEFAULT_LIEU_NAISSANCE);

        // Get all the agentList where lieuNaissance equals to UPDATED_LIEU_NAISSANCE
        defaultAgentShouldNotBeFound("lieuNaissance.equals=" + UPDATED_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllAgentsByLieuNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where lieuNaissance in DEFAULT_LIEU_NAISSANCE or UPDATED_LIEU_NAISSANCE
        defaultAgentShouldBeFound("lieuNaissance.in=" + DEFAULT_LIEU_NAISSANCE + "," + UPDATED_LIEU_NAISSANCE);

        // Get all the agentList where lieuNaissance equals to UPDATED_LIEU_NAISSANCE
        defaultAgentShouldNotBeFound("lieuNaissance.in=" + UPDATED_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllAgentsByLieuNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where lieuNaissance is not null
        defaultAgentShouldBeFound("lieuNaissance.specified=true");

        // Get all the agentList where lieuNaissance is null
        defaultAgentShouldNotBeFound("lieuNaissance.specified=false");
    }

    @Test
    @Transactional
    void getAllAgentsByLieuNaissanceContainsSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where lieuNaissance contains DEFAULT_LIEU_NAISSANCE
        defaultAgentShouldBeFound("lieuNaissance.contains=" + DEFAULT_LIEU_NAISSANCE);

        // Get all the agentList where lieuNaissance contains UPDATED_LIEU_NAISSANCE
        defaultAgentShouldNotBeFound("lieuNaissance.contains=" + UPDATED_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllAgentsByLieuNaissanceNotContainsSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where lieuNaissance does not contain DEFAULT_LIEU_NAISSANCE
        defaultAgentShouldNotBeFound("lieuNaissance.doesNotContain=" + DEFAULT_LIEU_NAISSANCE);

        // Get all the agentList where lieuNaissance does not contain UPDATED_LIEU_NAISSANCE
        defaultAgentShouldBeFound("lieuNaissance.doesNotContain=" + UPDATED_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllAgentsByNationaliteIsEqualToSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where nationalite equals to DEFAULT_NATIONALITE
        defaultAgentShouldBeFound("nationalite.equals=" + DEFAULT_NATIONALITE);

        // Get all the agentList where nationalite equals to UPDATED_NATIONALITE
        defaultAgentShouldNotBeFound("nationalite.equals=" + UPDATED_NATIONALITE);
    }

    @Test
    @Transactional
    void getAllAgentsByNationaliteIsInShouldWork() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where nationalite in DEFAULT_NATIONALITE or UPDATED_NATIONALITE
        defaultAgentShouldBeFound("nationalite.in=" + DEFAULT_NATIONALITE + "," + UPDATED_NATIONALITE);

        // Get all the agentList where nationalite equals to UPDATED_NATIONALITE
        defaultAgentShouldNotBeFound("nationalite.in=" + UPDATED_NATIONALITE);
    }

    @Test
    @Transactional
    void getAllAgentsByNationaliteIsNullOrNotNull() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where nationalite is not null
        defaultAgentShouldBeFound("nationalite.specified=true");

        // Get all the agentList where nationalite is null
        defaultAgentShouldNotBeFound("nationalite.specified=false");
    }

    @Test
    @Transactional
    void getAllAgentsByNationaliteContainsSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where nationalite contains DEFAULT_NATIONALITE
        defaultAgentShouldBeFound("nationalite.contains=" + DEFAULT_NATIONALITE);

        // Get all the agentList where nationalite contains UPDATED_NATIONALITE
        defaultAgentShouldNotBeFound("nationalite.contains=" + UPDATED_NATIONALITE);
    }

    @Test
    @Transactional
    void getAllAgentsByNationaliteNotContainsSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where nationalite does not contain DEFAULT_NATIONALITE
        defaultAgentShouldNotBeFound("nationalite.doesNotContain=" + DEFAULT_NATIONALITE);

        // Get all the agentList where nationalite does not contain UPDATED_NATIONALITE
        defaultAgentShouldBeFound("nationalite.doesNotContain=" + UPDATED_NATIONALITE);
    }

    @Test
    @Transactional
    void getAllAgentsByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where telephone equals to DEFAULT_TELEPHONE
        defaultAgentShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the agentList where telephone equals to UPDATED_TELEPHONE
        defaultAgentShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllAgentsByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultAgentShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the agentList where telephone equals to UPDATED_TELEPHONE
        defaultAgentShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllAgentsByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where telephone is not null
        defaultAgentShouldBeFound("telephone.specified=true");

        // Get all the agentList where telephone is null
        defaultAgentShouldNotBeFound("telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllAgentsByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where telephone contains DEFAULT_TELEPHONE
        defaultAgentShouldBeFound("telephone.contains=" + DEFAULT_TELEPHONE);

        // Get all the agentList where telephone contains UPDATED_TELEPHONE
        defaultAgentShouldNotBeFound("telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllAgentsByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where telephone does not contain DEFAULT_TELEPHONE
        defaultAgentShouldNotBeFound("telephone.doesNotContain=" + DEFAULT_TELEPHONE);

        // Get all the agentList where telephone does not contain UPDATED_TELEPHONE
        defaultAgentShouldBeFound("telephone.doesNotContain=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllAgentsByAdresseIsEqualToSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where adresse equals to DEFAULT_ADRESSE
        defaultAgentShouldBeFound("adresse.equals=" + DEFAULT_ADRESSE);

        // Get all the agentList where adresse equals to UPDATED_ADRESSE
        defaultAgentShouldNotBeFound("adresse.equals=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllAgentsByAdresseIsInShouldWork() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where adresse in DEFAULT_ADRESSE or UPDATED_ADRESSE
        defaultAgentShouldBeFound("adresse.in=" + DEFAULT_ADRESSE + "," + UPDATED_ADRESSE);

        // Get all the agentList where adresse equals to UPDATED_ADRESSE
        defaultAgentShouldNotBeFound("adresse.in=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllAgentsByAdresseIsNullOrNotNull() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where adresse is not null
        defaultAgentShouldBeFound("adresse.specified=true");

        // Get all the agentList where adresse is null
        defaultAgentShouldNotBeFound("adresse.specified=false");
    }

    @Test
    @Transactional
    void getAllAgentsByAdresseContainsSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where adresse contains DEFAULT_ADRESSE
        defaultAgentShouldBeFound("adresse.contains=" + DEFAULT_ADRESSE);

        // Get all the agentList where adresse contains UPDATED_ADRESSE
        defaultAgentShouldNotBeFound("adresse.contains=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllAgentsByAdresseNotContainsSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where adresse does not contain DEFAULT_ADRESSE
        defaultAgentShouldNotBeFound("adresse.doesNotContain=" + DEFAULT_ADRESSE);

        // Get all the agentList where adresse does not contain UPDATED_ADRESSE
        defaultAgentShouldBeFound("adresse.doesNotContain=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllAgentsByDateDeceIsEqualToSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where dateDece equals to DEFAULT_DATE_DECE
        defaultAgentShouldBeFound("dateDece.equals=" + DEFAULT_DATE_DECE);

        // Get all the agentList where dateDece equals to UPDATED_DATE_DECE
        defaultAgentShouldNotBeFound("dateDece.equals=" + UPDATED_DATE_DECE);
    }

    @Test
    @Transactional
    void getAllAgentsByDateDeceIsInShouldWork() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where dateDece in DEFAULT_DATE_DECE or UPDATED_DATE_DECE
        defaultAgentShouldBeFound("dateDece.in=" + DEFAULT_DATE_DECE + "," + UPDATED_DATE_DECE);

        // Get all the agentList where dateDece equals to UPDATED_DATE_DECE
        defaultAgentShouldNotBeFound("dateDece.in=" + UPDATED_DATE_DECE);
    }

    @Test
    @Transactional
    void getAllAgentsByDateDeceIsNullOrNotNull() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where dateDece is not null
        defaultAgentShouldBeFound("dateDece.specified=true");

        // Get all the agentList where dateDece is null
        defaultAgentShouldNotBeFound("dateDece.specified=false");
    }

    @Test
    @Transactional
    void getAllAgentsByCauseDeceIsEqualToSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where causeDece equals to DEFAULT_CAUSE_DECE
        defaultAgentShouldBeFound("causeDece.equals=" + DEFAULT_CAUSE_DECE);

        // Get all the agentList where causeDece equals to UPDATED_CAUSE_DECE
        defaultAgentShouldNotBeFound("causeDece.equals=" + UPDATED_CAUSE_DECE);
    }

    @Test
    @Transactional
    void getAllAgentsByCauseDeceIsInShouldWork() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where causeDece in DEFAULT_CAUSE_DECE or UPDATED_CAUSE_DECE
        defaultAgentShouldBeFound("causeDece.in=" + DEFAULT_CAUSE_DECE + "," + UPDATED_CAUSE_DECE);

        // Get all the agentList where causeDece equals to UPDATED_CAUSE_DECE
        defaultAgentShouldNotBeFound("causeDece.in=" + UPDATED_CAUSE_DECE);
    }

    @Test
    @Transactional
    void getAllAgentsByCauseDeceIsNullOrNotNull() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where causeDece is not null
        defaultAgentShouldBeFound("causeDece.specified=true");

        // Get all the agentList where causeDece is null
        defaultAgentShouldNotBeFound("causeDece.specified=false");
    }

    @Test
    @Transactional
    void getAllAgentsByCauseDeceContainsSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where causeDece contains DEFAULT_CAUSE_DECE
        defaultAgentShouldBeFound("causeDece.contains=" + DEFAULT_CAUSE_DECE);

        // Get all the agentList where causeDece contains UPDATED_CAUSE_DECE
        defaultAgentShouldNotBeFound("causeDece.contains=" + UPDATED_CAUSE_DECE);
    }

    @Test
    @Transactional
    void getAllAgentsByCauseDeceNotContainsSomething() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList where causeDece does not contain DEFAULT_CAUSE_DECE
        defaultAgentShouldNotBeFound("causeDece.doesNotContain=" + DEFAULT_CAUSE_DECE);

        // Get all the agentList where causeDece does not contain UPDATED_CAUSE_DECE
        defaultAgentShouldBeFound("causeDece.doesNotContain=" + UPDATED_CAUSE_DECE);
    }

    @Test
    @Transactional
    void getAllAgentsByDisponibiliteIsEqualToSomething() throws Exception {
        Disponibilite disponibilite;
        if (TestUtil.findAll(em, Disponibilite.class).isEmpty()) {
            agentRepository.saveAndFlush(agent);
            disponibilite = DisponibiliteResourceIT.createEntity(em);
        } else {
            disponibilite = TestUtil.findAll(em, Disponibilite.class).get(0);
        }
        em.persist(disponibilite);
        em.flush();
        agent.addDisponibilite(disponibilite);
        agentRepository.saveAndFlush(agent);
        Long disponibiliteId = disponibilite.getId();
        // Get all the agentList where disponibilite equals to disponibiliteId
        defaultAgentShouldBeFound("disponibiliteId.equals=" + disponibiliteId);

        // Get all the agentList where disponibilite equals to (disponibiliteId + 1)
        defaultAgentShouldNotBeFound("disponibiliteId.equals=" + (disponibiliteId + 1));
    }

    @Test
    @Transactional
    void getAllAgentsByMiseadispositionIsEqualToSomething() throws Exception {
        Miseadisposition miseadisposition;
        if (TestUtil.findAll(em, Miseadisposition.class).isEmpty()) {
            agentRepository.saveAndFlush(agent);
            miseadisposition = MiseadispositionResourceIT.createEntity(em);
        } else {
            miseadisposition = TestUtil.findAll(em, Miseadisposition.class).get(0);
        }
        em.persist(miseadisposition);
        em.flush();
        agent.addMiseadisposition(miseadisposition);
        agentRepository.saveAndFlush(agent);
        Long miseadispositionId = miseadisposition.getId();
        // Get all the agentList where miseadisposition equals to miseadispositionId
        defaultAgentShouldBeFound("miseadispositionId.equals=" + miseadispositionId);

        // Get all the agentList where miseadisposition equals to (miseadispositionId + 1)
        defaultAgentShouldNotBeFound("miseadispositionId.equals=" + (miseadispositionId + 1));
    }

    @Test
    @Transactional
    void getAllAgentsByDemandeDexplicationIsEqualToSomething() throws Exception {
        DemandeDexplication demandeDexplication;
        if (TestUtil.findAll(em, DemandeDexplication.class).isEmpty()) {
            agentRepository.saveAndFlush(agent);
            demandeDexplication = DemandeDexplicationResourceIT.createEntity(em);
        } else {
            demandeDexplication = TestUtil.findAll(em, DemandeDexplication.class).get(0);
        }
        em.persist(demandeDexplication);
        em.flush();
        agent.addDemandeDexplication(demandeDexplication);
        agentRepository.saveAndFlush(agent);
        Long demandeDexplicationId = demandeDexplication.getId();
        // Get all the agentList where demandeDexplication equals to demandeDexplicationId
        defaultAgentShouldBeFound("demandeDexplicationId.equals=" + demandeDexplicationId);

        // Get all the agentList where demandeDexplication equals to (demandeDexplicationId + 1)
        defaultAgentShouldNotBeFound("demandeDexplicationId.equals=" + (demandeDexplicationId + 1));
    }

    @Test
    @Transactional
    void getAllAgentsByPosteIsEqualToSomething() throws Exception {
        Poste poste;
        if (TestUtil.findAll(em, Poste.class).isEmpty()) {
            agentRepository.saveAndFlush(agent);
            poste = PosteResourceIT.createEntity(em);
        } else {
            poste = TestUtil.findAll(em, Poste.class).get(0);
        }
        em.persist(poste);
        em.flush();
        agent.setPoste(poste);
        agentRepository.saveAndFlush(agent);
        Long posteId = poste.getId();
        // Get all the agentList where poste equals to posteId
        defaultAgentShouldBeFound("posteId.equals=" + posteId);

        // Get all the agentList where poste equals to (posteId + 1)
        defaultAgentShouldNotBeFound("posteId.equals=" + (posteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAgentShouldBeFound(String filter) throws Exception {
        restAgentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agent.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].lieuNaissance").value(hasItem(DEFAULT_LIEU_NAISSANCE)))
            .andExpect(jsonPath("$.[*].nationalite").value(hasItem(DEFAULT_NATIONALITE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].dateDece").value(hasItem(DEFAULT_DATE_DECE.toString())))
            .andExpect(jsonPath("$.[*].causeDece").value(hasItem(DEFAULT_CAUSE_DECE)));

        // Check, that the count call also returns 1
        restAgentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAgentShouldNotBeFound(String filter) throws Exception {
        restAgentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAgentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAgent() throws Exception {
        // Get the agent
        restAgentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAgent() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        int databaseSizeBeforeUpdate = agentRepository.findAll().size();

        // Update the agent
        Agent updatedAgent = agentRepository.findById(agent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAgent are not directly saved in db
        em.detach(updatedAgent);
        updatedAgent
            .matricule(UPDATED_MATRICULE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .nationalite(UPDATED_NATIONALITE)
            .telephone(UPDATED_TELEPHONE)
            .adresse(UPDATED_ADRESSE)
            .dateDece(UPDATED_DATE_DECE)
            .causeDece(UPDATED_CAUSE_DECE);

        restAgentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAgent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAgent))
            )
            .andExpect(status().isOk());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
        Agent testAgent = agentList.get(agentList.size() - 1);
        assertThat(testAgent.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testAgent.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAgent.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testAgent.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testAgent.getLieuNaissance()).isEqualTo(UPDATED_LIEU_NAISSANCE);
        assertThat(testAgent.getNationalite()).isEqualTo(UPDATED_NATIONALITE);
        assertThat(testAgent.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testAgent.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testAgent.getDateDece()).isEqualTo(UPDATED_DATE_DECE);
        assertThat(testAgent.getCauseDece()).isEqualTo(UPDATED_CAUSE_DECE);
    }

    @Test
    @Transactional
    void putNonExistingAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentRepository.findAll().size();
        agent.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agent))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentRepository.findAll().size();
        agent.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agent))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentRepository.findAll().size();
        agent.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agent)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgentWithPatch() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        int databaseSizeBeforeUpdate = agentRepository.findAll().size();

        // Update the agent using partial update
        Agent partialUpdatedAgent = new Agent();
        partialUpdatedAgent.setId(agent.getId());

        partialUpdatedAgent
            .matricule(UPDATED_MATRICULE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .nationalite(UPDATED_NATIONALITE)
            .telephone(UPDATED_TELEPHONE)
            .adresse(UPDATED_ADRESSE);

        restAgentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgent))
            )
            .andExpect(status().isOk());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
        Agent testAgent = agentList.get(agentList.size() - 1);
        assertThat(testAgent.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testAgent.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testAgent.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testAgent.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testAgent.getLieuNaissance()).isEqualTo(DEFAULT_LIEU_NAISSANCE);
        assertThat(testAgent.getNationalite()).isEqualTo(UPDATED_NATIONALITE);
        assertThat(testAgent.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testAgent.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testAgent.getDateDece()).isEqualTo(DEFAULT_DATE_DECE);
        assertThat(testAgent.getCauseDece()).isEqualTo(DEFAULT_CAUSE_DECE);
    }

    @Test
    @Transactional
    void fullUpdateAgentWithPatch() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        int databaseSizeBeforeUpdate = agentRepository.findAll().size();

        // Update the agent using partial update
        Agent partialUpdatedAgent = new Agent();
        partialUpdatedAgent.setId(agent.getId());

        partialUpdatedAgent
            .matricule(UPDATED_MATRICULE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .nationalite(UPDATED_NATIONALITE)
            .telephone(UPDATED_TELEPHONE)
            .adresse(UPDATED_ADRESSE)
            .dateDece(UPDATED_DATE_DECE)
            .causeDece(UPDATED_CAUSE_DECE);

        restAgentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgent))
            )
            .andExpect(status().isOk());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
        Agent testAgent = agentList.get(agentList.size() - 1);
        assertThat(testAgent.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testAgent.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAgent.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testAgent.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testAgent.getLieuNaissance()).isEqualTo(UPDATED_LIEU_NAISSANCE);
        assertThat(testAgent.getNationalite()).isEqualTo(UPDATED_NATIONALITE);
        assertThat(testAgent.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testAgent.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testAgent.getDateDece()).isEqualTo(UPDATED_DATE_DECE);
        assertThat(testAgent.getCauseDece()).isEqualTo(UPDATED_CAUSE_DECE);
    }

    @Test
    @Transactional
    void patchNonExistingAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentRepository.findAll().size();
        agent.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agent))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentRepository.findAll().size();
        agent.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agent))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentRepository.findAll().size();
        agent.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(agent)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgent() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        int databaseSizeBeforeDelete = agentRepository.findAll().size();

        // Delete the agent
        restAgentMockMvc
            .perform(delete(ENTITY_API_URL_ID, agent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

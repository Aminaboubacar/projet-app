package com.cnss.ne.domain;

import static com.cnss.ne.domain.AgentTestSamples.*;
import static com.cnss.ne.domain.DemandeDexplicationTestSamples.*;
import static com.cnss.ne.domain.DisponibiliteTestSamples.*;
import static com.cnss.ne.domain.MiseadispositionTestSamples.*;
import static com.cnss.ne.domain.PosteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cnss.ne.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AgentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agent.class);
        Agent agent1 = getAgentSample1();
        Agent agent2 = new Agent();
        assertThat(agent1).isNotEqualTo(agent2);

        agent2.setId(agent1.getId());
        assertThat(agent1).isEqualTo(agent2);

        agent2 = getAgentSample2();
        assertThat(agent1).isNotEqualTo(agent2);
    }

    @Test
    void disponibiliteTest() throws Exception {
        Agent agent = getAgentRandomSampleGenerator();
        Disponibilite disponibiliteBack = getDisponibiliteRandomSampleGenerator();

        agent.addDisponibilite(disponibiliteBack);
        assertThat(agent.getDisponibilites()).containsOnly(disponibiliteBack);
        assertThat(disponibiliteBack.getAgent()).isEqualTo(agent);

        agent.removeDisponibilite(disponibiliteBack);
        assertThat(agent.getDisponibilites()).doesNotContain(disponibiliteBack);
        assertThat(disponibiliteBack.getAgent()).isNull();

        agent.disponibilites(new HashSet<>(Set.of(disponibiliteBack)));
        assertThat(agent.getDisponibilites()).containsOnly(disponibiliteBack);
        assertThat(disponibiliteBack.getAgent()).isEqualTo(agent);

        agent.setDisponibilites(new HashSet<>());
        assertThat(agent.getDisponibilites()).doesNotContain(disponibiliteBack);
        assertThat(disponibiliteBack.getAgent()).isNull();
    }

    @Test
    void miseadispositionTest() throws Exception {
        Agent agent = getAgentRandomSampleGenerator();
        Miseadisposition miseadispositionBack = getMiseadispositionRandomSampleGenerator();

        agent.addMiseadisposition(miseadispositionBack);
        assertThat(agent.getMiseadispositions()).containsOnly(miseadispositionBack);
        assertThat(miseadispositionBack.getAgent()).isEqualTo(agent);

        agent.removeMiseadisposition(miseadispositionBack);
        assertThat(agent.getMiseadispositions()).doesNotContain(miseadispositionBack);
        assertThat(miseadispositionBack.getAgent()).isNull();

        agent.miseadispositions(new HashSet<>(Set.of(miseadispositionBack)));
        assertThat(agent.getMiseadispositions()).containsOnly(miseadispositionBack);
        assertThat(miseadispositionBack.getAgent()).isEqualTo(agent);

        agent.setMiseadispositions(new HashSet<>());
        assertThat(agent.getMiseadispositions()).doesNotContain(miseadispositionBack);
        assertThat(miseadispositionBack.getAgent()).isNull();
    }

    @Test
    void demandeDexplicationTest() throws Exception {
        Agent agent = getAgentRandomSampleGenerator();
        DemandeDexplication demandeDexplicationBack = getDemandeDexplicationRandomSampleGenerator();

        agent.addDemandeDexplication(demandeDexplicationBack);
        assertThat(agent.getDemandeDexplications()).containsOnly(demandeDexplicationBack);
        assertThat(demandeDexplicationBack.getAgent()).isEqualTo(agent);

        agent.removeDemandeDexplication(demandeDexplicationBack);
        assertThat(agent.getDemandeDexplications()).doesNotContain(demandeDexplicationBack);
        assertThat(demandeDexplicationBack.getAgent()).isNull();

        agent.demandeDexplications(new HashSet<>(Set.of(demandeDexplicationBack)));
        assertThat(agent.getDemandeDexplications()).containsOnly(demandeDexplicationBack);
        assertThat(demandeDexplicationBack.getAgent()).isEqualTo(agent);

        agent.setDemandeDexplications(new HashSet<>());
        assertThat(agent.getDemandeDexplications()).doesNotContain(demandeDexplicationBack);
        assertThat(demandeDexplicationBack.getAgent()).isNull();
    }

    @Test
    void posteTest() throws Exception {
        Agent agent = getAgentRandomSampleGenerator();
        Poste posteBack = getPosteRandomSampleGenerator();

        agent.setPoste(posteBack);
        assertThat(agent.getPoste()).isEqualTo(posteBack);

        agent.poste(null);
        assertThat(agent.getPoste()).isNull();
    }
}

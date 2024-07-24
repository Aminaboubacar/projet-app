package com.cnss.ne.domain;

import static com.cnss.ne.domain.AgentTestSamples.*;
import static com.cnss.ne.domain.PosteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cnss.ne.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PosteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Poste.class);
        Poste poste1 = getPosteSample1();
        Poste poste2 = new Poste();
        assertThat(poste1).isNotEqualTo(poste2);

        poste2.setId(poste1.getId());
        assertThat(poste1).isEqualTo(poste2);

        poste2 = getPosteSample2();
        assertThat(poste1).isNotEqualTo(poste2);
    }

    @Test
    void agentTest() throws Exception {
        Poste poste = getPosteRandomSampleGenerator();
        Agent agentBack = getAgentRandomSampleGenerator();

        poste.addAgent(agentBack);
        assertThat(poste.getAgents()).containsOnly(agentBack);
        assertThat(agentBack.getPoste()).isEqualTo(poste);

        poste.removeAgent(agentBack);
        assertThat(poste.getAgents()).doesNotContain(agentBack);
        assertThat(agentBack.getPoste()).isNull();

        poste.agents(new HashSet<>(Set.of(agentBack)));
        assertThat(poste.getAgents()).containsOnly(agentBack);
        assertThat(agentBack.getPoste()).isEqualTo(poste);

        poste.setAgents(new HashSet<>());
        assertThat(poste.getAgents()).doesNotContain(agentBack);
        assertThat(agentBack.getPoste()).isNull();
    }
}

package com.cnss.ne.domain;

import static com.cnss.ne.domain.AgentTestSamples.*;
import static com.cnss.ne.domain.DisponibiliteTestSamples.*;
import static com.cnss.ne.domain.MotifDisponibiliteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cnss.ne.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DisponibiliteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Disponibilite.class);
        Disponibilite disponibilite1 = getDisponibiliteSample1();
        Disponibilite disponibilite2 = new Disponibilite();
        assertThat(disponibilite1).isNotEqualTo(disponibilite2);

        disponibilite2.setId(disponibilite1.getId());
        assertThat(disponibilite1).isEqualTo(disponibilite2);

        disponibilite2 = getDisponibiliteSample2();
        assertThat(disponibilite1).isNotEqualTo(disponibilite2);
    }

    @Test
    void motifDisponibiliteTest() throws Exception {
        Disponibilite disponibilite = getDisponibiliteRandomSampleGenerator();
        MotifDisponibilite motifDisponibiliteBack = getMotifDisponibiliteRandomSampleGenerator();

        disponibilite.setMotifDisponibilite(motifDisponibiliteBack);
        assertThat(disponibilite.getMotifDisponibilite()).isEqualTo(motifDisponibiliteBack);

        disponibilite.motifDisponibilite(null);
        assertThat(disponibilite.getMotifDisponibilite()).isNull();
    }

    @Test
    void agentTest() throws Exception {
        Disponibilite disponibilite = getDisponibiliteRandomSampleGenerator();
        Agent agentBack = getAgentRandomSampleGenerator();

        disponibilite.setAgent(agentBack);
        assertThat(disponibilite.getAgent()).isEqualTo(agentBack);

        disponibilite.agent(null);
        assertThat(disponibilite.getAgent()).isNull();
    }
}

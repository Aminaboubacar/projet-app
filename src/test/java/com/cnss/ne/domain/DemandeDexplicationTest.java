package com.cnss.ne.domain;

import static com.cnss.ne.domain.AgentTestSamples.*;
import static com.cnss.ne.domain.DemandeDexplicationTestSamples.*;
import static com.cnss.ne.domain.SanctionnerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cnss.ne.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DemandeDexplicationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemandeDexplication.class);
        DemandeDexplication demandeDexplication1 = getDemandeDexplicationSample1();
        DemandeDexplication demandeDexplication2 = new DemandeDexplication();
        assertThat(demandeDexplication1).isNotEqualTo(demandeDexplication2);

        demandeDexplication2.setId(demandeDexplication1.getId());
        assertThat(demandeDexplication1).isEqualTo(demandeDexplication2);

        demandeDexplication2 = getDemandeDexplicationSample2();
        assertThat(demandeDexplication1).isNotEqualTo(demandeDexplication2);
    }

    @Test
    void sanctionnerTest() throws Exception {
        DemandeDexplication demandeDexplication = getDemandeDexplicationRandomSampleGenerator();
        Sanctionner sanctionnerBack = getSanctionnerRandomSampleGenerator();

        demandeDexplication.addSanctionner(sanctionnerBack);
        assertThat(demandeDexplication.getSanctionners()).containsOnly(sanctionnerBack);
        assertThat(sanctionnerBack.getDemandeDexplication()).isEqualTo(demandeDexplication);

        demandeDexplication.removeSanctionner(sanctionnerBack);
        assertThat(demandeDexplication.getSanctionners()).doesNotContain(sanctionnerBack);
        assertThat(sanctionnerBack.getDemandeDexplication()).isNull();

        demandeDexplication.sanctionners(new HashSet<>(Set.of(sanctionnerBack)));
        assertThat(demandeDexplication.getSanctionners()).containsOnly(sanctionnerBack);
        assertThat(sanctionnerBack.getDemandeDexplication()).isEqualTo(demandeDexplication);

        demandeDexplication.setSanctionners(new HashSet<>());
        assertThat(demandeDexplication.getSanctionners()).doesNotContain(sanctionnerBack);
        assertThat(sanctionnerBack.getDemandeDexplication()).isNull();
    }

    @Test
    void agentTest() throws Exception {
        DemandeDexplication demandeDexplication = getDemandeDexplicationRandomSampleGenerator();
        Agent agentBack = getAgentRandomSampleGenerator();

        demandeDexplication.setAgent(agentBack);
        assertThat(demandeDexplication.getAgent()).isEqualTo(agentBack);

        demandeDexplication.agent(null);
        assertThat(demandeDexplication.getAgent()).isNull();
    }
}

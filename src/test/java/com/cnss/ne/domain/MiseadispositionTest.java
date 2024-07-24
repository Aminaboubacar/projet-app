package com.cnss.ne.domain;

import static com.cnss.ne.domain.AgentTestSamples.*;
import static com.cnss.ne.domain.MiseadispositionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cnss.ne.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MiseadispositionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Miseadisposition.class);
        Miseadisposition miseadisposition1 = getMiseadispositionSample1();
        Miseadisposition miseadisposition2 = new Miseadisposition();
        assertThat(miseadisposition1).isNotEqualTo(miseadisposition2);

        miseadisposition2.setId(miseadisposition1.getId());
        assertThat(miseadisposition1).isEqualTo(miseadisposition2);

        miseadisposition2 = getMiseadispositionSample2();
        assertThat(miseadisposition1).isNotEqualTo(miseadisposition2);
    }

    @Test
    void agentTest() throws Exception {
        Miseadisposition miseadisposition = getMiseadispositionRandomSampleGenerator();
        Agent agentBack = getAgentRandomSampleGenerator();

        miseadisposition.setAgent(agentBack);
        assertThat(miseadisposition.getAgent()).isEqualTo(agentBack);

        miseadisposition.agent(null);
        assertThat(miseadisposition.getAgent()).isNull();
    }
}

package com.cnss.ne.domain;

import static com.cnss.ne.domain.DemandeDexplicationTestSamples.*;
import static com.cnss.ne.domain.SanctionTestSamples.*;
import static com.cnss.ne.domain.SanctionnerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cnss.ne.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SanctionnerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sanctionner.class);
        Sanctionner sanctionner1 = getSanctionnerSample1();
        Sanctionner sanctionner2 = new Sanctionner();
        assertThat(sanctionner1).isNotEqualTo(sanctionner2);

        sanctionner2.setId(sanctionner1.getId());
        assertThat(sanctionner1).isEqualTo(sanctionner2);

        sanctionner2 = getSanctionnerSample2();
        assertThat(sanctionner1).isNotEqualTo(sanctionner2);
    }

    @Test
    void sanctionTest() throws Exception {
        Sanctionner sanctionner = getSanctionnerRandomSampleGenerator();
        Sanction sanctionBack = getSanctionRandomSampleGenerator();

        sanctionner.setSanction(sanctionBack);
        assertThat(sanctionner.getSanction()).isEqualTo(sanctionBack);

        sanctionner.sanction(null);
        assertThat(sanctionner.getSanction()).isNull();
    }

    @Test
    void demandeDexplicationTest() throws Exception {
        Sanctionner sanctionner = getSanctionnerRandomSampleGenerator();
        DemandeDexplication demandeDexplicationBack = getDemandeDexplicationRandomSampleGenerator();

        sanctionner.setDemandeDexplication(demandeDexplicationBack);
        assertThat(sanctionner.getDemandeDexplication()).isEqualTo(demandeDexplicationBack);

        sanctionner.demandeDexplication(null);
        assertThat(sanctionner.getDemandeDexplication()).isNull();
    }
}

package com.cnss.ne.domain;

import static com.cnss.ne.domain.DegreTestSamples.*;
import static com.cnss.ne.domain.SanctionTestSamples.*;
import static com.cnss.ne.domain.SanctionnerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cnss.ne.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SanctionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sanction.class);
        Sanction sanction1 = getSanctionSample1();
        Sanction sanction2 = new Sanction();
        assertThat(sanction1).isNotEqualTo(sanction2);

        sanction2.setId(sanction1.getId());
        assertThat(sanction1).isEqualTo(sanction2);

        sanction2 = getSanctionSample2();
        assertThat(sanction1).isNotEqualTo(sanction2);
    }

    @Test
    void sanctionnerTest() throws Exception {
        Sanction sanction = getSanctionRandomSampleGenerator();
        Sanctionner sanctionnerBack = getSanctionnerRandomSampleGenerator();

        sanction.addSanctionner(sanctionnerBack);
        assertThat(sanction.getSanctionners()).containsOnly(sanctionnerBack);
        assertThat(sanctionnerBack.getSanction()).isEqualTo(sanction);

        sanction.removeSanctionner(sanctionnerBack);
        assertThat(sanction.getSanctionners()).doesNotContain(sanctionnerBack);
        assertThat(sanctionnerBack.getSanction()).isNull();

        sanction.sanctionners(new HashSet<>(Set.of(sanctionnerBack)));
        assertThat(sanction.getSanctionners()).containsOnly(sanctionnerBack);
        assertThat(sanctionnerBack.getSanction()).isEqualTo(sanction);

        sanction.setSanctionners(new HashSet<>());
        assertThat(sanction.getSanctionners()).doesNotContain(sanctionnerBack);
        assertThat(sanctionnerBack.getSanction()).isNull();
    }

    @Test
    void degreTest() throws Exception {
        Sanction sanction = getSanctionRandomSampleGenerator();
        Degre degreBack = getDegreRandomSampleGenerator();

        sanction.setDegre(degreBack);
        assertThat(sanction.getDegre()).isEqualTo(degreBack);

        sanction.degre(null);
        assertThat(sanction.getDegre()).isNull();
    }
}

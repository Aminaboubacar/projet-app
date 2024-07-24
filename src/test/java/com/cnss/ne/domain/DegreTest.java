package com.cnss.ne.domain;

import static com.cnss.ne.domain.DegreTestSamples.*;
import static com.cnss.ne.domain.SanctionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cnss.ne.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DegreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Degre.class);
        Degre degre1 = getDegreSample1();
        Degre degre2 = new Degre();
        assertThat(degre1).isNotEqualTo(degre2);

        degre2.setId(degre1.getId());
        assertThat(degre1).isEqualTo(degre2);

        degre2 = getDegreSample2();
        assertThat(degre1).isNotEqualTo(degre2);
    }

    @Test
    void sanctionTest() throws Exception {
        Degre degre = getDegreRandomSampleGenerator();
        Sanction sanctionBack = getSanctionRandomSampleGenerator();

        degre.addSanction(sanctionBack);
        assertThat(degre.getSanctions()).containsOnly(sanctionBack);
        assertThat(sanctionBack.getDegre()).isEqualTo(degre);

        degre.removeSanction(sanctionBack);
        assertThat(degre.getSanctions()).doesNotContain(sanctionBack);
        assertThat(sanctionBack.getDegre()).isNull();

        degre.sanctions(new HashSet<>(Set.of(sanctionBack)));
        assertThat(degre.getSanctions()).containsOnly(sanctionBack);
        assertThat(sanctionBack.getDegre()).isEqualTo(degre);

        degre.setSanctions(new HashSet<>());
        assertThat(degre.getSanctions()).doesNotContain(sanctionBack);
        assertThat(sanctionBack.getDegre()).isNull();
    }
}

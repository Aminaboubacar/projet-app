package com.cnss.ne.domain;

import static com.cnss.ne.domain.DisponibiliteTestSamples.*;
import static com.cnss.ne.domain.MotifDisponibiliteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cnss.ne.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MotifDisponibiliteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MotifDisponibilite.class);
        MotifDisponibilite motifDisponibilite1 = getMotifDisponibiliteSample1();
        MotifDisponibilite motifDisponibilite2 = new MotifDisponibilite();
        assertThat(motifDisponibilite1).isNotEqualTo(motifDisponibilite2);

        motifDisponibilite2.setId(motifDisponibilite1.getId());
        assertThat(motifDisponibilite1).isEqualTo(motifDisponibilite2);

        motifDisponibilite2 = getMotifDisponibiliteSample2();
        assertThat(motifDisponibilite1).isNotEqualTo(motifDisponibilite2);
    }

    @Test
    void disponibiliteTest() throws Exception {
        MotifDisponibilite motifDisponibilite = getMotifDisponibiliteRandomSampleGenerator();
        Disponibilite disponibiliteBack = getDisponibiliteRandomSampleGenerator();

        motifDisponibilite.addDisponibilite(disponibiliteBack);
        assertThat(motifDisponibilite.getDisponibilites()).containsOnly(disponibiliteBack);
        assertThat(disponibiliteBack.getMotifDisponibilite()).isEqualTo(motifDisponibilite);

        motifDisponibilite.removeDisponibilite(disponibiliteBack);
        assertThat(motifDisponibilite.getDisponibilites()).doesNotContain(disponibiliteBack);
        assertThat(disponibiliteBack.getMotifDisponibilite()).isNull();

        motifDisponibilite.disponibilites(new HashSet<>(Set.of(disponibiliteBack)));
        assertThat(motifDisponibilite.getDisponibilites()).containsOnly(disponibiliteBack);
        assertThat(disponibiliteBack.getMotifDisponibilite()).isEqualTo(motifDisponibilite);

        motifDisponibilite.setDisponibilites(new HashSet<>());
        assertThat(motifDisponibilite.getDisponibilites()).doesNotContain(disponibiliteBack);
        assertThat(disponibiliteBack.getMotifDisponibilite()).isNull();
    }
}
